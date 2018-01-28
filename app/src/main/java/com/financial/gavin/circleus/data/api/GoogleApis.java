package com.financial.gavin.circleus.data.api;

import android.location.Location;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by gavin on 1/27/18.
 */

public interface GoogleApis {
	void initLocationSettingsRequest();
	void getCurrentLocation();
	void startLocationUpdates();
	<T> void setObserver(Observer<T> t);
}
