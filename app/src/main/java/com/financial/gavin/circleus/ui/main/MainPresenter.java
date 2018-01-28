package com.financial.gavin.circleus.ui.main;

import android.location.Location;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.data.DataManager;

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
	
	public <T> Observer<T> getObserver() {
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
					mMainView.setCurrentLocation((Location) t);
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
