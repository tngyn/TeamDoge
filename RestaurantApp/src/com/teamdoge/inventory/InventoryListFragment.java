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
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

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
		Parse.initialize(getActivity(), applicationId, clientKey);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.menu_refresh);
			if (refreshItem != null) {
				if (refreshing) {
					refreshItem
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}
	

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
			e.printStackTrace();
		}
		for (ParseObject food : foods) {
			String category = food.getString("category");
			if (groupList.contains(category) == false || groupList.isEmpty()) {
				groupList.add(category);
			}
		}

	}

	private void loadChild(String[] itemList) {
		childList = new ArrayList<String>();
		for (String Citem : itemList)
			childList.add(Citem);
	}

	/*****************************************************************/

	public void asyncCaller() {
		setRefreshActionButtonState(true);
		new MyAsyncTaskHelper().execute();
	}

	private class MyAsyncTaskHelper extends AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {

			createGroupList();

			createCollection();

			return groupList;
		}

		@Override
		protected void onPostExecute(List<String> item) {
			ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
					getActivity(), item, listInventory);

			expListView.setAdapter(expListAdapter);

			setRefreshActionButtonState(false);
		}
	}

	@Override
	public void run() {
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

		createGroupList();

		createCollection();
	}	
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
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
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
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
				e.printStackTrace();
			}

			String[] category1 = new String[foodNames.size()];
			String[] categoryWunits = new String[foodNames.size()];
			if (foodNames.isEmpty() == false) {
				for (ParseObject foodObj : foodNames) {
					category1[index] = foodObj.getString("name");
					categoryWunits[index] = foodObj.getString("name");
					index++;
				}

				loadChild(categoryWunits);

				listInventory.put(groupList.get(i).toString(), childList);
			}
		}
	}
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
}
