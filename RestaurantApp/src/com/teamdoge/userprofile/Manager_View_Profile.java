package com.teamdoge.userprofile;

import com.parse.Parse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

public class Manager_View_Profile extends Fragment {
	
	// establish Textviews/buttons
	private TextView lookuserNameText;
	private TextView lookuserEmailText;
	private TextView lookuserAcctText;
	private TextView lookuserPNumberText;
	private TextView lookuserUserNameText;
	private Button changeTypeButton;
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";	
	
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
		Parse.initialize(getActivity(), applicationId, clientKey);
	}
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
	
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
		
		// change employee type///////////////
		changeTypeButton.setOnClickListener(
				new View.OnClickListener() {
					
		@Override
		public void onClick(View v) {

		// This will need to be changed to the user the manager clicked on ***//
		Parse.initialize(getActivity(), applicationId, clientKey);
		ParseUser user = ParseUser.getCurrentUser();
		user.put("Acc_Type", "Manager");
		
		}});
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_manager__view__profile, container, false);
	}
	
	// menu and menu item section
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.manager__view__profile, menu);
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
}
