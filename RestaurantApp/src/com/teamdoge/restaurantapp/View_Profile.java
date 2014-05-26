package com.teamdoge.restaurantapp;

import com.parse.Parse;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
public class View_Profile extends Activity {

	
	// establish Text views
	private TextView userNameText;
	private TextView userEmailText;
	private TextView userAcctText;
	private TextView userPNumberText;
	private TextView userUserNameText;
	
	
	/** Called when the user clicks the set Available shifts button */
	// To do
	public void setAvailableTimes(View view) {
		Intent intent = new Intent(View_Profile.this, ShiftSelectorActivity.class);
		startActivity(intent);

	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view__profile);
		
		// setup initial Text views
		userNameText = (TextView) findViewById(R.id.userNameText);
		userUserNameText = (TextView) findViewById(R.id.userUserNameText);
		userEmailText = (TextView) findViewById(R.id.userEmailText);
		userAcctText = (TextView) findViewById(R.id.userAcctText);
	
		// link to parse
		Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
		
		// pull values from data base
		ParseUser user = ParseUser.getCurrentUser();
		String tempUserName = user.getUsername();
		String tempEmail = user.getEmail();
		String accountType = user.getString("Acc_Type");
		String tempRegName = user.getString("Name");
		
		
		
		// set text equal to data base values
		userNameText.setText(tempRegName);
		userUserNameText.setText(tempUserName);
		userEmailText.setText(tempEmail);
		userAcctText.setText(accountType);
		
		// No need to review beyond this point its nothing functional yet
		
		
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view__profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view__profile,
					container, false);
			return rootView;
		}
	}

}
