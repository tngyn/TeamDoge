package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.teamdoge.schedules.ListHeader;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.MyShiftList;
import com.teamdoge.schedules.TwoTextArrayAdapter;


/*
 * Compiles the My Shifts tab view.  If there are no shifts for one day in the
 * week, insert a shift that says "No Shift" to keep the format consistent
 */
public class MyShiftFragment extends ListFragment {

	
	
	// number of elements to display; if the number cannot be get from the
    // initial access, find the size from an array
	private final int daysOfWeek = 7;
	
	// Message strings
	private final String dialogTradeInitial = "Would you like to post ";
	private final String dialogTradeInitialNot = "Would you like to remove ";
	private final String dialogTradeEndNot = " down from swapping?";
	private final String dialogTradeEnd = " up for swapping?";
	
	// Parse-related
	private final String parse_key1 = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private final String parse_key2 = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
    private ParseUser user;
    private String restaurantID;
    private String username;
    private List<String> temp;
	private static ParseObject shiftObject;
	// List to hold all headers and shifts, the week's schedule,
	// and the user's schedule
	private List<ListItem> items;
	private static List<ArrayList<String>> weekSchedule;
	private static List<ArrayList<String>> userSchedule;
	
	// check if an entire day has been requested to trade or drop
	private static boolean entireDay = false;
	
	// boundaries for checking an entire day off
	private static int start = 0;
	private static int end = 0;
	private static int index = -1;
	
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
    
	
	public static MyShiftFragment newInstance() {
		MyShiftFragment fragment = new MyShiftFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	 }
	
	public MyShiftFragment() {
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
		username = user.getString("username");
		weekSchedule = new ArrayList<ArrayList<String>>();
		userSchedule = new ArrayList<ArrayList<String>>();
		
		ParseQuery<ParseObject> scheduleQuery = ParseQuery.getQuery("Schedule");
		scheduleQuery.whereEqualTo( "Id", restaurantID );
		try{
			List<ParseObject> scheduleList = scheduleQuery.find();
			ParseObject schedule = scheduleList.get(0);
			for( int dayCounter = 0; dayCounter < daysOfWeek; ++dayCounter ) {
				weekSchedule.add( (ArrayList)schedule.getList(day[dayCounter]));
			}	
		}
		catch(ParseException e1) {
			e1.printStackTrace();
		}
		
		
		ParseQuery<ParseObject> shiftQuery = ParseQuery.getQuery("Shifts");
		shiftQuery.whereEqualTo( "Username", username );
		try {
			List<ParseObject> shiftList = shiftQuery.find();
			shiftObject = shiftList.get(0);
			for( int dayCounter = 0; dayCounter < daysOfWeek; ++dayCounter ) {
				userSchedule.add( (ArrayList)shiftObject.getList(day[dayCounter]));
			}	
		}
		catch(ParseException e2) {
			e2.printStackTrace();
		}
		
	}
	
	
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
				
				AlertDialog.Builder box = 
						new AlertDialog.Builder(getActivity());
				box.setTitle("Shift Posting");
				
