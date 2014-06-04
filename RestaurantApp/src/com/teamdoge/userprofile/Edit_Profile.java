package com.teamdoge.userprofile;


import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.R.id;
import com.teamdoge.restaurantapp.R.layout;
import com.teamdoge.restaurantapp.R.menu;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Edit_Profile extends Activity {

	// establish Text views
	private EditText editNameText;
	private EditText editEmailText;
	private EditText editPNumberText;
	private Button submitBtn;
	private Button cancelBtn;
	private ParseFile sendProfilePic;
	private Bitmap newProfilePic;
	private byte[] getPhotoData;

	
	
	// Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
    private static int RESULT_LOAD_IMAGE = 1;

	

	///** Called when the user clicks the set Confirm button */
	public void applyEdit(View view) {
    	
    	
		// setup initial Edit Texts
		editNameText = (EditText) findViewById(R.id.editNameText);
		editEmailText = (EditText) findViewById(R.id.editEmailText);
		editPNumberText = (EditText) findViewById(R.id.editPNumberText);
		
		
    	
    	//get the information from the edit text fields
    	EditText editNameText = (EditText) findViewById(R.id.editNameText);
    	String userName = editNameText.getText().toString();
    	EditText editPNumberText = (EditText) findViewById(R.id.editPNumberText);
    	String userPhone = editPNumberText.getText().toString();
    	EditText editEmailText = (EditText) findViewById(R.id.editEmailText);
    	String userEmail = editEmailText.getText().toString();
    	
    	//store the information back in the database
    	ParseUser user = ParseUser.getCurrentUser();
    	user.setEmail(userEmail);
    	user.put("Name", userName);
    	user.put("PhoneNumber", userPhone);
    	

		onBackPressed();
	}


	// Cancel Button does not save anything to the database
	// just makes a new view profile screen
	public void cancelEdit(View view) {
		onBackPressed();
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Edit Profile");  
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_edit__profile);

		// setup initial Edit Texts
		editNameText = (EditText) findViewById(R.id.editNameText);
		editEmailText = (EditText) findViewById(R.id.editEmailText);
		editPNumberText = (EditText) findViewById(R.id.editPNumberText);
		submitBtn = (Button) findViewById(R.id.submit_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		

		// link to parse
		Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", 
		"k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");

		// get current user
		final ParseUser user = ParseUser.getCurrentUser();

		// pull values from data base
		String tempName = user.getString("Name");
		String tempEmail = user.getEmail();
		String tempPhone = user.getString("PhoneNumber");

		// set text equal to data base values
		editNameText.setText(tempName);
		editEmailText.setText(tempEmail);
		editPNumberText.setText(tempPhone);
		
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		cancelBtn.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
	     // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        
		submitBtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// setup initial Edit Texts
						editNameText = (EditText) findViewById(R.id.editNameText);
						editEmailText = (EditText) findViewById(R.id.editEmailText);
						editPNumberText = (EditText) findViewById(R.id.editPNumberText);
				    
				    	
				    	//get the information from the edit text fields
				    	EditText editNameText = (EditText) findViewById(R.id.editNameText);
				    	String userName = editNameText.getText().toString();
				    	Log.wtf("UsERNAME: ", userName);
				    	EditText editPNumberText = (EditText) findViewById(R.id.editPNumberText);
				    	String userPhone = editPNumberText.getText().toString();
				    	EditText editEmailText = (EditText) findViewById(R.id.editEmailText);
				    	String userEmail = editEmailText.getText().toString();
				    	Log.wtf("PHONENUMBER: ", userPhone);
				    	Log.d("ASDASD", user.getUsername());
				    	
				    	//store the information back in the database
				    	user.setEmail(userEmail);
				    	user.put("Name", userName);
				    	user.put("Phone_Number", userPhone);
				    	user.saveInBackground();

						onBackPressed();
					}
				});
		
		
		// To access gallery
	    Button changePic = (Button) findViewById(R.id.changeProfilePic);
	    changePic.setOnClickListener(new View.OnClickListener() {
	         
	        @Override
	        public void onClick(View arg0) {
	             
	            Intent i = new Intent(
	                    Intent.ACTION_PICK,
	                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	             
	            startActivityForResult(i, RESULT_LOAD_IMAGE);
	        }
	    });
	    
		}	 

	//pick image from  gallery
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	     
	    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };

	        Cursor cursor = getContentResolver().query(selectedImage,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        String picturePath = cursor.getString(columnIndex);
	        cursor.close();
	        
	        // convert into bitmap then compress and send
	        newProfilePic = (BitmapFactory.decodeFile(picturePath));
	        ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
            newProfilePic.compress(Bitmap.CompressFormat.PNG, 100, stream); 
            getPhotoData = stream.toByteArray();
            sendProfilePic = new ParseFile("Profile_Pic.Png", getPhotoData);
	        
	        
	     // get current user and save file
	        Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
			ParseUser user = ParseUser.getCurrentUser();
	        user.put("Profile_Pic", sendProfilePic);
	       // user.put("Profile_Pic", newProfilePic);
	        user.saveInBackground();

	    }	    
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
        	case android.R.id.home:	
        		onBackPressed();
        		this.finish();

        		return true;

        	default:
        		return super.onOptionsItemSelected(item);
		}
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