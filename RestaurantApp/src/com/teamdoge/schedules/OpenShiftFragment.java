package com.teamdoge.schedules;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

public class OpenShiftFragment extends ListFragment {
	
	private Menu optionsMenu;
	
	// number of elements to display; if the number cannot be get from the
    // initial access, find the size from an array
	private final int daysOfWeek = 7;
	private int numOfEmployees;
	
	// Message strings
	private final String dialogAccept = "Would you like to take this shift?";
	private final String dialogApprove = "Would you like to approve of this trade?";
	private final String dialogWait = "This trade is currently awaiting approval.";
	
	// Parse-related
	private final String parse_key1 = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private final String parse_key2 = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
    private ParseUser user;
    private String restaurantID;
    private String username;
    private String accountType;
	private static ParseObject shiftObject;
	
	// List to hold all headers and shifts, the week's schedule,
	// and the user's schedule
	private List<ListItem> items;
	private List<String> temp;
	private static List<String> employeeNames;
	private static List<String> positionNames;
	private List<ArrayList<String>> daySchedule;
	private List<ArrayList<String>> userSchedule;
	private static List<ArrayList<String>> weekSchedule;
	
	private static List<ArrayList<ArrayList<String>>> allSchedule;
	
	private static boolean Trade = false;
	private static boolean Approve = false;
	private static boolean managerAccept = false;
	private static boolean fromManager = false;
	
    //Array of ints to denote header ids
    private int[] headerID = new int[] { 0, 0, 0, 0, 0, 0, 0 }; 
	
    // Array of integers points to images stored in /res/drawable/
    // Needs access to individual photos
    private final String[] day = new String[]{
    	"Sunday",
    	"Monday",
    	"Tuesday",
    	"Wednesday",
    	"Thursday",
    	"Friday",
    	"Saturday"
    };
   
    
	public static OpenShiftFragment newInstance() {
		OpenShiftFragment fragment = new OpenShiftFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	 }
	
	public OpenShiftFragment() {
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
		
		// Gets all IDs
		restaurantID = user.getString("Owner_Acc");
		username = user.getString("username");
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		/*
		 * If an item in the My Shifts tab is held down, an Alert Dialog will
		 * ask if the user wants to put the selected shift or the entire day's
		 * shifts up for trading or dropping.
		 * If yes, a function needs to be called that communicates with parse
		 * to edit the database's information.  This information will be used
		 * for the creation of the Open Shifts tab
		 */
		OnItemLongClickListener listener = new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, final long id) {
			
				// HEADERS CLICKED
				if( id == headerID[0] || id == headerID[1] ||
					id == headerID[2] || id == headerID[3] ||
					id == headerID[4] || id == headerID[5] ||
					id == headerID[6] ){
					return false;
				}
				
				OpenShiftList shift = (OpenShiftList)items.get( (int)id );
				// if no shift available clicked, don't do anything
				if( shift.weekDay < 0 && shift.empNum < 0 && shift.shiftNumber < 0 ) {
					return false;
				}
				
				AlertDialog.Builder box = 
						new AlertDialog.Builder(getActivity());
				box.setTitle("Shift Trading");
				
				if( allSchedule.get(shift.weekDay).get(shift.empNum).get(shift.shiftNumber).equals("3") ) {
					box.setMessage( dialogAccept );
					if( accountType.equals("Manager") || accountType.equals("Owner") ) {
						managerAccept = true;
					}
				}
				else {
					if( accountType.equals("Manager") || accountType.equals("Owner") ) {
						box.setMessage( dialogApprove );
						fromManager = true;
					}
					else {
						box.setMessage( dialogWait );
						AlertDialog alert = box.create();
						alert.show();
						return false;
					}
				}
				
				// Sets up the yes/no buttons of the alert dialog pop-up
				box.setNegativeButton( "No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
				box.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
					OpenShiftList shift = (OpenShiftList)items.get( (int)id );
					public void onClick(DialogInterface dialog, int whichButton) {
						if( managerAccept || fromManager ) {
							Toast.makeText( getActivity().getBaseContext(), "Trade approved.",
								Toast.LENGTH_SHORT).show();
							Approve = true;
							fromManager = false;
						}
						else {
							Toast.makeText( getActivity().getBaseContext(), "Trade up to be approved.",
									Toast.LENGTH_SHORT).show();
							Trade = true;
						}
						
						if( Trade ) {
							Trade( shift );
							Trade = false;
						}
						if( Approve ) {
							if( managerAccept ) {
								managerTrade( shift );
								managerAccept = false;
							} else {
								approve( shift );
							}
							Approve = false;
						}
					
						dialog.dismiss();
					}
						
				});

				
				AlertDialog alert = box.create();
				alert.show();
	
