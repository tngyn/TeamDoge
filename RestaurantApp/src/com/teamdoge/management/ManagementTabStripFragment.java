package com.teamdoge.management;

import com.teamdoge.management.ManagerFragment.OnFragmentInteractionListener;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SuperAwesomeCardFragment;
import com.astuetz.PagerSlidingTabStrip;

import android.support.v4.app.FragmentPagerAdapter;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ManagementTabStripFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link ManagementTabStripFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class ManagementTabStripFragment extends Fragment {


	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * @return A new instance of fragment ManagementTabStripFragment.
	 */
	public static ManagementTabStripFragment newInstance() {
		ManagementTabStripFragment fragment = new ManagementTabStripFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ManagementTabStripFragment() {
		// Required empty public constructor
	}

	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.pager,
				container, false);
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
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//

	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction();
		}
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

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

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		private String[] TITLES = { "Set Shifts", "Compile Schedules",
				"Employees" };

		
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
				ShiftsManagerFragment frag0 = new ShiftsManagerFragment();
				return frag0;
			case 1:
				ManagerFragment frag1 = new ManagerFragment();
				return frag1;
			case 2:
				EmployeeListFragment frag = EmployeeListFragment.newInstance();
				return frag;
			default:
				return SuperAwesomeCardFragment.newInstance(position);
			}
		}
    }
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

	   

}
