package com.teamdoge.trackingmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.InvAddPageDropdownFrag;
import com.teamdoge.restaurantapp.R;

public class AddMenuItemActivity extends FragmentActivity
		implements OnItemSelectedListener, MealIngredientsFragment.OnFragmentInteractionListener {
	
		//used to clear all the text boxes (initialize them for typing)
		//private String init; (I don't think we need this here)
		
		private EditText meal_name_box;
		private EditText descrip_box;
		private Spinner category_dropdown;
		private List ingredientListItemList;
		
		private LinearLayout ll;
		private TextView mealNameText;
		private EditText mealNameBox;
		private TextView categoryDropdownText;
		private Spinner mealCategories;
		private Button addIngredient;
		private TextView mealDescripLabel;
		private EditText mealDescripBox;
		private Button menuItemSubmitButton;
		private Button menuItemCancel;
		
		private List<String> categorylist;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	        
	        getActionBar().setDisplayHomeAsUpEnabled(true);
			
//			setContentView(R.layout.activity_add_menu_item);
			
			ScrollView sv = new ScrollView(this);
			ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);
			sv.addView(ll);
			
			//Text View for Meal Name
			mealNameText = new TextView(this);
			mealNameText.setText(R.string.mealNameLabel);
			ll.addView(mealNameText);
			
			//Edit Text for Meal Name
			mealNameBox = new EditText(this);
			mealNameBox.setHint(R.string.meal_name_hint);
			ll.addView(mealNameBox);
			
			//Text View for Category Dropdown
			categoryDropdownText = new TextView(this);
			categoryDropdownText.setText(R.string.categoryLabel);
			ll.addView(categoryDropdownText);
			
			//Dropdown for the categories
			mealCategories = new Spinner(this);
			populateCategories("");
	        ll.addView(mealCategories);
	        
	        //Button to add ingredients
	        addIngredient = new Button(this);
	        addIngredient.setText(R.string.addIngredient);
	        addIngredient.setLayoutParams(new LinearLayout.LayoutParams(300, 100));
	        LinearLayout.LayoutParams layoutP=(LinearLayout.LayoutParams)addIngredient.getLayoutParams();
	        layoutP.gravity=Gravity.CENTER_HORIZONTAL;
	        addIngredient.setLayoutParams(layoutP);
	        addIngredient.setGravity(Gravity.CENTER);
	        ll.addView(addIngredient);
	        
	        //Text View for Description box
	        mealDescripLabel = new TextView(this);
	        mealDescripLabel.setText(R.string.descriptionBoxLabel);
	        ll.addView(mealDescripLabel);
			
	        //Text Box for Descriptions EditText
	        mealDescripBox = new EditText(this);
	        mealDescripBox.setHint(R.string.description_hint);
	        mealDescripBox.setMaxLines(4);
	        mealDescripBox.setSingleLine(false);
	        mealDescripBox.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
	        mealDescripBox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	        ll.addView(mealDescripBox);
	        
	        //Button for submitting the menu item to the db
	        menuItemSubmitButton = new Button(this);
	        menuItemSubmitButton.setText(R.string.button_submit);
	        menuItemSubmitButton.setId(1);
	        menuItemSubmitButton.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // do whatever stuff you wanna do here
	            	submit(v);
	            }
	        });
	        ll.addView(menuItemSubmitButton);
	        
	        Log.d("DO WE HEY ASDF", "yes we do0");
	        //Button for cancelling the menu item 
	        menuItemCancel = new Button(this);
	        Log.d("DO WE HEY ASDF", "yes we do00");
	        menuItemCancel.setText(R.string.button_cancel);
	        Log.d("DO WE HEY ASDF", "yes we do000");
//	        RelativeLayout.LayoutParams cancelParams = (RelativeLayout.LayoutParams) menuItemCancel.getLayoutParams();
	        Log.d("DO WE HEY ASDF", ""+menuItemSubmitButton.getId());
//	        cancelParams.addRule(RelativeLayout.BELOW, menuItemSubmitButton.getId());
	        Log.d("DO WE HEY ASDF", "yes we do00000");
//	        menuItemCancel.setLayoutParams(cancelParams);
	        Log.d("DO WE HEY ASDF", "yes we do000000");
	        menuItemCancel.setOnClickListener(new OnClickListener() {
	        	
	        	@Override
	        	public void onClick(View v) {
	        		cancel(v);
	        	}
	        });
	        ll.addView(menuItemCancel);
	        
			
			//Set the content view here.
			this.setContentView(sv);

			
//			if (savedInstanceState == null) {
//		        getSupportFragmentManager().beginTransaction()
//		                .add(R.id.container, new android.support.v4.app.Fragment())
//		                .commit();
//		    }
	        