				return true;
			}
		};
		
		getListView().setOnItemLongClickListener(listener);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		new MyAsyncTaskHelper().execute();
		
		return super.onCreateView(inflater, container, savedInstanceState);		
	}

	@Override
	public void onResume() {		
	    super.onResume();
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
	
	// PREPARES MY SHIFT LISTS
	private void createOpenShiftList() {		
		// current Day	
		SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEEE", Locale.US);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		String today = weekdayFormat.format(calendar.getTime());
		
		String[] date = new String[7];
		SimpleDateFormat datesFormat = new SimpleDateFormat( "MMMM d" );
		for( int h = 0; h < 7; ++h ) {
			date[h] = datesFormat.format(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		
		int headerCount = 0;
		while( !day[headerCount].equals(today) ) {
			++headerCount;
		}
		
		int counter = 0;
		
        items = new ArrayList<ListItem>();
        
        int dayCount = 0;
        while( dayCount < daysOfWeek ) {
        
        	items.add( new ListHeader( day[headerCount] + ", " + date[dayCount] ) );
        	headerID[headerCount] = counter;
        	int curCount = counter;
        	
        	for( int pplCount = 0; pplCount < numOfEmployees; ++pplCount ) {
        		int shiftBound = allSchedule.get(headerCount).get(pplCount).size();
        		for( int shiftCount = 0; shiftCount < shiftBound; ++shiftCount ) {
        			if( allSchedule.get(headerCount).get(pplCount).get(shiftCount).length() == 1 ) {
        				int shiftStatus = Integer.parseInt(allSchedule.get(headerCount).get(pplCount).get(shiftCount));
        				if( shiftStatus == 3 ) {
        					items.add( new OpenShiftList( 0, weekSchedule.get(headerCount).get(shiftCount),
        						employeeNames.get(pplCount), positionNames.get(pplCount), "Open for Trade",
        						headerCount, pplCount, shiftCount));
        					++counter;
        				}
        			}
        			else {
        				String[] tradeWith = convertTradeName( headerCount, pplCount, shiftCount );
        				if( tradeWith[1].equals("0")) {
        					items.add( new OpenShiftList( 0, weekSchedule.get(headerCount).get(shiftCount),
        						employeeNames.get(pplCount), positionNames.get(pplCount),
        						"Pending approval with " + tradeWith[0],
        						headerCount, pplCount, shiftCount));
        					++counter;
        				}
        			}
        		}
        	}
        	
        	if( curCount == counter ) {
        		items.add( new OpenShiftList( 0, "No Trades Available", " ", " ", " " ) );
        		++counter;
        	}
        	++counter;
        	++dayCount;
        	if( headerCount == (daysOfWeek - 1) ) {
        	  headerCount = 0;
        	}
        	else {
        	  ++headerCount;
        	}
        }
		
	}
	
	// Method to tokenize the username in the shift and return a string array
	private String[] convertTradeName( int week, int person, int shift ) {
		// tokenizes the username in shift.
		// tokens[0] is the username
		// tokens[1] is the numeral: 0 - original shift owner; 1 - person who wants the shift
		String[] tokens = allSchedule.get(week).get(person).get(shift).split("[:]");
		
		return tokens;
		
	}
	
	// Function that deals with setting up a trade request
	private void Trade( OpenShiftList shift ) {
		// Parse Object to hold the other shift
		ParseObject otherShift;
		ParseQuery<ParseObject> otherUser = ParseQuery.getQuery("Shifts");
		otherUser.whereEqualTo("Name", shift.name);
		
		try{
			List<ParseObject> otherUserList = otherUser.find();
			otherShift = otherUserList.get(0);
			temp = allSchedule.get(shift.weekDay).get(shift.empNum);
			temp.set(shift.shiftNumber, username + ":0"); 
			allSchedule.get(shift.weekDay).get(shift.empNum).set(shift.shiftNumber,
					temp.get(shift.shiftNumber));
			otherShift.put( day[shift.weekDay], temp);
			try {
				otherShift.save();
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			
			temp = userSchedule.get(shift.weekDay);
			temp.set(shift.shiftNumber, otherShift.getString("Username") + ":1");
			shiftObject.put( day[shift.weekDay], temp);
			try {
				shiftObject.save();
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		asyncCaller();
	}
	
	
	private void managerTrade( OpenShiftList shift ) {
		// Parse Object to hold the other shift
		ParseObject otherShift;
		ParseQuery<ParseObject> otherUser = ParseQuery.getQuery("Shifts");
		otherUser.whereEqualTo("Name", shift.name);
		
		try{
			List<ParseObject> otherUserList = otherUser.find();
			otherShift = otherUserList.get(0);
			temp = allSchedule.get(shift.weekDay).get(shift.empNum);
			temp.set(shift.shiftNumber, "0"); 
			allSchedule.get(shift.weekDay).get(shift.empNum).set(shift.shiftNumber,
					"0");
			otherShift.put( day[shift.weekDay], temp);
			try {
				otherShift.save();
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			
			temp = userSchedule.get(shift.weekDay);
			temp.set(shift.shiftNumber, "2");
			shiftObject.put( day[shift.weekDay], temp);
			try {
				shiftObject.save();
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		asyncCaller();
	}
	
	
	
	private void approve( OpenShiftList shift ) {
		String[] user = convertTradeName(shift.weekDay, shift.empNum, shift.shiftNumber);
		
		// Parse objects
		ParseObject curShift;
		ParseQuery<ParseObject> curUser = ParseQuery.getQuery("Shifts");
		curUser.whereEqualTo("Name", shift.name);
		ParseQuery<ParseObject> otherUser = ParseQuery.getQuery("Shifts");
		otherUser.whereEqualTo("Username", user[0]);
		
		try{
			List<ParseObject> curUserList = curUser.find();
			List<ParseObject> otherUserList = otherUser.find();
			curShift = curUserList.get(0);
			shiftObject = otherUserList.get(0);
			
			temp = allSchedule.get(shift.weekDay).get(shift.empNum);
			
			if( user[1].equals("0") ) {
				temp.set(shift.shiftNumber, "0");
				allSchedule.get(shift.weekDay).
					get(shift.empNum).set(shift.shiftNumber, "0");
				curShift.put( day[shift.weekDay], temp );
				
				curShift.saveInBackground();
				
				
				temp = shiftObject.getList( day[shift.weekDay] );
				temp.set(shift.shiftNumber, "2");
				shiftObject.put( day[shift.weekDay], temp );
				shiftObject.saveInBackground();
			}
			else {
				temp.set(shift.shiftNumber, "2");
				allSchedule.get(shift.weekDay).
					get(shift.empNum).set(shift.shiftNumber, "2");
				curShift.put( day[shift.weekDay], temp );
				try{
					curShift.save();
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
				
				temp = shiftObject.getList( day[shift.weekDay] );
				temp.set(shift.shiftNumber, "0");
				shiftObject.put( day[shift.weekDay], temp );
				try{
					shiftObject.save();
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		asyncCaller();
	}
	
	
	
	private void createLists() {		
		// Initializes all arrays used
		employeeNames = new ArrayList<String>();
		positionNames = new ArrayList<String>();
		weekSchedule = new ArrayList<ArrayList<String>>();
		userSchedule = new ArrayList<ArrayList<String>>();
		allSchedule = new ArrayList<ArrayList<ArrayList<String>>>();
		
		// Queries for user's account type
		ParseQuery<ParseObject> accountQuery = ParseQuery.getQuery("Shifts");
		accountQuery.whereEqualTo("Username", username);
		try{
			List<ParseObject> accountList = accountQuery.find();
			ParseObject account = accountList.get(0);
			accountType = account.getString("Acc_Type");
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
		
		// Queries for the restaurant's shift schedules
		ParseQuery<ParseObject> scheduleQuery = ParseQuery.getQuery("Schedule");
		scheduleQuery.whereEqualTo( "Id", restaurantID );
		try{
			List<ParseObject> scheduleList = scheduleQuery.find();
			ParseObject schedule = scheduleList.get(0);
			for( int dayCounter = 0; dayCounter < daysOfWeek; ++dayCounter ) {
				weekSchedule.add( (ArrayList)schedule.getList(day[dayCounter]));
			}	
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
			
		// Queries for all employees via shifts
		// If a regular user, do not include the user in the view
		// Create an array based on Weekday, Employee, and the Shifts
		ParseQuery<ParseObject> shiftQuery = ParseQuery.getQuery("Shifts");
		shiftQuery.whereEqualTo( "Id", restaurantID );
		if( !accountType.equals("Manager") || !accountType.equals("Owner") ) {
			shiftQuery.whereNotEqualTo( "Username", username );
		}
		try {
			List<ParseObject> scheduleList = shiftQuery.find();
			numOfEmployees = scheduleList.size();
						
			for( int employeeCount = 0; employeeCount < numOfEmployees; ++employeeCount ) {
				shiftObject = scheduleList.get(employeeCount);
				employeeNames.add( shiftObject.getString("Name") );
				positionNames.add( shiftObject.getString("Acc_Type") );
			}
			
			for( int dayCount = 0; dayCount < daysOfWeek; ++dayCount ) {
				daySchedule = new ArrayList<ArrayList<String>>();
				for( int empCount = 0; empCount < numOfEmployees; ++empCount ) {
					shiftObject = scheduleList.get(empCount);
					daySchedule.add( (ArrayList)shiftObject.getList(day[dayCount]));
				}
				allSchedule.add( (ArrayList)daySchedule );
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Get current user's schedule information
		ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("Shifts");
		userQuery.whereEqualTo( "Username", username );
		try {
			List<ParseObject> userList = userQuery.find();
			shiftObject = userList.get(0);
			for( int dayCounter = 0; dayCounter < daysOfWeek; ++dayCounter ) {
				userSchedule.add( (ArrayList)shiftObject.getList(day[dayCounter]));
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void asyncCaller() {
    	setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}
	
	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {
		@Override
		protected List<ListItem> doInBackground(Void... params) {
			createLists();
			createOpenShiftList();
			
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
