package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.teamdoge.restaurantapp.ManagerFragment.OnFragmentInteractionListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link InventoryList.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link InventoryList#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class InventoryList extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private List<ParseObject> foodNames;

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
	public static InventoryList newInstance() {
		InventoryList fragment = new InventoryList();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public InventoryList() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(getActivity(),
				"0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8",
				"k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		setHasOptionsMenu(true);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TextView textView = new TextView(getActivity());
		// textView.setText(R.string.hello_blank_fragment);
		// View v = inflater.inflate(R.layout.fragment_manager, container,
		// false);
		/*************** ExpandableView for Inventory ***********************/
		createGroupList();

		createCollection();
		View v = inflater.inflate(R.layout.activity_inventory_list, container,
				false);
		expListView = (ExpandableListView) v.findViewById(R.id.categoryList);
		final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
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
			Intent mIntent = new Intent(getActivity(), Edit_item.class);
			startActivity(mIntent);
			return false;
		}

	};

	/*************** ExpandableView for Inventory ***********************/
	private void createGroupList() {
		groupList = new ArrayList<String>();
		groupList.add(getString(R.string.category1));
		groupList.add(getString(R.string.category2));
	}

	private void createCollection() {
		int index = 0;
		// preparing items in category(child)
		// set a query to check the food items
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");

		// try to find a food with the same name as the one we entered.
		query.whereEqualTo("category", "Fruit");

		try {
			foodNames = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] category1 = new String[foodNames.size()];
		String[] category2 = { getString(R.string.item),
				getString(R.string.item), getString(R.string.item) };
		if (foodNames.isEmpty() == false) {
			for (ParseObject foodObj : foodNames) {
				category1[index] = foodObj.getString("name");
				index++;
			}
			listInventory = new LinkedHashMap<String, List<String>>();

			for (String category : groupList) {
				if (category.equals("Fruit")) {
					loadChild(category1);
				} else if (category.equals("Category Two"))
					loadChild(category2);

				listInventory.put(category, childList);
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
		inflater.inflate(R.menu.menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.item_add:
			Intent intent = new Intent(getActivity(), Add_item.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
