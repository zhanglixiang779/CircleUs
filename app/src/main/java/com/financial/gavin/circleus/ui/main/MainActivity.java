package com.financial.gavin.circleus.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.R;
import com.financial.gavin.circleus.data.model.User;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback,
						MainContract.View, View.OnClickListener, PlaceSelectionListener, GoogleMap.OnMarkerClickListener,
						GoogleMap.OnMapLoadedCallback {
	
	@Inject
	MainContract.Presenter mMainPresenter;
	@Inject
	List<Marker> mUserMarkers;
	@Inject
	List<LatLng> mLatLngList;
	
	private GoogleMap mMap;
	private Circle mCircle;
	private SupportPlaceAutocompleteFragment autocompleteFragment;
	private SupportStreetViewPanoramaFragment streetViewPanoramaFragment;
	private Marker mDestMarker;
	private SlidingUpPanelLayout mSlidingUpPanelLayout;
	private View mapView;
	private RecyclerView mRecyclerView;
	private Button mDestButton;
	private FloatingActionMenu mClickHereFabMenu;
	private FloatingActionButton mCreateGroupFab, mAddFriendFab, mGroupChatFab, mPanoramaViewFab, mRegisterFab;
	private List<User> mUsers;
	private LatLng mSelectedMarkerLatLng;
	
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
	private static final int PADDING_BOTTOM = 170;
	private static final int HSV_ALPHA = 70;
	private static final int HSV_HUE = 123;
	private static final float HSV_SATURATION = 0.6f;
	private static final float HSV_VALUE = 0.56f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CircleUsApplication.getInstance().getActivityComponent().inject(this);
		mRecyclerView = findViewById(R.id.slider);
		mDestButton = findViewById(R.id.change_destination_btn);
		mSlidingUpPanelLayout = findViewById(R.id.sliding_layout);
		mClickHereFabMenu = findViewById(R.id.click_here_fab_menu);
		mRegisterFab = findViewById(R.id.register_fab);
		mAddFriendFab = findViewById(R.id.add_user_fab);
		mCreateGroupFab = findViewById(R.id.create_group_fab);
		mGroupChatFab = findViewById(R.id.group_chat_fab);
		mPanoramaViewFab = findViewById(R.id.panorama_view_fab);
		mMainPresenter.addView(this);
		setOnClickListener();
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
		autocompleteFragment = (SupportPlaceAutocompleteFragment)
				getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
		autocompleteFragment.setHint(getString(R.string.auto_complete_hint));
		autocompleteFragment.setOnPlaceSelectedListener(this);
		
		streetViewPanoramaFragment =
				(SupportStreetViewPanoramaFragment) getSupportFragmentManager()
						.findFragmentById(R.id.street_view_panorama);
		
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
		mMainPresenter.getUsers();
		
		//This call is invoked after the map layout is complete
		mMap.setOnMapLoadedCallback(this);
		mMap.setOnMarkerClickListener(this);
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
		//These two lines are necessary for auto complete fragment to work with map fragment together.
		super.onActivityResult(requestCode, resultCode, data);
		autocompleteFragment.onActivityResult(requestCode, resultCode, data);
		
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
			default:
				break;
		}
	}
	
	@Override
	public void setCurrentLocation(Location location) {
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
				LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(latLng)
						.zoom(ZOOM_LEVEL)
						.build();
				matchUserWithMarker(user);
				
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), ANIMATION_PERIOD, null);
			}
		});
	}
	
	@Override
	public void setUsers(List<User> users) {
		mUsers = users;
	}
	
	/**
	 * Handle OnClick listener
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.change_destination_btn:
				autocompleteFragment.getView().setVisibility(View.VISIBLE);
				mDestButton.setVisibility(View.GONE);
				break;
			case R.id.create_group_fab:
				mClickHereFabMenu.close(true);
				Toast.makeText(this, "create group fab clicked", Toast.LENGTH_SHORT).show();
				break;
			case R.id.add_user_fab:
				//TODO: Add a user
				mClickHereFabMenu.close(true);
				break;
			case R.id.group_chat_fab:
				//TODO: Add group chat
				mClickHereFabMenu.close(true);
				break;
			case R.id.panorama_view_fab:
				circleUs(mLatLngList);
				mClickHereFabMenu.close(true);
			case R.id.register_fab:
				//TODO: Add register
				mClickHereFabMenu.close(true);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Handle OnPlaceSelectedListener
	 * @param place
	 */
	@Override
	public void onPlaceSelected(Place place) {
		//TODO: will implement place picker here for user to confirm
		mDestButton.setVisibility(View.VISIBLE);
		autocompleteFragment.getView().setVisibility(View.GONE);
		if (mDestMarker != null) {
			mDestMarker.remove();
			mLatLngList.remove(mLatLngList.size() - 1);
		}
		mDestMarker = mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
				.title(String.valueOf(place.getName()))
				.position(place.getLatLng()));
		mLatLngList.add(mDestMarker.getPosition());
		circleUs(mLatLngList);
	}
	
	@Override
	public void onError(Status status) {
		Toast.makeText(MainActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Handle OnMapLoadedCallback
	 */
	@Override
	public void onMapLoaded() {
		for (User user : mUsers) {
			Marker userMarker = mMap.addMarker(new MarkerOptions()
					.position(new LatLng(user.getLatitude(), user.getLongitude()))
					.title(user.getName()));
			userMarker.setTag(user.getName());
			mUserMarkers.add(userMarker);
			mLatLngList.add(userMarker.getPosition());
		}
		circleUs(mLatLngList);
	}
	
	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
		if (streetViewPanorama != null) {
			streetViewPanorama.setPosition(mSelectedMarkerLatLng);
			mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
		}
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		mSelectedMarkerLatLng = marker.getPosition();
		streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
		return false;
	}
	
	private void circleUs(List<LatLng> latLngList) {
		if (mCircle != null) {
			mCircle.remove();
		}
		
		LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
		for (LatLng latlng : latLngList) {
			latLngBoundsBuilder.include(latlng);
		}
		
		LatLng center = latLngBoundsBuilder.build().getCenter();
		int fillColor = Color.HSVToColor(HSV_ALPHA, new float[]{HSV_HUE, HSV_SATURATION, HSV_VALUE});
		CircleOptions circleOptions = new CircleOptions()
				.fillColor(fillColor)
				.strokeColor(Color.TRANSPARENT)
				.center(center)
				.radius(computeCircleRadius(latLngList, center));
		mCircle = mMap.addCircle(circleOptions);
//		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, ZOOM_LEVEL));
		mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), dpToPx(16)));
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
	
	private void matchUserWithMarker(User user) {
		for (Marker userMarker : mUserMarkers) {
			if (userMarker.getTag().equals(user.getName())) {
				userMarker.showInfoWindow();
			}
		}
	}
	
	private int dpToPx(int dp) {
		Resources r = getResources();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
	}
	
	private double computeCircleRadius(List<LatLng> latLngs, LatLng center) {
		double radius = 0;
		
		for (LatLng latlng : latLngs) {
			double distance = SphericalUtil.computeDistanceBetween(latlng, center);
			radius = radius > distance ? radius : distance;
		}
		return radius;
	}
	
	private void setOnClickListener() {
		mDestButton.setOnClickListener(this);
		mCreateGroupFab.setOnClickListener(this);
		mAddFriendFab.setOnClickListener(this);
		mGroupChatFab.setOnClickListener(this);
		mPanoramaViewFab.setOnClickListener(this);
		mRegisterFab.setOnClickListener(this);
	}
}
