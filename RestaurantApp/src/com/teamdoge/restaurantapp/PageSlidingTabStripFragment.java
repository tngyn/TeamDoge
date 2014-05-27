package com.teamdoge.restaurantapp;


import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;
import com.parse.ParseUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.Parse;
import com.parse.ParseUser;
/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link PageSlidingTabStripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events. Use the
 * {@link PageSlidingTabStripFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class PageSlidingTabStripFragment extends Fragment {
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


}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


//	public static final String TAG = PageSlidingTabStripFragment.class
//			.getSimpleName();
//
//	public static PageSlidingTabStripFragment newInstance() {
//		return new PageSlidingTabStripFragment();
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setRetainInstance(true);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.pager, container, false);
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//
//		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
//				.findViewById(R.id.tabs);
//		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
//		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
//		pager.setAdapter(adapter);
//		tabs.setViewPager(pager);
//
//	}
//
//	public class MyPagerAdapter extends FragmentPagerAdapter {
//
//		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
//			super(fm);
//		}
//
//		private final String[] TITLES = { "Categories", "Home", "Top Paid",
//				"Top Free" };
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return TITLES[position];
//		}
//
//		@Override
//		public int getCount() {
//			return TITLES.length;
//		}
//
//		@Override
//		public Fragment getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
////		@Override
////		public SherlockFragment getItem(int position) {
////			return SuperAwesomeCardFragment.newInstance(position);
////		}
//
//	}
//
//}