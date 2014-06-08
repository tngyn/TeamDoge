package com.teamdoge.userprofile;
import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.userprofile.ExpandableListAdapterTimePicker;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SettingsListAdapter;
import com.teamdoge.schedules.Shifts;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
 
public class ShiftSelectorActivity extends Activity {
 
    protected ExpandableListAdapterTimePicker listAdapter;
    protected View expListView;
    protected List<String> listDataHeader;
    protected List<List<String>> listDataChild;
    protected  List<List<String>> parseList;
    protected List<List<String>> userList;
    protected ParseQuery<ParseObject> query;
    protected ParseQuery<ParseObject> shiftsQuery;
    private final String AM = " AM";
    private final String PM = " PM";
    private final String DASH = " - ";
	private SettingsListAdapter adapter;
	private ExpandableListView shiftsList;
	private ArrayList<Shifts> shifts;
	protected Context mContext;
	protected static List<ParseObject> scheduleList;
	protected static List<ParseObject> shiftList;
	protected ParseObject[] shceduleObjectArray;
	protected ParseObject[] shiftObjectArray;
	protected ArrayList<String> userNames;
	protected int index;
	protected static String[][] shiftsArray;
	private View button;
	private ParseObject shiftObject;
	private String name;
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Parse.initialize(this, applicationId, clientKey);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_shifts_management_activity);
		listDataHeader = new ArrayList<String>();
        listDataChild = new ArrayList<List<String>>();
        userList = new ArrayList<List<String>>();
 
        // Adding child data
        listDataHeader.add("Monday");
        listDataHeader.add("Tuesday");
        listDataHeader.add("Wednesday");
        listDataHeader.add("Thursday");
        listDataHeader.add("Friday");
        listDataHeader.add("Saturday");
        listDataHeader.add("Sunday");
		query = ParseQuery.getQuery("Schedule");
		ParseUser user = ParseUser.getCurrentUser();
		name = user.getUsername();
		String restaurantId = user.getString("Owner_Acc");
		shiftsQuery = ParseQuery.getQuery("Shifts");
		shiftsQuery.whereEqualTo("Username", name);
		try {
			List<ParseObject> scheduleList = shiftsQuery.find();
			shiftObject = scheduleList.get(0);
			for (int i = 0; i < listDataHeader.size(); i++) {
				userList.add((List) shiftObject.getList(listDataHeader.get(i)));
			}

		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}

		query.whereEqualTo( "Id", restaurantId );

        
		try {
			List<ParseObject> scheduleList = query.find();
			ParseObject schedule = scheduleList.get(0);
			parseList = new ArrayList<List<String>>();

			
			for (int i = 0; i < listDataHeader.size(); i++) {
				parseList.add((List) schedule.getList(listDataHeader.get(i)));
				convertShifts(parseList.get(i));
			}
		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}
        for (int i = 0; i < 7; i++) {
        	listDataChild.add(parseList.get(i)); // Header, Child data

        }

		mContext = this;
		shiftsList = (ExpandableListView)findViewById(R.id.shifts);
		shifts = Shifts.getCategories(listDataHeader, listDataChild, userList);
		adapter = new SettingsListAdapter(this, 
				shifts, shiftsList);
        shiftsList.setAdapter(adapter);

		button = findViewById(R.id.refresh);
		button.setVisibility(View.VISIBLE);
		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();
    }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
    
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
    
    /*
     * Preparing the list data
     */
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {
	        case android.R.id.home:	
	        	onBackPressed();
	            this.finish();

	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

	protected void aSyncThread() {

        shiftsList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				
				CheckedTextView checkbox = (CheckedTextView)v.findViewById(R.id.list_item_text_child);
				checkbox.toggle();
				
				// find parent view by tag
				View parentView = shiftsList.findViewWithTag(shifts.get(groupPosition).name);
				if(parentView != null) {
					TextView sub = (TextView)parentView.findViewById(R.id.list_item_text_subscriptions);
					
					if(sub != null) {
						Shifts category = shifts.get(groupPosition);
						if(checkbox.isChecked()) {
							if (userList.get(groupPosition).get(childPosition).equals("0"))
							  setValue(groupPosition,childPosition,"1");
						}
						else {
							setValue(groupPosition,childPosition,"0");
						}		
						
						// display selection list
						sub.setText(category.selection.toString());
					}
				}				
				return true;
			}
		});
		button = findViewById(R.id.refresh);
		button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
					  for (int i = 0; i < listDataHeader.size(); i++) {
						  shiftObject.put(listDataHeader.get(i), (userList.get(i)));
						  shiftObject.saveInBackground();
						  
					  }
					  onBackPressed();
					}
				});
	}
 
	public boolean setValue(int i, int j, String value) {
		userList.get(i).set(j, value);
		return true;
	}

	private void convertShifts(List<String> times) {
		for( int shiftCounter = 0; shiftCounter < times.size(); ++shiftCounter ){
			// tokenizes the string into two to get times
			String[] tokens = times.get(shiftCounter).split("[-|\\:]");
	    	
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
	    	times.set(shiftCounter, tokens[0] + DASH + tokens[2]);
		}
	}
	public String[] copy (ArrayList<?> source, String[] destination) {
		for (int i = 0; i < source.size(); i++) {
		  destination[i] = (String) source.get(i);
		}
		return destination;
	}

	public class MyAsyncTaskHelper extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... params) {
            aSyncThread();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            /**
             * update ui thread and remove dialog
             */
            super.onPostExecute(result);
        }
    }
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//
}
