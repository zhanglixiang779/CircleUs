package com.financial.gavin.circleus.data.api;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.di.activityScope.GoogleServiceModule;
import com.financial.gavin.circleus.di.activityScope.PresentersModule;
import com.financial.gavin.circleus.ui.main.MainActivity;
import com.financial.gavin.circleus.ui.main.MainContract;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gavin on 1/27/18.
 */

public class GoogleApisImpl implements GoogleApis {
	
	private static final int INTERVAL = 500;
	private static final int FAST_INTERVAL = 100;
	private Observer mObserver;
	
	@Inject
	FusedLocationProviderClient mFusedLocationProviderClient;
	@Inject
	LocationRequest mLocationRequest;
	@Inject
	SettingsClient mSettingsClient;
	@Inject
	LocationSettingsRequest.Builder mLocationSettingsRequestBuilder;
	
	public GoogleApisImpl() {
		CircleUsApplication.getInstance().getAppComponent()
				.getActivityComponent(new PresentersModule(), new GoogleServiceModule()).inject(this);
	}
	
	private LocationRequest createLocationRequest() {
		mLocationRequest.setInterval(INTERVAL);
		mLocationRequest.setFastestInterval(FAST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		return mLocationRequest;
	}
	
	public void initLocationSettingsRequest() {
		mLocationSettingsRequestBuilder.addLocationRequest(createLocationRequest());
		Task<LocationSettingsResponse> task = mSettingsClient.checkLocationSettings(mLocationSettingsRequestBuilder.build());
		task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
			@Override
			public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
				getCurrentLocation();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Observable.just(e)
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(mObserver);
			}
		});
	}
	
	@SuppressLint("MissingPermission")
	@Override
	public void getCurrentLocation() {
		mFusedLocationProviderClient.getLastLocation()
				.addOnSuccessListener(new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						// Got last known location. In some rare situations this can be null.
						if (location != null) {
							Observable.just(location)
									.subscribeOn(Schedulers.io())
									.observeOn(AndroidSchedulers.mainThread())
									.subscribe(mObserver);
						} else {
							getCurrentLocation();
						}
					}
				});
	}
	
	@Override
	@SuppressLint("MissingPermission")
	public void startLocationUpdates() {
		LocationCallback locationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				for (Location location : locationResult.getLocations()) {
					//TODO: use location data later
				}
			}
		};
		
		mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback , null);
	}
	
	@Override
	public <T> void setObserver(Observer<T> t) {
		mObserver = t;
	}
}
