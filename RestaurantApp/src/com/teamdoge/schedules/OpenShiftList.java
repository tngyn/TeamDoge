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
    private final String AM = " AM";
    private final String PM = " PM";
    private final String DASH = " - ";
    
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

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
    
    private void convertShift(){
    	// tokenizes the string into two to get the times
    	String[] tokens = shift.split("[-|\\:]");
    	
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
    	
    	
    	// restructures the shift string
    	displayShift = tokens[0] + DASH + tokens[2]; 	
    }
    
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//
    
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//

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
        
        if( shift.length() < 12 ) {
        	convertShift();
        }
       
        profileImg.setImageResource(image);
        hours.setText(displayShift);
        username.setText(name);
        role.setText(position);
        stat.setText(status);

        return view;
    }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//

}