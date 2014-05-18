package com.teamdoge.restaurantapp;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


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

//		int imageId = getResources().getIdentifier(
//				sections.toLowerCase(Locale.getDefault()), "drawable",
//				getActivity().getPackageName());
//		((ImageView) rootView.findViewById(R.id.image))
//				.setImageResource(imageId);
		
		// Set title bar
	    ((MainActivity) getActivity())
	            .setActionBarTitle(sections);
		
		return rootView;
	}
}