package com.teamdoge.restaurantapp;


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

public class Edit_Profile extends Activity {
	// establish Text views
	private EditText editNameText;
	private EditText editEmailText;
	private EditText editPNumberText;
	
			
	
	///** Called when the user clicks the set Confirm button */
	public void setAvailableTimes(View view) {
    	Intent intent = new Intent(Edit_Profile.this, ProfileActivity.class);
	
		startActivity(intent);
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit__profile);
		
		// setup initial Edit Texts
		editNameText = (EditText) findViewById(R.id.editNameText);
		editEmailText = (EditText) findViewById(R.id.editEmailText);
		editPNumberText = (EditText) findViewById(R.id.editPNumberText);
		
		// link to parse
		Parse.initialize(this, "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", 
		"fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
				

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit__profile, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_edit__profile,
					container, false);
			return rootView;
		}
	}

}
