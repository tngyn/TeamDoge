package com.teamdoge.schedules;

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
import com.teamdoge.restaurantapp.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ScheduleFragment extends ListFragment {
	
//	private OnFragmentInteractionListener mListener;
	
	private Menu optionsMenu;
	
	// number of elements to display; if the number cannot be get from the
    // initial access, find the size from an array
	int numOfEmployees;
	int numOfShifts;
	
    // Strings for formating shift
    private final String AM = " AM";
    private final String PM = " PM";
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
		setHasOptionsMenu(true);
		// Initializes connectivity to specific Parse database
				Parse.initialize(getActivity(), parse_key1, parse_key2);
				
				// Gets current Parse user
				user = ParseUser.getCurrentUser();
				
				// Gets restaurant ID
				restaurantID = user.getString("Owner_Acc");
	}
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {

	        case R.id.menu_refresh:
	        	asyncCaller();
	        	return true;
	           
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
	public void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu
	            .findItem(R.id.menu_refresh);
	        if (refreshItem != null) {
	            if (refreshing) {
	                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
	            } else {
	                refreshItem.setActionView(null);
	            }
	        }
	    }
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		new MyAsyncTaskHelper().execute();
		
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;
		inflater.inflate(R.menu.refresh, menu);
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
	
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
			String[] tokens = dayShift.get(shiftCounter).split("[-|\\:]");
			
			// convert parsed tokens into integers
			// converts parsed tokens into integers
	    	int startHour = Integer.parseInt(tokens[0]);
	    	int endHour = Integer.parseInt(tokens[2]);
	    	
	    	// checks if start hour is 12 AM
	    	if( startHour == 0 ) {
	    		tokens[0] = "12:" + tokens[1] + AM;
	    	}
	    	// checks if start time is 12 PM
	    	else if( startHour == 12 ) {
	    		tokens[0] = "12:" + tokens[1] + PM;
	    	}
	    	// otherwise converts start time
	    	else if( startHour < 12 ) {
	    		tokens[0] = "" + startHour + ":" + tokens[1] + AM;
	    	}
	    	else {
	    		tokens[0] = "" + (startHour - 12) + ":" + tokens[1] + PM;
	    	}
	    	
	    	
	    	// checks if end hour is 12 AM
	    	if( endHour == 0 ) {
	    		tokens[2] = "12:" + tokens[3] + AM;
	    	}
	    	// checks if end time is 12 PM
	    	else if( endHour == 12 ) {
	    		tokens[2] = "12:" + tokens[3] + PM;
	    	}
	    	//otherwise converts end time
	    	else if( endHour < 12 ) {
	    		tokens[2] = "" + endHour + ":" + tokens[3] + AM;
	    	}
	    	else {
	    		tokens[2] = "" + (endHour - 12) + ":" + tokens[3] + PM;
	    	}
	    	
	    	
	    	// restructures the shift string
	    	displayShift.add(tokens[0] + DASH + tokens[2]);   	

		}
	}
	
	public void asyncCaller() {
    	setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}
	
	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(Void... params) {
			createScheduleList();
			
			return items;
			
		}
		
		@Override
		protected void onPostExecute(List<ListItem> items) {
			TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
	        setListAdapter(adapter);
	        setRefreshActionButtonState(false);
		}
	}	
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//
}
