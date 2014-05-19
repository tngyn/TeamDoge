package com.teamdoge.restaurantapp;

import java.util.Arrays;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class Tab extends FragmentActivity {
	TabAdapter adapter;
	ViewPager mViewPager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		final ActionBar actionBar = getActionBar();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		ParseUser.logInInBackground("my name", "my pass", new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    } else {
			    	
			    }
			  }
			});
		ParseObject o = new ParseObject("Schedule");
		o.addAllUnique("AvailableTimes", Arrays.asList("4-5","5-6","6-7"));
		o.saveInBackground();
		ParseUser u = ParseUser.getCurrentUser();
		String id = o.getObjectId();
		u.put("Work", id);
		/**ParseUser user = new ParseUser();
		user.setUsername("my name");	
		user.setPassword("my pass");
		user.setEmail("asdf@yahoo.com");
		user.addAllUnique("StartTime", Arrays.asList("1-2","2-3","3-4"));
		user.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      // Hooray! Let them use the app now.
			    } else {
			      ParseUser.logOut();
			      finish();
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    }
			  }
		});*/
		
		adapter = new TabAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        mViewPager.setAdapter(adapter);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	mViewPager.setCurrentItem(tab.getPosition());
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        }
	    };
		/**for(int i = 0; i < 3; i++) {
		  actionBar.addTab(actionBar.newTab()
				  .setText("Tab " + (i+1)).setTabListener(tabListener));
		} */
	    ActionBar.Tab tab1 = actionBar.newTab();
	    tab1.setText("Tab " + 1);
	    tab1.setTabListener(tabListener);
	    actionBar.addTab(tab1);
	    ActionBar.Tab tab2 = actionBar.newTab();
	    tab2.setText("Tab " + 2);
	    tab2.setTabListener(tabListener);
		actionBar.addTab(tab2);
	    ActionBar.Tab tab3 = actionBar.newTab();
	    tab3.setText("Tab " + 3);
	    tab3.setTabListener(tabListener);
	    actionBar.addTab(tab3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if( id == R.id.Return) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onPause() {
		super.onPause();
		finish();
	}
	
	protected void onResume() {
		super.onResume();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/**public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab, container,
					false);
			return rootView;
		}
	}
	
*/
} 
