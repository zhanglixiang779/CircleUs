package com.financial.gavin.circleus;

import android.app.Application;

import com.financial.gavin.circleus.di.activityScope.ActivityComponent;
import com.financial.gavin.circleus.di.activityScope.GoogleServiceModule;
import com.financial.gavin.circleus.di.activityScope.PresentersModule;
import com.financial.gavin.circleus.di.appScope.AppComponent;
import com.financial.gavin.circleus.di.appScope.AppModule;
import com.financial.gavin.circleus.di.appScope.DaggerAppComponent;

/**
 * Created by gavin on 1/20/18.
 */

public class CircleUsApplication extends Application {
	
	private AppComponent mAppComponent;
	private ActivityComponent mActivityComponent;
	private static CircleUsApplication instance;
	
	public static CircleUsApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		mAppComponent = DaggerAppComponent.builder()
				.appModule(new AppModule(this))
				.build();
	}
	
	public synchronized ActivityComponent getActivityComponent() {
		if (mActivityComponent == null) {
			mActivityComponent = mAppComponent.getActivityComponent(new PresentersModule(), new GoogleServiceModule());
		}
		return mActivityComponent;
	}
	
	public void dropActivityComponent() {
		mActivityComponent = null;
	}
}
