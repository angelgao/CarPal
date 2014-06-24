package com.androidbegin.parselogintutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class RequestedUsersActivity extends Activity {
	private List<ParseObject> lstRequestedUsers = DisplayRequestsActivity.getLstRequestedUsers();
	private static List<String> lstMatch = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_requested_users);
		
		RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup1);
		RadioButton button;
		
		int i =0;
		for(ParseObject item:lstRequestedUsers){
			button = new RadioButton(this);
			String temp = "\n" + "Username:" + item.getString("username") + "\n" +
							"Email: " + item.getString("email") + "\n" +
							"Phone: " + item.getString("phone") + "\n";
					
		    button.setText(temp);
		    button.setId(i);
		    group.addView(button);
		    i ++;
    	}
    	
	}


	public void loadAccept(View view){
		RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup1);
		final int index = group.getCheckedRadioButtonId();
		ParseObject selectedRide = DisplayRequestsActivity.getSelectedRide();
		//Change Ride db to make matched = true;/////////////////////////////

		// Create a pointer to an object of class Point with id dlkj83d
		ParseObject point = ParseObject.createWithoutData("rides", selectedRide.getObjectId());

		// Set a new value on quantity
		point.put("matched", true);

		// Save
		point.saveInBackground(new SaveCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		      // Saved successfully.
		    } else {
		    	Toast.makeText(
						getApplicationContext(),
						e.toString(),
						Toast.LENGTH_LONG).show();
		    }
		  }
		});		

		updateDriverConfirmed(index);
		
