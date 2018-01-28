package com.financial.gavin.circleus.data;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.data.api.GoogleApis;
import com.financial.gavin.circleus.data.api.GoogleApisImpl;
import com.financial.gavin.circleus.di.activityScope.GoogleServiceModule;
import com.financial.gavin.circleus.di.activityScope.PresentersModule;

import javax.inject.Inject;

/**
 * Created by gavin on 1/27/18.
 */

public class DataManagerImpl implements DataManager {
	
	@Inject
	GoogleApis mGoogleApis;
	
	public DataManagerImpl() {
		CircleUsApplication.getInstance().getAppComponent()
				.inject(this);
	}
	
	@Override
	public GoogleApis getGoogleApis() {
		return mGoogleApis;
	}
}
