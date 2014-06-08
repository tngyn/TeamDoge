package com.teamdoge.management;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.EmployeeList;
import com.teamdoge.schedules.ListItem;
import com.teamdoge.schedules.TwoTextArrayAdapter;

public class EmployeeListFragment extends ListFragment {
	
	// Parse-related
	private final String parse_key1 = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private final String parse_key2 = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
    private ParseUser user;
    private String restaurantID;
    // List to hold all the employees
    private List<ListItem> items;
    private static List<String> employeeNames;
    private static List<String> employeePositions;
    private static List<ParseObject> shiftList;
    private ParseObject shift;
    
    private Menu optionsMenu;
	
	
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
		setHasOptionsMenu(true);
		initParse();
	}

	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	private void initParse() {
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
		ParseQuery<ParseObject> shiftQuery = ParseQuery.getQuery("Shifts");
		shiftQuery.whereEqualTo("Id", restaurantID);
		shiftQuery.orderByAscending("Name");
		shiftQuery.whereNotEqualTo("Acc_Type", "Owner");
		try {
			shiftList = shiftQuery.find();
			for( int employeeCount = 0; employeeCount < shiftList.size(); ++employeeCount ) {
				shift = shiftList.get(employeeCount);
				employeeNames.add( shift.getString("Name") );
				employeePositions.add( shift.getString("Acc_Type") );
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
			
		if (user.getString("Acc_Type").equals("Owner")) { 
		
		OnItemLongClickListener listener = new OnItemLongClickListener() {
			
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, final long id) {
					AlertDialog.Builder box = 
							new AlertDialog.Builder(getActivity());
					box.setTitle("Set Manager");
					if (employeePositions.get((int) id).equals("Manager")) {
						box.setMessage( "Change Manager to Employee?" );
					}
					else {
						box.setMessage("Change Employee to Manager?");
					}
					box.setNegativeButton( "No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
						}
					});
					box.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							if (employeePositions.get((int)id).equals("Manager")) {
								shift = shiftList.get((int)id);
								shift.put("Acc_Type", "Employee");
								shift.saveInBackground();
							}
							else {
								shift = shiftList.get((int)id);
								shift.put("Acc_Type", "Manager");
								shift.saveInBackground();
							}
							asyncCaller();
						}
					});
					AlertDialog alert = box.create();
					alert.show();
		
					
				
					return false;
				}
		};
		
			getListView().setOnItemLongClickListener(listener);
		}
	}
	
	@Override
	public void onResume() {		
	    super.onResume();
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//	
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();
		return super.onCreateView(inflater, container, savedInstanceState);	
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {

	        case R.id.menu_refresh:
	        	asyncCaller();
	        	return true;
	           
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
	public void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu
	            .findItem(R.id.menu_refresh);
	        if (refreshItem != null) {
	            if (refreshing) {
	                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
	            } else {
	                refreshItem.setActionView(null);
	            }
	        }
	    }
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// Prepares the listview
	private void createEmployeeList() {
		
		items = new ArrayList<ListItem>();
		
		for( int employeeCounter = 0; employeeCounter <shiftList.size(); ++employeeCounter ) {
			items.add( new EmployeeList( 0, employeeNames.get(employeeCounter),
					employeePositions.get(employeeCounter), 100, 100) );
		}
		
	}
	
	public void asyncCaller() {
    	setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}
	
	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<ListItem>> {

		@Override
		protected List<ListItem> doInBackground(Void... params) {
			initParse();
			createEmployeeList();
			
			return items;
			
		}
		
		@Override
		protected void onPostExecute(List<ListItem> items) {
			TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getActivity().getBaseContext(), items);
			setListAdapter(adapter);
			setRefreshActionButtonState(false);
		}
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;
		inflater.inflate(R.menu.refresh, menu);
	}
}
