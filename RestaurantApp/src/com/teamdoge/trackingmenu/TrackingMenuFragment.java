package com.teamdoge.trackingmenu;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.ManagerFragment;
import com.teamdoge.restaurantapp.ManagerFragment.OnFragmentInteractionListener;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SuperAwesomeCardFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TrackingMenuFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TrackingMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 
 */
public class TrackingMenuFragment extends Fragment {

	private OnFragmentInteractionListener mListener;

	ParseUser user;
	private String accountType;
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * @return A new instance of fragment TrakingMenu.
	 */
	// TODO: Rename and change types and number of parameters
	public static TrackingMenuFragment newInstance() {
		TrackingMenuFragment fragment = new TrackingMenuFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TrackingMenuFragment() {
		// Required empty public constructor
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	public static final String TAG = TrackingMenuFragment.class
			.getSimpleName();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
		
		Parse.initialize(getActivity(), "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		
		user = ParseUser.getCurrentUser();
	    accountType = user.getString("Acc_Type");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
		
		tabs.setShouldExpand(true);
		
		if (accountType.equals("Employee")) {
			tabs.setIndicatorHeight(0);
		}
		
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}
		
		// Added spaces in titles to even out in tab bar.
//		private final String[] TITLES = { "  My Shifts   ",
//				"    Schedule    ",
//				"Open Shifts" };

		private String[] TITLES = { "Menu",
				"Statistics" };
		
		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			
			if (accountType.equals("Employee")) {
				return TITLES.length - 1;
			}
			
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {

			default:
				return SuperAwesomeCardFragment.newInstance(position);
			}
		}

	}
	
	
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {

	        case R.id.item_add:
	        	Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);
	        	startActivity(intent);
	        	return true;
	           
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
}
