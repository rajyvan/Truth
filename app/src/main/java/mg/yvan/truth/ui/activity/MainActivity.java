package mg.yvan.truth.ui.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnSearchQueryChange;
import mg.yvan.truth.manager.SessionManager;
import mg.yvan.truth.manager.TruthFragmentManager;
import mg.yvan.truth.models.database.RealmHelper;
import mg.yvan.truth.service.SynchroService;
import mg.yvan.truth.ui.fragment.BaseFragment;
import mg.yvan.truth.ui.fragment.SearchResultFragment;
import mg.yvan.truth.ui.view.CustomActionBarDrawerToggle;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private CustomActionBarDrawerToggle mToggle;

    private ImageView mIvProfilePhoto;
    private TextView mTvProfileName;

    private ProfileTracker mProfileTracker;

    private MenuItem mFacebookMenuItem;
    private MenuItem mLogoutMenuItem;

    private ProgressDialog mProgressDialog;

    private SyncReceiver mSyncReceiver;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configureToolbar();

        IntentFilter syncIntentFilter = new IntentFilter(SynchroService.ACTION_BROADCAST_END);
        mSyncReceiver = new SyncReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mSyncReceiver, syncIntentFilter);


        final View headerView = mNavView.getHeaderView(0);
        mIvProfilePhoto = (ImageView) headerView.findViewById(R.id.iv_photo);
        mTvProfileName = (TextView) headerView.findViewById(R.id.tv_name);

        mNavView.setNavigationItemSelectedListener(this);

        TruthFragmentManager.displayBible(this);
        handleIntent(getIntent());

        configureMenu();

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                setProfile(currentProfile);
            }
        };

        final Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            Profile.fetchProfileForCurrentAccessToken();
        } else {
            setProfile(Profile.getCurrentProfile());
        }
        configureBackStackListener();
    }

    private void configureToolbar() {
        mToggle = new CustomActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToggle.syncState();

        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    private void configureBackStackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            final BaseFragment fragment = (BaseFragment) TruthFragmentManager.getCurrentFragment(MainActivity.this);
            if (fragment != null) {
                if (fragment.getTitle() > 0) {
                    setTitle(fragment.getTitle());
                }

                if (fragment.showBackButton()) {
                    mToggle.animateToBackArrow();
                } else {
                    mToggle.animateToMenu();
                }
            }
        });
    }

    private void configureMenu() {
        mNavView.setItemIconTintList(null);
        final MenuItem identityItem = mNavView.getMenu().findItem(R.id.identity);
        identityItem.setTitle(SessionManager.getInstance().isLogged() ? R.string.indentity : R.string.login);

        mFacebookMenuItem = mNavView.getMenu().findItem(R.id.login_facebook);
        mLogoutMenuItem = mNavView.getMenu().findItem(R.id.nav_logout);
        updateMenuItemsVisibility();

        mLogoutMenuItem.setOnMenuItemClickListener(item -> {
            LoginManager.getInstance().logOut();
            return true;
        });

        mFacebookMenuItem.setOnMenuItemClickListener(item -> {
            List<String> permissions = Arrays.asList(getResources().getStringArray(R.array.facebook_permissions));

            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, (user, e) -> {
                if (e == null && user != null) {
                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
                        try {
                            String mail = object.getString("email");
                            user.setEmail(mail);
                            user.saveEventually();
                        } catch (JSONException e1) {
                            // do nothing
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    request.setParameters(parameters);
                    request.executeAsync();
                    Profile.fetchProfileForCurrentAccessToken();
                } else {
                    ParseUser.logOutInBackground();
                }

                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Synchronisation de données");
                }

                mProgressDialog.show();
                Intent intent = new Intent(this, SynchroService.class);
                intent.setAction(SynchroService.ACTION_SYNC);
                startService(intent);
            });
            return true;
        });
    }

    private void updateMenuItemsVisibility() {
        mFacebookMenuItem.setVisible(!SessionManager.getInstance().isLogged());
        mLogoutMenuItem.setVisible(SessionManager.getInstance().isLogged());
    }

    private void setProfile(Profile profile) {

        final int photoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);

        if (!isFinishing()) {
            if (profile != null) {
                mTvProfileName.setText(profile.getName());
                Glide.with(this).load(profile.getProfilePictureUri(photoSize, photoSize)).into(mIvProfilePhoto);
                updateMenuItemsVisibility();
            } else {
                mTvProfileName.setText(R.string.anonyme);
                mIvProfilePhoto.setImageResource(R.mipmap.ic_launcher);
            }
        }
        updateMenuItemsVisibility();

        final ParseUser user = ParseUser.getCurrentUser();
        if (profile != null && user != null) {
            user.setUsername(profile.getName());
            user.put("photo", profile.getProfilePictureUri(photoSize, photoSize).toString());
            user.saveEventually();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            TruthFragmentManager.displaySearchResult(MainActivity.this, query);
            EventBus.getDefault().post(new OnSearchQueryChange(query));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    Fragment currentFragment = TruthFragmentManager.getCurrentFragment(MainActivity.this);
                    if (currentFragment != null && currentFragment instanceof SearchResultFragment) {
                        onBackPressed();
                    }
                } else {
                    TruthFragmentManager.displaySearchResult(MainActivity.this, s);
                    EventBus.getDefault().post(new OnSearchQueryChange(s));
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            } else {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bible) {
            TruthFragmentManager.displayBible(this);
        } else if (id == R.id.nav_verse) {
            TruthFragmentManager.displayMyVerse(this);
        } else if (id == R.id.nav_comment) {
            TruthFragmentManager.displayComments(this);
        } else if (id == R.id.nav_community) {
            TruthFragmentManager.displayMyStatitistic(this);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        RealmHelper.getInstance().release();
        super.onDestroy();
    }

    private class SyncReceiver extends BroadcastReceiver {

        private SyncReceiver() {

        }

        public void onReceive(Context context, Intent intent) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }
}
