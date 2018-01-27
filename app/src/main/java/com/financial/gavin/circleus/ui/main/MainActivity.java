package com.financial.gavin.circleus.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.ProcessingInstruction;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, MainContract.View {
	
	private GoogleMap mMap;
	private Marker mMarker;
	private static final int REQUEST_CHECK_SETTINGS = 100;
	private static final int INTERVAL = 500;
	private static final int FAST_INTERVAL = 100;
	View mapView;
	
	LatLng mCurrentLatLng;
	@Inject
	LocationRequest mLocationRequest;
	@Inject
	SettingsClient mSettingsClient;
	@Inject
	LocationSettingsRequest.Builder mLocationSettingsRequestBuilder;
	@Inject
	FusedLocationProviderClient mFusedLocationProviderClient;
	@Inject
	MainContract.Presenter mMainPresenter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CircleUsApplication.getInstance().getActivityComponent().inject(this);
		mMainPresenter.addView(this);
		
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		mapView = mapFragment.getView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@SuppressLint("MissingPermission")
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		
		if (mapView != null &&
				mapView.findViewById(Integer.parseInt("1")) != null) {
			// Get the button view
			View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
			// and next place it, on bottom right (as Google Maps app)
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
					locationButton.getLayoutParams();
			// position on right bottom
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			layoutParams.setMargins(0, 0, 30, 30);
		}
		mMap.setMyLocationEnabled(true);
		mMainPresenter.initLocationSettingsRequest();
	}
	
	private LocationRequest createLocationRequest() {
		mLocationRequest.setInterval(INTERVAL);
		mLocationRequest.setFastestInterval(FAST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		return mLocationRequest;
	}
	
	@Override
	public void initLocationSettingsRequest() {
		mLocationSettingsRequestBuilder.addLocationRequest(createLocationRequest());
		Task<LocationSettingsResponse> task = mSettingsClient.checkLocationSettings(mLocationSettingsRequestBuilder.build());
		task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
			@Override
			public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
				mMainPresenter.getCurrentLocation();
			}
		}).addOnFailureListener(this, new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mMainPresenter.handleError(e);
			}
		});
	}
	
	@Override
	public void handleError(Exception e) {
		if (e instanceof ResolvableApiException) {
			// Location settings are not satisfied, but this can be fixed
			// by showing the user a dialog.
			try {
				// Show the dialog by calling startResolutionForResult(),
				// and check the result in onActivityResult().
				ResolvableApiException resolvable = (ResolvableApiException) e;
				resolvable.startResolutionForResult(MainActivity.this,
						REQUEST_CHECK_SETTINGS);
			} catch (IntentSender.SendIntentException sendEx) {
				// Ignore the error for now.
			}
		}
	}
	
	@SuppressLint("MissingPermission")
	@Override
	public void getCurrentLocation() {
		mFusedLocationProviderClient.getLastLocation()
				.addOnSuccessListener(this, new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						// Got last known location. In some rare situations this can be null.
						if (location != null) {
							// Logic to handle location object
							mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//							mMarker = mMap.addMarker(new MarkerOptions().position(mCurrentLatLng).title("your current location"));
							CameraPosition cameraPosition = new CameraPosition.Builder()
									.target(new LatLng(location.getLatitude(), location.getLongitude()))
									.zoom(17)
									.build();
							mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
						}
						
						mMainPresenter.startLocationUpdates();
					}
				});
	}
	
	@SuppressLint("MissingPermission")
	@Override
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CHECK_SETTINGS:
				switch (resultCode) {
					case Activity.RESULT_OK:
						mMainPresenter.getCurrentLocation();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(this, getString(R.string.location_error), Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
				break;
		}
	}
}
