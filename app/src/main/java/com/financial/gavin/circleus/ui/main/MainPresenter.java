package com.financial.gavin.circleus.ui.main;

/**
 * Created by gavin on 1/21/18.
 */

public class MainPresenter implements MainContract.Presenter {
	
	private MainContract.View mMainView;
	
	@Override
	public void addView(MainContract.View view) {
		mMainView = view;
	}
	
	@Override
	public void dropView() {
		mMainView = null;
	}
}
