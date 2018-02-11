package com.financial.gavin.circleus.ui.main;

import android.location.Location;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.data.DataManager;
import com.financial.gavin.circleus.data.firebase.Mock;
import com.financial.gavin.circleus.data.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by gavin on 1/21/18.
 */

public class MainPresenter implements MainContract.Presenter {
	
	private MainContract.View mMainView;
	
	public MainPresenter() {
		getDataManager().getGoogleApis().setObserver(getObserver());
	}
	
	private DataManager getDataManager() {
		return CircleUsApplication.getInstance().getDataManager();
	}
	
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
		getDataManager().getGoogleApis().initLocationSettingsRequest();
	}
	
	@Override
	public void startLocationUpdates() {
		getDataManager().getGoogleApis().startLocationUpdates();
	}
	
	@Override
	public List<User> getUsers() {
		List<User> users = new Mock().getUsers();
		mMainView.updateSlider(users);
		return users;
	}
	
	private  <T> Observer<T> getObserver() {
		return new Observer<T>() {
			@Override
			public void onSubscribe(Disposable d) {
			
			}
			
			@Override
			public void onNext(T t) {
				if (t instanceof Exception) {
					mMainView.handleCurrentLocationError((Exception) t);
				}
				
				if (t instanceof Location) {
//					mMainView.setCurrentLocation((Location) t);
					//TODO: Send the real time location to firebase
				}
				
			}
			
			@Override
			public void onError(Throwable e) {
			
			}
			
			@Override
			public void onComplete() {
			
			}
		};
	}
}
