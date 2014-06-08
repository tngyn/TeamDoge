package com.teamdoge.schedules;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ScheduleList implements ListItem {
	// R.id number linked to the drawable profile image
	private final int image;
	// Name of the owner of the shift
    private final String name;
    // Position the employee has for that shift
    private final String position;

    // Constructor to create a schedule list item
    public ScheduleList(int img, String n, String p) {
    	this.image = img;
        this.name = n;
        this.position = p;
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
            view = (View) inflater.inflate(R.layout.fragment_schedule_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        ImageView profileImg = (ImageView) view.findViewById(R.id.user_image);
        TextView username = (TextView) view.findViewById(R.id.user_name);
        TextView role = (TextView) view.findViewById(R.id.position);
       
        profileImg.setImageResource(image);
        username.setText(name);
        role.setText(position);

        return view;
    }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//

}