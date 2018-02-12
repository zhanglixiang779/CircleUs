package com.financial.gavin.circleus.di.appScope;

import android.app.Application;
import android.content.Context;

import com.financial.gavin.circleus.data.DataManager;
import com.financial.gavin.circleus.data.DataManagerImpl;
import com.financial.gavin.circleus.data.api.GoogleApis;
import com.financial.gavin.circleus.data.api.GoogleApisImpl;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gavin on 1/20/18.
 */


@Module
public class AppModule {
	
	private Context instance;
	
	public AppModule(Context instance) {
		this.instance = instance;
	}
	
	@Singleton
	@Provides
	Context providesApplicationContext() {
		return instance;
	}
	
	@Singleton
	@Provides
	DataManager providesDataManager() {
		return new DataManagerImpl();
	}
	
	@Singleton
	@Provides
	GoogleApis providesGoogleApis() {
		return new GoogleApisImpl();
	}
	
	@Singleton
	@Provides
	List<Marker> providesUserMarkers() {
		return new ArrayList<>();
	}
}
