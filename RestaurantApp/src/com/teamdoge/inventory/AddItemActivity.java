package com.teamdoge.inventory;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

public class AddItemActivity extends FragmentActivity implements
		OnItemSelectedListener,
		InvAddPageDropdownFrag.OnFragmentInteractionListener {
	// used to clear all the text boxes (initialize them for typing)
	// private String init; (I don't think we need this here)

	private EditText item_name_box;
	private EditText quantity_box;
	private EditText descrip_box;
	private Spinner category_dropdown;
	private Spinner units_dropdown;
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

	List<String> categorylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory_screen);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Parse.initialize(this, applicationId, clientKey);
		
		// initialize the food name box
		item_name_box = (EditText) findViewById(R.id.item_name_box);
		// initialize the quantity box
		quantity_box = (EditText) findViewById(R.id.quant_box);
		// initialize the description box
		descrip_box = (EditText) findViewById(R.id.description_box);
		// call the method that initializes all the textboxes. We set everything
		// to empty.
		clearBoxes();

		// get out dropdown objects here
		category_dropdown = (Spinner) findViewById(R.id.categories_dropdown);
		units_dropdown = (Spinner) findViewById(R.id.units_dropdown);
		// populate our dropdown menus with options and set their item select
		// listeners
		// populate with "" to initialize it to empty

		MyAsyncTaskHelper task = new MyAsyncTaskHelper();
		task.execute();

		// Set Title to add item
		getActionBar().setTitle("Add Item");

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

		// if the newCategory is empty, we don't do add anything to the 0'th
		// position
		// otherwise we set the newCategory as the 0'th position.
		if (newCategory != "") {
			categorydataAdapter.add(newCategory);
		}

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
	
	public void submit(View view) {
		boolean everythingWorks = true;
		final String foodName = item_name_box.getText().toString();
		if (foodName == "") {
			everythingWorks = false;
		}

		int quant = -1;
		if (quantity_box.getText().toString().equals("")) {
			everythingWorks = false;
		} else {
			quant = Integer.parseInt(quantity_box.getText().toString());
		}
		// we have to set this as a final int if we're passing to the DB.
		final int quan = quant;

		final String description = descrip_box.getText().toString();
		if (description == "") {
			everythingWorks = false;
		}

		final String units = units_dropdown.getSelectedItem().toString();
		if (units.equalsIgnoreCase("Select Units"))
			everythingWorks = false;

		final String categories = category_dropdown.getSelectedItem()
				.toString();
		if (categories.equalsIgnoreCase("Select a Category"))
			everythingWorks = false;

		// if everythingWorks == true, then all the fields are filled out
		// properly.
		if (everythingWorks) {
			String userId = "";
			ParseUser currentUser = ParseUser.getCurrentUser();

			if (currentUser != null) {
				userId = currentUser.getString("Owner_Acc");
			}

			// set a query to check the food items
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");

			// try to find a food with the same name as the one we entered.
			query.whereEqualTo("userId", userId);

			List<ParseObject> foodNames = null;
			try {
				foodNames = query.find();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (foodNames.isEmpty() == false) {
				boolean exists = false;
				for (ParseObject food : foodNames) {
					if (food.getString("name").equalsIgnoreCase(foodName))
						exists = true;
				}
				// if exists = true then don't create it
				if (exists) {
					Context context = getApplicationContext();
					CharSequence text = "Item Already Exists";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					// clear all the boxes
					clearBoxes();

					// send a toast to show that it's been submitted to the DB
					Context context = getApplicationContext();
					CharSequence text = "Item Added";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();

					// send the information to the DB.
					ParseObject food = new ParseObject("Food");
					// name of the food
					food.put("name", foodName);
					// quantity of the food to track original value
					food.put("quantity", quan);

					food.put("shrinkTrackQuantity", quan);

					food.put("description", description);
					// units of food e.g. oz. lbs. kg.
					food.put("units", units);
					// categories of the food
					food.put("category", categories);

					// userId associated with each food.

					food.put("userId", userId);
					food.saveInBackground();
					onBackPressed();
				}
			} else {
				// clear all the boxes
				clearBoxes();

				// send a toast to show that it's been submitted to the DB
				Context context = getApplicationContext();
				CharSequence text = "Item Added";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				// send the information to the DB.
				ParseObject food = new ParseObject("Food");
				// name of the food
				food.put("name", foodName);
				// quantity of the food to track original value
				food.put("quantity", quan);

				food.put("shrinkTrackQuantity", quan);

				food.put("description", description);
				// units of food e.g. oz. lbs. kg.
				food.put("units", units);
				// categories of the food
				food.put("category", categories);

				// userId associated with each food.

				food.put("userId", userId);
				food.saveInBackground();
				onBackPressed();
			}
		}

		// if any fields are missing, we send a toast about missing fields.
		else {
			Context context = getApplicationContext();
			CharSequence text = "Missing Fields";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void clearBoxes() {
		// set all the text to the empty string
		String init = "";
		item_name_box.setText(init);
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
	

	public void cancel(View view) {
		onBackPressed();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			this.finish();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//

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

		}
	}

}