package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.login.LoginActivity;
import com.teamdoge.login.SignUpActivity;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.TwoTextArrayAdapter;
import com.teamdoge.userprofile.ShiftSelectorActivity.MyAsyncTaskHelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
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
	private ArrayList temp;
	private ParseObject scheduleObject;
	private static boolean[][] working;
	private static int[] indexArray;
    private final String AM = ":00 AM";
    private final String PM = ":00 PM";
    private final String DASH = " - ";
    protected static String[][] names;
    protected static String Day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		super.onCreate(savedInstanceState);
	    Day = this.getIntent().getStringExtra("Day");
		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();
       
        
        
	}

	protected void createView() {
		
		adapter = new SettingsListAdapter(this, 
				shifts, shiftsList);
        shiftsList.setAdapter(adapter);
		button = findViewById(R.id.refresh);
		button.setVisibility(View.VISIBLE);
	}

	protected void getData() {
		ParseUser curruser = ParseUser.getCurrentUser();
	    owner = curruser.getString("Owner_Acc");
		setContentView(R.layout.activity_day_shifts_management_activity);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
		query.whereEqualTo("Id", owner);

		Log.d("Check", "So far so good");
		try {
			scheduleList = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		ParseQuery<ParseObject> shiftquery = ParseQuery.getQuery("Shifts");
		shiftquery.whereEqualTo("Id", owner);

		Log.d("Check", "schedule is fine");
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
	
	private void convertShifts() {
		for( int shiftCounter = 0; shiftCounter < schedules.length; ++shiftCounter ){
			// tokenizes the string into two to get times
			String[] tokens = schedules[shiftCounter].split("[-]");
			// convert parsed tokens into integers
	    	int start = Integer.parseInt(tokens[0]);
	    	int end = Integer.parseInt(tokens[1]);
	    	
	    	// checks if start time is 12 AM
	    	if( start == 0 ) {
	    		tokens[0] = "12:00 AM";
	    	}
	    	
	    	else if (start == 12) {
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
	    	
	    	else if (end == 12) {
	    		tokens[0] = "12:00 PM";
	    	}
	    	//otherwise converts end time
	    	else if( end < 12 ) {
	    		tokens[1] = "" + end + AM;
	    	}
	    	else {
	    		tokens[1] = "" + (end - 12) + PM;
	    	}
	    	
	    	// restructures the shift strings
	       schedules[shiftCounter] = ( tokens[0] + DASH + tokens[1]);
		}
	}

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
    						  Log.d("TESTING", shiftObject.getString("Name"));
    					      Toast.makeText(getApplicationContext(),"Scheduled Compiled", Toast.LENGTH_LONG).show();
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
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

