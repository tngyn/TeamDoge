package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_shifts_management_activity);
		mContext = this;
		shiftsList = (ExpandableListView)findViewById(R.id.shifts);
		shifts = Shifts.getCategories();
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

	        // Other case statements...

	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
