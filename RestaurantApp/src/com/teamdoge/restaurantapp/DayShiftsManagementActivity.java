package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	protected List<ParseObject> scheduleList;
	protected List<ParseUser> usersList;
	protected ParseObject[] schedule;
	protected ParseUser[] users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		super.onCreate(savedInstanceState);
		ParseUser curruser = ParseUser.getCurrentUser();
	    final String DAY = this.getIntent().getStringExtra("Day");
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
		ParseQuery<ParseUser> userquery = ParseUser.getQuery();
		userquery.whereEqualTo("Owner_Acc", owner);

		Log.d("Check", "schedule is fine");
		try {
			usersList = userquery.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		
		Log.d("Check", DAY);
		String[] schedules;
		ArrayList temp;
		ParseObject scheduleObject;
		ParseObject userObject;
		String Availability = "Available_" + DAY;
		scheduleObject = scheduleList.get(0);
	    temp = (ArrayList) scheduleObject.get(DAY);
		schedules = new String[temp.size()];
		schedules = copy(temp, schedules);
		String[][] names = new String[temp.size()][usersList.size()];
		Log.d("ASD",schedules[0]);
		for (int i = 0; i < schedules.length; i++) {
			String[] available;
			for (int j = 0; j < usersList.size(); j++) {
				userObject = usersList.get(j);
				temp = (ArrayList) userObject.get(Availability);
				Log.d("Check", "So Far So Good");
				if (temp.get(i).equals("1")) {
				  String value = userObject.getString("Name") + ":" + userObject.getString("Acc_Type");
				  names[i][j] = value;
				}
			}
		}
		mContext = this;
		shiftsList = (ExpandableListView)findViewById(R.id.shifts);
		shifts = Shifts.getCategories(schedules, names);
		adapter = new SettingsListAdapter(this, 
				shifts, shiftsList);
        shiftsList.setAdapter(adapter);
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
							// add child category to parent's selection list
							category.selection.add(checkbox.getText().toString());
							
							// sort list in alphabetical order
							Collections.sort(category.selection, new CustomComparator());
						}
						else {
							// remove child category from parent's selection list
							category.selection.remove(checkbox.getText().toString());
						}		
						
						// display selection list
						sub.setText(category.selection.toString());
					}
				}				
				return true;
			}
		});
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
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
