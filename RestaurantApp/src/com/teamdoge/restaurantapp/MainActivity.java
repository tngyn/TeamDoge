package com.teamdoge.restaurantapp;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.parse.Parse;
import com.parse.ParseUser;
import com.teamdodge.trackingmenu.TrackingMenuFragment;
import com.teamdoge.login.LoginActivity;
import com.teamdoge.restaurantapp.ManagerFragment.OnFragmentInteractionListener;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;




public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener {


    /**
     * Used to store the last screen title.
     * For use in {@link #restoreActionBar()}.                                   
     */
    private CharSequence mTitle;

    
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private String[] mDrawerSections;

	ParseUser user;
	private String accountType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		
		user = ParseUser.getCurrentUser();
	    accountType = user.getString("Acc_Type");
		
		if (accountType.equals("Owner")) {
			Toast.makeText(getApplicationContext(),"it works", Toast.LENGTH_LONG).show();
			Log.d("works", "works");
		}
		else {
			Toast.makeText(getApplicationContext(),accountType, Toast.LENGTH_LONG).show();
		}
		
		
		
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		
		
//		mTitle = mDrawerTitle = "testing something right now";
		mDrawerSections = getResources().getStringArray(R.array.drawer_sections_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerSections));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
		

	}

	
	public void setActionBarTitle(String title) {
	    getActionBar().setTitle(title);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {

		switch (item.getItemId()) {

			case android.R.id.home: {
				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
				break;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// The click listener for ListView in the navigation drawer :)
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void selectItem(int position) {

		switch (position) {
		// Schedule
		case 0:
			
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content,
					PageSlidingTabStripFragment.newInstance(),
					PageSlidingTabStripFragment.TAG).commit();
			getActionBar().setTitle("Schedule");
			break;
		
		// Inventory
		case 1:
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.content,
				InventoryList.newInstance()).commit();
			getActionBar().setTitle("Inventory");
			break;

			
		// Tracking Menu
		case 2:
			
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.content,
					TrackingMenuFragment.newInstance()).commit();
			getActionBar().setTitle("Tracking Menu");
			Log.wtf("test", "NOT IN CASE 2 LOL");
			break;
		
		// Profile
//		case 3:
//			break;
//			
		case 4:
			
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
	    	startActivity(intent);
	    	user.put("Remember_Me", false);
	    	user.saveInBackground();
	    	ParseUser.logOut();
	    	user = ParseUser.getCurrentUser();
	    	finish();
	    	break;
	    	
		default:

			Fragment fragment = new NavigationDrawerSections();
			Bundle args = new Bundle();
			args.putInt(NavigationDrawerSections.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
			break;
		}

		mDrawerLayout.closeDrawer(mDrawerList);
	}


	@Override
	public void onFragmentInteraction() {
		

		
	}
	
}