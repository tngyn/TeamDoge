package com.teamdoge.restaurantapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;
public class Edit_Profile extends Activity {

	// establish Text views
	private TextView userNameText;
	private TextView userEmailText;
	private TextView userAcctText;
	private TextView userPNumberText;
	private TextView userUserNameText;


	//** Called when the user clicks the set Available shifts button */
//	public void setAvailableTimes(View view) {
//    	Intent intent = new Intent(Edit_Profile.this, ProfileActivity.class);
//
//		startActivity(intent);
//	}

	//** Called on when the edit button on the action bar is pressed.
	public void openEdit() {
    	Intent intent = new Intent(Edit_Profile.this, Edit_Profile.class);

		startActivity(intent);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Profile"); 
		setContentView(R.layout.activity_view__profile);

		// setup initial Text views
		userNameText = (TextView) findViewById(R.id.userNameText);
		userUserNameText = (TextView) findViewById(R.id.userUserNameText);
		userEmailText = (TextView) findViewById(R.id.userEmailText);
		userAcctText = (TextView) findViewById(R.id.userAcctText);
		userPNumberText = (TextView) findViewById(R.id.userPNumberText);

		// link to parse
		Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");

		// pull values from data base
		ParseUser user = ParseUser.getCurrentUser();
		String tempUserName = user.getUsername();
		String tempEmail = user.getEmail();
		String accountType = user.getString("Acc_Type");
		String tempRegName = user.getString("Name");
		String tempPhone = user.getString("PhoneNumber");

		// set text equal to data base values
		userNameText.setText(tempRegName);
		userUserNameText.setText(tempUserName);
		userEmailText.setText(tempEmail);
		userAcctText.setText(accountType);
		userPNumberText.setText(tempPhone);
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view__profile, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.actionEdit:
	            openEdit();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


}