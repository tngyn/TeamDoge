package com.teamdoge.inventory;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

public class View_Item extends FragmentActivity implements
		OnItemSelectedListener,
		InvAddPageDropdownFrag.OnFragmentInteractionListener {
	// used to clear all the text boxes (initialize them for typing)
	// private String init; (I don't think we need this here)

	private EditText item_name_box;
	private EditText quantity_box;
	private EditText descrip_box;
	private Spinner category_dropdown;
	private Spinner units_dropdown;
	private String itemName;
	List<String> categorylist;

	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view__item);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		itemName = intent.getStringExtra("item");

		getActionBar().setTitle(itemName);

		Parse.initialize(this, applicationId, clientKey);

		// initialize the food name box
		item_name_box = (EditText) findViewById(R.id.viewItem_name_box);
		item_name_box.setText(itemName);
		item_name_box.setFocusable(false);
		// initialize the quantity box
		quantity_box = (EditText) findViewById(R.id.viewQuant_box);
		quantity_box.setFocusable(false);
		// initialize the description box
		descrip_box = (EditText) findViewById(R.id.viewDescription_box);
		// call the method that initializes all the textboxes. We set everything
		// to empty.
		clearBoxes();

		// get out dropdown objects here
		category_dropdown = (Spinner) findViewById(R.id.viewCategories_dropdown);
		units_dropdown = (Spinner) findViewById(R.id.viewUnits_dropdown);

		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();

	}
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
	
	public List<ParseObject> background() {
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

		return foods;
	}

	public void fillInfo(String item) {
		List<ParseObject> foodNames = background();
		if (foodNames.isEmpty() == false) {
			for (ParseObject food : foodNames) {
				if (food.getString("name").equalsIgnoreCase(item)) {
					int quanty = food.getInt("quantity");
					String quantString = "" + quanty;
					quantity_box.setText(quantString);
					quantity_box.setFocusable(false);

					String unit = food.getString("units");
					for (int i = 0; i < units_dropdown.getCount(); i++) {
						if (unit.equalsIgnoreCase(units_dropdown
								.getItemAtPosition(i).toString())) {
							units_dropdown.setSelection(i);
						}
					}

					// Category dropdown
					String cateName = food.getString("category");
					for (int i = 0; i < category_dropdown.getCount(); i++) {
						if (cateName.equalsIgnoreCase(category_dropdown
								.getItemAtPosition(i).toString())) {
							category_dropdown.setSelection(i);
						}
					}

					// Description
					String description = food.getString("description");
					descrip_box.setText(description);
					descrip_box.setFocusable(false);
					// We've found what we need, no need to continue.
					break;
				}
			}
		}
	}

	public void clearBoxes() {
		// set all the text to the empty string
		String init = "";
		quantity_box.setText(init);
		descrip_box.setText(init);
	}

	public List<String> setAdapterList(String type) {
		// create the list that will be used to populate the adapters
		List<String> added = new ArrayList<String>();

		List<ParseObject> foods = background();

		// go through the foods and add their units to the units data adapter
		for (ParseObject food : foods) {
			String units = food.getString(type);
			// this if initially adds in a string to added the first time
			if (added.isEmpty()) {
				added.add(units);
			}
			// added isn't empty, so we will see if it's in there, if it isn't
			// we add to adapter
			else if (added.contains(units) == false) {
				added.add(units);
			}
		}

		return added;
	}

	public void populateCategories(String newCategory) {
		// populate the categories dropdown here.

		// list of added categories we'll be checking against for duplicates.
		List<String> added = new ArrayList<String>();

		// create our own category adapter
		ArrayAdapter<String> categorydataAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1))
							.setHint(getItem(getCount())); // "Hint to be displayed"
				}

				return v;
			}

			@Override
			public int getCount() {
				return super.getCount() - 1; // you dont display last item. It
												// is used as hint.
			}
		};
		categorydataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// position we'll set the selection to
		int position = 0;

		added = setAdapterList("category");

		for (String units : added)
			categorydataAdapter.add(units);

		// add in the new and select units manually (select category won't show
		// up when dropped down)
		categorydataAdapter.add("New");
		categorydataAdapter.add("Select a Category");

		// one more check for if newCategory is empty or not since we have to
		// set the position if it is.
		if (newCategory == "") {
			position = categorydataAdapter.getCount();
		}

		category_dropdown.setAdapter(categorydataAdapter);
		category_dropdown.setSelection(position);
		category_dropdown.setOnItemSelectedListener(this);

	}

	public void populateUnits(String newUnits) {
		// populate the units dropdown here

		// the list of strings that are added
		List<String> added = new ArrayList<String>();

		// units datadapter that we're making
		ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1))
							.setHint(getItem(getCount())); // "Hint to be displayed"
				}

				return v;
			}

			@Override
			public int getCount() {
				return super.getCount() - 1; // you dont display last item. It
												// is used as hint.
			}
		};

		unitsdataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// position we'll set the selection to
		int position = 0;

		// if the newCategory is empty, we don't do add anything to the 0'th
		// position
		// otherwise we set the newCategory as the 0'th position.
		if (newUnits != "") {
			unitsdataAdapter.add(newUnits);
		}

		added = setAdapterList("units");

		for (String units : added)
			unitsdataAdapter.add(units);

		// add in the new and select units manually (select units won't show up
		// when dropped down)
		unitsdataAdapter.add("New");
		unitsdataAdapter.add("Select Units");

		// one more check for if newCategory is empty or not since we have to
		// set the position if it is.
		if (newUnits == "") {
			position = unitsdataAdapter.getCount();
		}

		units_dropdown.setAdapter(unitsdataAdapter);
		units_dropdown.setSelection(position);
		units_dropdown.setOnItemSelectedListener(this);
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//

	public void showNewCategoryDialog() {
		DialogFragment newFragment = new InvAddPageDropdownFrag();
		newFragment.show(this.getFragmentManager(), "categories");
	}

	public void showNewUnitsDialog() {
		DialogFragment newFragment = new InvAddPageDropdownFrag();
		newFragment.show(this.getFragmentManager(), "units");
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.equals(category_dropdown)) {
			if (parent.getItemAtPosition(position).toString() == "New") {
				showNewCategoryDialog();
			}
		} else {
			if (parent.getItemAtPosition(position).toString() == "New") {
				showNewUnitsDialog();
			}
		}

	}


	// We won't do anything if nothing is selected.
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// This method does nothing, but we need it because we implement
		// OnItemSelectedListener
		// has to be implemented or we get compile errors
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {

		// get the name of the new item to add from the fragment
		Dialog dialogView = dialog.getDialog();
		EditText newItemToAdd = (EditText) dialogView
				.findViewById(R.id.new_item_box);
		String newItemAdding = newItemToAdd.getText().toString();

		Fragment prev = getFragmentManager().findFragmentByTag("categories");
		if (prev != null) {
			// the fragment is called by categories dropdown
			populateCategories(newItemAdding);
		} else {
			// the fragment is called by units dropdown
			populateUnits(newItemAdding);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// check which dropdown called this
		Fragment prev = getFragmentManager().findFragmentByTag("categories");
		if (prev != null) {
			// category dropdown called it
			category_dropdown.setSelection(category_dropdown.getAdapter()
					.getCount());
		} else {
			// units dropdown called it
			units_dropdown.setSelection(units_dropdown.getAdapter().getCount());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			this.finish();
			return true;
		case R.id.actionEdit:
			Intent intent = new Intent(View_Item.this, EditItemActivity.class);
			intent.putExtra("item", itemName);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}

	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.view__profile, menu);
		return true;
	}
	
	private class MyAsyncTaskHelper extends
			AsyncTask<Void, Void, List<ParseObject>> {

		@Override
		protected List<ParseObject> doInBackground(Void... params) {
			return background();

		}

		@Override
		protected void onPostExecute(List<ParseObject> items) {
			populateCategories("");
			populateUnits("");
			fillInfo(itemName);
		}
	}

}