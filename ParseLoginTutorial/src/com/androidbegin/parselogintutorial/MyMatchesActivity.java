package com.androidbegin.parselogintutorial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;


public class MyMatchesActivity extends Activity {
//	 private List<Match> lstAcceptedMatch = new ArrayList<Match>();
//	 private List<Match> lstPendingMatch = new ArrayList<Match>();
	 private List<Map<String, String>> lstMatch = Welcome.getLstMatch();
	 //String pickUp, dropOff, user;
	 ProgressDialog proDialog;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_my_matches);
		
		ListView lv = (ListView) findViewById(R.id.lvResults);
		
		if (lstMatch.isEmpty()){
			Toast.makeText(
					getApplicationContext(),
					"No requests exist.",
					Toast.LENGTH_LONG).show();
		}else{
			
	    	SimpleAdapter simpleAdpt = new SimpleAdapter(this, lstMatch, android.R.layout.simple_list_item_1, new String[] {"car"}, new int[] {android.R.id.text1});	   
		    lv.setAdapter(simpleAdpt);
		}
		
//		lv.setOnItemClickListener(new OnItemClickListener() {
//	        @Override
//	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//	        	
//	        	
//	        	if (getIntent().getStringExtra("activity").equals("driverRequests")){
//	        		List<String> lstRideID = Welcome.getLstRideID();
//	        		String rideID = lstRideID.get(position);
//	        		
//	        		
//	        		
//	        	}
//	        	
//	        	selectedCar = carpostinglist.get(position);
//	        	Intent intent = new Intent(Displayresults.this, Singleride.class);
//	            startActivity(intent);
//	        }
//	    });
		
  }

	protected void startLoading() {
	    proDialog = new ProgressDialog(this);
	    proDialog.setMessage("loading...");
	    proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    proDialog.setCancelable(false);
	    proDialog.show();
	}

	protected void stopLoading() {
	    proDialog.dismiss();
	    proDialog = null;
	} 

	private HashMap<String, String> createList(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);
	    
	    return planet;
	}

}
