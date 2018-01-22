package com.financial.gavin.circleus;

import android.app.Application;

import com.financial.gavin.circleus.di.appScope.activityScope.ActivityComponent;
import com.financial.gavin.circleus.di.appScope.activityScope.SplashModule;
import com.financial.gavin.circleus.di.appScope.AppComponent;
import com.financial.gavin.circleus.di.appScope.AppModule;
import com.financial.gavin.circleus.di.appScope.DaggerAppComponent;

/**
 * Created by gavin on 1/20/18.
 */

public class CircleUsApplication extends Application {
	
	private AppComponent mAppComponent;
	private ActivityComponent mActivityActivityComponent;
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
		if (mActivityActivityComponent == null) {
			mActivityActivityComponent = mAppComponent.getActivityComponent(new SplashModule());
		}
		return mActivityActivityComponent;
	}
	
	public void dropActivityComponent() {
		mActivityActivityComponent = null;
	}
}
