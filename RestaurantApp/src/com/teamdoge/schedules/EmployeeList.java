package com.teamdoge.schedules;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;

public class EmployeeList implements ListItem {
	// R.id number linked to the drawable profile image
		private final int image;
		// Name of the owner of the shift
	    private final String name;
	    // Position the employee has for that shift
	    private final String position;
	    // Hours worked this week
	    private final int weekHour;
	    // Hours worked in total
	    private final int totalHour;


	    // Constructor to create a schedule list item
	    public EmployeeList(int img, String n, String p, int wk, int ttl) {
	    	this.image = img;
	        this.name = n;
	        this.position = p;
	        this.weekHour = wk;
	        this.totalHour = ttl;
	    }
	    

	    // implementation needed for the TwoTextArrayAdapter
	    @Override
	    public int getViewType() {
	        return RowType.LIST_ITEM.ordinal();
	    }
	    
		// *******************************************************************************************************************//
		// 													  View 															  //
		// *******************************************************************************************************************//

	    // Correlates the passed in strings and images to the fields in the layout to
	    // return each individual shift block
	    @Override
	    public View getView(LayoutInflater inflater, View convertView) {
	        View view;
	        if (convertView == null) {
	            view = (View) inflater.inflate(R.layout.fragment_employee_list_item, null);
	            // Do some initialization
	        } else {
	            view = convertView;
	        }

	        ImageView profileImg = (ImageView) view.findViewById(R.id.user_image);
	        TextView username = (TextView) view.findViewById(R.id.user_name);
	        TextView role = (TextView) view.findViewById(R.id.position);
	        TextView total = (TextView) view.findViewById(R.id.total_hours);
	        TextView current = (TextView) view.findViewById(R.id.hours_worked);
	        
	        String wkHrs = "Hours worked this week: " + Integer.toString(weekHour);
	        String ttlHrs = "Total hours worked: " + Integer.toString(totalHour);
	       
	        profileImg.setImageResource(image);
	        username.setText(name);
	        role.setText(position);
	        current.setText(wkHrs);
	        total.setText(ttlHrs);

	        return view;
	    }
	    
		// *******************************************************************************************************************//
		//                                                  End View                                                          //
		// *******************************************************************************************************************//
}
