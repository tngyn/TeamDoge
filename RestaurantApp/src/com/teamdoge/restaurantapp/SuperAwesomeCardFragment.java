package com.teamdoge.restaurantapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;



public class SuperAwesomeCardFragment extends Fragment {

	private static final String ARG_POSITION = "position";

	private int position;
	
	ParseUser user;
	private String accountType;

	public static SuperAwesomeCardFragment newInstance(int position) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(getActivity(), "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		
		user = ParseUser.getCurrentUser();
	    accountType = user.getString("Acc_Type");
	    
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
//				.getDisplayMetrics());

		TextView v = new TextView(getActivity());
//		params.setMargins(margin, margin, margin, margin);
//		v.setLayoutParams(params);
//		v.setLayoutParams(params);
		v.setGravity(Gravity.CENTER);
//		v.setBackgroundResource(R.drawable.background_card);
//		v.setText("CARD " + (position + 1));

		if (position == 0) {
			//CARD 1
			if (accountType.equals("Owner"))
				v.setText("I'm an Owner.");
			else
				v.setText("I'm an Employee.");
		}
		else if (position == 1) {
			//CARD 2
			v.setText("This is card 2. Hard coded to the max");
		}
		else if (position == 2) {
			//CARD 3
			v.setText("This is card 3. Hard coded to the max");
		}
		
		
		fl.addView(v);
		return fl;
	}

}