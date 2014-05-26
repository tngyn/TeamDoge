package com.teamdoge.trackingmenu;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.teamdoge.restaurantapp.InvAddPageDropdownFrag;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.R.layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddMenuItemActivity extends FragmentActivity
		implements OnItemSelectedListener, InvAddPageDropdownFrag.OnFragmentInteractionListener {
	
		//used to clear all the text boxes (initialize them for typing)
		//private String init; (I don't think we need this here)
		
		private EditText meal_name_box;
		private EditText descrip_box;
		private Spinner category_dropdown;
		
		List<String> categorylist;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	        
	        getActionBar().setDisplayHomeAsUpEnabled(true);
			
			setContentView(R.layout.activity_add_menu_item);
	        
//	        Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
	        
	        Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
	        
	        //init = "";
	        //initialize the food name box
	        meal_name_box = (EditText) findViewById(R.id.meal_name_box);
	        //initialize the quantity box
	        //initialize the description box
	        descrip_box = (EditText) findViewById(R.id.mealDescription_box);
	        //call the method that initializes all the textboxes. We set everything to empty.
	        clearBoxes();

	        //get out dropdown objects here
	        category_dropdown = (Spinner) findViewById(R.id.meal_categories_dropdown);
	        //populate our dropdown menus with options and set their item select listeners
	        populateDropdowns();
	        //category_dropdown.
	      
	    }
	    
	    public void submit(View view){
	    	boolean everythingWorks = true;
	    	final String foodName = meal_name_box.getText().toString();
	    	if(foodName == ""){
	    		everythingWorks = false;
	    	}
	    	
	    	
	    	final String description = descrip_box.getText().toString();
	    	if(description == "") {
	    		everythingWorks = false;
	    	}
	    	
	    	final String categories = category_dropdown.getSelectedItem().toString();
	    	
	    	//if everythingWorks == true, then all the fields are filled out properly.
	    	if(everythingWorks){
	    		
	    		//set a query to check the food items
	    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
	    		
	    		//try to find a food with the same name as the one we entered.
	    		query.whereEqualTo("name", foodName);
	    		
	    		query.findInBackground(new FindCallback<ParseObject>() {
	    		    public void done(List<ParseObject> foodNames, ParseException e) {
	    		        if (e == null) {
	    		    		//The item already exists in the database if isEmpty is false
							if (foodNames.isEmpty() == false) {
								Context context = getApplicationContext();
								CharSequence text = "Item Already Exists";
								int duration = Toast.LENGTH_SHORT;
								Toast toast = Toast.makeText(context, text, duration);
								toast.show();
							}
							//item doesn't exist yet, add it.
							else {
								//clear all the boxes
		    		        	clearBoxes();
		    		    		
		    		        	//send a toast to show that it's been submitted to the DB
		    		    		Context context = getApplicationContext();
		    		    		CharSequence text = "Item Added";
		    		    		int duration = Toast.LENGTH_SHORT;
		    		    		Toast toast = Toast.makeText(context, text, duration);
		    		    		toast.show();
		    		    		
		    		    		//send the information to the DB.
		    		    		ParseObject food = new ParseObject("Food");
		    		    		food.put("name", foodName);
		    		    		food.put("description", description);
		    		    		food.put("category", categories);
		    		    		food.saveInBackground();
							}
	    		        	
	    		        } else {    		            	
	    		        	//fatal error, this should never happen
	    		        	Context context = getApplicationContext();
	    		    		CharSequence text = "Error";
	    		    		int duration = Toast.LENGTH_SHORT;
	    		    		Toast toast = Toast.makeText(context, text, duration);
	    		    		toast.show();
	    		        }
	    		    }
	    		});
	    	}
	    	//if any fields are missing, we send a toast about missing fields.
	    	else {
	    		Context context = getApplicationContext();
	    		CharSequence text = "Missing Fields";
	    		int duration = Toast.LENGTH_SHORT;
	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.show();
	    		
	    	}
	    }
	    
	    public void cancel(View view){
	    	clearBoxes();
	    	category_dropdown.setSelection(1);
	    	
	    }
	    
	    public void clearBoxes(){
	    	//set all the text to the empty string
	    	String init = "";
	    	meal_name_box.setText(init);
	    	descrip_box.setText(init);    	
	    }
	    
	    public void populateDropdowns() { 	
	    	
	    	//ParseUser user = ParseUser.getCurrentUser(); gets the current user.
	    	
	    	//populate the units dropdown here.
//	    	List<String> unitslist = new ArrayList<String>();
//	    	unitslist.add("Select Units");
//	    	unitslist.add("lbs.");
//	    	unitslist.add("oz.");
//	    	unitslist.add("litres");
//	    	unitslist.add("New");
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
//	    	ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, unitslist);
	    	unitsdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	unitsdataAdapter.add("lbs.");
	    	unitsdataAdapter.add("oz.");
	    	unitsdataAdapter.add("litres");
	    	unitsdataAdapter.add("New");
	    	unitsdataAdapter.add("Select Units");
	    	
	    	//populate the categories dropdown here.
	    	//clear the list, then add in the new items
//	    	categorylist = new ArrayList<String>();
//	    	categorylist.clear();
//	    	categorylist.add("Meat");
//	    	categorylist.add("Veggies");
//	    	categorylist.add("Sweets");
//	    	categorylist.add("New");
//	    	categorylist.add("Select a Category");
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
	    	categorydataAdapter.add("Meat");
	    	categorydataAdapter.add("Veggies");
	    	categorydataAdapter.add("Sweets");
	    	categorydataAdapter.add("New");
	    	categorydataAdapter.add("Select a Category");
	    	category_dropdown.setAdapter(categorydataAdapter);
	    	category_dropdown.setSelection(categorydataAdapter.getCount());
	    	category_dropdown.setOnItemSelectedListener(this);
	    }
	    
	    public void populateCategories(String newCategory) {
	    	//populate the categories dropdown here.
	    	//clear the list, then add in the new items
	    	
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
	    	categorydataAdapter.add(newCategory);
	    	categorydataAdapter.add("Meat");
	    	categorydataAdapter.add("Veggies");
	    	categorydataAdapter.add("Sweets");
	    	categorydataAdapter.add("New");
	    	categorydataAdapter.add("Select a Category");
	    	category_dropdown.setAdapter(categorydataAdapter);
	    	category_dropdown.setSelection(0);
	    }
	    
	    public void populateUnits(String newCategory) {
	    	//populate the units dropdown here.
	    	//clear the list, then add in the new items
	    	
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
	    	unitsdataAdapter.add(newCategory);
	    	unitsdataAdapter.add("Meat");
	    	unitsdataAdapter.add("Veggies");
	    	unitsdataAdapter.add("Sweets");
	    	unitsdataAdapter.add("New");
	    	unitsdataAdapter.add("Select a Category");
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
	    }

	
	@Override
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
}
