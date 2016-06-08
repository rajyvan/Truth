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
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnSearchQueryChange;
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

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);

        TruthFragmentManager.displayBible(this);
        handleIntent(getIntent());
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
