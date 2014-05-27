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
import android.widget.TextView;
import android.os.Build;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.teamdoge.restaurantapp.R;

public class View_Profile extends Fragment {

	
	// establish Text views
	private TextView userNameText;
	private TextView userEmailText;
	private TextView userAcctText;
	private TextView userPNumberText;
	private TextView userUserNameText;
	private Button editButton;
	private Button shiftsButton;
	
	
	///** Called when the user clicks the set Available shifts button */
	
	public static View_Profile newInstance() {
	View_Profile fragment = new View_Profile();
	Bundle args = new Bundle();
	fragment.setArguments(args);
	return fragment;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		// link to parse
		Parse.initialize(getActivity(), "fb0rPJ5AFeAx5JNdMV7Yxlcw3paruRc2XNPjOUWo", "fDpkgdVM4vwTTjYdQSq5kMRyuoEQzt6JCuI3ivWC");
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_view_profile, container, false);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// setup initial Text views
		userNameText = (TextView) view.findViewById(R.id.userNameText);
		userUserNameText = (TextView) view.findViewById(R.id.userUserNameText);
		userEmailText = (TextView) view.findViewById(R.id.userEmailText);
		userAcctText = (TextView) view.findViewById(R.id.userAcctText);
		editButton = (Button) view.findViewById(R.id.button2);
		shiftsButton = (Button) view.findViewById(R.id.button1);
		
		// pull values from data base
		ParseUser user = ParseUser.getCurrentUser();
		String tempUserName = user.getUsername();
		String tempEmail = user.getEmail();
		String accountType = user.getString("Acc_Type");
		String tempRegName = user.getString("Name");
		
		editButton.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						editProfile(editButton);
						Intent intent = new Intent(getActivity(), Edit_Profile.class);
						
						startActivity(intent);
					}
				});	
		
		
		shiftsButton.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						editProfile(editButton);
						Intent intent = new Intent(getActivity(), ShiftSelectorActivity.class);
						
						startActivity(intent);
					}
				});
		
		// set text equal to data base values
		userNameText.setText(tempRegName);
		userUserNameText.setText(tempUserName);
		userEmailText.setText(tempEmail);
		userAcctText.setText(accountType);
		
		
	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.view__profile, menu);
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
}
