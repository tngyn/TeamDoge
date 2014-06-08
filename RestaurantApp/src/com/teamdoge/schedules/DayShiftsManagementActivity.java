package com.teamdoge.schedules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.SettingsListAdapter;

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

public class DayShiftsManagementActivity extends Activity {

	private SettingsListAdapter adapter;
	private ExpandableListView shiftsList;
	private ArrayList<Shifts> shifts;
	private String owner;
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
	private String[] schedules;
	private ArrayList<?> temp;
	private ParseObject scheduleObject;
	private static boolean[][] working;
	private final String AM = " AM";
    private final String PM = " PM";
    private final String DASH = " - ";
    protected static String[][] names;
    protected static String Day;
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_day_shifts_management_activity);
		Parse.initialize(this, applicationId, clientKey);
		super.onCreate(savedInstanceState);
	    Day = this.getIntent().getStringExtra("Day");
		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();
       
		getActionBar().setTitle("Assign Shifts");
        
	}
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//

	protected void createView() {
		
		adapter = new SettingsListAdapter(this, 
				shifts, shiftsList);
        shiftsList.setAdapter(adapter);
		button = findViewById(R.id.refresh);
		button.setVisibility(View.VISIBLE);
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	@Override
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
	public String[] copy (ArrayList<?> source, String[] destination) {
		for (int i = 0; i < source.size(); i++) {
		  destination[i] = (String) source.get(i);
		}
		return destination;
	}
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

	protected void getData() {
		ParseUser curruser = ParseUser.getCurrentUser();
	    owner = curruser.getString("Owner_Acc");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
		query.whereEqualTo("Id", owner);

		try {
			scheduleList = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		ParseQuery<ParseObject> shiftquery = ParseQuery.getQuery("Shifts");
		shiftquery.whereEqualTo("Id", owner);

		try {
			shiftList = shiftquery.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		scheduleObject = scheduleList.get(0);
	    temp = (ArrayList<?>) scheduleObject.get(Day);
		schedules = new String[temp.size()];
		schedules = copy(temp, schedules);
		names = new String[temp.size()][shiftList.size()];
		working = new boolean[temp.size()][shiftList.size()];
		userNames = new ArrayList<String>();
		shiftsArray = new String[shiftList.size()][schedules.length];
		convertShifts();
		for (int i = 0;i < schedules.length; i++) {
			for (int j = 0; j < shiftList.size(); j++) {
				shiftObject = shiftList.get(j);
				if (i == 0) {
				  userNames.add(shiftObject.getString("Name"));
				}
				temp = (ArrayList<?>) shiftObject.get(Day);
				shiftsArray[j][i] = (String) temp.get(i);
				if (temp.get(i).equals("2")) 
				  working[i][j] = true;
				else
				  working[i][j] = false;
				if (!temp.get(i).equals("0")) {
				  String value = shiftObject.getString("Name") + ":" + shiftObject.getString("Acc_Type");
				  names[i][j] = value;
				}
			}
		}
		mContext = this;
		shiftsList = (ExpandableListView)findViewById(R.id.shifts);
		shifts = Shifts.getCategories(schedules, names, working);
	}
	
	public boolean setArray(int i, int j, String value) {
		shiftsArray[i][j] = value;
		return true;
	}
	
	public class CustomComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
	
	private void convertShifts() {
		for( int shiftCounter = 0; shiftCounter < schedules.length; ++shiftCounter ){
			// tokenizes the string into two to get times
			String[] tokens = schedules[shiftCounter].split("[-|\\:]");
	    	
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
	    	
	       schedules[shiftCounter] = ( tokens[0] + DASH + tokens[2]);
		}
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

	public class MyAsyncTaskHelper extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected Boolean doInBackground(Void... params) {
    	    getData();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

    		createView();
            super.onPostExecute(result);
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
    							
    							index = userNames.indexOf(checkbox.getText().toString());
    							setArray(index,groupPosition,"2");
    						}
    						else {
    							index = userNames.indexOf(checkbox.getText().toString());
    							setArray(index,groupPosition,"1");
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
    					  for (int i = 0; i < shiftList.size(); i++) {
    						  shiftObject = shiftList.get(i);
    					      final String DAY = Day;
    						  shiftObject.put(DAY, Arrays.asList(shiftsArray[i]));
    						  shiftObject.saveInBackground();
    						  
    					  }
    					  onBackPressed();
    					}
    				});
        }
    }
}

