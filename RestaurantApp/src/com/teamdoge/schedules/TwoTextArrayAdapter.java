package com.teamdoge.schedules;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TwoTextArrayAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
    
    public TwoTextArrayAdapter(Context context, List<ListItem> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }
    
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }
    
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
}