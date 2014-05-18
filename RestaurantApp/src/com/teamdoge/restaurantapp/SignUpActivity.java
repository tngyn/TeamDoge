package com.teamdoge.restaurantapp;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class SignUpActivity extends Activity {

	public static final String CREDENTIALS = "com.teamdoge.restaurantapp.CREDENTIALS";
	public static final String TYPE = "com.teamdoge.restaurantapp.TYPE";
	private EditText eUsername;
	private String username;
	private EditText ePassword;
	private String password;
	private EditText rePassword;
	private String rpassword;
	private View mViewSignUp;
	private View mViewLoading;
	private EditText eEmail;
	private String email;
	private Spinner spinner;
	private static String type;
	private String name;
	private String lastName;
	private boolean keepGoing;
	private boolean signup;
	ParseUser user = new ParseUser();
	
	protected void onCreate(Bundle savedInstanceState) {
		
		getActionBar().setTitle("Sign Up");  
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		Parse.initialize(this, "0yjygXOUQ9x0ZiMSNUV7ZaWxYpSNm9txqpCZj6H8", "k5iKrdOVYp9PyYDjFSay2W2YODzM64D5TqlGqxNF");
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_sign_up);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						keepGoing = true; // Flag to approve sign up if all parameters are ok
						mViewSignUp = findViewById(R.id.login_form); //Sign Up Layout
					    mViewLoading = findViewById(R.id.loading); //Loading Layout
						eUsername = (EditText) findViewById(R.id.username_register); //Content of the username Box
						username = eUsername.getText().toString(); //String of the content of the username Box
						ePassword = (EditText) findViewById(R.id.password_register); //Content of the password box
						password = ePassword.getText().toString(); //String of the password box
					    eEmail = (EditText) findViewById(R.id.email_register); //Content of the Email Box
						email = eEmail.getText().toString(); // String of the Email Box
						rePassword = (EditText) findViewById(R.id.password_register_confirm); // Content of the confirmation password
						rpassword = rePassword.getText().toString(); //String of the confirmation password
						spinner = (Spinner) findViewById(R.id.spinner2); // Spinner for the type of account
						spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener()); // Setting up the spinner
						name = ((EditText) findViewById(R.id.name)).getText().toString(); //String of the name
						lastName = ((EditText) findViewById(R.id.last_name)).getText().toString(); //String of the Last Name
						//Setting all the boxes error to null
					    ePassword.setError(null);
					    eUsername.setError(null);
					    eEmail.setError(null);
					    rePassword.setError(null);
					    ePassword.setError(null);
					    eUsername.setError(null);
					    rePassword.setError(null);
					    //If username box is empty show error and prevent register
					    if (TextUtils.isEmpty(username)) {
					    	eUsername.setError(getString(R.string.error_field_required));
					    	eUsername.requestFocus();
					    	keepGoing = false;
					    }
					    //If email box is empty show error and prevent register
						if (TextUtils.isEmpty(email)) {
							eEmail.setError(getString(R.string.error_field_required));
							eEmail.requestFocus();
							keepGoing = false;
						}
						//Also check if email is valid by checking if it includes a @
						else if (!checkEmail()) {
						  eEmail.setError(getString(R.string.error_email));
						  eEmail.requestFocus();
							keepGoing = false;
						}
						//Check if confirmation password box is empty and show error and prevent register
						if (TextUtils.isEmpty(rpassword)) {
						  rePassword.setError(getString(R.string.error_field_required));
						  rePassword.requestFocus();
							keepGoing = false;
						}
						//Check if password box is empty and show error and prevent register
					    if (TextUtils.isEmpty(password)) {
					      ePassword.setError(getString(R.string.error_field_required));
					      ePassword.requestFocus();
							keepGoing = false;
						}
					    //Check if password and confirmation password match and show error and prevent register
					    if (!checkPassword()) {
						  ePassword.setError(getString(R.string.error_password));
						  ePassword.setError(getString(R.string.error_password));
						  ePassword.requestFocus();
						  keepGoing = false;
						}
					    //If no error then procceed with register
						if (keepGoing) {
						  //Show loading animation
						  showProgress(true);
						  //Set all the parameters
						  signup = true;
						  user.setUsername(username);
						  user.setEmail(email);
						  user.setPassword(password);
						  user.put("Acc_Type", String.valueOf(spinner.getSelectedItem()));
						  user.put("Name", name + " " + lastName);
						  user.put("Remember_Me", false);
						  //Upload the information
						  user.signUpInBackground(new SignUpCallback() {
							  public void done(ParseException e) {
							    if (e == null) {
							      //If no exception then goes to login page
							      Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
							      signup = true;
							      showProgress(false);
								  if (signup) {
								        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); // Intent to go back to loginActivity
								        startActivity(intent);
								        finish();
									  }
							    } else {
							      //If there is error show a toast with the error
							      Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
							      Log.d("error", e.toString());
							      showProgress(false);
							      signup = false;
							    }
							  }
						  });

						}
					}
				});
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
	public boolean checkEmail() {
		return (email.contains("@"));
	}
	public boolean checkPassword() {
		return password.equals(rpassword);
	}
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mViewLoading.setVisibility(View.VISIBLE);
			mViewLoading.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewLoading.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mViewSignUp.setVisibility(View.VISIBLE);
			mViewSignUp.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewSignUp.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mViewLoading.setVisibility(show ? View.VISIBLE : View.GONE);
			mViewSignUp.setVisibility(show ? View.GONE : View.VISIBLE);
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
			View rootView = inflater.inflate(R.layout.fragment_sign_up,
					container, false);
			return rootView;
		}
	}
	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		 
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			switch(pos) {
			case 0:
			  type = parent.getItemAtPosition(pos).toString();
			  break;
			case 1:
			  type = parent.getItemAtPosition(pos).toString();
			  break;
			default:
			  break;
			}
		  }
		 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }
		 
		}

}
