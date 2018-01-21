package com.financial.gavin.circleus.di.activityScope;

import com.financial.gavin.circleus.ui.splash.SplashContract;
import com.financial.gavin.circleus.ui.splash.SplashContract.Presenter;
import com.financial.gavin.circleus.ui.splash.SplashPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gavin on 1/21/18.
 */

@Module
public class SplashModule {
	
	@ActivityScope
	@Provides
	Presenter providesSplashPresenter() {
		return new SplashPresenter();
	}
}