//	        Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
	        
	        Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
	        
	        //init = "";
	        //initialize the food name box
//	        meal_name_box = (EditText) findViewById(R.id.meal_name_box);
//	        Log.d("DO WE HEY ASDF", "yes we do");
//	        //initialize the quantity box
//	        //initialize the description box
//	        Log.d("DO WE HEY ASDF", "yes we do0");
//	        descrip_box = (EditText) findViewById(R.id.mealDescription_box);
//	        Log.d("DO WE HEY ASDF", "yes we do00");
//	        //call the method that initializes all the textboxes. We set everything to empty.
//	        //clearBoxes();
//	        Log.d("DO WE HEY ASDF", "yes we do0000");
//	        
//	        addIngredient = (Button) findViewById(R.id.menuAddIngredient);
//	        Log.d("DO WE HEY ASDF", "yes we do1");
//
//	        //get out dropdown objects here
//	        category_dropdown = (Spinner) findViewById(R.id.meal_categories_dropdown);
//	        Log.d("DO WE HEY ASDF", "yes we do2");
//	        //populate our dropdown menus with options and set their item select listeners
//	       // populateCategories("");
//	        //category_dropdown.
//	        Log.d("DO WE HEY ASDF", "yes we do");
//	        ingredientListItemList = new ArrayList();
//	        ingredientListItemList.add(new IngredientListItem("Example 1"));
//	        ingredientListItemList.add(new IngredientListItem("Example 2"));
//	        ingredientListItemList.add(new IngredientListItem("Example 3"));
//	        IngredientListAdapter mAdapter = new IngredientListAdapter(AddMenuItemActivity.this, ingredientListItemList);
//	        Log.d("DO WE HEY ASDF", "yes we do");
	      
	    }
	    
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        IngredientListItem item = (IngredientListItem) this.ingredientListItemList.get(position);
	        Toast.makeText(AddMenuItemActivity.this, item.getIngredientName() + " Clicked!"
	                , Toast.LENGTH_SHORT).show();
	    }
	    
	public void submit(View view) {
		boolean everythingWorks = true;
		final String foodName = mealNameBox.getText().toString();
		if (foodName == "") {
			everythingWorks = false;
		}

		final String description = mealDescripBox.getText().toString();
		if (description == "") {
			everythingWorks = false;
		}

		final String categories = mealCategories.getSelectedItem()
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
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");

			// try to find a food with the same name as the one we entered.
			query.whereEqualTo("Acc_ID", userId);

			List<ParseObject> foodNames = null;
			try {
				foodNames = query.find();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
					ParseObject food = new ParseObject("Menu");
					// name of the food
					food.put("Name", foodName);
					// // quantity of the food to track original value
					// food.put("quantity", quan);
					//
					// food.put("shrinkTrackQuantity", quan);
					//
					food.put("description", description);
					// // units of food e.g. oz. lbs. kg.
					// food.put("units", units);
					// categories of the food
					food.put("category", categories);

					// userId associated with each food.

					food.put("Acc_ID", userId);
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
				ParseObject food = new ParseObject("Meal");
				// name of the food
				food.put("Name", foodName);
				// // quantity of the food to track original value
				// food.put("quantity", quan);
				//
				// food.put("shrinkTrackQuantity", quan);
				//
				food.put("description", description);
				// // units of food e.g. oz. lbs. kg.
				// food.put("units", units);
				// categories of the food
				food.put("category", categories);

				// userId associated with each food.

				food.put("Acc_ID", userId);
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
	    
//	    public void submit(View view){
//	    	boolean everythingWorks = true;
//	    	final String foodName = meal_name_box.getText().toString();
//	    	if(foodName == ""){
//	    		everythingWorks = false;
//	    	}
//	    	
//	    	
//	    	final String description = descrip_box.getText().toString();
//	    	if(description == "") {
//	    		everythingWorks = false;
//	    	}
//	    	
//	    	final String categories = category_dropdown.getSelectedItem().toString();
//	    	
//	    	//if everythingWorks == true, then all the fields are filled out properly.
//	    	if(everythingWorks){
//	    		
//	    		//set a query to check the food items
//	    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
//	    		
//	    		//try to find a food with the same name as the one we entered.
//	    		query.whereEqualTo("name", "menuItemName");
//	    		
//	    		query.findInBackground(new FindCallback<ParseObject>() {
//	    		    public void done(List<ParseObject> menuItemNames, ParseException e) {
//	    		        if (e == null) {
//	    		    		//The item already exists in the database if isEmpty is false
//							if (menuItemNames.isEmpty() == false) {
//								Context context = getApplicationContext();
//								CharSequence text = "Item Already Exists";
//								int duration = Toast.LENGTH_SHORT;
//								Toast toast = Toast.makeText(context, text, duration);
//								toast.show();
//							}
//							//item doesn't exist yet, add it.
//							else {
//								//clear all the boxes
//		    		        	clearBoxes();
//		    		    		
//		    		        	//send a toast to show that it's been submitted to the DB
//		    		    		Context context = getApplicationContext();
//		    		    		CharSequence text = "Item Added";
//		    		    		int duration = Toast.LENGTH_SHORT;
//		    		    		Toast toast = Toast.makeText(context, text, duration);
//		    		    		toast.show();
//		    		    		
//		    		    		//get current user, they're id is tagged to each food that's created
//		    		    		String userId = "";
//		    		    		ParseUser currentUser = ParseUser.getCurrentUser();
//		    		    		if(currentUser != null) {
//		    		    			userId = currentUser.getObjectId();
//		    		    		}
//		    		    		
//		    		    		//send the information to the DB.
//		    		    		ParseObject food = new ParseObject("Menu");
//		    		    		food.put("menuItemName", foodName);
//		    		    		food.put("description", description);
//		    		    		food.put("category", categories);
//		    		    		food.put("userId", userId);
//		    		    		food.saveInBackground();
//		    		    		onBackPressed();
//							}
//	    		        	
//	    		        } else {    		            	
//	    		        	//fatal error, this should never happen
//	    		        	Context context = getApplicationContext();
//	    		    		CharSequence text = "Error";
//	    		    		int duration = Toast.LENGTH_SHORT;
//	    		    		Toast toast = Toast.makeText(context, text, duration);
//	    		    		toast.show();7
//	    		        }
//	    		    }
//	    		});
//	    	}
//	    	//if any fields are missing, we send a toast about missing fields.
//	    	else {
//	    		Context context = getApplicationContext();
//	    		CharSequence text = "Missing Fields";
//	    		int duration = Toast.LENGTH_SHORT;
//	    		Toast toast = Toast.makeText(context, text, duration);
//	    		toast.show();
//	    		
//	    	}
//	    }
	    
	    public void addIngredientPressed(View view){
	    	DialogFragment newFragment = new MealIngredientsFragment();
	    	newFragment.show(this.getFragmentManager(), "Ingredient Name");
	    }
	    
	    public void addUnitPressed(View view){
	    //	DialogFragment newFragment = new MealIngredientUnitFrag();
	    //	newFragment.show(this.getFragmentManager(), "Ingredient Units");
	    }
	    
	    public void cancel(View view){
	    	onBackPressed();	    	
	    }
	    
	    public void clearBoxes(){
	    	//set all the text to the empty string
	    	String init = "";
	    	meal_name_box.setText(init);
	    	descrip_box.setText(init);    	
	    }
	    
	    public List<ParseObject> background(){
	    	//get the current userID
	    	String userId = "";
			ParseUser currentUser = ParseUser.getCurrentUser();
			if(currentUser != null) {
				userId = currentUser.getString("Owner_Acc");
			}
			
			
			//Find all the foods that are tied to this id
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
			query.whereEqualTo("Acc_ID", userId);
			List<ParseObject> foods = null;
			try {
				foods = query.find();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return foods;
	    }
	    
	    public List<String> setAdapterList(String category){
	    	//create the list that will be used to populate the adapters
	    	List<String> added = new ArrayList<String>();
	    	
//	    	String userId = "";
//	    	ParseUser currentUser = ParseUser.getCurrentUser();
//	    	if(currentUser != null)
//	    		userId = currentUser.getString("Owner_Acc");
//	    	
//	    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
//	    	query.whereEqualTo("userId", userId);
//	    	List<ParseObject> foods = null;
//	    	try{
//	    		foods = query.find();
//	    	} catch (ParseException e){
//	    		e.printStackTrace();
//	    	}
	    	
	    	List<ParseObject> categories = background();
			
			// go through the foods and add their units to the units data adapter
			for (ParseObject categorie : categories) {
				String cat = categorie.getString(category);
				// this if initially adds in a string to added the first time
				if (added.isEmpty()) {
					added.add(cat);
				}
				// added isn't empty, so we will see if it's in there, if it isn't
				// we add to adapter
				else if (added.contains(cat) == false) {
					added.add(cat);
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
	    	
	    	for(String category : added)
				categorydataAdapter.add(category);
			
			//add in the new and select units manually (select category won't show up when dropped down)
	    	categorydataAdapter.add("New");
	    	categorydataAdapter.add("Select a Category");
	    	
	    	//one more check for if newCategory is empty or not since we have to set the position if it is.
	    	if (newCategory == "") {
				position = categorydataAdapter.getCount();
			}
	    	
	    	mealCategories.setAdapter(categorydataAdapter);
	    	mealCategories.setSelection(position);
	    	mealCategories.setOnItemSelectedListener(this);
	    	
	    	
	    }
	    
	    public void showNewCategoryDialog() {
	    	DialogFragment newFragment = new InvAddPageDropdownFrag();
	    	newFragment.show(this.getFragmentManager(), "categories");
	    	//newFragment.show(manager, tag);
	    }
	    
	    
	    @Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
// Only one if statement if no units_dropdown since the parent is always category dropdown.
//	    	if (parent.equals(category_dropdown)) {
				if (parent.getItemAtPosition(position).toString() == "New") {
					showNewCategoryDialog();
				}
//			} 
//Don't need units as far as I know.
//			else {
//				if(parent.getItemAtPosition(position).toString() == "New") {
//					showNewUnitsDialog();
//				}
//			}
			
		}
	    
		//We won't do anything if nothing is selected.
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			//This method does nothing, but we need it because we implement OnItemSelectedListener
			//has to be implemented or we get compile errors		
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
	
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    	Dialog dialogView = dialog.getDialog();
		EditText newIngredientToAdd = (EditText) dialogView.findViewById(R.id.new_ingredient_box);
		String newIngredientAdding = newIngredientToAdd.getText().toString();
		
		EditText newIngredientQuantityValue = (EditText) dialogView.findViewById(R.id.new_ingredients_quant_box);
		String newIngredQuantVal = newIngredientQuantityValue.getText().toString();
		
		Spinner newIngredientUnits = (Spinner) dialogView.findViewById(R.id.new_ingredient_units_dropdown);
		String  newIngredientUnitType = newIngredientUnits.getSelectedItem().toString();
		
		Log.wtf("THIS WAS DONE", newIngredientAdding + " " + newIngredQuantVal + " " + newIngredientUnitType);
    }
	
	@Override
    public void onDialogNegativeClick(DialogFragment dialog) {
		//doNothing
    }

	//we don't need units as far as I know
//    public void populateUnits(String newCategory) {
//    	//populate the units dropdown here
//
//		//the list of strings that are added 
//		List<String> added = new ArrayList<String>();
//		
//    	//get the current userID
//    	String userId = "";
//		ParseUser currentUser = ParseUser.getCurrentUser();
//		if(currentUser != null) {
//			userId = currentUser.getObjectId();
//		}
//		
//		//Find all the foods that are tied to this id
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food");
//		query.whereEqualTo("userId", userId);
//		List<ParseObject> foods = null;
//		try {
//			foods = query.find();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//units datadapter that we're making
//    	ArrayAdapter<String> unitsdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {
//    		@Override
//    	    public View getView(int position, View convertView, ViewGroup parent) {
//
//    	        View v = super.getView(position, convertView, parent);
//    	        if (position == getCount()) {
//    	            ((TextView)v.findViewById(android.R.id.text1)).setText("");
//    	            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
//    	        }
//
//    	        return v;
//    	    }
//    		@Override
//    	    public int getCount() {
//    	        return super.getCount()-1; // you dont display last item. It is used as hint.
//    	    }
//    	};
//    	
//    	unitsdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		
//		//position we'll set the selection to
//    	int position = 0;
//    	
//    	//if the newCategory is empty, we don't do add anything to the 0'th position
//    	//otherwise we set the newCategory as the 0'th position.
//		if (newUnits != "") {
//			unitsdataAdapter.add(newUnits);
//		}
//    	
//		//go through the foods and add their units to the units data adapter
//		for (ParseObject food : foods) {
//			String units = food.getString("units");
//			// this if initially adds in a string to added the first time
//			if (added.isEmpty()) {
//				added.add(units);
//				unitsdataAdapter.add(units);
//			}
//			// added isn't empty, so we will see if it's in there, if it isn't
//			// we add to adapter
//			else if (added.contains(units) == false) {
//				added.add(units);
//				unitsdataAdapter.add(units);
//			}
//		}
//		
//		//add in the new and select units manually (select units won't show up when dropped down)
//    	unitsdataAdapter.add("New");
//    	unitsdataAdapter.add("Select Units");
//    	
//    	//one more check for if newCategory is empty or not since we have to set the position if it is.
//    	if (newUnits == "") {
//			position = unitsdataAdapter.getCount();
//		}
//    	
//    	units_dropdown.setAdapter(unitsdataAdapter);
//    	units_dropdown.setSelection(position);
//    	units_dropdown.setOnItemSelectedListener(this);
//    }
    
// Don't need unit as far as I know
//    public void showNewUnitsDialog() {
//    	DialogFragment newFragment = new InvAddPageDropdownFrag();
//    	newFragment.show(this.getFragmentManager(), "units");
//    }
	
}

