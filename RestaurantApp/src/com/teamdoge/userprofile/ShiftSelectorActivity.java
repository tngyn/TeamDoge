package com.teamdoge.userprofile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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
 
    protected ExpandableListAdapterTimePicker listAdapter;
    protected View expListView;
    protected List<String> listDataHeader;
    protected HashMap<String, List<String>> listDataChild;
    protected  List<List<String>> parseList;
    protected ParseQuery<ParseObject> query;
 
    
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
        Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
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
		query = ParseQuery.getQuery("Schedule");
		ParseUser user = ParseUser.getCurrentUser();
		String restaurantId = user.getString("Owner_Acc");
		Log.d("ASD", restaurantId);
		query.whereEqualTo( "Id", restaurantId );

        
		try {
			List<ParseObject> scheduleList = query.find();
			ParseObject schedule = scheduleList.get(0);

			
			for (int i = 0; i < listDataHeader.size(); i++) {
				Log.d("ASDAS","Trying" + listDataHeader.get(i));
				parseList.add((List) schedule.getList(listDataHeader.get(i)));
				Log.d("ASDAS","Success" + parseList.get(i).size());
			}
		}
		catch (ParseException e1) {
			e1.printStackTrace();
		}

        Log.d("ASDAS","ASD");
        for (int i = 0; i < 7; i++) {
        	listDataChild.put(listDataHeader.get(i), parseList.get(i)); // Header, Child data

        }
    }
	@Override
	public void onClick(View v) {
		Log.d("Debugging", "click on disabled element detected");
		// TODO Auto-generated method stub
		v.setEnabled(true);
	}
}