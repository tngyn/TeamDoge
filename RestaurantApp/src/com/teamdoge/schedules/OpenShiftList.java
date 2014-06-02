package com.teamdoge.schedules;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class OpenShiftList implements ListItem {
	// R.id number linked to the drawable profile image
	private final int image;
	// Original hour block of the shift
	private String shift;
	// Display string for shift
	private String displayShift;
	// Name of the owner of the shift
    public final String name;
    // Position the employee has for that shift
    private final String position;
    // Status of shift
    private String status;
    
    // counter integers
    public int weekDay;
    public int empNum;
    public int shiftNumber;
    
    // Strings for formating shift
    private final String AM = ":00 AM";
    private final String PM = ":00 PM";
    private final String DASH = " - ";

    // Constructor to create a schedule list item
    public OpenShiftList(int img, String time, String n, String p, String stat, int w, int s, int num) {
    	this.image = img;
    	this.shift = time;
        this.name = n;
        this.position = p;
        this.status = stat;
        this.weekDay = w;
        this.empNum = s;
        this.shiftNumber = num;
    }
    
    public OpenShiftList( int img, String time, String n, String p, String num ) {
    	this.image = img;
    	this.shift = time;
        this.name = n;
        this.displayShift = time;
        this.position = p;
        this.status = num;
        this.weekDay = -1;
        this.empNum = -1;
        this.shiftNumber = -1;
    }

    // implementation needed for the TwoTextArrayAdapter
    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    // Correlates the passed in strings and images to the fields in the layout to
    // return each individual shift block
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.fragment_open_shift_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        ImageView profileImg = (ImageView) view.findViewById(R.id.user_image);
        TextView hours = (TextView) view.findViewById(R.id.shift_hours);
        TextView username = (TextView) view.findViewById(R.id.user_name);
        TextView role = (TextView) view.findViewById(R.id.position);
        TextView stat = (TextView) view.findViewById(R.id.status);
        
        if( shift.length() < 6 ) {
        	convertShift();
        }
       
        profileImg.setImageResource(image);
        hours.setText(displayShift);
        username.setText(name);
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
    	
    	// checks if start time is 12 AM
    	if( start == 0 ) {
    		tokens[0] = "12:00 AM";
    	}
    	// checks if start time is 12 PM
    	else if ( start == 12 ) {
    		tokens[0] = "12:00 PM";
    	}
    	// converts start time
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
    	// checks if start time is 12 PM
    	else if ( end == 12 ) {
    		tokens[1] = "12:00 PM";
    	}
    	//converts end time
    	else if( end < 12 ) {
    		tokens[1] = "" + end + AM;
    	}
    	else {
    		tokens[1] = "" + (end - 12) + PM;
    	}
    	
    	// restructures the shift string
    	displayShift = tokens[0] + DASH + tokens[1];   	
    }

}