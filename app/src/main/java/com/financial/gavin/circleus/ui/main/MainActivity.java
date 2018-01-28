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
	
	
	@Inject
	MainContract.Presenter mMainPresenter;
	
	private GoogleMap mMap;
	private static final int REQUEST_CHECK_SETTINGS = 100;
	private static final int ZOOM_LEVEL = 17;
	private static final int ANIMATION_PERIOD = 1000;
	private static final int MARGIN_LEFT = 0;
	private static final int MARGIN_RIGHT = 30;
	private static final int MARGIN_TOP = 0;
	private static final int MARGIN_BOTTOM = 30;
	private View mapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CircleUsApplication.getInstance().getActivityComponent().inject(this);
		mMainPresenter.addView(this);
		
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
		mMap.setMyLocationEnabled(true);
		adjustMyLocationButton(mapView);
		mMainPresenter.initLocationSettingsRequest();
	}
	
	private void adjustMyLocationButton(View mapView) {
		if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
			// Get the button view
			View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
			// and next place it, on bottom right (as Google Maps app)
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
			// position on right bottom
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			layoutParams.setMargins(MARGIN_LEFT, MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM);
		}
	}
	
	@Override
	public void handleCurrentLocationError(Exception e) {
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CHECK_SETTINGS:
				switch (resultCode) {
					case Activity.RESULT_OK:
						mMainPresenter.initLocationSettingsRequest();
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
	
	@Override
	public void setCurrentLocation(Location location) {
		// Logic to handle location object
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location.getLongitude()))
				.zoom(ZOOM_LEVEL)
				.build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), ANIMATION_PERIOD, null);
		
		mMainPresenter.startLocationUpdates();
		
	}
}
