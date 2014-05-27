package com.teamdoge.schedules;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class MyShiftList implements ListItem {
	// Original hour block of the shift
	private String shift;
	// Display string for shift
	private String displayShift;
    // Position the employee has for that shift
    private final String position;
    // Status of shift
    private String status;
    public int i;
    public int j;
    
    
    // Strings for formating shift
    private final String AM = ":00 AM";
    private final String PM = ":00 PM";
    private final String DASH = " - ";

    // Constructor to create a schedule list item
    public MyShiftList( String time, String pos, String stat ) {
    	this.shift = time;
    	this.displayShift = time;
        this.position = pos;
        this.status = stat;
    }
    
    public MyShiftList( String time, String pos, String stat, int i , int j) {
    	this.shift = time;
    	this.displayShift = time;
        this.position = pos;
        this.status = stat;
        this.i = i;
        this.j = j;
    }

    // implementation needed for the TwoTextArrayAdapter
    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }
    
    public void setStatus( int stat ) {
    	switch(stat) {
    		case 0:
    			status = " ";
    			break;
    		case 1:
    			status = "Trade pending";
    			break;
    		case 2:
    			status = "Pending approval";
    			break;
    		default:
    			status = " ";
    	}
    }

    // Correlates the passed in strings and images to the fields in the layout to
    // return each individual shift block
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            //view = (View) inflater.inflate(R.layout.schedule_list_items, null);
            view = (View) inflater.inflate(R.layout.fragment_my_shift_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView time = (TextView) view.findViewById(R.id.shift_hours);
        TextView role = (TextView) view.findViewById(R.id.position);
        TextView stat = (TextView) view.findViewById(R.id.status);
       
    	if( new String("No Shift").equals(shift) == false ) {
          convertShift();
    	}
        
        time.setText(displayShift);
        role.setText(position);
        stat.setText(status);

        return view;
    }
    
    private void convertShift(){
    	// tokenizes the string into two to get the times
    	String[] tokens = shift.split("[-]");
    	
    	// converts parsed tokens into integers
    	int start = Integer.parseInt(tokens[0]);
    	int end = Integer.parseInt(tokens[1]);
    	
    	// checks if start time is 12 AM
    	if( start == 0 ) {
    		tokens[0] = "12:00 AM";
    	}
    	
    	// converts start time
    	if( start < 12 ) {
    		tokens[0] = "" + start + AM;
    	}
    	else {
    		tokens[0] = "" + start + PM;
    	}
    	
    	// checks if end time is 12 AM
    	if( end == 0 ) {
    		tokens[1] = "12:00 AM";
    	}
    	
    	//converts end time
    	if( end < 12 ) {
    		tokens[1] = "" + end + AM;
    	}
    	else {
    		tokens[1] = "" + end + PM;
    	}
    	
    	// restructures the shift string
    	displayShift = tokens[0] + DASH + tokens[1];   	
    }

}