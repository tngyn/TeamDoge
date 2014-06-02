package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.Edit_item;
import com.teamdoge.schedules.EmployeeList;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.TwoTextArrayAdapter;
import com.teamdoge.userprofile.View_Profile;

public class EmployeeListFragment extends ListFragment {
	
	// number of employees
	private int numOfEmployees;
	
	// Parse-related
	private final String parse_key1 = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private final String parse_key2 = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
    private ParseUser user;
    private String restaurantID;
    
    // List to hold all the employees
    private List<ListItem> items;
    //private static List<ArrayList<String>> allEmployees;
    private static List<String> employeeNames;
    private static List<String> employeePositions;
    //private static List<Integer> employeeImages;
    //private static List<Integer> employeeHours;
    //private static List<Integer> employeeTotalHours;
    
	
	
	public static EmployeeListFragment newInstance() {
		EmployeeListFragment fragment = new EmployeeListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	public EmployeeListFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initializes connectivity to specific Parse database
		Parse.initialize(getActivity(), parse_key1, parse_key2);
		
		// Gets current Parse user
		user = ParseUser.getCurrentUser();
			
		// Gets all IDs
		restaurantID = user.getString("Owner_Acc");
		
		// Initializes the employee arrays
		employeeNames = new ArrayList<String>();
		employeePositions = new ArrayList<String>();
		
		// Queries for the all the employees
		ParseQuery<ParseUser> employeeQuery = ParseUser.getQuery();
		employeeQuery.whereEqualTo( "Owner_Acc", restaurantID );
		employeeQuery.orderByAscending("Name");
		try {
			List<ParseUser> employeeList = employeeQuery.find();
			numOfEmployees = employeeQuery.count();
			
			for( int employeeCount = 0; employeeCount < numOfEmployees; ++employeeCount ) {
				ParseObject employee = employeeList.get(employeeCount);
				employeeNames.add( employee.getString("Name") );
				employeePositions.add( employee.getString("Acc_Type") );
			}
	
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		OnItemLongClickListener listener = new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, final long id) {
			/*	Intent mIntent = new Intent(getActivity(), View_Profile.class);
				startActivity(mIntent);*/
				
		/* NEEDS A NEW ACTIVITY OR FRAGMENT TO CALL THAT GETS THE PROFILE OF ANOTHER
				 * PERSON */
				
			
				return false;
			}
		};
			getListView().setOnItemLongClickListener(listener);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();
		
		return super.onCreateView(inflater, container, savedInstanceState);	
	}
	
	@Override
	public void onResume() {		
	    super.onResume();
	}
	
	
	// Prepares the listview
	private void createEmployeeList() {
		
		items = new ArrayList<ListItem>();
		
		for( int employeeCounter = 0; employeeCounter < numOfEmployees; ++employeeCounter ) {
			items.add( new EmployeeList( 0, employeeNames.get(employeeCounter),
					employeePositions.get(employeeCounter), 100, 100) );
		}
		
	}
	
	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(Void... params) {
			createEmployeeList();
			
			return items;
			
		}
		
		@Override
		protected void onPostExecute(List<ListItem> items) {
			TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
			setListAdapter(adapter);
		}
		
	}

}
