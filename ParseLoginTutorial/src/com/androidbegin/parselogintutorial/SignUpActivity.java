package com.androidbegin.parselogintutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	String usernametxt;
	String passwordtxt;
	EditText password;
	EditText username;
	String emailtxt;
	String phonetxt;
	EditText email;
	EditText phone;
	Button signup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		
		signup = (Button) findViewById(R.id.signup);
	
	signup.setOnClickListener(new OnClickListener() {

		public void onClick(View arg0) {
			final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				// Retrieve the text entered from the EditText
				usernametxt = username.getText().toString();
				passwordtxt = password.getText().toString();
				emailtxt = email.getText().toString();
				phonetxt = phone.getText().toString();
				
				// Force user to fill up the form
				if (usernametxt.equals("") || passwordtxt.equals("") || emailtxt.equals("") || phonetxt.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please complete the sign up form",
							Toast.LENGTH_LONG).show();

				} else {
					// Save new user data into Parse.com Data Storage
					ParseUser user = new ParseUser();
					user.setUsername(usernametxt);
					user.setPassword(passwordtxt);
					user.setEmail(emailtxt);
					user.put("phone", phonetxt);
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							if (e == null) {
								// Show a simple Toast message upon successful registration
								Toast.makeText(getApplicationContext(),
										"Registration successful.",
										Toast.LENGTH_SHORT).show();
								ParseInstallation installation = ParseInstallation.getCurrentInstallation();
								installation.put("user", ParseUser.getCurrentUser());
								installation.saveInBackground();

								ParseUser.logInInBackground(usernametxt, passwordtxt,
										new LogInCallback() {
											public void done(ParseUser user, ParseException e) {
												if (user != null) {
													// If user exist and authenticated, send user to Welcome.class
													Intent intent = new Intent(
															SignUpActivity.this,
															Welcome.class);
													startActivity(intent);
													Toast.makeText(getApplicationContext(),
															"Successfully Logged in",
															Toast.LENGTH_SHORT).show();
													finish();
												} else {
													Toast.makeText(
															getApplicationContext(),
															"Login failed.",
															Toast.LENGTH_LONG).show();
												}
											}
										});
							} else {
								Toast.makeText(getApplicationContext(),
										"Sign up Error", Toast.LENGTH_LONG)
										.show();
							}
						}
					});
				}
			} else {
				Toast.makeText(
						getApplicationContext(),
						"Error: No internet connection",
						Toast.LENGTH_LONG).show();
			}
		}
	});
	}
}
	

