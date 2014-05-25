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
