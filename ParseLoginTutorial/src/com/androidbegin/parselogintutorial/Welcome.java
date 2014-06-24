package com.androidbegin.parselogintutorial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Welcome extends Activity {
	private static List<CarPosting> lstCarPost = new ArrayList<CarPosting>();
	ProgressDialog proDialog;
	private static List<Map<String, String>> lstMatch = new ArrayList<Map<String,String>>();
	private static List<ParseObject> lstRide = new ArrayList<ParseObject>();
	Button logout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.welcome);
		
		// Retrieve current user from Parse.com
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		// Convert currentUser into String
		String struser = currentUser.getUsername().toString();
		
		// Locate TextView in welcome.xml
		TextView txtuser = (TextView) findViewById(R.id.txtuser);

		// Set the currentUser String into TextView
		txtuser.setText("You are logged in as " + struser);
		
		// Locate Button in welcome.xml
		logout = (Button) findViewById(R.id.logout);

		// Logout Button Click Listener
		logout.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// Logout current user
				ParseUser.logOut();
				finish();
			}
		});
	}
	
	public static List<CarPosting> getLstCarPost(){
		return lstCarPost;
	}
	
	protected void startLoading() {
	    proDialog = new ProgressDialog(this);
	    proDialog.setMessage("Loading...");
	    proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    proDialog.setCancelable(false);
	    proDialog.show();
	}

	protected void stopLoading() {
	    proDialog.dismiss();
	    proDialog = null;
	}
	
	public void loadDriverRequests(View view){
		ParseQuery<ParseObject> pendingQuery = ParseQuery.getQuery("rides");
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		pendingQuery.whereEqualTo("driver", currentUser);
		pendingQuery.addDescendingOrder("matched");
	
		startLoading();	
		
		pendingQuery.findInBackground(new FindCallback<ParseObject>() {
			  public void done(List<ParseObject> objects, ParseException e) {
			    if (e == null) {
			    	lstRide.clear();
			    	for(ParseObject item:objects){
//			    		String temp;
//			    		String status = "";
//			    		Boolean boolStatus = item.getBoolean("matched");
//			    		Date pickUpDate = item.getDate("pickupdate");
//			    		Date updatedDate = item.getDate("updatedAt");
//			    		
//			    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			    		
//			    		if (boolStatus){
//			    			status = "Matched";
//			    		} else {
//			    			//should have if statement here to differ between no requests and requests
//			    			status = "Pending";
//			    		}
//			    		
//			    		temp = "Pick-up Location: " + item.getString("pickuplocation") + "\n" +
//			    				"Drop-off Location: " + item.getString("dropofflocation") + "\n" +
//			    				"Pick-up Date:" + pickUpDate + "\n" +
//			    				"Status: " + status.toUpperCase() + "\n" +
//			    				"Last Updated: " + updatedDate;
	
			    		lstRide.add(item);
			    	}
			  
			    	stopLoading();
			    	Intent intent = new Intent(Welcome.this, DisplayRequestsActivity.class);
			    	//intent.putExtra("activity", "driverRequests");
					startActivity(intent);

			    	}else {
			    	Toast.makeText(
							getApplicationContext(),
							e.toString(),
							Toast.LENGTH_LONG).show();
			    	stopLoading();
			    }
			  }
			});
	}
	
	public void loadMatches(View view){
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {

			final int PENDING = 0;
			final int ACCEPTED = 1;
			final int REJECTED = -1;
			
			ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("rides");
			innerQuery.whereGreaterThanOrEqualTo("pickupdate",Calendar.getInstance().getTime());
			innerQuery.whereEqualTo("cancelled", false);
			
			ParseQuery<ParseObject> pendingQuery = ParseQuery.getQuery("Match");
			ParseUser currentUser = ParseUser.getCurrentUser();
		
			pendingQuery.whereMatchesQuery("ride", innerQuery);
			pendingQuery.whereEqualTo("passengerID", currentUser);
			pendingQuery.whereNotEqualTo("driverConfirmed", REJECTED);
			pendingQuery.addDescendingOrder("driverConfirmed");
			pendingQuery.include("ride");
			pendingQuery.include("driverID");
			
			startLoading();	
			
			pendingQuery.findInBackground(new FindCallback<ParseObject>() {
				  public void done(List<ParseObject> objects, ParseException e) {
				    if (e == null) {
				    	lstMatch.clear();
				    	for(ParseObject item:objects){
				    		ParseObject driver = item.getParseObject("driverID");
				    		ParseObject ride = item.getParseObject("ride");
				    		
				    		String temp;
				    		String status = "";
				    		int intStatus = item.getInt("driverConfirmed");
				    		
				    		if (intStatus == PENDING){
				    			status = "Pending";
				    		} else if (intStatus == ACCEPTED){
				    			status = "Accepted";
				    		}
				    		
				    		temp = "Driver: " + driver.getString("username") + "\n" +
				    				"Pick-up Location: " + ride.getString("pickuplocation") + "\n" +
				    				"Drop-off Location: " + ride.getString("dropofflocation") + "\n" +
				    				"Status: " + status;
				    		
				    	
				    		lstMatch.add(createList("car", temp));
				    	}
				    	stopLoading();
				    	Intent intent = new Intent(Welcome.this, MyMatchesActivity.class);
				    	intent.putExtra("activity", "passengerMatch");
						startActivity(intent);

				    	}else {
				    	Toast.makeText(
								getApplicationContext(),
								e.toString(),
								Toast.LENGTH_LONG).show();
				    	stopLoading();
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

	private HashMap<String, String> createList(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);
	    
	    return planet;
	}
	
	public static List<Map<String, String>> getLstMatch(){
		return lstMatch;
	}
	
	private void loadDisplayresults(View view){
		Intent intent = new Intent(this, Displayresults.class);
		intent.putExtra("activity", "Welcome");
	    startActivity(intent);
	}
	
	public void loadsearch(View view){
		Intent intent = new Intent(this, SearchRideActivity.class);
		startActivity(intent);
	}
	
	public void loadcreate(View view){
		Intent intent = new Intent(this, CreateRideActivity.class);
		startActivity(intent);
	}
	
	public static List<ParseObject> getLstRide(){
		return lstRide;
}
	
	public void loadmyrides(final View view){
		
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {

			ParseQuery<ParseObject> query = ParseQuery.getQuery("rides");
			query.whereEqualTo("driver", ParseUser.getCurrentUser());
			query.whereEqualTo("cancelled", false);
			
			startLoading();
			query.findInBackground(new FindCallback<ParseObject>() {
				  public void done(List<ParseObject> objects, ParseException e) {
				    if (e == null) {
				    	lstCarPost.clear();
				    	for(ParseObject item:objects){
				    		ParseUser driver = ParseUser.getCurrentUser();
				    		CarPosting carPost = new CarPosting(
				    			item.getObjectId(),
				    			item.getString("pickuplocation"),
				    			item.getString("pickupintersec"),
				    			item.getString("dropofflocation"),
				    			item.getString("dropoffintersec"),
				    			item.getDate("pickupdate"),
				    			item.getInt("price"),
				    			item.getInt("numseats"),
				    			driver.getObjectId(),
				    			driver.getUsername(),
				    			driver.getEmail(),
				    			driver.getInt("phone")
				    		);
				    		lstCarPost.add(carPost);
				    	}
				    	stopLoading();
				    	loadDisplayresults(view);
				    	}else {
				    	Toast.makeText(
								getApplicationContext(),
								e.toString(),
								Toast.LENGTH_LONG).show();
				    	stopLoading();
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
}