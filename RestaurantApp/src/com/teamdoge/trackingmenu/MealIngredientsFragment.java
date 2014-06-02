package com.teamdoge.trackingmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MealIngredientsFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MealIngredientsFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class MealIngredientsFragment extends DialogFragment {
	
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
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private Spinner units_dropdown;
	
	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * @return A new instance of fragment MealIngredientsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MealIngredientsFragment newInstance(String param1, String param2) {
		MealIngredientsFragment fragment = new MealIngredientsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.fragment_meal_ingredients, null))
	    .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
	    	@Override
	    	public void onClick(DialogInterface dialog, int id) {
	    		mListener.onDialogPositiveClick(MealIngredientsFragment.this);
	    	}
	    })
	    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(MealIngredientsFragment.this);					
			}
		});
		
		Log.wtf("WE ARE HERE", "BEFORE PARSE INITIALIZE");
		
		Parse.initialize(getActivity(), "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		Log.wtf("WE ARE HERE", "AFTER PARSE INITIALIZE");
		units_dropdown = (Spinner) getView().findViewById(R.id.new_ingredient_units_dropdown);
		Log.wtf("WE ARE HERE", "BEFORE POPULATE");
		populateUnits("");
		Log.wtf("WE ARE HERE", "AFTER POPULATE");
	return builder.create();
	}

	public MealIngredientsFragment() {
		// Required empty public constructor
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
	
	public void populateUnits(String newUnits) {
    	//populate the units dropdown here

		//the list of strings that are added 
		List<String> added = new ArrayList<String>();
		
    	//get the current userID
    	String userId = "";
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser != null) {
			userId = currentUser.getString("Owner_Acc");
		}
		
		//Find all the foods that are tied to this id
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
		query.whereEqualTo("userId", userId);
		List<ParseObject> foods = null;
		try {
			foods = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//units datadapter that we're making
    	ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
    			                                    android.R.layout.simple_spinner_dropdown_item) {
    		@Override
    	    public View getView(int position, View convertView, ViewGroup parent) {

    	        View v = super.getView(position, convertView, parent);
    	        if (position == getCount()) {
    	            ((TextView)v.findViewById(android.R.id.text1)).setText("");
    	            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
    	        }

    	        return v;
    	    }
    		@Override
    	    public int getCount() {
    	        return super.getCount()-1; // you dont display last item. It is used as hint.
    	    }
    	};
    	
    	unitsdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//position we'll set the selection to
    	int position = 0;
    	
    	//if the newCategory is empty, we don't do add anything to the 0'th position
    	//otherwise we set the newCategory as the 0'th position.
		if (newUnits != "") {
			unitsdataAdapter.add(newUnits);
		}
    	
		//go through the foods and add their units to the units data adapter
		for (ParseObject food : foods) {
			String units = food.getString("units");
			// this if initially adds in a string to added the first time
			if (added.isEmpty()) {
				added.add(units);
				unitsdataAdapter.add(units);
			}
			// added isn't empty, so we will see if it's in there, if it isn't
			// we add to adapter
			else if (added.contains(units) == false) {
				added.add(units);
				unitsdataAdapter.add(units);
			}
		}
		
		//add in the new and select units manually (select units won't show up when dropped down)
    	unitsdataAdapter.add("New");
    	unitsdataAdapter.add("Select Units");
    	
    	//one more check for if newCategory is empty or not since we have to set the position if it is.
    	if (newUnits == "") {
			position = unitsdataAdapter.getCount();
		}
    	
    	units_dropdown.setAdapter(unitsdataAdapter);
    	units_dropdown.setSelection(position);
    	Log.wtf("DO WE GET HERE", "WE DOOOOO");
    	units_dropdown.setOnItemSelectedListener((OnItemSelectedListener) getActivity());
    }

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// Inflate the layout for this fragment
//		return inflater.inflate(R.layout.fragment_meal_ingredients, container,
//				false);
//	}
//
//	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}


}
