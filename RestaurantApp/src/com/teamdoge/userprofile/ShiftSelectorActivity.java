package com.teamdoge.userprofile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.teamdoge.userprofile.ExpandableListAdapterTimePicker;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.R.id;
import com.teamdoge.restaurantapp.R.layout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
 
public class ShiftSelectorActivity extends Activity implements OnClickListener {
 
    ExpandableListAdapterTimePicker listAdapter;
    View expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
 
    
    public void selectShift(View v) {
    	Log.d("Debugging", "selectShift called");
    	if (v.getTag() == "true") {
    		v.setTag("false");
    		v.setBackgroundColor(Color.parseColor("#E27171")); //RED
    	} else {
    		v.setTag("true");
    		v.setBackgroundColor(Color.parseColor("#99FF66")); //GREEN
    	}

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shift_selector);
        Log.d("Debugging", "creating activity");
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.Availabilities);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapterTimePicker(this, listDataHeader, listDataChild);

        
        // setting list adapter
        ((ExpandableListView) expListView).setAdapter(listAdapter);
        
        
        ((ExpandableListView) expListView).setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Monday");
        listDataHeader.add("Tuesday");
        listDataHeader.add("Wednesday");
        listDataHeader.add("Thursday");
        listDataHeader.add("Friday");
        listDataHeader.add("Saturday");
        listDataHeader.add("Sunday");
 
        // Adding child data
        List<String> shifts = new ArrayList<String>();
        shifts.add("11 am - 2 pm");
        shifts.add("2 pm - 4 pm");
        shifts.add("4 pm - 8pm");

        for (int i = 0; i < 7; i++) {
        	listDataChild.put(listDataHeader.get(i), shifts); // Header, Child data

        }
    }
	@Override
	public void onClick(View v) {
		Log.d("Debugging", "click on disabled element detected");
		// TODO Auto-generated method stub
		v.setEnabled(true);
	}
}