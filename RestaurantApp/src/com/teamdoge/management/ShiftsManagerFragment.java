package com.teamdoge.management;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TimePicker;

public class ShiftsManagerFragment extends ListFragment {
	
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
	ParseObject shiftObject;
	private List<List<String>> shiftTimeList;
	private List<List<String>> convertedShiftTimeList;
	private static String  totalStartTime;
	private static String  totalEndTime;
	private static int weekDay;
	private static String shift;
	private static boolean addNew;
	
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
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
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
		createShiftList();
		TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), shiftItems);
        setListAdapter(adapter);
        setRefreshActionButtonState(false);
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	public void showNewTimesDialog(int index) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();	

		final View layout = inflater.inflate(R.layout.fragment_shift_manager_dialog, null);

		TimePicker startTime = (TimePicker)layout.findViewById(R.id.startTimePicker);
		TimePicker endTime = (TimePicker)layout.findViewById(R.id.endTimePicker);
		ShiftList shiftList;
		shiftList =  (ShiftList) shiftItems.get(index);
		if (!addNew) {
			shift = shiftList.altShift;
			String[] tokens = shift.split("[-|\\:]");
			startTime.setCurrentHour(Integer.parseInt(tokens[0]));
			startTime.setCurrentMinute(Integer.parseInt(tokens[1]));
			endTime.setCurrentHour(Integer.parseInt(tokens[2]));
			endTime.setCurrentMinute(Integer.parseInt(tokens[3]));
		}
		weekDay = shiftList.weekDay;
		builder.setView(layout)
		    .setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int id) {
		    		TimePicker startTime = (TimePicker)layout.findViewById(R.id.startTimePicker);
		    		TimePicker endTime = (TimePicker)layout.findViewById(R.id.endTimePicker);
		    		DecimalFormat formatter = new DecimalFormat("00");
		    		int startTimeHour = startTime.getCurrentHour();
		    		int startTimeMins = startTime.getCurrentMinute();
		    		totalStartTime = formatter.format(startTimeHour) + ":" + formatter.format(startTimeMins);
		    		int endTimeHour = endTime.getCurrentHour();
		    		int endTimeMins = endTime.getCurrentMinute();
		    		totalEndTime = formatter.format(endTimeHour) + ":" + formatter.format(endTimeMins);
		    		if (startTimeHour < endTimeHour && addNew) {
		    			shiftTimeList.set(weekDay, addShift(totalStartTime + "-" + totalEndTime, weekDay));
		    			shiftObject.put(day[weekDay], shiftTimeList.get(weekDay));
		    			shiftObject.saveInBackground();
		    			updateShifts(shiftTimeList.get(weekDay).size(), weekDay);
		    			addNew = false;
		    		}
		    		else if (startTimeHour == endTimeHour && startTimeMins < endTimeMins && addNew) {
		    			shiftTimeList.set(weekDay, addShift(totalStartTime + "-" + totalEndTime, weekDay));
		    			shiftObject.put(day[weekDay], shiftTimeList.get(weekDay));
		    			shiftObject.saveInBackground();
		    			updateShifts(shiftTimeList.get(weekDay).size(), weekDay);
		    			addNew = false;
		    		}
		    		else if (startTimeHour < endTimeHour) {
		    			shiftTimeList.set(weekDay, editShift(totalStartTime + "-" + totalEndTime, weekDay));
		    			shiftObject.put(day[weekDay], shiftTimeList.get(weekDay));
		    			shiftObject.saveInBackground();
		    		}
		    		else if (startTimeHour == endTimeHour && startTimeMins < endTimeMins) {
		    			shiftTimeList.set(weekDay, editShift(totalStartTime + "-" + totalEndTime, weekDay));
		    			shiftObject.put(day[weekDay], shiftTimeList.get(weekDay));
		    			shiftObject.saveInBackground();
		    		}
		    	}
		    })
		    .setNegativeButton(R.string.Delete, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
	    			shiftTimeList.set(weekDay, deleteShift(weekDay));
	    			shiftObject.put(day[weekDay], shiftTimeList.get(weekDay));
	    			shiftObject.saveInBackground();
	    			updateShifts(shiftTimeList.get(weekDay).size(), weekDay);
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
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		boolean keepGoing = true;
		addNew = false;
		for (int i = 0; i < headerID.length; i++) {
			if (headerID[i] == id) {
				keepGoing = false;
			}
			if (headerID[i] -1 == id) {
				addNew = true;
			}
			if (id == shiftItems.size() - 1)
				addNew = true;
		}
		if (keepGoing)
		  showNewTimesDialog((int)id);
	}
	
	private List<String> addShift(String shift, int day) {
		List<String> temp = new ArrayList<String>();
		boolean added = false;
		for (int i = 0; i < shiftTimeList.get(day).size(); i++) {
			String[] tokens = shift.split("[-|\\:]");
			String[] altTokes = shiftTimeList.get(day).get(i).split("[-|\\:]");
			if (Integer.parseInt(tokens[0]) < Integer.parseInt(altTokes[0]) && !added) {
				temp.add(shift);
				added = true;
			}
			else if (Integer.parseInt(tokens[0]) == Integer.parseInt(altTokes[0]) && Integer.parseInt(tokens[1]) < Integer.parseInt(altTokes[1]) && !added) {
				temp.add(shift);
				added = true;
			}
			temp.add(shiftTimeList.get(day).get(i));
			if (i == shiftTimeList.get(day).size() -1 && !added) {
				temp.add(shift);
			}
		}
			
		return temp;
		
	}
	
	private List<String> editShift(String shifts, int day) {
		List<String> temp = new ArrayList<String>();
		boolean added = false;
		int index = shiftTimeList.get(day).indexOf(shift);
		for (int i = 0; i < shiftTimeList.get(day).size(); i++) {
			if (i == index)
				i++;
			if (i ==  shiftTimeList.get(day).size())
				break;
			String[] tokens = shifts.split("[-|\\:]");
			String[] altTokes = shiftTimeList.get(day).get(i).split("[-|\\:]");
			if (Integer.parseInt(tokens[0]) < Integer.parseInt(altTokes[0]) && !added) {
				temp.add(shifts);
				added = true;
			}
			else if (Integer.parseInt(tokens[0]) == Integer.parseInt(altTokes[0]) && Integer.parseInt(tokens[1]) < Integer.parseInt(altTokes[1]) && !added) {
				temp.add(shifts);
				added = true;
			}
			temp.add(shiftTimeList.get(day).get(i));
			if (i == shiftTimeList.get(day).size() -1 && !added) {
				temp.add(shifts);
			}
		}
		return temp;
		
	}
	public List<String> deleteShift( int day) {
		List<String> temp = new ArrayList<String>();
		int index = shiftTimeList.get(day).indexOf(shift);
		for (int i = 0; i < shiftTimeList.get(day).size(); i++) {
			if (i == index)
				i++;
			if (i ==  shiftTimeList.get(day).size())
				break;
			temp.add(shiftTimeList.get(day).get(i));
		}
		return temp;
	}
	
	public void updateShifts(int size, int days) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Shifts");
		query.whereEqualTo("Id", restaurantID);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < size; i++)
			list.add("0");
		try {
			List<ParseObject> sL = query.find();
			for (int i = 0;i < sL.size(); i++) {
				ParseObject s = sL.get(i);
				s.put(day[days], list);
				s.save();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
	
	@SuppressWarnings("unchecked")
	private void createShiftList() {
		
		ParseQuery<ParseObject> scheduleQuery = ParseQuery.getQuery("Schedule");
		scheduleQuery.whereEqualTo( "Id", restaurantID );
		try {
			shiftList = scheduleQuery.find();
			shiftObject = shiftList.get(0);
		}
		catch (ParseException e2) {
			e2.printStackTrace();
			dayShift.add("");
		}

		shiftTimeList = new ArrayList<List<String>>();
		convertedShiftTimeList = new ArrayList<List<String>>();
		for (int i = 0; i < day.length; i++) {
		  shiftTimeList.add((List)shiftObject.getList(day[i]));
		  convertedShiftTimeList.add((List)shiftObject.getList(day[i]));
		  convertedShiftTimeList.set(i,convertShifts(convertedShiftTimeList.get(i)));
		}
		shiftItems = new ArrayList<ListItem>();
		int counter = 0;
		String shiftName;
		for (int i = 0; i < day.length; i++) {
		  shiftItems.add( new ListHeader( day[i] ));
		  headerID[i] = counter;
		  counter++;
		  for (int j = 0; j < convertedShiftTimeList.get(i).size(); j++) {
			  shiftName = "Shift #" + (1 + j);
			  shiftItems.add(new ShiftList(shiftName, convertedShiftTimeList.get(i).get(j), i, shiftTimeList.get(i).get(j)));
			  counter++;
		  }
		  shiftItems.add(new ShiftList("Add Shift", i));
		  counter++;
		}
	}
	
	
	private List<String> convertShifts(List<String> shift) {
		List<String> temp = new ArrayList<String>();
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
	    	temp.add( tokens[0] + DASH + tokens[2]);
		}
		return temp;
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//
	
	public void asyncCaller() {
    	setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}

	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(Void... params) {
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
}
