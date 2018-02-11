package com.financial.gavin.circleus.ui.main;

import android.location.Location;

import com.financial.gavin.circleus.data.model.User;
import com.financial.gavin.circleus.ui.base.BasePresenter;
import com.financial.gavin.circleus.ui.base.BaseView;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Created by gavin on 1/21/18.
 */

public interface MainContract {
	
	interface View extends BaseView<Presenter> {
		void setCurrentLocation(Location location);
		void handleCurrentLocationError(Exception e);
		void updateSlider(List<User> users);
	}
	
	interface Presenter extends BasePresenter<View> {
		void initLocationSettingsRequest();
		void startLocationUpdates();
		List<User> getUsers();
	}
}
