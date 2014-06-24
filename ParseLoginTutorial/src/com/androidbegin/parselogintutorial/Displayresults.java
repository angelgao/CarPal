package com.androidbegin.parselogintutorial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Displayresults extends Activity {
	List <CarPosting> carpostinglist = new ArrayList<CarPosting>();
	ProgressDialog proDialog;

	/*public static CarPosting maptocarposting(ParseObject item){
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		// Convert currentUser into String
		String struser = currentUser.getUsername().toString();
		CarPosting_obsolete newride = new CarPosting_obsolete(
				item.getString("pickuplocation"), item.getString("dropofflocation"), 
				item.getInt("numseats"), item.getString("pickupintersec"), 
				item.getDate("pickupdate"), item.getInt("lengthride"), 
				item.getDouble("price"), item.getBoolean("luggagespace"),
				item.getString("additionaldetails"), false, struser, false);
		return newride;
	}*/
	
	
	public static CarPosting selectedCar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_displayresults);
		
		
	    
		/*final List<CarPosting> carpostinglist = new ArrayList<CarPosting>();
		
		ParseQuery<ParseObject> results = ParseQuery.getQuery("rides");
		results.whereEqualTo("dropofflocation", "Toronto");
		results.whereEqualTo("pickuplocation", "Waterloo");
		
		results.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> queryresults, ParseException e) {
		        if (e == null) {
		            for(ParseObject item:queryresults){
		            	CarPosting newcarposting = maptocarposting(item);
		            	carpostinglist.add(newcarposting);
		            	Log.d("yolo", "erroryo" + newcarposting.getAdditionalDetails());
		            }
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});*/

		
		
		
		
		/*final List <CarPosting> carpostinglist = new ArrayList<CarPosting>();
	    
	    CarPosting car1 = new CarPosting();
	    CarPosting car2 = new CarPosting();
	    
	    car1.setPickUpLocation("Toronto");
	    car2.setPickUpLocation("Ottawa");
	    
	    carpostinglist.add(car1);
	    carpostinglist.add(car2);
	   
	   List<String> strCar = new ArrayList<String>();*/
		if(getIntent().getStringExtra("activity").equals("SearchRideActivity")){
			carpostinglist = SearchRideActivity.getLstCarPost();
			}else if (getIntent().getStringExtra("activity").equals("Welcome")){
				carpostinglist = Welcome.getLstCarPost();
			}
		
		
	   List<Map<String, String>> lstCarPosting = new ArrayList<Map<String,String>>();
	   SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.US);

	   for (CarPosting c: carpostinglist){
		   String display = "From " + c.getPickUpLocation() + " to " + c.getDropOffLocation() + "\n"
				   + df.format(c.getPickUpTime()) + "          $" + c.getPrice();
		   lstCarPosting.add(createList("car", display));
	   }
	    
	    
	    // Get the ListView component from the layout
	    ListView lv = (ListView) findViewById(R.id.listView1);
	    
	    
	    // This is a simple adapter that accepts as parameter
	    // Context
	    // Data list
	    // The row layout that is used during the row creation
	    // The keys used to retrieve the data
	    // The View id used to show the data. The key number and the view id must match
	    SimpleAdapter simpleAdpt = new SimpleAdapter(this, lstCarPosting, android.R.layout.simple_list_item_1, new String[] {"car"}, new int[] {android.R.id.text1});	   
	    lv.setAdapter(simpleAdpt);
	    

	
	    lv.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
				if (activeNetwork != null && activeNetwork.isConnected()) {
					selectedCar = carpostinglist.get(position);
		        	ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
		        	query.whereEqualTo("ride", ParseObject.createWithoutData("rides", selectedCar.getObjectID()));
		        	query.whereEqualTo("passengerID", ParseUser.getCurrentUser());
		        	startLoading();
		        	query.getFirstInBackground(new GetCallback<ParseObject>() {
		                @Override
		                public void done(ParseObject parseObject, ParseException e) {
		                	stopLoading();
	                		  Intent intent = new Intent(Displayresults.this, Singleride.class);  
		                	if (e == null) {
		                		intent.putExtra("alreadyRequested", true);
		                	  }else{
			                		intent.putExtra("alreadyRequested", false);
		                	  }
		                	startActivity(intent);
		                	
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
		
		
		
		
		
		
/*	    // Convert ArrayList to array
	    //listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));

		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
	        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
	        "Android", "iPhone", "WindowsMobile" };
		
	    final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < values.length; ++i) {
	      list.add(values[i]);
	    }
	    
	    final ListView listview = (ListView) findViewById(R.id.listView1);
	    
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);*/
	    
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
	
	private HashMap<String, String> createList(String key, String name) {
	    HashMap<String, String> planet = new HashMap<String, String>();
	    planet.put(key, name);
	    
	    return planet;
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
			View rootView = inflater.inflate(R.layout.fragment_displayresults,
					container, false);
			return rootView;
		}
	}

	public void loadsingleride(View view){
		Intent intent = new Intent(this, Singleride.class);
	    startActivity(intent);
	}
	
}
