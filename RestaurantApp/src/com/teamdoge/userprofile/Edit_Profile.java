package com.teamdoge.userprofile;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.provider.MediaStore;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

import android.graphics.Bitmap;

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
	private ParseObject shifts;

	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";

	private static int RESULT_LOAD_IMAGE = 1;

	// /** Called when the user clicks the set Confirm button */
	public void applyEdit(View view) {

		// setup initial Edit Texts
		editNameText = (EditText) findViewById(R.id.editNameText);
		editEmailText = (EditText) findViewById(R.id.editEmailText);
		editPNumberText = (EditText) findViewById(R.id.editPNumberText);

		// get the information from the edit text fields
		EditText editNameText = (EditText) findViewById(R.id.editNameText);
		String userName = editNameText.getText().toString();
		EditText editPNumberText = (EditText) findViewById(R.id.editPNumberText);
		String userPhone = editPNumberText.getText().toString();
		EditText editEmailText = (EditText) findViewById(R.id.editEmailText);
		String userEmail = editEmailText.getText().toString();

		// store the information back in the database
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
		Parse.initialize(this, applicationId, clientKey);

		// get current user
		final ParseUser user = ParseUser.getCurrentUser();

		// pull values from data base
		String tempName = user.getString("Name");
		String tempEmail = user.getEmail();
		String tempPhone = user.getString("Phone_Number");

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Shifts");
		query.whereEqualTo("Username", user.getUsername());
		try {
			List<ParseObject> list = query.find();
			shifts = list.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// set text equal to data base values
		editNameText.setText(tempName);
		editEmailText.setText(tempEmail);
		editPNumberText.setText(tempPhone);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// setup initial Edit Texts
				editNameText = (EditText) findViewById(R.id.editNameText);
				editEmailText = (EditText) findViewById(R.id.editEmailText);
				editPNumberText = (EditText) findViewById(R.id.editPNumberText);

				// get the information from the edit text fields
				EditText editNameText = (EditText) findViewById(R.id.editNameText);
				String userName = editNameText.getText().toString();
				EditText editPNumberText = (EditText) findViewById(R.id.editPNumberText);
				String userPhone = editPNumberText.getText().toString();
				EditText editEmailText = (EditText) findViewById(R.id.editEmailText);
				String userEmail = editEmailText.getText().toString();

				// store the information back in the database
				shifts.put("Name", userName);
				user.setEmail(userEmail);
				user.put("Name", userName);
				user.put("Phone_Number", userPhone);
				user.saveInBackground();
				shifts.saveInBackground();
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
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

	// pick image from gallery
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
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
			Parse.initialize(this, applicationId, clientKey);
			ParseUser user = ParseUser.getCurrentUser();
			user.put("Profile_Pic", sendProfilePic);
			// user.put("Profile_Pic", newProfilePic);
			user.saveInBackground();

		}
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

	// *******************************************************************************************************************//
	// 													Controller 														  //
	// *******************************************************************************************************************//
	
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
	
	// *******************************************************************************************************************//
	// 													End Controller 													  //
	// *******************************************************************************************************************//
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//

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
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
}
