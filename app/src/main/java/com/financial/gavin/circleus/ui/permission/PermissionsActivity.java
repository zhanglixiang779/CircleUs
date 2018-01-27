package com.financial.gavin.circleus.ui.permission;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.R;
import com.financial.gavin.circleus.ui.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class PermissionsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks
			, PermissionsContract.View {
	
	private static final int LOCATION_REQUEST_CODE = 1;
	
	@Inject
	PermissionsContract.Presenter mPermissionsPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CircleUsApplication.getInstance().getActivityComponent().inject(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mPermissionsPresenter.addView(this);
		mPermissionsPresenter.requestLocationPermission();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		// Forward results to EasyPermissions
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
	
	@AfterPermissionGranted(LOCATION_REQUEST_CODE)
	@Override
	public void requestLocationPermission() {
		String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
		if (EasyPermissions.hasPermissions(this, perms)) {
			// Already have permission, do the thing
			mPermissionsPresenter.startMainActivity();
		} else {
//			 Do not have permissions, request them now
				EasyPermissions.requestPermissions(
					new PermissionRequest.Builder(this, LOCATION_REQUEST_CODE, perms)
							.setRationale(R.string.location_rationale)
							.setPositiveButtonText(R.string.rationale_ask_ok)
							.setNegativeButtonText(R.string.rationale_ask_cancel)
//							.setTheme(R.style.my_fancy_style)
							.build());
		}
	}
	
	@Override
	public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
		if (requestCode == LOCATION_REQUEST_CODE && perms.get(0).equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
			mPermissionsPresenter.startMainActivity();
		}
	}
	
	@Override
	public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
		// (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
		// This will display a dialog directing them to enable the permission in app settings.
		if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
			new AppSettingsDialog.Builder(this).build().show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
			// Do something after user returned from app settings screen, like showing a Toast.
			
		}
	}
	
	@Override
	public void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(0, 0);
	}
	
	@Override
	protected void onDestroy() {
		mPermissionsPresenter.dropView();
		super.onDestroy();
	}
}
