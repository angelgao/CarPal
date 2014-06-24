package com.androidbegin.parselogintutorial;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SearchRideActivity extends FragmentActivity implements OnItemSelectedListener, OnDateSetListener {
	private static List<CarPosting> lstCarPost = new ArrayList<CarPosting>();
	private int departYear = -1;
	private int departMonth = -1;
	private int departDay = -1;
	ProgressDialog proDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_ride);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public static List<CarPosting> getLstCarPost(){
		return lstCarPost;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_ride, menu);
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
	
	public static class DatePickerFragment extends DialogFragment{

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);

	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), (SearchRideActivity)getActivity(), year, month, day);
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
			View rootView = inflater.inflate(R.layout.fragment_search_ride,
					container, false);
			return rootView;
		}
	}
	
	public void showDatePickerDialog(View v) {
	    DatePickerFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
        //do some stuff for example write on log and update TextField on activity
		departYear = year;
		departMonth = month;
		departDay = day;
		
		DecimalFormat digits = new DecimalFormat("00");
		String printMonth = digits.format(month + 1);
		String printDay = digits.format(day);
        ((Button) findViewById(R.id.btnChangeDate)).setText(printMonth + "/" + printDay + "/" + year);
    }
	
	public void onItemSelected(AdapterView<?> parent, View view,
			int pos, long id) {
		Object item = parent.getSelectedItem();
		Toast.makeText(
				getApplicationContext(),
				item.toString(),
				Toast.LENGTH_LONG).show();
	}
	public void onNothingSelected(AdapterView<?> parent) {
	// Another interface callback
	}
	
	protected void startLoading() {
	    proDialog = new ProgressDialog(this);
	    proDialog.setMessage("Searching for rides...");
	    proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    proDialog.setCancelable(false);
	    proDialog.show();
	}

	protected void stopLoading() {
	    proDialog.dismiss();
	    proDialog = null;
	} 
	
	private void loadDisplayresults(View view){
		Intent intent = new Intent(this, Displayresults.class);
		intent.putExtra("activity", "SearchRideActivity");
	    startActivity(intent);
	}
	
	
	public void search(final View view){
		
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			Spinner sorter = (Spinner) findViewById(R.id.sort_spinner);
			String sort = sorter.getSelectedItem().toString();
			Spinner spinner = (Spinner) findViewById(R.id.price_spinner);
			String price = spinner.getSelectedItem().toString();
			
			String pickuplocation = ((EditText) findViewById(R.id.originCity)).getText().toString().trim().toUpperCase();
		    String dropofflocation = ((EditText) findViewById(R.id.destinationCity)).getText().toString().trim().toUpperCase();
		    String seats= ((EditText) findViewById(R.id.capacity)).getText().toString();
		    Calendar fromCalendar = new GregorianCalendar(departYear,departMonth,departDay,0,0,0);
		    Calendar toCalendar = new GregorianCalendar(departYear,departMonth,departDay,23,59,59);
		    	    
		    ParseQuery<ParseObject> query = ParseQuery.getQuery("rides");
		    
		    query.whereNotEqualTo("driver", ParseUser.getCurrentUser());
		    query.whereEqualTo("matched", false);
		    query.whereEqualTo("cancelled", false);
		    query.include("driver");
		    if(departYear != -1 && toCalendar.before(Calendar.getInstance())){
			Toast.makeText(
					getApplicationContext(),
					"Date entered cannot be before today.",
					Toast.LENGTH_LONG).show();
		    }else{
		    	if(!pickuplocation.matches("")){
			    	query.whereEqualTo("pickuplocation", pickuplocation);
			    }
				if(!dropofflocation.matches("")){
					query.whereEqualTo("dropofflocation",dropofflocation);
				}
				if(departYear != -1){
						query.whereGreaterThanOrEqualTo("pickupdate", fromCalendar.getTime());
						query.whereLessThanOrEqualTo("pickupdate", toCalendar.getTime());
				}else{
					query.whereGreaterThanOrEqualTo("pickupdate",Calendar.getInstance().getTime());
				}
				if(price.matches("0-5")){
					query.whereGreaterThanOrEqualTo("price", 0);
					query.whereLessThanOrEqualTo("price", 5);
				}else if(price.matches("6-15")){
					query.whereGreaterThanOrEqualTo("price", 6);
					query.whereLessThanOrEqualTo("price", 15);
				}else if(price.matches("16-25")){
					query.whereGreaterThanOrEqualTo("price", 16);
					query.whereLessThanOrEqualTo("price", 25);
				}else if(price.matches("26-35")){
					query.whereGreaterThanOrEqualTo("price", 26);
					query.whereLessThanOrEqualTo("price", 35);
				}else if(price.matches(">35")){
					query.whereGreaterThanOrEqualTo("price", 36);
				}
				
				if(!seats.matches("")){
					query.whereGreaterThanOrEqualTo("numseats", Integer.parseInt(seats));
				}
				
				if(sort.equals("Sort by lowest price")){
					query.orderByAscending("price");
				}else if(sort.equals("Sort by earliest time")){
					query.orderByAscending("pickupdate");
				}else if(sort.equals("Sort by newest post")){
					query.orderByDescending("createdAt");
				}
				
				startLoading();
				query.findInBackground(new FindCallback<ParseObject>() {
					  public void done(List<ParseObject> objects, ParseException e) {
					    if (e == null) {
					    	lstCarPost.clear();
					    	for(ParseObject item:objects){
					    		ParseObject driver = item.getParseObject("driver");
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
					    			driver.getString("username"),
					    			driver.getString("email"),
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
		    }
		} else {
			Toast.makeText(
					getApplicationContext(),
					"Error: No internet connection",
					Toast.LENGTH_LONG).show();
		}
		
		
	}
}
