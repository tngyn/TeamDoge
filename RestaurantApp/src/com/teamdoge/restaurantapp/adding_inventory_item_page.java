package com.teamdoge.restaurantapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class adding_inventory_item_page extends Activity {
	//used to clear all the text boxes (initialize them for typing)
	private String init;
	
	private EditText item_name_box;
	private EditText quantity_box;
	private EditText descrip_box;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_screen);
        
        Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
        
        //Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
        
        init = "";
        //initialize the food name box
        item_name_box = (EditText) findViewById(R.id.item_name_box);
        item_name_box.setText(init);
        //initialize the quantity box
        quantity_box = (EditText) findViewById(R.id.quant_box);
        quantity_box.setText(init);
        //initialize the description box
        descrip_box = (EditText) findViewById(R.id.description_box);
        descrip_box.setText(init);
		
        
//		ParseObject food = new ParseObject("Food");
//		food.put("name", "chocolate");
//		food.put("quantity", 135);
//		food.put("units", "lbs");
//		food.put("description", "Hi Russel!");
//		food.put("Order_Date", "5/7/14");
//		food.saveInBackground();
		
		
        /*
		ParseUser user = new ParseUser();
		user.setUsername("Russel");
		user.setPassword("RusselIsAwesome");
		user.setEmail("russelisconceited@gmail.com");
		user.put("Name", "Jimmy Rustler");
		user.put("Account_Type", 5);
		user.put("Code", "RusselLovesHimself");
		//int[] arry = {0, 1, 2};
		//user.put("Free_Times", arry );
		user.put("Phone_Number", "555-444-333");
		user.put("Home_Screen_Preference", 3);
		user.put("Shrink_Tracking", 12);
		user.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      // Hooray! Let them use the app now.
			    } else {
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    }
			  }
			});
		ParseUser.logInInBackground("Jerry", "showmethemoney", new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      // Hooray! The user is logged in.
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    }
			  }
			});
		*/
		
    }
    
    public void submit(View view){
    	String foodName = "";
    	foodName = item_name_box.getText().toString();
    	item_name_box.setText(init);
    	
    	int quantity;
    	quantity = Integer.parseInt(quantity_box.getText().toString());
    	quantity_box.setText(init);
    	
    	String description = "";
    	description = descrip_box.getText().toString();
    	descrip_box.setText(init);
    	
    	ParseObject food = new ParseObject("Food");
    	food.put("name", foodName);
    	food.put("quantity", quantity);
    	food.put("description", description);
    	food.saveInBackground();
    }
    
    public void cancel(View view){
    	item_name_box.setText(init);
    	quantity_box.setText(init);
    	descrip_box.setText(init);
    }

}
