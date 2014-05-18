package com.teamdoge.restaurantapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
/*import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;*/
import android.widget.TextView;

public class ProfileActivity extends Activity implements TimePickedListener {

	//start paste
	public void showTimePickerDialog(View v) { //View v here is the button object that was clicked
		Log.d("Debugging","showing TimePickerDialog");
		DialogFragment newFragment = new TimePickerFragment();
		Bundle args = new Bundle();
		args.putInt("sourceId", v.getId());
		newFragment.setArguments(args);
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void makeEditable(View v) {
		Log.d("Debugging","makeEditable");
		EditText et1 = (EditText) findViewById(R.id.from_field);
		EditText et2 = (EditText) findViewById(R.id.to_field);
		if (et1.isEnabled()) {
			et1.setEnabled(false);
			et2.setEnabled(false);
			((Button) v).setText("Edit");
		} else {
			et1.setEnabled(true);
			et2.setEnabled(true);
			((Button) v).setText("Done");
		}
	}
	
	public void onTimePicked(Calendar time, int sourceId) {
		// display selected time in the TextView
		// TODO: make picker come up when textfield is clicked. 
		Log.d("Debugging", "found: Id=" + sourceId);
		EditText et = (EditText) findViewById(sourceId);
		Log.d("Debugging", "EditText identified: Id=" + sourceId + "et=" + et.toString());
		et.setText(DateFormat.format("h:mm a", time));
	}
	//end paste
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//initialize parse database
		//Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_profile,
					container, false);
			return rootView;
		}
	}


}
