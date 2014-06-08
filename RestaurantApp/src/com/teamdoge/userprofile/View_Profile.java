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

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;



public class View_Profile extends Fragment {

	// establish Text views
	private TextView userNameText;
	private TextView userEmailText;
	private TextView userAcctText;
	private TextView userPNumberText;
	private TextView userUserNameText;
	private Button shiftsButton;
	private ParseImageView profile_pic;
	
	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
	
	private Menu optionsMenu;
	
	// new fragement creation
	public static View_Profile newInstance() {
	View_Profile fragment = new View_Profile();
	Bundle args = new Bundle();
	fragment.setArguments(args);
	return fragment;
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		// link to parse
		Parse.initialize(getActivity(), applicationId, clientKey);
	}
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
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
		userPNumberText = (TextView) view.findViewById(R.id.userPNumberText);
		shiftsButton = (Button) view.findViewById(R.id.button1);
		
		parseFetch(view);
		setTextViews();

		// shift selector	
		shiftsButton.setOnClickListener(
		new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ShiftSelectorActivity.class);
			startActivity(intent);
				}
			});	
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.view__profile, menu);
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

	private void parseFetch(View view) {
		// Load the profile picture from parse
		// note the image must be stored as a .bmp
		profile_pic = (ParseImageView) view.findViewById(R.id.profile_pic);
		ParseFile photoFile = ParseUser.getCurrentUser().getParseFile("Profile_Pic");
	    if (photoFile != null) {
	    	profile_pic.setParseFile(photoFile);
	    	profile_pic.loadInBackground(new GetDataCallback() {
	            @Override
	            public void done(byte[] data, ParseException e) {
	            	profile_pic.setVisibility(View.VISIBLE);
	            
	            }
	    	});
	    }
		
	}

	private void setTextViews() {
		// pull values from data base
		ParseUser user = ParseUser.getCurrentUser();
		String tempUserName = user.getUsername();
		String tempEmail = user.getEmail();
		String accountType = user.getString("Acc_Type");
		String tempRegName = user.getString("Name");
		String tempPhone = user.getString("Phone_Number");
		
		// set text equal to data base values
		userNameText.setText(tempRegName);
		userUserNameText.setText(tempUserName);
		userEmailText.setText(tempEmail);
		userAcctText.setText(accountType);
		userPNumberText.setText(tempPhone);
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== R.id.actionEdit){
        	Intent intent = new Intent(getActivity(), Edit_Profile.class);
        	startActivity(intent);
        	return true;
        }
        else if (item.getItemId() == R.id.menu_refresh) {
        	setRefreshActionButtonState(true);
        	parseFetch(getView());
    		setTextViews();
    		setRefreshActionButtonState(false);
        	return true;
        }
           
        	else
            return super.onOptionsItemSelected(item);
	
    }
	
	
	public void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu
	            .findItem(R.id.menu_refresh);
	        if (refreshItem != null) {
	            if (refreshing) {
	                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
	            } else {
	                refreshItem.setActionView(null);
	            }
	        }
	    }
	}
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
}

