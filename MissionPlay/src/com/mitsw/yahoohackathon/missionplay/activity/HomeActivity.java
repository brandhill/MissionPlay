/*
 * Copyright 2013 Mitsw
 *
 */

package com.mitsw.yahoohackathon.missionplay.activity;

import java.util.Locale;

import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshAttacher;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.ion.Ion;
import com.mitsw.yahoohackathon.missionplay.Constants;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.fragment.MissionListFragment;
import com.mitsw.yahoohackathon.missionplay.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class HomeActivity extends FragmentActivity {
    
    private final String TAG = HomeActivity.class.getSimpleName();
    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    
    private int mCurrentPage;
    
    
    private PullToRefreshAttacher mPullToRefreshAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.main_menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
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

        mCurrentPage = 0;
        
        if (savedInstanceState == null) {
            selectItem(mCurrentPage);
        }
        
        // The attacher should always be created in the Activity's onCreate
        mPullToRefreshAttacher = new PullToRefreshAttacher(this);
        
        String urlString = "http://yahoohacktaiwan.bibby.be/Home/Register";
        
        
        JsonObject json = new JsonObject();
        json.addProperty("SocialNetworkId", "bar");
        json.addProperty("DeviceId", "bar");
        json.addProperty("PicUrl", "bar");
        json.addProperty("Name", "bar");
        json.addProperty("Email", "bar");
        json.addProperty("Lat", "bar");
        json.addProperty("Lng", "bar");
        json.addProperty("MobileType", 0);
        json.addProperty("SocialNetworkType", 0);

        Ion.with(HomeActivity.this, urlString)
        .setJsonObjectBody(json)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
           @Override
            public void onCompleted(Exception e, JsonObject result) {
                // do stuff with the result or error
               
               if(e !=null){
                   Log.d(TAG, "Exception : "+ e.toString());
               }
               
               if (result != null){
                   Log.d(TAG, "EE : "+ result.toString());
               }
            }
        });        
        
        
        /*
        AsyncHttpClient.getDefaultInstance().getJSONObject(urlString, new AsyncHttpClient.JSONObjectCallback() {

			@Override
			public void onCompleted(Exception e, AsyncHttpResponse response, JSONObject result) {

                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                System.out.println("I got a JSONObject: " + result);					
			}
            // Callback is invoked with any exceptions/errors, and the result, if available.

			
        });        
        //*/
        /*
        AsyncHttpClient.getDefaultInstance().getString(urlString, new AsyncHttpClient.StringCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                System.out.println("I got a string: " + result);
            }
        });
        */        
    }
    
    
    
    public PullToRefreshAttacher getPullToRefreshAttacher() {
        return mPullToRefreshAttacher;
    }
    
    @Override
    public void onStart() {
      super.onStart();      
      EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
      super.onStop();      
      EasyTracker.getInstance(this).activityStop(this);
    }        
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        
        
        
            
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        
        


        return super.onPrepareOptionsMenu(menu);
    }

    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_add:
            
            
            
            
            final Intent intent = new Intent().setClass(HomeActivity.this, AddNewMissionActivity.class);
            startActivity(intent);

            /*
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            */
            
            
            
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mCurrentPage == position) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                selectItem(position);
                mCurrentPage = position;
            }
        }
    }
    
    private Uri photoUri = null;
    private Uri tempUri = null;
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);        
        
        switch (requestCode) {
        case Constants.REQUEST_CODE_TAKE_PICTURE:
        	if (resultCode == RESULT_OK) {
        		Log.d(TAG, "REQUEST_CODE_TAKE_PICTURE is ok");
        		Log.d(TAG, "photoUri :"+photoUri.toString());
        	}
        	break;
        	
        case Constants.REQUEST_CODE_CHOOSE_EXISTING:
        	if (resultCode == RESULT_OK) {
        		Log.d(TAG, "REQUEST_CODE_CHOOSE_EXISTING is ok");
        	}
        	break;
        }
        
        
        /*
        if (data == null){
        	Log.d(TAG, "data is null");
        }
        
        if (tempUri != null) {
            photoUri = tempUri;
        } else if (data != null) {
            photoUri = data.getData();
            Log.d(TAG, "photoUri : " + photoUri.toString());
        }
        */
        
    }    

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        
        if (position == 0){
        	fragment = MissionListFragment.newInstance(0);
        } else if (position == 1){
            fragment = MissionListFragment.newInstance(1);
        } else if (position == 2){
            fragment = MissionListFragment.newInstance(2);
        } else if (position == 3){
            
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.clearMemoryCache();
            imageLoader.clearDiscCache();
            
            
            Session.getActiveSession().closeAndClearTokenInformation();
            final Intent intent = new Intent().setClass(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (position == 4){
        	photoUri = Utils.getTempUri();
            Utils.showPhotoChoice(this,photoUri);
        }
        
        invalidateOptionsMenu() ;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.main_menu_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }
    
    
    
}