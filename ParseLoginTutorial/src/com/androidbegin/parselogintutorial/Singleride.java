package com.androidbegin.parselogintutorial;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Singleride extends Activity {
	private CarPosting specificPosting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_singleride);
		specificPosting = Displayresults.selectedCar;
		final Button request = (Button) findViewById(R.id.btnRequest);
		final Button cancel = (Button) findViewById(R.id.btnCancel);
		if(specificPosting.getUserID() == ParseUser.getCurrentUser().getObjectId()){
			cancel.setVisibility(View.VISIBLE);
		}else{
			request.setVisibility(View.VISIBLE);
		}
		if(specificPosting.getCancelled() == true){
			cancel.setEnabled(false);
	    	cancel.setText("POST CANCELLED");
		}
		if(getIntent().getBooleanExtra("alreadyRequested", true) == true){
			request.setEnabled(false);
			request.setText("REQUEST SENT");
		}
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
				if (activeNetwork != null && activeNetwork.isConnected()) {
					ParseObject ride = ParseObject.createWithoutData("rides", specificPosting.getObjectID());
					ride.put("cancelled", true);
					ride.saveInBackground(new SaveCallback() {
						  public void done(ParseException e) {
							    if (e == null) {
							    	cancel.setEnabled(false);
							    	cancel.setText("POST CANCELLED");
							    } else {
							    	Toast.makeText(getApplicationContext(), "Error: Action failed.",Toast.LENGTH_LONG).show();
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
		
		request.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
				if (activeNetwork != null && activeNetwork.isConnected()) {
					ParseUser currentUser = ParseUser.getCurrentUser();
					
					String rideID = Displayresults.selectedCar.getObjectID();
					String driverID = Displayresults.selectedCar.getUserID();
					String passengerID = currentUser.getObjectId();;
									
					ParseObject match = new ParseObject("Match");
					match.put("ride", ParseObject.createWithoutData("rides", rideID));
					match.put("driverID", ParseObject.createWithoutData("_User", driverID));
					match.put("passengerID", ParseObject.createWithoutData("_User", passengerID));
					match.put("driverConfirmed", 0);
					
					ParseACL matchACL = new ParseACL();
					matchACL.setPublicReadAccess(true);
					matchACL.setPublicWriteAccess(true);
					match.setACL(matchACL);
					match.saveInBackground();
					
					ParseQuery pushQuery = ParseInstallation.getQuery();
					pushQuery.whereEqualTo("user", ParseObject.createWithoutData("_User", driverID));
					ParsePush push = new ParsePush();
					push.setQuery(pushQuery); // Set our Installation query
					push.setMessage("You have received a carpool request on CarPal!");
					push.sendInBackground();
					
					request.setEnabled(false);
					request.setText("REQUEST SENT");
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Error: No internet connection",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		
		//ParseUser driver = Displayresults.selectedUser;
		SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.US);
		
			TextView originCity = (TextView) findViewById(R.id.originCity);
			originCity.setText("Origin City: " + specificPosting.getPickUpLocation());
			
			TextView originLocation = (TextView) findViewById(R.id.originLocation);
			originLocation.setText("Origin (Specific Location): " + specificPosting.getExactPickupLocation());
			
			TextView destinationCity = (TextView) findViewById(R.id.destinationCity);
			destinationCity.setText("Destination City: " + specificPosting.getDropOffLocation());
			
			TextView destinationLocation = (TextView) findViewById(R.id.destinationLocation);
			destinationLocation.setText("Destination (Specific Location): " + specificPosting.getExactDropoffLocation());
			
			TextView pickUpDate = (TextView) findViewById(R.id.datetime);
			pickUpDate.setText("Time: " + df.format(specificPosting.getPickUpTime()));
			
			TextView price = (TextView) findViewById(R.id.price);
			price.setText("Price: $" + specificPosting.getPrice());
			
			TextView seats = (TextView) findViewById(R.id.seats);
			seats.setText("Available Seats: " + specificPosting.getNumSeats());
			
			TextView username = (TextView) findViewById(R.id.username);
			username.setText("Username: " + specificPosting.getUserName());
			
			TextView email = (TextView) findViewById(R.id.email);
			email.setText("Email: " + specificPosting.getEmail());
			
			TextView phone = (TextView) findViewById(R.id.phone);
			phone.setText("Phone Number: " + specificPosting.getPhone());
			
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
			View rootView = inflater.inflate(R.layout.fragment_singleride,
					container, false);
			return rootView;
		}
	}

}