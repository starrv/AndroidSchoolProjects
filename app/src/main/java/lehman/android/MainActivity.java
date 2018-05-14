package lehman.android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mLabTitles;

    private static final String TAG = AppHelper.APP_LOG_TAG + "MainActivity"; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, String.format("Starting onCreate : %s", savedInstanceState));

        super.onCreate(savedInstanceState);
        // enable transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        setupDrawerLayout();

        if (savedInstanceState == null) {
            selectItem(0);
        }

        Log.v(TAG, "onCreate completed");
    }

    private void setupDrawerLayout() {

        Log.v(TAG, "Starting setupDrawerLayout");

        mLabTitles = getResources().getStringArray(R.array.labs_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mLabTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Log.v(TAG, "setupDrawerLayout completed");
    }

	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, String.format("Starting onCreateOptionsMenu : %s", menu));

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);

        Log.v(TAG, "onCreateOptionsMenu completed");
		return super.onCreateOptionsMenu(menu);
	}

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v(TAG, String.format("Starting onPrepareOptionsMenu : %s", menu));
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);

        Log.v(TAG, "onPrepareOptionsMenu completed");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, String.format("Starting onOptionsItemSelected : %s", item));

         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.v(TAG, "mDrawerToggle.onOptionsItemSelected(item) return true");
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                Log.v(TAG, "handle action_websearch");
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                Log.v(TAG, "handle action_websearch completed");
                return true;
            default:
                Log.v(TAG, "otherwise, ?? ");
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem(int position) {
        Log.v(TAG, String.format("Starting selectItem : %s", position));

        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getFragmentManager();

        if (position == 1) {
            fragment = new HelloFragment();
        } else if (position == 2) {
            fragment = new Lab0Fragment();
        } else if (position == 3) {
            fragment = new Lab1Fragment();
        } else if (position == 4) {
            fragment = new Lab2Fragment();
        } else if (position == 5) {
            fragment = new Lab3Fragment();
        } else if (position == 6) {
            fragment = new Lab4Fragment();
        }

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mLabTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        Log.v(TAG, "selectItem completed");
    }

    public void setTitle(CharSequence title) {
        Log.v(TAG, String.format("Starting setTitle : %s", title));
        mTitle = title;
        getActionBar().setTitle(mTitle);
        Log.v(TAG, "setTitle completed");
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.v(TAG, String.format("Starting onPostCreate : %s", savedInstanceState));
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        Log.v(TAG, "onPostCreate completed");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v(TAG, String.format("Starting onConfigurationChanged : %s", newConfig));
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged completed");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.v(TAG, "Starting DrawerItemClickListener.onItemClick");
            selectItem(position);
            Log.v(TAG, "DrawerItemClickListener.onItemClick completed");
        }
    }
}