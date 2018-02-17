package com.financial.gavin.circleus.ui.permission;

/**
 * Created by gavin on 1/21/18.
 */

public class PermissionsPresenter implements PermissionsContract.Presenter {
	
	private PermissionsContract.View mPermissionView;
	
	@Override
	public void addView(PermissionsContract.View view) {
		mPermissionView = view;
	}
	
	@Override
	public void dropView() {
		mPermissionView = null;
	}
	
	@Override
	public void startMainActivity() {
		mPermissionView.startMainActivity();
	}
	
	@Override
	public void requestPermissions() {
		mPermissionView.requestPermissions();
	}
}
