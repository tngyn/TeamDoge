package com.teamdoge.restaurantapp;

import android.content.Intent;
import android.content.res.Configuration;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.teamdoge.inventory.InventoryListFragment;
import com.teamdoge.login.LoginActivity;
import com.teamdoge.management.ManagementTabStripFragment;
import com.teamdoge.management.ManagerFragment.OnFragmentInteractionListener;
import com.teamdoge.schedules.ScheduleTabStripFragment;
import com.teamdoge.userprofile.View_Profile;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener {
    
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;

	private String[] mDrawerSections;

	static ParseUser user;
	static ParseObject shiftUser;
	private static String accountType;
	
	ProgressBar bar;
	
	private Boolean isFrag0Visible;
	private Boolean isFrag1Visible;
	private Boolean isFrag2Visible;
	private Boolean isFrag3Visible;
	private ScheduleTabStripFragment frag0 ;
	private InventoryListFragment frag1;
	private View_Profile frag2;
	private ManagementTabStripFragment frag3;
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Parse.initialize(this, applicationId, clientKey);

		user = ParseUser.getCurrentUser();
	    accountType = user.getString("Acc_Type");

		initFragments();

		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


		if (accountType.equals("Owner") || accountType.equals("Manager")) {

			mDrawerSections = getResources().getStringArray(R.array.drawer_sections_array_for_owner);

		}
		else
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
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//

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
			
			if (accountType.equals("Owner") || accountType.equals("Manager")) {
				selectItemForOwner(position);
			}
			else
				selectItem(position);
		}
	}

	public void selectItemForOwner(int position) {
		mDrawerLayout.closeDrawer(mDrawerList);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		switch (position) {
		
		// Schedule
		case 0:
			
			if (isFrag0Visible) {
				ft.show(frag0);
			}
			else {
				ft.hide(frag1);
				ft.hide(frag2);
				ft.show(frag0);
				ft.hide(frag3);
				isFrag0Visible = true;
				isFrag1Visible = false;
				isFrag2Visible = false;
				isFrag3Visible = false;
			}
			ft.commit();
			
			getActionBar().setTitle("Schedule");
			break;

		// Inventory
		case 1:
			
			if (isFrag1Visible) {
				ft.show(frag1);
			}
			else {
				ft.hide(frag0);
				ft.hide(frag2);
				ft.show(frag1);
				ft.hide(frag3);
				isFrag0Visible = false;
				isFrag1Visible = true;
				isFrag2Visible = false;
				isFrag3Visible = false;
			}
			ft.commit();
			
			getActionBar().setTitle("Inventory");
			break;

		// Profile
		case 2:

			if (isFrag2Visible) {
				ft.show(frag2);
			}
			else {
				ft.hide(frag0);
				ft.hide(frag1);
				ft.show(frag2);
				ft.hide(frag3);
				isFrag0Visible = false;
				isFrag1Visible = false;
				isFrag2Visible = true;
				isFrag3Visible = false;
			}
			ft.commit();
			
			getActionBar().setTitle("Profile");
			break;
		
		// Management
		case 3:
			if (isFrag3Visible) {
				ft.show(frag3);
			}
			else {
				ft.hide(frag0);
				ft.hide(frag1);
				ft.hide(frag2);
				ft.show(frag3);
				isFrag0Visible = false;
				isFrag1Visible = false;
				isFrag2Visible = false;
				isFrag3Visible = true;
			}
			ft.commit();
			
			getActionBar().setTitle("Management");
			break;
			
		// Logout
		case 4:

			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
	    	startActivity(intent);
	    	user.put("Remember_Me", false);
	    	user.saveInBackground();
	    	ParseUser.logOut();
	    	user = ParseUser.getCurrentUser();
	    	finish();
	    	break;
	    	
		}
	}

	private void selectItem(int position) {

		mDrawerLayout.closeDrawer(mDrawerList);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		switch (position) {
		// Schedule
		case 0:
			
			if (isFrag0Visible) {
				ft.show(frag0);
			}
			else {
				ft.hide(frag1);
				ft.hide(frag2);
				ft.show(frag0);
				isFrag0Visible = true;
				isFrag1Visible = false;
				isFrag2Visible = false;
			}
			ft.commit();
			
			getActionBar().setTitle("Schedule");
			break;

		// Inventory
		case 1:
			
			if (isFrag1Visible) {
				ft.show(frag1);
			}
			else {
				ft.hide(frag0);
				ft.hide(frag2);
				ft.show(frag1);
				isFrag0Visible = false;
				isFrag1Visible = true;
				isFrag2Visible = false;
			}
			ft.commit();
			
			getActionBar().setTitle("Inventory");
			break;

		// Profile
		case 2:

			if (isFrag2Visible) {
				ft.show(frag2);
			}
			else {
				ft.hide(frag0);
				ft.hide(frag1);
				ft.show(frag2);
				isFrag0Visible = false;
				isFrag1Visible = false;
				isFrag2Visible = true;
			}
			ft.commit();
			
			getActionBar().setTitle("Profile");
			break;
		
		// Logout
		case 3:
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
	    	startActivity(intent);
	    	user.put("Remember_Me", false);
	    	user.saveInBackground();
	    	ParseUser.logOut();
	    	user = ParseUser.getCurrentUser();
	    	finish();
	    	break;
		}
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.

		mDrawerToggle.syncState();
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onFragmentInteraction() {}

	private void initFragments() {
		
		frag0 = ScheduleTabStripFragment.newInstance();
		frag1 = InventoryListFragment.newInstance();
		frag2 = View_Profile.newInstance();
		
		isFrag0Visible = true;
		isFrag1Visible = false;
		isFrag2Visible = false;
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content, frag0);
		ft.add(R.id.content, frag1);
		ft.add(R.id.content, frag2);
		
		if (accountType.equals("Owner") || accountType.equals("Manager")) {
			frag3 = ManagementTabStripFragment.newInstance();
			isFrag3Visible = false;
			ft.add(R.id.content, frag3);
			ft.hide(frag3);
		}
		
		ft.hide(frag1);
		ft.hide(frag2);

		ft.commit();
	}
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//	
}
