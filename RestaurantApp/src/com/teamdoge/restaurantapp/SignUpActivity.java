package com.teamdoge.restaurantapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	private View mViewPassword;
	private View mViewSignUp;
	private View mViewLoading;
	private EditText eEmail;
	private String email;
	private View mViewEmail;
	private Spinner spinner;
	private static String type;
	private String name;
	private String lastName;
	private String userData;
	private String emailData;
	private boolean keepGoing;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						keepGoing = true;
						mViewSignUp = findViewById(R.id.login_form);
					    mViewLoading = findViewById(R.id.loading);
					    mViewEmail = findViewById(R.id.email_register);
						eUsername = (EditText) findViewById(R.id.username_register);
						username = eUsername.getText().toString();
						Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
						ePassword = (EditText) findViewById(R.id.password_register);
						password = ePassword.getText().toString();
					    eEmail = (EditText) mViewEmail;
						email = eEmail.getText().toString();
						mViewPassword = findViewById(R.id.password_register_confirm);
						rePassword = (EditText) mViewPassword;
						rpassword = rePassword.getText().toString();
						spinner = (Spinner) findViewById(R.id.spinner2);
						spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
						name = ((EditText) findViewById(R.id.name)).getText().toString();
						lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
					    ((TextView) mViewEmail).setError(null);
					    ((TextView) mViewPassword).setError(null);
					    eUsername.setError(null);;
					    rePassword.setError(null);;
					    if (TextUtils.isEmpty(username)) {
					    	(eUsername).setError(getString(R.string.error_field_required));
					    	eUsername.requestFocus();
					    	keepGoing = false;
					    }
						if (TextUtils.isEmpty(email)) {
							(eEmail).setError(getString(R.string.error_field_required));
							eEmail.requestFocus();
							keepGoing = false;
						}
						else if (!checkEmail()) {
						  ((TextView) mViewEmail).setError(getString(R.string.error_email));
						  mViewEmail.requestFocus();
							keepGoing = false;
						}
						if (TextUtils.isEmpty(rpassword)) {
						  (rePassword).setError(getString(R.string.error_field_required));
						  rePassword.requestFocus();
							keepGoing = false;
						}
					    if (TextUtils.isEmpty(password)) {
					      (ePassword).setError(getString(R.string.error_field_required));
					      ePassword.requestFocus();
							keepGoing = false;
						}
					    if (!checkPassword()) {
						  ((TextView) mViewPassword).setError(getString(R.string.error_password));
						  (ePassword).setError(getString(R.string.error_password));
						  mViewPassword.requestFocus();
							keepGoing = false;
						}
						if (keepGoing) {
						  showProgress(true);
						  String credential = username + ":" + password + ":" + email;
						  intent.putExtra(CREDENTIALS, credential);
						  intent.putExtra(TYPE, String.valueOf(spinner.getSelectedItem()));
						  startActivity(intent);
						  Toast.makeText(getApplicationContext(),"I'm a " + String.valueOf(spinner.getSelectedItem()) +
								                                  " and my name is " + name + " " + lastName, Toast.LENGTH_SHORT).show();
						  finish();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
