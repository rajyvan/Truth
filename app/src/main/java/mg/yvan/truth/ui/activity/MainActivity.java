package mg.yvan.truth.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnSearchQueryChange;
import mg.yvan.truth.manager.SessionManager;
import mg.yvan.truth.manager.TruthFragmentManager;
import mg.yvan.truth.ui.dialog.SelectBookDialog;
import mg.yvan.truth.ui.fragment.SearchResultFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ImageView mIvProfilePhoto;
    private TextView mTvProfileName;

    private ProfileTracker mProfileTracker;
    private AccessTokenTracker mAccessTokenTracker;

    private MenuItem mGoogleMenuItem;
    private MenuItem mFacebookMenuItem;
    private MenuItem mLogoutMenuItem;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);

        final View headerView = mNavView.getHeaderView(0);
        mIvProfilePhoto = (ImageView) headerView.findViewById(R.id.iv_photo);
        mTvProfileName = (TextView) headerView.findViewById(R.id.tv_name);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

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

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // On AccessToken changes fetch the new profile which fires the event on
                // the ProfileTracker if the profile is different
                Profile.fetchProfileForCurrentAccessToken();
                updateMenuItemsVisibility();
            }
        };
        Profile.fetchProfileForCurrentAccessToken();
        setProfile(Profile.getCurrentProfile());
    }

    private void configureMenu() {
        mNavView.setItemIconTintList(null);
        final MenuItem identityItem = mNavView.getMenu().findItem(R.id.identity);
        identityItem.setTitle(SessionManager.getInstance().isLogged() ? R.string.indentity : R.string.login);

        mGoogleMenuItem = mNavView.getMenu().findItem(R.id.login_google);
        mFacebookMenuItem = mNavView.getMenu().findItem(R.id.login_facebook);
        mLogoutMenuItem = mNavView.getMenu().findItem(R.id.nav_logout);
        updateMenuItemsVisibility();

        mLogoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LoginManager.getInstance().logOut();
                return true;
            }
        });

        mGoogleMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        mFacebookMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<String> permissions = Arrays.asList(getResources().getStringArray(R.array.facebook_permissions));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile.fetchProfileForCurrentAccessToken();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Facebook connect", "Canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Facebook connect", "Error: " + error.getLocalizedMessage());
                    }
                });
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissions);
                return true;
            }
        });
    }

    private void updateMenuItemsVisibility() {
        mGoogleMenuItem.setVisible(!SessionManager.getInstance().isLogged());
        mFacebookMenuItem.setVisible(!SessionManager.getInstance().isLogged());
        mLogoutMenuItem.setVisible(SessionManager.getInstance().isLogged());
    }

    private void setProfile(Profile profile) {
        if (!isFinishing()) {
            if (profile != null) {
                mTvProfileName.setText(profile.getName());
                final int photoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
                Glide.with(this).load(profile.getProfilePictureUri(photoSize, photoSize)).into(mIvProfilePhoto);
                updateMenuItemsVisibility();
            } else {
                mTvProfileName.setText(R.string.anonyme);
                mIvProfilePhoto.setImageResource(R.mipmap.ic_launcher);
            }
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment dialogFragment = getSupportFragmentManager().findFragmentByTag(SelectBookDialog.TAG);
            if (dialogFragment != null) {

            }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bible) {
            // Handle the camera action
        } else if (id == R.id.nav_verse) {

        } else if (id == R.id.nav_comment) {

        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_logout) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