//		//Update Match db to make driverConfirmed = -1/////////////////////////////
//		
////		//Querying everything gives me zero results!!!
////		
////		ParseQuery<ParseObject> newQuery = ParseQuery.getQuery("Match");
////		//newQuery.whereEqualTo("driver", ParseUser.getCurrentUser());
////		//newQuery.whereEqualTo("cancelled", false);
////		
////		//startLoading();
////		newQuery.findInBackground(new FindCallback<ParseObject>() {
////			  public void done(List<ParseObject> objects, ParseException e) {
////			    if (e == null) {
////			    	
////			    	for(ParseObject item:objects){
////			    		Toast.makeText(
////								getApplicationContext(),
////								"Not empty!",
////								Toast.LENGTH_LONG).show();
////			    	}
////
////			    	}else {
////			    	Toast.makeText(
////							getApplicationContext(),
////							e.toString(),
////							Toast.LENGTH_LONG).show();
////
////			    }
////			  }
////			});
//
//		
//		
//		
//		
//			ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
//			
//			ParseUser currentUser = ParseUser.getCurrentUser();
//			
//			
//			//query.whereEqualTo("driverID", currentUser);
//			//query.whereEqualTo("ride", DisplayRequestsActivity.pubSelectedRide);
//			//query.include("passengerID");
//			
//			//LST IS EMPTY EVEN WHEN NO QUERY CONDITIONS?!?!?!??!/////////////////////////////////
//			
//    		query.findInBackground(new FindCallback<ParseObject>() {
//    			  public void done(List<ParseObject> objects, ParseException e) {
//    			    if (e == null) {
////    			    	ParseACL acl = new ParseACL();
////    			    	acl.setPublicWriteAccess(true);
////    			    	currentUser.setACL(acl);
//    			    	ParseObject acceptedUser = lstRequestedUsers.get(index);
//  			    	for(ParseObject item:objects){
//  			    		//String a = item.getParseObject("passengerID").getObjectId();
//  			    		//String b = acceptedUser.getObjectId();
////  			    		item.put("driverConfirmed", -1);
////  			    		
////  			    		item.saveInBackground(new SaveCallback() {
////			    			  public void done(ParseException e) {
////			    			    if (e == null) {
////			    			      // Saved successfully.
////			    			    } else {
////			    			    	Toast.makeText(
////			    							getApplicationContext(),
////			    							e.toString(),
////			    							Toast.LENGTH_LONG).show();
////			    			    }
////			    			  }
////			    			});
//  			    		
////  			    		if (item.getParseObject("passengerID").getObjectId() != acceptedUser.getObjectId()){
////  			    			item.put("driverConfirmed", -1);
////  			    			item.saveInBackground(new SaveCallback() {
////  			    			  public void done(ParseException e) {
////  			    			    if (e == null) {
////  			    			      // Saved successfully.
////  			    			    } else {
////  			    			    	Toast.makeText(
////  			    							getApplicationContext(),
////  			    							e.toString(),
////  			    							Toast.LENGTH_LONG).show();
////  			    			    }
////  			    			  }
////  			    			});
////  			    		}
////  			    		else{
////  			    			item.put("driverConfirmed", 1);
////  			    			item.saveInBackground();
////  			    		}
//  			    		lstMatch.add(item.getObjectId());
//  			    	}
//  			    	
//    			  }
//    			    else {
//    			    	Toast.makeText(
//    							getApplicationContext(),
//    							e.toString(),
//    							Toast.LENGTH_LONG).show();
//    			    }
//    			  }
//    			});
//				
////    		ParseObject pointer = ParseObject.createWithoutData("Match", lstRequestedUsers.get(index).getObjectId());
////
////			// Set a new value on quantity
////			pointer.put("driverConfirmed", -1);
////
////			// Save
////			pointer.saveInBackground(new SaveCallback() {
////			  public void done(ParseException e) {
////			    if (e == null) {
////			      // Saved successfully.
////			    } else {
////			    	Toast.makeText(
////							getApplicationContext(),
////							e.toString(),
////							Toast.LENGTH_LONG).show();
////			    }
////			  }
////			});
//    		
//    		if (lstMatch.size() == 0){
//    			Toast.makeText(
//						getApplicationContext(),
//						"lst is empty!!!!!",
//						Toast.LENGTH_LONG).show();
//    		}
//    		
//    		for (String match : lstMatch){
//    			
//    			Toast.makeText(
//						getApplicationContext(),
//						match,
//						Toast.LENGTH_LONG).show();
//		    	
//    			
//    			
//    			// Create a pointer to an object of class Point with id dlkj83d
//    			ParseObject pointer = ParseObject.createWithoutData("Match", match);
//
//    			// Set a new value on quantity
//    			pointer.put("driverConfirmed", -1);
//
//    			// Save
//    			pointer.saveInBackground(new SaveCallback() {
//    			  public void done(ParseException e) {
//    			    if (e == null) {
//    			      // Saved successfully.
//    			    } else {
//    			    	Toast.makeText(
//    							getApplicationContext(),
//    							e.toString(),
//    							Toast.LENGTH_LONG).show();
//    			    }
//    			  }
//    			});
//    		}

    		Toast.makeText(
					getApplicationContext(),
					"The request has been accepted.",
					Toast.LENGTH_LONG).show();
    		
    		Button accept = (Button) findViewById(R.id.button1);
    		accept.setEnabled(false);
    		
    		for (int i = 0; i < group.getChildCount(); i++){
    			group.getChildAt(i).setEnabled(false);
    		}
    		
    		ParseObject acceptedUser = lstRequestedUsers.get(index);
			ParseQuery pushQuery = ParseInstallation.getQuery();
			pushQuery.whereEqualTo("user", acceptedUser);
			ParsePush push = new ParsePush();
			push.setQuery(pushQuery); // Set our Installation query
			push.setMessage("Your request has been accepted!");
			push.sendInBackground();
		
	}
	
	public void updateDriverConfirmed(int index){
		
		List<ParseObject> lstMatches = DisplayRequestsActivity.getLstMatches();
		
		for (ParseObject match: lstMatches){
			int val = -1;
			
			ParseObject acceptedUser = lstRequestedUsers.get(index);
			ParseObject tempUser = match.getParseObject("passengerID");
			
			if (acceptedUser.getObjectId() == tempUser.getObjectId()){
				val = 1;
			}
			
			
			match.put("driverConfirmed", val);
			
    		match.saveInBackground(new SaveCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      // Saved successfully.
			    } else {
			    	Toast.makeText(
							getApplicationContext(),
							e.toString(),
							Toast.LENGTH_LONG).show();
			    }
			  }
			  
			  
			});
			
//    		Toast.makeText(
//					getApplicationContext(),
//					match.getObjectId(),
//					Toast.LENGTH_SHORT).show();
    		
		}
		
	    }
	}