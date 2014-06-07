package com.teamdoge.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		findViewById(R.id.recover_password_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						eEmail = (EditText) findViewById(R.id.recover_email); //Content of the Email Box
						email = eEmail.getText().toString(); // String of the Email Box
						ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
							public void done(ParseException e) {
								if (e == null) {
									Toast.makeText(getApplicationContext(),"Link sent to email", Toast.LENGTH_LONG).show();
							        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class); // Intent to go back to loginActivity
							        startActivity(intent);
							        finish();
								} else {
									Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_forgot_password,
					container, false);
			return rootView;
		}
	}

}
