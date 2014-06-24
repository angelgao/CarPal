package com.androidbegin.parselogintutorial;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class CreateRideActivity extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener  {
	private int departYear =-1;
	private int departMonth = -1;
	private int departDay = -1;
	private int departHour = -1;
	private int departMinute =-1;
	//static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_ride);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_ride, menu);
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
	
	public static class TimePickerFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), (CreateRideActivity)getActivity(), hour, minute,
		DateFormat.is24HourFormat(getActivity()));
		}
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
	        return new DatePickerDialog(getActivity(), (CreateRideActivity)getActivity(), year, month, day);
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
			View rootView = inflater.inflate(R.layout.fragment_create_ride,
					container, false);
			return rootView;
		}
	}
	
	public void showDatePickerDialog(View v) {
	    DatePickerFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog(View v) {
		TimePickerFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	@Override
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
	
	public void onTimeSet(TimePicker view, int hour, int minute) {
		departMinute = minute;
		departHour = hour;
		
		DecimalFormat digits = new DecimalFormat("00");
		String printHour = digits.format(hour);
		String printMinute = digits.format(minute);
        ((Button) findViewById(R.id.btnChangeTime)).setText(printHour + ":" + printMinute);
	}
	
	public void createPost(View view){
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			String pickuplocation = ((EditText) findViewById(R.id.originCity)).getText().toString().trim().toUpperCase();
		    String pickupintersec = ((EditText) findViewById(R.id.originLocation)).getText().toString().trim();
		    String dropofflocation = ((EditText) findViewById(R.id.destinationCity)).getText().toString().trim().toUpperCase();
		    String dropoffintersec= ((EditText) findViewById(R.id.destinationLocation)).getText().toString().trim();
		    String seats= ((EditText) findViewById(R.id.capacity)).getText().toString();
		    String price = ((EditText) findViewById(R.id.price)).getText().toString();
		    
		    Calendar calendar = new GregorianCalendar(departYear,departMonth,departDay,departHour,departMinute,0);
			
			
			if(pickuplocation.matches("") || pickupintersec.matches("") || dropofflocation.matches("")
					|| dropoffintersec.matches("") || seats.matches("") || price.matches("")){
				Toast.makeText(
						getApplicationContext(),
						"Do not leave any fields blank.",
						Toast.LENGTH_LONG).show();
			}else if(departYear == -1 || departMonth == -1|| departDay == -1|| departHour == -1|| departMinute == -1){
				Toast.makeText(getApplicationContext(), "Do not leave the dates blank.", Toast.LENGTH_LONG).show();
			}else if(calendar.before(Calendar.getInstance())){
				Toast.makeText(
						getApplicationContext(),
						"Date and time entered must be a future date.",
						Toast.LENGTH_LONG).show();
			}
			else{
			
			    int numseats =Integer.parseInt(seats);
			    int numprice = Integer.parseInt(price);
			    
			    ParseObject rides = new ParseObject("rides");
				rides.put("driver", ParseUser.getCurrentUser());
				rides.put("pickuplocation", pickuplocation);
				rides.put("pickupintersec", pickupintersec);
				rides.put("dropofflocation", dropofflocation);
				rides.put("dropoffintersec", dropoffintersec);
				rides.put("numseats", numseats);
				rides.put("pickupdate", calendar.getTime());
				rides.put("price", numprice);
				rides.put("cancelled", false);
				rides.put("matched", false);
				
				//more fields
				rides.saveInBackground();
				Toast.makeText(
						getApplicationContext(),
						"Posting is created.",
						Toast.LENGTH_LONG).show();
				
				this.finish();
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"Error: No internet connection",
					Toast.LENGTH_LONG).show();
			}
	    
	}

}
