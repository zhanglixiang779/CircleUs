package com.financial.gavin.circleus.ui.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.R;
import com.financial.gavin.circleus.ui.main.MainActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.util.List;
import javax.inject.Inject;

public class PermissionsActivity extends AppCompatActivity implements PermissionsContract.View {
	
	private static final String SCHEMA = "package";
	private static final int DETAILS_SETTINGS_REQUEST_CODE = 100;
	private static final int DRAW_OVER_OTHER_REQUEST_CODE = 2084;
	private boolean isPermissionsChecked;
	
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
		mPermissionsPresenter.requestPermissions();
	}
	
	@Override
	public void requestPermissions() {
		Dexter.withActivity(this)
				.withPermissions(
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.READ_CONTACTS)
				.withListener(new MultiplePermissionsListener() {
					@Override
					public void onPermissionsChecked(MultiplePermissionsReport report) {
						if (report.areAllPermissionsGranted() && !isPermissionsChecked) {
							isPermissionsChecked = true;
							openDrawOverlaySettings();
						}
					}
					
					@Override
					public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
						showSettingsDialog(token);
					}
				})
				.withErrorListener(new PermissionRequestErrorListener() {
					@Override
					public void onError(DexterError error) {
					
					}
				})
				.onSameThread()
				.check();
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DRAW_OVER_OTHER_REQUEST_CODE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
				mPermissionsPresenter.startMainActivity();
			} else {
				Toast.makeText(this, getString(R.string.over_lay_settings_error), Toast.LENGTH_LONG).show();
				openDrawOverlaySettings();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void showSettingsDialog(final PermissionToken token) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_title))
				.setMessage(getString(R.string.dialog_message))
				.setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						token.continuePermissionRequest();
						openDetailSettings();
					}
				})
				
				.setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						token.cancelPermissionRequest();
					}
				}).show();
	}
	
	// navigating user to app settings
	private void openDetailSettings() {
		Intent intent = createIntent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		startActivityForResult(intent, DETAILS_SETTINGS_REQUEST_CODE);
	}
	
	private void openDrawOverlaySettings() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
			Intent intent = createIntent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
			startActivityForResult(intent, DRAW_OVER_OTHER_REQUEST_CODE);
		} else {
			mPermissionsPresenter.startMainActivity();
		}
	}
	
	private Intent createIntent(String settings) {
		Intent intent = new Intent(settings);
		Uri uri = Uri.fromParts(SCHEMA, getPackageName(), null);
		intent.setData(uri);
		return intent;
	}
}
