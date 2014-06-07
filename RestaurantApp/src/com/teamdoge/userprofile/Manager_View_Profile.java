package com.teamdoge.userprofile;

import com.parse.Parse;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.trackingmenu.AddMenuItemActivity;

public class Manager_View_Profile extends Fragment {
	
	// establish Textviews/buttons
	private TextView lookuserNameText;
	private TextView lookuserEmailText;
	private TextView lookuserAcctText;
	private TextView lookuserPNumberText;
	private TextView lookuserUserNameText;
	private Button changeTypeButton;
	
	
	
	// new fragment created when called
	public static Manager_View_Profile newInstance() {
	Manager_View_Profile fragment = new Manager_View_Profile();
	Bundle args = new Bundle();
	fragment.setArguments(args);
	return fragment;
	}	
		
	// link to parse
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Parse.initialize(getActivity(), "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_manager__view__profile, container, false);
	}
	
	
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// setup initial Text views
		lookuserNameText = (TextView) view.findViewById(R.id.lookuserNameText);
		lookuserUserNameText = (TextView) view.findViewById(R.id.lookuserUserNameText);
		lookuserEmailText = (TextView) view.findViewById(R.id.lookuserEmailText);
		lookuserAcctText = (TextView) view.findViewById(R.id.lookuserAcctText);
		lookuserPNumberText = (TextView) view.findViewById(R.id.lookuserPNumberText);
		changeTypeButton = (Button) view.findViewById(R.id.button2);
		
		
		// This will need to be changed to the user the manager clicked on ***//
		ParseUser user = ParseUser.getCurrentUser();
		// ****************************************************//
		
		// pull values from database
		String looktempUserName = user.getUsername();
		String looktempEmail = user.getEmail();
		String lookaccountType = user.getString("Acc_Type");
		String looktempRegName = user.getString("Name");
		String looktempPhone = user.getString("Phone_Number");
		
		// set text equal to data base values
		lookuserNameText.setText(looktempRegName);
		lookuserUserNameText.setText(looktempUserName);
		lookuserEmailText.setText(looktempEmail);
		lookuserAcctText.setText(lookaccountType);
		lookuserPNumberText.setText(looktempPhone);
		
		// change button text based on type
		if (lookaccountType == "Employee"){
		changeTypeButton.setText("Make Manager");}
		
		//else if (accountType == "Manager"){
		//changeTypeButton.setText("Make Employee");}
		
		
	////	// change employee type///////////////
		changeTypeButton.setOnClickListener(
				new View.OnClickListener() {
					
		@Override
		public void onClick(View v) {

		// This will need to be changed to the user the manager clicked on ***//
		Parse.initialize(getActivity(), "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		ParseUser user = ParseUser.getCurrentUser();
		// ****************************************************//
						
		// get initial account value
		//String accountType = user.getString("Acc_Type");
				    	
		// check to see what type before changing
		//if (accountType == "Employee"){
			user.put("Acc_Type", "Manager");
				//    }
		//else if (accountType == "Manager"){
		//	user.put("Acc_Type", "Employee");
				//    }
				    	
				    	  	
		// refresh all the values for view
		//accountType = user.getString("Acc_Type");// get the new string value
				    	
		// update button label
		//if (accountType == "Employee"){
		//changeTypeButton.setText("Make Manager");
		//		}
		//else if (accountType == "Manager"){
		//changeTypeButton.setText("Make Employee");
		//		}
		// update account type label
		//lookuserAcctText.setText(accountType);
		// End refresh views
			}
		});// end of click
		
	}// end of on create
	
	
	
	
	// menu and menu item section
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.manager__view__profile, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
