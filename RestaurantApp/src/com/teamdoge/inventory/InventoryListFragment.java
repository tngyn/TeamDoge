package com.teamdoge.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.management.ManagerFragment.OnFragmentInteractionListener;
import com.teamdoge.restaurantapp.ExpandableListAdapter;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.R.id;
import com.teamdoge.restaurantapp.R.layout;
import com.teamdoge.restaurantapp.R.menu;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link InventoryListFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link InventoryListFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class InventoryListFragment extends Fragment implements Runnable {

	private List<ParseObject> foodNames;


	private Menu optionsMenu;

	private OnFragmentInteractionListener mListener;

	/*************** ExpandableView for Inventory ***********************/
	List<String> groupList;
	List<String> childList;
	Map<String, List<String>> listInventory;
	ExpandableListView expListView;

	/*****************************************************************/

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 */
	// TODO: Rename and change types and number of parameters
	public static InventoryListFragment newInstance() {
		InventoryListFragment fragment = new InventoryListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public InventoryListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(getActivity(),
				"0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8",
				"k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TextView textView = new TextView(getActivity());
		// textView.setText(R.string.hello_blank_fragment);
		// View v = inflater.inflate(R.layout.fragment_manager, container,
		// false);
		/*************** ExpandableView for Inventory ***********************/

		View v = inflater.inflate(R.layout.activity_inventory_list, container,
				false);
		expListView = (ExpandableListView) v.findViewById(R.id.categoryList);
		
		run();

		ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
				getActivity(), groupList, listInventory);
		expListView.setAdapter(expListAdapter);
		expListView.setOnChildClickListener(ExpandList_ItemClicked);
		/*****************************************************************/

		return v;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */

	/*************** ExpandableView for Inventory ***********************/
	private OnChildClickListener ExpandList_ItemClicked = new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Intent mIntent = new Intent(getActivity(), View_Item.class);
			mIntent.putExtra("item", getValue(groupPosition, childPosition));
			startActivity(mIntent);

			return false;
		}

	};

	private String getValue(int groupPos, int childPos) {
		int c = 0;
		String temp = "";
		Set<String> keys = listInventory.keySet(); // It will return you all the
													// keys in Map in the form
													// of the Set
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			List<String> value = (List<String>) listInventory.get(key); // Here
																		// is an
																		// Individual
																		// Record
																		// in
																		// your
																		// HashMap
			if (c == groupPos) {
				temp = value.get(childPos);
			}
			c++;
		}
		return temp;
	}

	/*************** ExpandableView for Inventory ***********************/
	private void createGroupList() {
		groupList = new ArrayList<String>();
		// get the current userID
		String userId = "";
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			userId = currentUser.getString("Owner_Acc");
		}

		// Find all the foods that are tied to this id
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
		query.whereEqualTo("userId", userId);
		List<ParseObject> foods = null;
		try {
			foods = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ParseObject food : foods) {
			String category = food.getString("category");
			if (groupList.contains(category) == false || groupList.isEmpty()) {
				// Log.wtf("category", category);
				groupList.add(category);
			}
		}
		// Log.wtf("Array", ""+groupList.size());

	}

	private void createCollection() {
		// preparing items in category(child)
		// set a query to check the food items
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
		listInventory = new LinkedHashMap<String, List<String>>();
		for (int i = 0; i < groupList.size(); i++) {
			int index = 0;
			// try to find a food with the same name as the one we entered.
			query.whereEqualTo("category", groupList.get(i));
			try {
				foodNames = query.find();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] category1 = new String[foodNames.size()];
			String[] categoryWunits = new String[foodNames.size()];
			if (foodNames.isEmpty() == false) {
				// String[] category1 = new String[foodNames.size()];
				for (ParseObject foodObj : foodNames) {
					category1[index] = foodObj.getString("name");
					categoryWunits[index] = foodObj.getString("name");
					index++;
				}

				loadChild(categoryWunits);
				//loadChild(category1);

				listInventory.put(groupList.get(i).toString(), childList);
			}
		}
	}

	private void loadChild(String[] itemList) {
		childList = new ArrayList<String>();
		for (String Citem : itemList)
			childList.add(Citem);
	}

	/*****************************************************************/

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;
		inflater.inflate(R.menu.inventorylist_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_refresh:
			asyncCaller();
			return true;

		case R.id.item_add:
			Intent intent = new Intent(getActivity(), AddItemActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}



	public void asyncCaller() {
		Log.wtf("CMONNN", "INSIDE ASYNCCALLERRR");
		setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}

	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.wtf("TESTINGGG", "IM IN DO IN BACKGROUND AFTER CREATING STUFF");

			createGroupList();

			createCollection();

			Log.wtf("TESTINGGG", "IM IN DO IN BACKGROUND AFTER CREATING STUFF");
			return groupList;
		}

		@Override
		protected void onPostExecute(List<String> item) {
			Log.wtf("TESTINGGG", "IM IN ON POST EXECUTE");
			//dff
			ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
					getActivity(), item, listInventory);

			Log.wtf("TESTINGGG", "IM IN ON POST EXECUTE");
			expListView.setAdapter(expListAdapter);

			setRefreshActionButtonState(false);

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

		createGroupList();

		createCollection();
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

}
