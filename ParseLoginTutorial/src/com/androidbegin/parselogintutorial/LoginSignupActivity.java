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

public class LoginSignupActivity extends Activity {
	// Declare Variables
	Button loginbutton;
	Button signup;
	String usernametxt;
	String passwordtxt;
	EditText password;
	EditText username;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from main.xml
		setContentView(R.layout.loginsignup);
		// Locate EditTexts in main.xml
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		// Locate Buttons in main.xml
		loginbutton = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);

		// Login Button Click Listener
		loginbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
				if (activeNetwork != null && activeNetwork.isConnected()) {
					// Retrieve the text entered from the EditText
					usernametxt = username.getText().toString();
					passwordtxt = password.getText().toString();

					// Send data to Parse.com for verification
					ParseUser.logInInBackground(usernametxt, passwordtxt,
							new LogInCallback() {
								public void done(ParseUser user, ParseException e) {
									if (user != null) {
										ParseInstallation installation = ParseInstallation.getCurrentInstallation();
										installation.put("user", ParseUser.getCurrentUser());
										installation.saveInBackground();
										// If user exist and authenticated, send user to Welcome.class
										Intent intent = new Intent(
												LoginSignupActivity.this,
												Welcome.class);
										startActivity(intent);
										Toast.makeText(getApplicationContext(),
												"Successfully logged in",
												Toast.LENGTH_LONG).show();
										finish();
									} else {
										Toast.makeText(
												getApplicationContext(),
												"Error: Username or password are incorrect.",
												Toast.LENGTH_LONG).show();
									}
								}
							});
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Error: No internet connection",
							Toast.LENGTH_LONG).show();
				}
				
			}
		});
		// Sign up Button Click Listener
		signup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(LoginSignupActivity.this, SignUpActivity.class);
				startActivity(i);
			}
		});

	}
}
