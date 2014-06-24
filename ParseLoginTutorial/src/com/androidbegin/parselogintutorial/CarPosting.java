package com.androidbegin.parselogintutorial;

import java.util.Date;

public class CarPosting {
	private String objectID;
	private String pickUpLocation;
	private String dropOffLocation;
	private int numSeats;
	private String exactPickupLocation;
	private String exactDropoffLocation;
	private Date pickUpTime;
	private int price;
	//private String additionalDetails;
	private boolean cancelled;
	private boolean matched;
	private String userID;
	private String username;
	private String email;
	private int phone;

	
	public CarPosting(){
		objectID = "";
		pickUpLocation = "Toronto";
		dropOffLocation = "Ottawa";
		exactPickupLocation = "Bloor-Dundas";
		exactDropoffLocation = "Bank-Huntley";
		numSeats = 0;
		exactPickupLocation = "K3A 1W0";
		exactDropoffLocation = "L4H 3B2";
		pickUpTime = new Date();
		price = 11;
		//additionalDetails = "";
		cancelled = false;
		username = "abaek";	
		matched = false;
		
	}
	
	public CarPosting(String oID, String pickUp, String exactpickup, String dropOff, String exactdropoff, Date timePickUp, int p, int seats,
			String id, String name, String userEmail, int userPhone){
		objectID = oID;
		pickUpLocation = pickUp;
		dropOffLocation = dropOff;
		numSeats = seats;
		exactPickupLocation = exactpickup;
		exactDropoffLocation = exactdropoff;
		pickUpTime = timePickUp;
		price = p;
		cancelled = false;
		matched = false;
		userID = id;
		username = name;
		email = userEmail;
		phone = userPhone;
		
	}
	
	public String getObjectID(){
		return objectID;
	}
	
	public String getPickUpLocation(){
		return pickUpLocation;
	}
	
	public String getDropOffLocation(){
		return dropOffLocation;
	}
	
	public Integer getNumSeats(){
		return numSeats;
	}
	
	public String getExactPickupLocation(){
		return exactPickupLocation;
	}
	
	public String getExactDropoffLocation(){
		return exactDropoffLocation;
	}
	
	
	public Date getPickUpTime(){
		return pickUpTime;
	}
	
	
	public int getPrice(){
		return price;
	}
	
	public Boolean getCancelled(){
		return cancelled;
	}
	
	public String getUserID(){
		return userID;
	}
	
	public String getUserName(){
		return username;
	}
	
	public boolean getMatched(){
		return matched;
	}
	
	public String getEmail(){
		return email;
	}
	
	public int getPhone(){
		return phone;
	}
	
}