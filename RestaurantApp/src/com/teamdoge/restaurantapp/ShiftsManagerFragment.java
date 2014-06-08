package com.teamdoge.restaurantapp;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TimePicker;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.ShiftManagerDialogFragment.OnFragmentInteractionListener;
import com.teamdoge.schedules.ListHeader;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.TwoTextArrayAdapter;
import com.teamdoge.shifts.ShiftList;

public class ShiftsManagerFragment extends ListFragment{
	
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
	
	private static List<ListItem> shiftItems;
	private List<String> dayShift = new ArrayList<String>();
	List<ParseObject> shiftList;
	ParseObject shift;
	private List<List<String>> shiftTimeList;

    private int[] headerID = new int[] { 0, 0, 0, 0, 0, 0, 0 }; 
    
    private final String[] day = new String[]{
        	"Sunday",
        	"Monday",
        	"Tuesday",
        	"Wednesday",
        	"Thursday",
        	"Friday",
        	"Saturday"
        };

	public static ScheduleFragment newInstance() {
		ScheduleFragment fragment = new ScheduleFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	public ShiftsManagerFragment() {
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
//		createScheduleList();
		

//		TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
//        setListAdapter(adapter);
		
//		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
//		task.execute();
       
		//new MyAsyncTaskHelper().execute();
		createShiftList();
		TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), shiftItems);
        setListAdapter(adapter);
        setRefreshActionButtonState(false);
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	@SuppressWarnings("unchecked")
	private void createShiftList() {
		
		ParseQuery<ParseObject> scheduleQuery = ParseQuery.getQuery("Schedule");
		scheduleQuery.whereEqualTo( "Id", restaurantID );
		try {
			shiftList = scheduleQuery.find();
			shift = shiftList.get(0);
		}
		catch (ParseException e2) {
			e2.printStackTrace();
			dayShift.add("");
		}

		 // Log.d("FAFA", day[0]);
		shiftTimeList = new ArrayList<List<String>>();
		for (int i = 0; i < day.length; i++) {
		  shiftTimeList.add((List)shift.getList(day[i]));
		  //Log.d("ASD", "" + i);
		  shiftTimeList.set(i, convertShifts(shiftTimeList.get(i)));
		}
		shiftItems = new ArrayList<ListItem>();
		int counter = 0;
		String shiftName;
		for (int i = 0; i < day.length; i++) {
		  shiftItems.add( new ListHeader( day[i] ));
		  headerID[i] = counter;
		  counter++;
		  for (int j = 0; j < shiftTimeList.get(i).size(); j++) {
			  shiftName = "Shift #" + (1 + j);
			  shiftItems.add(new ShiftList(shiftName, shiftTimeList.get(i).get(j), i, j));
			  counter++;
		  }
		  shiftItems.add(new ShiftList("Add Shift", i));
		  counter++;
		}
	}
	
	
	private List<String> convertShifts(List<String> shift) {
		//Log.d("ASD", "Calling converShift" + shift.size());
		for( int i = 0; i < shift.size(); i++ ){
			// tokenizes the string into two to get times
			String[] tokens = shift.get(i).split("[-|\\:]");
	    	
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
	    	// restructures the shift strings
	    	shift.set(i, tokens[0] + DASH + tokens[2]);
		}
		return shift;
	}
	
	public void asyncCaller() {
		Log.wtf("CMONNN", "INSIDE ASYNCCALLERRR");
    	setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}
	
	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			createShiftList();
			
			return shiftItems;
			
		}
		
		@Override
		protected void onPostExecute(List<ListItem> items) {
			TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
	        setListAdapter(adapter);
	        setRefreshActionButtonState(false);
		}
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;
		inflater.inflate(R.menu.refresh, menu);
	}
	
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
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		showNewTimesDialog();
	}
	
	public void showNewTimesDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();	
		
		final View layout = inflater.inflate(R.layout.fragment_shift_manager_dialog, null);
		
		builder.setView(layout)
		    .setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int id) {
		    		TimePicker startTime = (TimePicker)layout.findViewById(R.id.startTimePicker);
		    		TimePicker endTime = (TimePicker)layout.findViewById(R.id.endTimePicker);
		    		final Calendar c = Calendar.getInstance();
		    		int hour = c.get(Calendar.HOUR_OF_DAY);
		    		//startTime.toString()
		    		
		    		int startTimeHour = startTime.getCurrentHour();
		    		int startTimeMins = startTime.getCurrentMinute();
		    		String totalStartTime = startTimeHour + ":" + startTimeMins;
		    		
		    		int endTimeHour = endTime.getCurrentHour();
		    		int endTimeMins = endTime.getCurrentMinute();
		    		String totalEndTime = endTimeHour + ":" + endTimeMins;
		    		
		    		
		    		Log.wtf("HEY THERE Jimmy McJimmathon", "Time is: " + totalStartTime);
		    		Log.wtf("HEY THERE Mr. CLEAN", "Time is: " + totalEndTime);
		    		onDialogPositiveClick("");
		    	}
		    })
		    .setNegativeButton(R.string.Delete, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Delete this shift.
				}
			})
			.setNeutralButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//we do nothing since we don't actually need functionality here.
				}
			});
		
		builder.show();
	}

	public void onDialogPositiveClick(String stringy) {
		Log.wtf("alsdkfjlksdjfj", "sadjflsdjlkfjsdkl00");
	}
	
}
