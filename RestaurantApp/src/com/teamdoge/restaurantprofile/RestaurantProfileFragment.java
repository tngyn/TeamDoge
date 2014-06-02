package com.teamdoge.restaurantprofile;

import com.teamdoge.restaurantapp.ManagerFragment.OnFragmentInteractionListener;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SuperAwesomeCardFragment;
import com.teamdoge.trackingmenu.AddMenuItemActivity;
import com.teamdoge.userprofile.Edit_Profile;
import com.astuetz.PagerSlidingTabStrip;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * {@link RestaurantProfileFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link RestaurantProfileFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class RestaurantProfileFragment extends Fragment {


	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * @return A new instance of fragment RestaurantProfileFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static RestaurantProfileFragment newInstance() {
		RestaurantProfileFragment fragment = new RestaurantProfileFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public RestaurantProfileFragment() {
		// Required empty public constructor
	}

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

		Log.d("ASD", "I HASD");
		tabs.setShouldExpand(true);
		
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
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
		
		// Added spaces in titles to even out in tab bar.
//		private final String[] TITLES = { "  My Shifts   ",
//				"    Schedule    ",
//				"Open Shifts" };

		private String[] TITLES = { "Employees",
				"Profile" };
		
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//switch (item.getItemId()) {

        if (item.getItemId()== R.id.item_add){
        	Intent intent = new Intent(getActivity(), AddMenuItemActivity.class);
        	startActivity(intent);
        	return true;
        }
           
        	else
            return super.onOptionsItemSelected(item);
	
    }

	   

}
