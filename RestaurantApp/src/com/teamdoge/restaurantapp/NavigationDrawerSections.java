package com.teamdoge.restaurantapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NavigationDrawerSections extends Fragment {
	public static final String ARG_SECTION_NUMBER = "nav_number";

	public NavigationDrawerSections() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nav_sections, container,
				false);
		int i = getArguments().getInt(ARG_SECTION_NUMBER);
		String sections = getResources().getStringArray(R.array.drawer_sections_array)[i];
		
		// Set title bar
	    ((MainActivity) getActivity())
	            .setActionBarTitle(sections);
		
		return rootView;
	}
}