package com.teamdoge.restaurantapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ManagerFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ManagerFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class ManagerFragment extends Fragment {


	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ManagerFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ManagerFragment newInstance() {
		ManagerFragment fragment = new ManagerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ManagerFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_manager, container, false);
		v.findViewById(R.id.monday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		v.findViewById(R.id.tuesday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		v.findViewById(R.id.wednesday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		v.findViewById(R.id.thursday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		v.findViewById(R.id.friday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		v.findViewById(R.id.saturday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		v.findViewById(R.id.sunday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						startActivity(intent);
					}
				});
		// Inflate the layout for this fragment
		return v;
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
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction();
		public void myClickMethod();
	}
	
}

