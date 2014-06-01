package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

public class Edit_item extends FragmentActivity implements OnItemSelectedListener, InvAddPageDropdownFrag.OnFragmentInteractionListener{
	//used to clear all the text boxes (initialize them for typing)
	//private String init; (I don't think we need this here)
	
	private EditText item_name_box;
	private EditText quantity_box;
	private EditText descrip_box;
	private Spinner category_dropdown;
	private Spinner units_dropdown;
	private String item;
	List<String> categorylist;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Intent intent = getIntent();
        item = intent.getStringExtra("item");
        Log.wtf("IN EDIT", item);
        //Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
        
        Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
        
        //initialize the food name box
        item_name_box = (EditText) findViewById(R.id.edititem_name_box);
        Log.wtf("ITEM BOX", ".");
        item_name_box.setText(item);
        //initialize the quantity box
        quantity_box = (EditText) findViewById(R.id.editquant_box);
        //initialize the description box
        descrip_box = (EditText) findViewById(R.id.editdescription_box);
        //call the method that initializes all the textboxes. We set everything to empty.
        clearBoxes();


        //get out dropdown objects here
        category_dropdown = (Spinner) findViewById(R.id.editcategories_dropdown);
        units_dropdown = (Spinner) findViewById(R.id.editunits_dropdown);
        //populate our dropdown menus with options and set their item select listeners
        //populateDropdowns();
        populateCategories("");
        populateUnits("");
        fillInfo(item);
      
    }
    
    public void fillInfo(String item){
    	ParseQuery<ParseObject> itemQ = ParseQuery.getQuery("Food");
    	
    	itemQ.whereEqualTo("name", item);
    	List<ParseObject> foods = null;
    	try {
    		foods = itemQ.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int quanty = foods.get(0).getInt("quantity");
    	String quantString = "" + quanty;
    	quantity_box.setText(quantString);
    	//Unit dropdown
    	String unit = foods.get(0).getString("units");
    	for( int i = 0; i < units_dropdown.getCount(); i++){
    		if( unit.equalsIgnoreCase(units_dropdown.getItemAtPosition(i).toString())){
    			//Log.wtf("Units dropdown name", ""+ units_dropdown.getItemAtPosition(i).toString());
    			units_dropdown.setSelection(i);
    		}
    			
    	}
    	
    	//Category dropdown
    	String cateName = foods.get(0).getString("category");
    	for( int i = 0; i < category_dropdown.getCount(); i++){
    		if( cateName.equalsIgnoreCase(category_dropdown.getItemAtPosition(i).toString())){
    			//Log.wtf("Units dropdown name", ""+ units_dropdown.getItemAtPosition(i).toString());
    			category_dropdown.setSelection(i);
    		}
    			
    	}
    	//Description
    	String description = foods.get(0).getString("description");
    	descrip_box.setText(description);
    }
    
    public void submit(View view){
    	boolean everythingWorks = true;
    	final String foodName = item;
    	final String newName = item_name_box.getText().toString();
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
						/*if (foodNames.isEmpty() == false) {
							Context context = getApplicationContext();
							CharSequence text = "Item Already Exists";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
						//item doesn't exist yet, add it.
						else {*/
    		        		//if(foodNames.isEmpty() == true)
    		        			
							//clear all the boxes
	    		        	clearBoxes();
	    		    		
	    		        	//send a toast to show that it's been submitted to the DB
	    		    		Context context = getApplicationContext();
	    		    		CharSequence text = "Item Added";
	    		    		int duration = Toast.LENGTH_SHORT;
	    		    		Toast toast = Toast.makeText(context, text, duration);
	    		    		toast.show();
	    		    		
	    		    		//send the information to the DB.
	    		    		//ParseObject food = new ParseObject("Food");
	    		    		ParseObject food = foodNames.get(0);
	    		    		food.put("name", newName);
	    		    		food.put("quantity", quan);
	    		    		food.put("description", description);
	    		    		food.put("units", units);
	    		    		food.put("category", categories);
	    		    		food.saveInBackground();
	    		    		onBackPressed();
	    		    		
						//}
    		        	
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
    	onBackPressed();
    	
    }
    
    public void clearBoxes(){
    	//set all the text to the empty string
    	String init = "";
    	quantity_box.setText(init);
    	descrip_box.setText(init);    	
    }
    
    public void populateDropdowns() { 	
    	
    	//ParseUser user = ParseUser.getCurrentUser(); gets the current user.
    	
    	//populate the units dropdown here.
//    	List<String> unitslist = new ArrayList<String>();
//    	unitslist.add("Select Units");
//    	unitslist.add("lbs.");
//    	unitslist.add("oz.");
//    	unitslist.add("litres");
//    	unitslist.add("New");
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
//    	ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, unitslist);
    	unitsdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	unitsdataAdapter.add("lbs.");
    	unitsdataAdapter.add("oz.");
    	unitsdataAdapter.add("litres");
    	unitsdataAdapter.add("New");
    	unitsdataAdapter.add("Select Units");
    	units_dropdown.setAdapter(unitsdataAdapter);
    	units_dropdown.setSelection(unitsdataAdapter.getCount());
    	units_dropdown.setOnItemSelectedListener(this);
    	
    	//populate the categories dropdown here.
    	//clear the list, then add in the new items
//    	categorylist = new ArrayList<String>();
//    	categorylist.clear();
//    	categorylist.add("Meat");
//    	categorylist.add("Veggies");
//    	categorylist.add("Sweets");
//    	categorylist.add("New");
//    	categorylist.add("Select a Category");
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
    
    /*public void populateCategories(String newCategory) {
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
    	units_dropdown.setAdapter(unitsdataAdapter);
    	units_dropdown.setSelection(0);
    }*/
    public void populateCategories(String newCategory) {
        //populate the categories dropdown here.
       
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
               
                //go through the foods and add their categories to the category data adapter
        for(ParseObject food : foods) {
                String category = food.getString("category");
                //this if initially adds in a string to added the first time
                if(added.isEmpty()) {
                        added.add(category);
                        categorydataAdapter.add(category);
                }
                //added isn't empty, so we will see if it's in there, if it isn't we add to adapter
                else if(added.contains(category) == false) {
                        added.add(category);
                        categorydataAdapter.add(category);
                }
        }
               
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
       
                //go through the foods and add their units to the units data adapter
                for (ParseObject food : foods) {
                        String units = food.getString("units");
                        // this if initially adds in a string to added the first time
                        if (added.isEmpty()) {
                                added.add(units);
                                unitsdataAdapter.add(units);
                        }
                        // added isn't empty, so we will see if it's in there, if it isn't
                        // we add to adapter
                        else if (added.contains(units) == false) {
                                added.add(units);
                                unitsdataAdapter.add(units);
                        }
                }
               
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
}