package com.teamdoge.trackingmenu;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.ManagerFragment.OnFragmentInteractionListener;
import com.teamdoge.restaurantapp.ManagerFragment;
import com.teamdoge.restaurantapp.PageSlidingTabStripFragment;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SuperAwesomeCardFragment;
import com.teamdoge.restaurantapp.PageSlidingTabStripFragment.MyPagerAdapter;
import com.teamdoge.restaurantapp.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
//	// TODO: Rename parameter arguments, choose names that match
//	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";
//
//	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;
//
//	private OnFragmentInteractionListener mListener;
//
//	/**
//	 * Use this factory method to create a new instance of this fragment using
//	 * the provided parameters.
//	 * 
//	 * @param param1
//	 *            Parameter 1.
//	 * @param param2
//	 *            Parameter 2.
//	 * @return A new instance of fragment TrakingMenu.
//	 */
//	// TODO: Rename and change types and number of parameters
//	public static TrackingMenuFragment newInstance() {
//		TrackingMenuFragment fragment = new TrackingMenuFragment();
//		Bundle args = new Bundle();
//		
////		args.putString(ARG_PARAM1, param1);
////		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
//		return fragment;
//	}
//
//	public TrackingMenuFragment() {
//		// Required empty public constructor
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		setHasOptionsMenu(true);
////		if (getArguments() != null) {
////			mParam1 = getArguments().getString(ARG_PARAM1);
////			mParam2 = getArguments().getString(ARG_PARAM2);
////		}
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// Inflate the layout for this fragment
//		return inflater.inflate(R.layout.fragment_tracking_menu, container,
//				false);
//	}
//
//	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction();
//		}
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}
	
	
	ParseUser user;
	private String accountType;
	
	public static final String TAG = PageSlidingTabStripFragment.class
			.getSimpleName();

	public static PageSlidingTabStripFragment newInstance() {
		return new PageSlidingTabStripFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
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

		private String[] TITLES = { "My Shifts",
				"Schedule",
				"Open Shifts" };
		
		@Override
		public CharSequence getPageTitle(int position) {
			
			if (accountType.equals("Owner")) {
				TITLES[0] = "Set Shifts";
			}
			
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
			case 0:
				if (accountType.equals("Owner")) {
				  ManagerFragment m = new ManagerFragment();
				  return m;
				}
				else
				  return SuperAwesomeCardFragment.newInstance(position);
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
