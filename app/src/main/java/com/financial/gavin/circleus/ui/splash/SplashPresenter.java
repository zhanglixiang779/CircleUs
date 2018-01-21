package com.financial.gavin.circleus.ui.splash;

/**
 * Created by gavin on 1/20/18.
 */

public class SplashPresenter implements SplashContract.Presenter {
	
	private SplashContract.View mSplashView;
	
	@Override
	public void addView(SplashContract.View view) {
		mSplashView = view;
	}
	
	@Override
	public void dropView() {
		mSplashView = null;
	}
	
	@Override
	public void startMainActivity() {
		mSplashView.startMainActivity();
	}
}
