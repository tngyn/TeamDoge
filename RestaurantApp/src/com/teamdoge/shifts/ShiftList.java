package com.teamdoge.shifts;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;

public class ShiftList implements ListItem {
	
		// Name of the shift
	    private final String name;
	    // Hours worked in total
	    private final String shift;
	    private int i;
	    private int j;

	    // Constructor to create a schedule list item
	    public ShiftList(String n, String p, int i, int j) {
	        this.name = n;
	        this.shift = p;
	        this.i = i;
	        this.j = j;
	    }
	    
	    public ShiftList(String n, int i) {
	        this.name = n;
	        this.shift = "";
	        this.i = i;
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
}
