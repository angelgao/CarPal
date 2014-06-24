package com.androidbegin.parselogintutorial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DisplayRequestsActivity extends Activity {
	private static List<ParseObject> lstRequestedUsers = new ArrayList<ParseObject>();
	private static List<ParseObject> lstMatches = new ArrayList<ParseObject>();
	private static ParseObject selectedRide;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_display_requests);
		List<ParseObject> lstRides = Welcome.getLstRide();
		
			
		   List<Map<String, String>> lstRideString = new ArrayList<Map<String,String>>();

		   for (ParseObject c: lstRides){
				String temp;
				String status = "";
				Boolean boolStatus = c.getBoolean("matched");
				Date pickUpDate = c.getDate("pickupdate");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				if (boolStatus){
					status = "Matched";
				} else {
					//should have if statement here to differ between no requests and requests
					status = "Pending";
				}
				
				temp = "Pick-up Location: " + c.getString("pickuplocation") + "\n" +
						"Drop-off Location: " + c.getString("dropofflocation") + "\n" +
						"Pick-up Date:" + pickUpDate + "\n" +
						"Status: " + status.toUpperCase() + "\n";
				
			   lstRideString.add(createList("car", temp));
		   }

		    ListView lv = (ListView) findViewById(R.id.listView1);
		    
		    SimpleAdapter simpleAdpt = new SimpleAdapter(this, lstRideString, android.R.layout.simple_list_item_1, new String[] {"car"}, new int[] {android.R.id.text1});	   
		    lv.setAdapter(simpleAdpt);
		
		    
		    lv.setOnItemClickListener(new OnItemClickListener() {
		        @Override
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        	List<ParseObject> lstRides = Welcome.getLstRide();
		        	 selectedRide = lstRides.get(position);
		        	if(selectedRide.getBoolean("matched") == false){
		        		
		        		ParseQuery<ParseObject> pendingQuery = ParseQuery.getQuery("Match");
		        		ParseUser currentUser = ParseUser.getCurrentUser();
		        		
		        		pendingQuery.whereEqualTo("driverID", currentUser);
		        		pendingQuery.whereEqualTo("ride", selectedRide);
		        		pendingQuery.include("passengerID");
		        		
		        		pendingQuery.findInBackground(new FindCallback<ParseObject>() {
		      			  public void done(List<ParseObject> objects, ParseException e) {
		      			    if (e == null) {
		      			    	lstRequestedUsers.clear();
		      			    	lstMatches = objects;
	        			    	for(ParseObject item:objects){
	        			    		ParseObject user = item.getParseObject("passengerID");
	        			    	
	        			    		lstRequestedUsers.add(user);
	        			    		
	        			    	}
	        			    	
	        			    	if (lstRequestedUsers.isEmpty()){
	        			    		Toast.makeText(
			      							getApplicationContext(),
			      							"No Requests!",
			      							Toast.LENGTH_LONG).show();
			      			    	
	        			    	} else{
	        			    		
	        			    	Intent intent = new Intent(DisplayRequestsActivity.this, RequestedUsersActivity.class);
	    			            startActivity(intent);
	        			    	}
		      			  }
		      			    else {
		      			    	Toast.makeText(
		      							getApplicationContext(),
		      							e.toString(),
		      							Toast.LENGTH_LONG).show();
		      			    }
		      			  }
		      			});
		        		
//		        		pendingQuery.findInBackground(new FindCallback<ParseObject>() {
//		        			  public void done(List<ParseObject> objects, ParseException e) {
//		        				  @Override
//		        				  public void done(List<ParseObject> arg0,
//											ParseException arg1) {
//		        			    if (e == null) {
//		        			    	lstRequestedUsers.clear();
//		        			    	for(ParseObject item:objects){
//		        			    		ParseObject user = item.getParseObject("User");
//		        			    	
//		        			    		lstRequestedUsers.add(user);
//		        			    	}
//		        			    	Intent intent = new Intent(DisplayRequestsActivity.this, RequestedUsersActivity.class);
//		    			            startActivity(intent);
//
//	        			    	}else {
//		        			    	Toast.makeText(
//		        							getApplicationContext(),
//		        							e.toString(),
//		        							Toast.LENGTH_LONG).show();
//		        			    }
//		        			  }
//
//		        			  }
//		        			});
		        		
		        	}
		        	
		        }
		    });
	}

	
	private HashMap<String, String> createList(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);
	    
	    return planet;
	}

	public static List<ParseObject> getLstRequestedUsers(){
		return lstRequestedUsers;
	}
	
	public static ParseObject getSelectedRide(){
		return selectedRide;
	}
	
	public static List<ParseObject> getLstMatches(){
		return lstMatches;
	}
	
}