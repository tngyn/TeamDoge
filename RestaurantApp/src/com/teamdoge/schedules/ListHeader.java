package com.teamdoge.schedules;

import com.teamdoge.schedules.TwoTextArrayAdapter.RowType;
import com.teamdoge.restaurantapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ListHeader implements ListItem {
    private final String name;

    public ListHeader(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
    
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.fragment_schedule_header, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(name);

        return view;
    }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//

}