				/* 
				 * Checks if a weekday header was selected.  If so, the
				 * entireDay boolean is set to true and the start/end
				 * boundaries are set based on the Ids of the headers based
				 * on their position in the items List
				 */
				// SUNDAY
				if( id == headerID[0] ) {
					box.setMessage( dialogTradeInitial + "all of your Sunday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[0] + 1;
					end = headerID[1];
					entireDay = true;
					index = 0;
				}
				// MONDAY
				else if( id == headerID[1] ) {
					box.setMessage( dialogTradeInitial + "all of your Monday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[1] + 1;
					end = headerID[2];
					entireDay = true;
					index = 1;
				}
				// TUESDAY
				else if( id ==  headerID[2] ) {
					box.setMessage( dialogTradeInitial + "all of your Tuesday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[2] + 1;
					end = headerID[3];
					entireDay = true;
					index = 2;
				}
				// WEDNESDAY
				else if( id == headerID[3] ) {
					box.setMessage( dialogTradeInitial + "all of your Wednesday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[3] + 1;
					end = headerID[4];
					entireDay = true;
					index = 3;
				}
				// THURSDAY
				else if( id == headerID[4] ) {
					box.setMessage( dialogTradeInitial + "all of your Thursday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[4] + 1;
					end = headerID[5];
					entireDay = true;
					index = 4;
				}
		        // FRIDAY
				else if( id == headerID[5] ) {
					box.setMessage( dialogTradeInitial + "all of your Friday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[5] + 1;
					end = headerID[6];
					entireDay = true;
					index = 5;
				}
				// SATURDAY
				else if( id == headerID[6] ) {
					box.setMessage( dialogTradeInitial + "all of your Saturday"
							+ " shifts" + dialogTradeEnd );
					start = headerID[6] + 1;
					end = items.size();
					entireDay = true;
					index = 6;
				}
				// SINGLE SHIFT SELECTED
				else {			
					MyShiftList shift = (MyShiftList)items.get( (int)id );
					if (userSchedule.get(shift.i).get(shift.j).equals("2"))
						box.setMessage( dialogTradeInitial + "this shift" 
								+ dialogTradeEnd );
						else if (userSchedule.get(shift.i).get(shift.j).equals("3"))
							box.setMessage( dialogTradeInitialNot + "this shift" 
									+ dialogTradeEndNot );
					entireDay = false;
				}
				
				// Sets up the yes/no buttons of the alert dialog pop-up
				box.setNegativeButton( "No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
				box.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText( getActivity().getBaseContext(), "Posted.",
								Toast.LENGTH_SHORT).show();
						
						/* 
						 * if only a single shift is selected, edit the
						 * MyShiftList object in the items list.  Otherwise,
						 * use the start/end boundaries to set a set the
						 * status of multiple shifts
						 */
						if( !entireDay ) {
							MyShiftList shift = (MyShiftList)items.get( (int)id );
							if (userSchedule.get(shift.i).get(shift.j).equals("2"))
								  userSchedule.get(shift.i).set(shift.j,"3");
								else if (userSchedule.get(shift.i).get(shift.j).equals("3"))
									  userSchedule.get(shift.i).set(shift.j,"2");
							int shiftStatus = Integer.parseInt(
		        					userSchedule.get(shift.i).get(shift.j) );
							shift.setStatus(shiftStatus);
							items.set( (int)start, shift );
							entireDay = false;
							temp = userSchedule.get(shift.i);
							shiftObject.put(day[shift.i], temp);
							try {
								shiftObject.save();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} 
						else {
							for(; start < end; ++start ) {
								for (int i = 0; i < userSchedule.get(index).size(); i++) {
									if (userSchedule.get(index).get(i).equals("2"))
									  userSchedule.get(index).set(i,"3");
								}
								MyShiftList shift = (MyShiftList)items.get( (int)start );
								int shiftStatus = Integer.parseInt(
			        					userSchedule.get(shift.i).get(shift.j) );
								shift.setStatus(shiftStatus);
								items.set( (int)start, shift );
							}
							if (index != -1) {
								temp = userSchedule.get(index);
								shiftObject.put(day[index], temp);
								try {
									shiftObject.save();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							  
							start = 0;
							end = 0;
							entireDay = false;
							index = -1;
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
		
		prepareMyShiftList();

        TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
        setListAdapter(adapter);
       
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	
	@Override
	public void onResume() {		
	    super.onResume();
	}
	
	
	// PREPARES MY SHIFT LISTS
	private void prepareMyShiftList() {	
		int counter = 0;
		
        items = new ArrayList<ListItem>();
        
        for( int headerCount = 0; headerCount < daysOfWeek; ++headerCount ) {
        	items.add( new ListHeader(day[headerCount]) );
        	headerID[headerCount] = counter;
        	int curCount = counter;
        	int scheduleBound = userSchedule.get(headerCount).size();
        	for( int shiftCount = 0; shiftCount < scheduleBound; ++shiftCount ) {
        		if( userSchedule.get(headerCount).get(shiftCount).length() == 1) {
        			int shiftStatus = Integer.parseInt(
        					userSchedule.get(headerCount).get(shiftCount) );
        			if (shiftStatus == 0 || shiftStatus == 1) {
        				
        			}
        			if( shiftStatus == 2  ) {
        				items.add( new MyShiftList(
        						weekSchedule.get(headerCount).get(shiftCount),
        						user.getString("Acc_Type"), " ", headerCount, shiftCount ) );
        				++counter;
        			}
        			if( shiftStatus == 3 ) {
        				items.add( new MyShiftList(
        						weekSchedule.get(headerCount).get(shiftCount),
        						user.getString("Acc_Type"), "Trade pending", headerCount, shiftCount ) );
        				++counter;
        			}
        		} else {
        			items.add( new MyShiftList(
        					weekSchedule.get(headerCount).get(shiftCount),
        					user.getString("Acc_Type"), "Pending approval", headerCount, shiftCount ) );
    				++counter;
        		}
        	}
        	if( curCount == counter ) {
        	  items.add( new MyShiftList( "No Shift", " ", " " ));
        	  ++counter;
        	}
        	++counter;
        }	
		
	}
	
	private void postShift() {
	
	}

}