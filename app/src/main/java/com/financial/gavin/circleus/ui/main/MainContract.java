package com.financial.gavin.circleus.ui.main;

import android.location.Location;

import com.financial.gavin.circleus.ui.base.BasePresenter;
import com.financial.gavin.circleus.ui.base.BaseView;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by gavin on 1/21/18.
 */

public interface MainContract {
	
	interface View extends BaseView<Presenter> {
		void initLocationSettingsRequest();
		void getCurrentLocation();
		void startLocationUpdates();
		void handleError(Exception e);
	}
	
	interface Presenter extends BasePresenter<View> {
		void initLocationSettingsRequest();
		void getCurrentLocation();
		void startLocationUpdates();
		void handleError(Exception e);
	}
}
