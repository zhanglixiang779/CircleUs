package com.financial.gavin.circleus.di.activityScope;

import com.financial.gavin.circleus.ui.main.MainContract;
import com.financial.gavin.circleus.ui.main.MainPresenter;
import com.financial.gavin.circleus.ui.permission.PermissionsContract;
import com.financial.gavin.circleus.ui.permission.PermissionsPresenter;
import com.financial.gavin.circleus.ui.splash.SplashContract;
import com.financial.gavin.circleus.ui.splash.SplashPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gavin on 1/22/18.
 */

@Module
public class PresentersModule {
	
	@ActivityScope
	@Provides
	PermissionsContract.Presenter providesPermissionsPresenter() {
		return new PermissionsPresenter();
	}
	
	@ActivityScope
	@Provides
	SplashContract.Presenter providesSplashPresenter() {
		return new SplashPresenter();
	}
	
	@ActivityScope
	@Provides
	MainContract.Presenter providesMainPresenter() {
		return new MainPresenter();
	}
}
