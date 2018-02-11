package com.financial.gavin.circleus.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.R;
import com.financial.gavin.circleus.data.model.User;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, MainContract.View {
	
	@Inject
	MainContract.Presenter mMainPresenter;
	@Inject
	LatLngBounds.Builder mBoundsBuilder;
	
	private static final int REQUEST_CHECK_SETTINGS = 100;
	private static final int INDIVIDUAL_ZOOM_LEVEL = 15;
	private static final int ZOOM_LEVEL = 12;
	private static final int ANIMATION_PERIOD = 1000;
	private static final int MARGIN_LEFT = 0;
	private static final int MARGIN_RIGHT = 30;
	private static final int MARGIN_TOP = 0;
	private static final int MARGIN_BOTTOM = 30;
	private static final int PADDING_LEFT = 0;
	private static final int PADDING_TOP = 0;
	private static final int PADDING_RIGHT = 0;
	private static final int PADDING_BOTTOM = 180;
	
	private GoogleMap mMap;
	private View mapView;
	private RecyclerView mRecyclerView;
	private List<User> users;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CircleUsApplication.getInstance().getActivityComponent().inject(this);
		mRecyclerView = findViewById(R.id.slider);
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
		mMap.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, dpToPx(PADDING_BOTTOM));
		adjustMyLocationButton(mapView);
		mMainPresenter.initLocationSettingsRequest();
		users = mMainPresenter.getUsers();
		//This call is invoked after the map layout is complete
		mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				for (User user : users) {
					mMap.addMarker(new MarkerOptions()
							.position(user.getLatLng())
							.title(user.getName()));
					mBoundsBuilder.include(user.getLatLng());
				}
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mBoundsBuilder.build().getCenter(), ZOOM_LEVEL));
			}
		});
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
				.zoom(INDIVIDUAL_ZOOM_LEVEL)
				.build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), ANIMATION_PERIOD, null);
	}
	
	@Override
	public void updateSlider(final List<User> users) {
		SnapHelper snapHelper = new LinearSnapHelper();
		snapHelper.attachToRecyclerView(mRecyclerView);
		RecyclerView.LayoutManager layoutManager =
				new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		UserViewAdapter adapter = new UserViewAdapter(users, this);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.scrollToPosition(Integer.MAX_VALUE /2);
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				LinearLayoutManager lm = (LinearLayoutManager)recyclerView.getLayoutManager();
				int position = lm.findFirstCompletelyVisibleItemPosition();
				User user = users.get(position % users.size());
				LatLng latLng = user.getLatLng();
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(latLng)
						.zoom(INDIVIDUAL_ZOOM_LEVEL)
						.build();
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), ANIMATION_PERIOD, null);
			}
		});
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
	
	private int dpToPx(int dp) {
		Resources r = getResources();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
	}
}
