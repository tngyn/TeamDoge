package com.teamdoge.management;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;

public class ShiftList implements ListItem {
	
		// Name of the shift
	    public final String name;
	    // Hours worked in total
	    public final String shift;
	    public final String altShift;
	    public final int weekDay;
	    @SuppressWarnings("unused")
		private final int i;

		// *******************************************************************************************************************//
		// 													Model 														      //
		// *******************************************************************************************************************//
	    
	    // Constructor to create a schedule list item
	    public ShiftList(String n, String s, int weekDay, String altShifts) {
	        this.name = n;
	        this.shift = s;
	        this.weekDay = weekDay;
	        this.altShift = altShifts;
	        this.i = 0;
	    }
	    
	    public ShiftList(String n, int w) {
	        this.name = n;
	        this.shift = "";
	        this.weekDay = w;
	        this.i = -1;
	        this.altShift = "";
	    }
	    
		// *******************************************************************************************************************//
		// 													End Model 														  //
		// *******************************************************************************************************************//
	    
		// *******************************************************************************************************************//
		// 													  View 															  //
		// *******************************************************************************************************************//
	    
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
	            view = (View) inflater.inflate(R.layout.fragment_shift_list_item, null);
	            // Do some initialization
	        } else {
	            view = convertView;
	        }
	        TextView shiftName = (TextView) view.findViewById(R.id.shift_name);
	        TextView shiftTime = (TextView) view.findViewById(R.id.shift_time);
	        shiftName.setText(name);
	        shiftTime.setText(shift);
	        return view;
	    }
	    
		// *******************************************************************************************************************//
		//                                                  End View                                                          //
		// *******************************************************************************************************************//
}
