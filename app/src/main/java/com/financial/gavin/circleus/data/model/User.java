package com.financial.gavin.circleus.data.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gavin on 1/28/18.
 */

public class User {
	private String name;
	private String phoneNumber;
	private String thumbNailUrl;
	private LatLng latLng;
	
	public User(String name, String phoneNumber, String thumbNailUrl, LatLng latLng) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.thumbNailUrl = thumbNailUrl;
		this.latLng = latLng;
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
	
	public LatLng getLatLng() {
		return latLng;
	}
	
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}
}
