//package com.teamdoge.restaurantapp;
//
//import android.app.Activity;
//import android.app.ActionBar;
//import android.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.os.Build;
//
//public class Add_item extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_add_item);
//
//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.add_item, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_add_item,
//					container, false);
//			return rootView;
//		}
//	}
//
//}
package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Add_item extends FragmentActivity implements OnItemSelectedListener, InvAddPageDropdownFrag.OnFragmentInteractionListener{
	//used to clear all the text boxes (initialize them for typing)
	//private String init; (I don't think we need this here)
	
	private EditText item_name_box;
	private EditText quantity_box;
	private EditText descrip_box;
	private Spinner category_dropdown;
	private Spinner units_dropdown;
	
	List<String> categorylist;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_screen);
        
        //Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
        
        Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
        
        //init = "";
        //initialize the food name box
        item_name_box = (EditText) findViewById(R.id.item_name_box);
        //initialize the quantity box
        quantity_box = (EditText) findViewById(R.id.quant_box);
        //initialize the description box
        descrip_box = (EditText) findViewById(R.id.description_box);
        //call the method that initializes all the textboxes. We set everything to empty.
        clearBoxes();

        //get out dropdown objects here
        category_dropdown = (Spinner) findViewById(R.id.categories_dropdown);
        units_dropdown = (Spinner) findViewById(R.id.units_dropdown);
        //populate our dropdown menus with options and set their item select listeners
        //populate with "" to initialize it to empty
        populateUnits("");
    	populateCategories("");
        //category_dropdown.
    	
    	//Set Title to add item
    	getActionBar().setTitle("Add Item");
      
    }
    
    public void submit(View view){
    	boolean everythingWorks = true;
    	final String foodName = item_name_box.getText().toString();
    	if(foodName == ""){
    		everythingWorks = false;
    	}
    	
    	int quant = -1;
    	if(quantity_box.getText().toString().equals("")){
    		everythingWorks = false;
    	}    	
    	else {
    		quant = Integer.parseInt(quantity_box.getText().toString());
    	}
    	//we have to set this as a final int if we're passing to the DB.
    	final int quan = quant;
    	
    	final String description = descrip_box.getText().toString();
    	if(description == "") {
    		everythingWorks = false;
    	}
    	
    	final String units = units_dropdown.getSelectedItem().toString();
    	if(units.equalsIgnoreCase("Select Units"))
    		everythingWorks = false;
    	
    	final String categories = category_dropdown.getSelectedItem().toString();
    	if(categories.equalsIgnoreCase("Select a Category"))
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (foodNames.isEmpty() == false) {
				boolean exists = false;
				for(ParseObject food : foodNames) {
					if(food.getString("name").equalsIgnoreCase(foodName))
						exists = true;
				}
				//if exists = true then don't create it 
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
    
    public void cancel(View view){    	
    	onBackPressed();
    }
    
    public void clearBoxes(){
    	//set all the text to the empty string
    	String init = "";
    	item_name_box.setText(init);
    	quantity_box.setText(init);
    	descrip_box.setText(init);    	
    }
    
    public List<String> setAdapterList(String type){
    	//create the list that will be used to populate the adapters
    	List<String> added = new ArrayList<String>();
    	
    	//get the current userID
    	String userId = "";
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser != null) {
			userId = currentUser.getString("Owner_Acc");
		}
		
		
		//Find all the foods that are tied to this id
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
		query.whereEqualTo("userId", userId);
		List<ParseObject> foods = null;
		try {
			foods = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
    	//populate the categories dropdown here.
		
		//list of added categories we'll be checking against for duplicates.
		List<String> added = new ArrayList<String>();
    	
		//create our own category adapter
    	ArrayAdapter<String> categorydataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {
    		@Override
    	    public View getView(int position, View convertView, ViewGroup parent) {

    	        View v = super.getView(position, convertView, parent);
    	        if (position == getCount()) {
    	            ((TextView)v.findViewById(android.R.id.text1)).setText("");
    	            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
    	        }

    	        return v;
    	    }       

    	    @Override
    	    public int getCount() {
    	        return super.getCount()-1; // you dont display last item. It is used as hint.
    	    }
    	};
    	categorydataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	//position we'll set the selection to
    	int position = 0;
    	
    	//if the newCategory is empty, we don't do add anything to the 0'th position
    	//otherwise we set the newCategory as the 0'th position.
		if (newCategory != "") {
			categorydataAdapter.add(newCategory);
		}
		
    	added = setAdapterList("category");
    	
    	for(String units : added)
			categorydataAdapter.add(units);
		
		//add in the new and select units manually (select category won't show up when dropped down)
    	categorydataAdapter.add("New");
    	categorydataAdapter.add("Select a Category");
    	
    	//one more check for if newCategory is empty or not since we have to set the position if it is.
    	if (newCategory == "") {
			position = categorydataAdapter.getCount();
		}
    	
    	category_dropdown.setAdapter(categorydataAdapter);
    	category_dropdown.setSelection(position);
    	category_dropdown.setOnItemSelectedListener(this);
    	
    	
    }
    
    public void populateUnits(String newUnits) {
    	//populate the units dropdown here

		//the list of strings that are added 
		List<String> added = new ArrayList<String>();

		//units datadapter that we're making
    	ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {
    		@Override
    	    public View getView(int position, View convertView, ViewGroup parent) {

    	        View v = super.getView(position, convertView, parent);
    	        if (position == getCount()) {
    	            ((TextView)v.findViewById(android.R.id.text1)).setText("");
    	            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
    	        }

    	        return v;
    	    }
    		@Override
    	    public int getCount() {
    	        return super.getCount()-1; // you dont display last item. It is used as hint.
    	    }
    	};
    	
    	unitsdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//position we'll set the selection to
    	int position = 0;
    	
    	//if the newCategory is empty, we don't do add anything to the 0'th position
    	//otherwise we set the newCategory as the 0'th position.
		if (newUnits != "") {
			unitsdataAdapter.add(newUnits);
		}
		
		added = setAdapterList("units");
		
		for(String units : added)
			unitsdataAdapter.add(units);
		
		//add in the new and select units manually (select units won't show up when dropped down)
    	unitsdataAdapter.add("New");
    	unitsdataAdapter.add("Select Units");
    	
    	//one more check for if newCategory is empty or not since we have to set the position if it is.
    	if (newUnits == "") {
			position = unitsdataAdapter.getCount();
		}
    	
    	units_dropdown.setAdapter(unitsdataAdapter);
    	units_dropdown.setSelection(position);
    	units_dropdown.setOnItemSelectedListener(this);
    }
    
    public void showNewCategoryDialog() {
    	DialogFragment newFragment = new InvAddPageDropdownFrag();
    	newFragment.show(this.getFragmentManager(), "categories");
    	//newFragment.show(manager, tag);
    }
    
    public void showNewUnitsDialog() {
    	DialogFragment newFragment = new InvAddPageDropdownFrag();
    	newFragment.show(this.getFragmentManager(), "units");
    }
    
    @Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (parent.equals(category_dropdown)) {
			if (parent.getItemAtPosition(position).toString() == "New") {
				showNewCategoryDialog();
			}
		} 
		else {
			if(parent.getItemAtPosition(position).toString() == "New") {
				showNewUnitsDialog();
			}
		}
		
	}
    
	//We won't do anything if nothing is selected.
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//This method does nothing, but we need it because we implement OnItemSelectedListener
		//has to be implemented or we get compile errors		
	}
    
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    	
    	//get the name of the new item to add from the fragment
    	Dialog dialogView = dialog.getDialog();
		EditText newItemToAdd = (EditText) dialogView.findViewById(R.id.new_item_box);
		String newItemAdding = newItemToAdd.getText().toString();
		
    	Fragment prev = getFragmentManager().findFragmentByTag("categories");
		if (prev != null) {
			//the fragment is called by categories dropdown
			populateCategories(newItemAdding);
		}else {
			//the fragment is called by units dropdown
			populateUnits(newItemAdding);
		}
    }
    
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	//check which dropdown called this
    	Fragment prev = getFragmentManager().findFragmentByTag("categories");
    	if(prev != null) {
    		//category dropdown called it
    		category_dropdown.setSelection(category_dropdown.getAdapter().getCount()); 
    	}
    	else {
    		//units dropdown called it
    		units_dropdown.setSelection(units_dropdown.getAdapter().getCount());
    	}
    }

