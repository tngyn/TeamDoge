package com.teamdoge.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.RequestPasswordResetCallback;
import com.parse.ParseUser;
import com.teamdoge.restaurantapp.R;

public class ForgotPasswordActivity extends Activity {

	private String email;
	private EditText eEmail;

	private String applicationId = "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8";
	private String clientKey = "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF";
	
	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Parse.initialize(this, applicationId, clientKey);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		findViewById(R.id.recover_password_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						eEmail = (EditText) findViewById(R.id.recover_email); // Content
																				// of
																				// the
																				// Email
																				// Box
						email = eEmail.getText().toString(); // String of the
																// Email Box
						ParseUser.requestPasswordResetInBackground(email,
								new RequestPasswordResetCallback() {
									public void done(ParseException e) {
										if (e == null) {
											Toast.makeText(
													getApplicationContext(),
													"Link sent to email",
													Toast.LENGTH_LONG).show();
											Intent intent = new Intent(
													ForgotPasswordActivity.this,
													LoginActivity.class); // Intent
																			// to
																			// go
																			// back
																			// to
																			// loginActivity
											startActivity(intent);
											finish();
										} else {
											Toast.makeText(
													getApplicationContext(),
													e.toString(),
													Toast.LENGTH_LONG).show();
										}
									}
								});
					}
				});
	}	
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
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
			View rootView = inflater.inflate(R.layout.fragment_forgot_password,
					container, false);
			return rootView;
		}
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//

}
