package com.teamdoge.restaurantapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.teamdoge.schedules.*;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScheduleFragment extends ListFragment {
	
//	private OnFragmentInteractionListener mListener;
	
	// number of elements to display; if the number cannot be get from the
    // initial access, find the size from an array
	int numOfEmployees;
	int numOfShifts;
	
    // Strings for formating shift
    private final String AM = ":00 AM";
    private final String PM = ":00 PM";
    private final String DASH = " - ";
	
	// Parse-related
	private final String parse_key1 = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private final String parse_key2 = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
	private ParseUser user;
	private String restaurantID;
	private String weekDay;
	
	private List<ListItem> items;
	private List<String> dayShift = new ArrayList<String>();
	private List<String> displayShift = new ArrayList<String>();
	private List<ParseObject> shiftParseList = new ArrayList<ParseObject>();
	
	private ArrayList<String> NAME;
	private ArrayList<String> POSITION;
	private List<List<String>> TIME;

	
    // Array of integers points to images stored in /res/drawable/
    // Needs access to individual photos
    int[] img = new int[]{
    	R.drawable.earth,
    	R.drawable.jupiter,
    	R.drawable.mars,
    	R.drawable.mercury,
    	R.drawable.neptune,
    	R.drawable.saturn,
    	R.drawable.uranus,
    	R.drawable.venus,
    };
  
	
	public static ScheduleFragment newInstance() {
		ScheduleFragment fragment = new ScheduleFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	public ScheduleFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initializes connectivity to specific Parse database
				Parse.initialize(getActivity(), parse_key1, parse_key2);
				
				// Gets current Parse user
				user = ParseUser.getCurrentUser();
				
				// Gets restaurant ID
				restaurantID = user.getString("Owner_Acc");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		createScheduleList();
		

//		TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
//        setListAdapter(adapter);
		
		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();
       
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	@SuppressWarnings("unchecked")
	private void createScheduleList() {
		// GETS CURRENT DAY
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());
		
		ParseQuery<ParseObject> scheduleQuery = ParseQuery.getQuery("Schedule");
		scheduleQuery.whereEqualTo( "Id", restaurantID );
		try {
			List<ParseObject> scheduleList = scheduleQuery.find();
			ParseObject schedule = scheduleList.get(0);
			dayShift = schedule.getList(weekDay);
		}
		catch (ParseException e2) {
			e2.printStackTrace();
			dayShift.add("");
		}
		
		numOfShifts = dayShift.size();
		
		ParseQuery<ParseObject> shiftQuery = ParseQuery.getQuery("Shifts");
		shiftQuery.whereEqualTo( "Id", restaurantID );
		shiftQuery.orderByAscending("Name");
		try {
			shiftParseList = shiftQuery.find();	
			numOfEmployees = shiftParseList.size();
		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		NAME = new ArrayList<String>();
		POSITION = new ArrayList<String>();
		TIME = new ArrayList<List<String>>();
		
		for( int index = 0; index < numOfEmployees; ++index ) {
			ParseObject shift = shiftParseList.get(index);
			NAME.add( shift.getString("Name") );
			POSITION.add( shift.getString("Acc_Type") );
			TIME.add( (List)shift.getList(weekDay) );
		}
				
		items = new ArrayList<ListItem>();
		
		items.add( new ListHeader( weekDay ));
		
		if( numOfShifts > 0) {
			convertShifts();
		
			for( int headerCount = 0; headerCount < numOfShifts; ++headerCount ){
				items.add( new ListHeader(displayShift.get(headerCount)) );
				int postCount = 0;
				for( int shiftCount = 0; shiftCount < numOfEmployees; ++shiftCount ) {
					if (TIME.get(shiftCount).get(headerCount).length() == 1) {
					int shiftStatus = Integer.parseInt(TIME.get(shiftCount).get(headerCount));
					if( shiftStatus > 1 ) {
						items.add( new ScheduleList( 0, NAME.get(shiftCount),
								POSITION.get(shiftCount) ) );
						++postCount;
						}
					}
				}
				if( postCount == 0 ) {
					items.add( new ScheduleList( 0, "No Employees Assigned", " ") );
				}
			}
        }
		else {
			items.add( new ScheduleList( 0, "No Shift", " ") );
		}
	}
	
	private void convertShifts() {
		for( int shiftCounter = 0; shiftCounter < numOfShifts; ++shiftCounter ){
			// tokenizes the string into two to get times
			String[] tokens = dayShift.get(shiftCounter).split("[-]");
			// convert parsed tokens into integers
	    	int start = Integer.parseInt(tokens[0]);
	    	int end = Integer.parseInt(tokens[1]);
	    	
	    	// checks if start time is 12 AM
	    	if( start == 0 ) {
	    		tokens[0] = "12:00 AM";
	    	}
	    	// checks if start time is 12 PM
	    	else if( start == 12 ) {
	    		tokens[0] = "12:00 PM";
	    	}
	    	// otherwise converts start time
	    	else if( start < 12 ) {
	    		tokens[0] = "" + start + AM;
	    	}
	    	else {
	    		tokens[0] = "" + (start - 12) + PM;
	    	}
	    	
	    	// checks if end time is 12 AM
	    	if( end == 0 ) {
	    		tokens[1] = "12:00 AM";
	    	}
	    	// checks if end time is 12 PM
	    	else if( end == 12 ) {
	    		tokens[1] = "12:00 PM";
	    	}
	    	//otherwise converts end time
	    	else if( end < 12 ) {
	    		tokens[1] = "" + end + AM;
	    	}
	    	else {
	    		tokens[1] = "" + (end - 12) + PM;
	    	}
	    	
	    	// restructures the shift strings
	    	displayShift.add( tokens[0] + DASH + tokens[1]);
		}
	}
	
	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			createScheduleList();
			
			return items;
			
		}
		
		@Override
		protected void onPostExecute(List<ListItem> items) {
			TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
	        setListAdapter(adapter);
		}
		
	}
	
}