//  public void populateDropdowns() { 	
//	
////	//get the current userID
////	String userId = "";
////	ParseUser currentUser = ParseUser.getCurrentUser();
////	if(currentUser != null) {
////		userId = currentUser.getObjectId();
////	}
////	
////	//Find all the foods that are tied to this id
////	ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
////	query.whereEqualTo("userId", userId);
////	List<ParseObject> foods = null;
////	try {
////		foods = query.find();
////	} catch (ParseException e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
////	
////	//units datadapter that we're making
////	ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {
////		@Override
////	    public View getView(int position, View convertView, ViewGroup parent) {
////
////	        View v = super.getView(position, convertView, parent);
////	        if (position == getCount()) {
////	            ((TextView)v.findViewById(android.R.id.text1)).setText("");
////	            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
////	        }
////
////	        return v;
////	    }
////		@Override
////	    public int getCount() {
////	        return super.getCount()-1; // you dont display last item. It is used as hint.
////	    }
////	};
////	
////	unitsdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////	
////	//the list of strings that are added 
////	List<String> added = new ArrayList<String>();
////	
////	//go through the foods and add their units to the units data adapter
////	for (ParseObject food : foods) {
////		String units = food.getString("units");
////		// this if initially adds in a string to added the first time
////		if (added.isEmpty()) {
////			added.add(units);
////			unitsdataAdapter.add(units);
////		}
////		// added isn't empty, so we will see if it's in there, if it isn't
////		// we add to adapter
////		else if (added.contains(units) == false) {
////			added.add(units);
////			unitsdataAdapter.add(units);
////		}
////	}
////	
////	//add in the new and select units manually (select units won't show up when dropped down)
////	unitsdataAdapter.add("New");
////	unitsdataAdapter.add("Select Units");
////	units_dropdown.setAdapter(unitsdataAdapter);
////	units_dropdown.setSelection(unitsdataAdapter.getCount());
////	units_dropdown.setOnItemSelectedListener(this);
//	
//	
////	//adapter for the categories
////	ArrayAdapter<String> categorydataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {
////		@Override
////	    public View getView(int position, View convertView, ViewGroup parent) {
////
////	        View v = super.getView(position, convertView, parent);
////	        if (position == getCount()) {
////	            ((TextView)v.findViewById(android.R.id.text1)).setText("");
////	            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
////	        }
////
////	        return v;
////	    }       
////
////	    @Override
////	    public int getCount() {
////	        return super.getCount()-1; // you dont display last item. It is used as hint.
////	    }
////	};
////	categorydataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////	
////	//clear the added list before populating for category
////	added.clear();
////	
////	//go through the foods and add their categories to the category data adapter
////	for(ParseObject food : foods) {
////		String category = food.getString("category");
////		//this if initially adds in a string to added the first time
////		if(added.isEmpty()) {
////			added.add(category);
////			categorydataAdapter.add(category);
////		}
////		//added isn't empty, so we will see if it's in there, if it isn't we add to adapter
////		else if(added.contains(category) == false) {
////			added.add(category);
////			categorydataAdapter.add(category);
////		}
////	}
////	
////	//add in the new and select units manually (select category won't show up when dropped down)
////	categorydataAdapter.add("New");
////	categorydataAdapter.add("Select a Category");
////	category_dropdown.setAdapter(categorydataAdapter);
////	category_dropdown.setSelection(categorydataAdapter.getCount());
////	category_dropdown.setOnItemSelectedListener(this);
//	
//	populateUnits("");
//	populateCategories("");
//}
    
}
