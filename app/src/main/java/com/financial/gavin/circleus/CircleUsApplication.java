package com.financial.gavin.circleus;

import android.app.Application;

import com.financial.gavin.circleus.data.DataManager;
import com.financial.gavin.circleus.data.firebase.Mock;
import com.financial.gavin.circleus.data.model.User;
import com.financial.gavin.circleus.di.activityScope.ActivityComponent;
import com.financial.gavin.circleus.di.activityScope.GoogleServiceModule;
import com.financial.gavin.circleus.di.activityScope.PresentersModule;
import com.financial.gavin.circleus.di.appScope.AppComponent;
import com.financial.gavin.circleus.di.appScope.AppModule;
import com.financial.gavin.circleus.di.appScope.DaggerAppComponent;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gavin on 1/20/18.
 */

public class CircleUsApplication extends Application {
	
	@Inject
	DataManager mDataManager;
	@Inject
	FirebaseDatabase mFirebase;
	
	private AppComponent mAppComponent;
	private ActivityComponent mActivityComponent;
	private static CircleUsApplication instance;
	
	public static CircleUsApplication getInstance() {
		return instance;
	}
	
	public DataManager getDataManager() {
		return mDataManager;
	}
	
	public FirebaseDatabase getFirebase() {
		return mFirebase;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		mAppComponent = getAppComponent();
		mAppComponent.inject(this);
		
		List<User> users = new Mock().getUsers();
		mFirebase.getReference().child("groups").child("group1").setValue(users);
	}
	
	public synchronized AppComponent getAppComponent() {
		return DaggerAppComponent.builder()
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
