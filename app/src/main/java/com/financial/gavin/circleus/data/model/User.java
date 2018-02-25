package com.financial.gavin.circleus.data.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gavin on 1/28/18.
 */

public class User {
	private String name;
	private String phoneNumber;
	private String thumbNailUrl;
	private double latitude;
	private double longitude;
	
	public User() {
	}
	
	public User(String name, String phoneNumber, String thumbNailUrl, double latitude, double longitude) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.thumbNailUrl = thumbNailUrl;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getThumbNailUrl() {
		return thumbNailUrl;
	}
	
	public void setThumbNailUrl(String thumbNailUrl) {
		this.thumbNailUrl = thumbNailUrl;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
