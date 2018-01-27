package com.financial.gavin.circleus.di.activityScope;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gavin on 1/22/18.
 */

@Module
public class GoogleServiceModule {
	
	@ActivityScope
	@Provides
	LocationRequest providesLocationRequest() {
		return new LocationRequest();
	}
	
	@ActivityScope
	@Provides
	LocationSettingsRequest.Builder providesBuilder() {
		return new LocationSettingsRequest.Builder();
	}
	@ActivityScope
	@Provides
	SettingsClient providesSettingsClient(Context context) {
		return LocationServices.getSettingsClient(context);
	}
	
	@ActivityScope
	@Provides
	FusedLocationProviderClient providesFusedLocationProviderClient(Context context) {
		return LocationServices.getFusedLocationProviderClient(context);
	}
}
