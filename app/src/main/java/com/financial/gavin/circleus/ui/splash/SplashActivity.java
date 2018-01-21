package com.financial.gavin.circleus.ui.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.ui.main.MainActivity;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
	
	@Inject
	SplashContract.Presenter mSplashPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CircleUsApplication.getInstance().getActivityComponent().inject(this);
	}
	
	@Override
	protected void onResume() {
		mSplashPresenter.addView(this);
		mSplashPresenter.startMainActivity();
		super.onResume();
	}
	
	@Override
	public void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		mSplashPresenter.dropView();
		CircleUsApplication.getInstance().dropActivityComponent();
		super.onDestroy();
	}
}
