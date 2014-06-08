package com.teamdoge.schedules;


import com.astuetz.PagerSlidingTabStrip;
import com.parse.Parse;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SuperAwesomeCardFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ScheduleTabStripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events. Use the
 * {@link ScheduleTabStripFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class ScheduleTabStripFragment extends Fragment {
	
	ParseUser user;
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
	
	public static final String TAG = ScheduleTabStripFragment.class
			.getSimpleName();

	public static ScheduleTabStripFragment newInstance() {
		return new ScheduleTabStripFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		Parse.initialize(getActivity(), applicationId, clientKey);
		
		user = ParseUser.getCurrentUser();
		
	}

	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
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
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//


	public class MyPagerAdapter extends FragmentPagerAdapter {
		
		// *******************************************************************************************************************//
		// 													Model 														      //
		// *******************************************************************************************************************//

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		private String[] TITLES = { "My Shifts",
				"Schedule",
				"Open Shifts" };

		@Override
		public CharSequence getPageTitle(int position) {
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
					MyShiftFragment ms = new MyShiftFragment();
					return ms;
				case 1:
					ScheduleFragment schedule = new ScheduleFragment();
					return schedule;
				case 2:
					OpenShiftFragment openShift = new OpenShiftFragment();
					return openShift;
				default:
					return SuperAwesomeCardFragment.newInstance(position);
				}
		}
		
		// *******************************************************************************************************************//
		// 													End Model 														  //
		// *******************************************************************************************************************//
	}
	
}