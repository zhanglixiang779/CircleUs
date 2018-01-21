package com.financial.gavin.circleus.di.activityScope;

import com.financial.gavin.circleus.ui.splash.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by gavin on 1/21/18.
 */

@ActivityScope
@Subcomponent(modules = SplashModule.class)
public interface ActivityComponent {
	void inject(SplashActivity splashActivity);
}
