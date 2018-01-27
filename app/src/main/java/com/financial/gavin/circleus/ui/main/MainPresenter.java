package com.financial.gavin.circleus.ui.main;

import android.location.Location;

/**
 * Created by gavin on 1/21/18.
 */

public class MainPresenter implements MainContract.Presenter {
	
	private MainContract.View mMainView;
	
	@Override
	public void addView(MainContract.View view) {
		mMainView = view;
	}
	
	@Override
	public void dropView() {
		mMainView = null;
	}
	
	@Override
	public void initLocationSettingsRequest() {
		mMainView.initLocationSettingsRequest();
	}
	
	@Override
	public void getCurrentLocation() {
		mMainView.getCurrentLocation();
	}
	
	@Override
	public void startLocationUpdates() {
		mMainView.startLocationUpdates();
	}
	
	@Override
	public void handleError(Exception e) {
		mMainView.handleError(e);
	}
}
