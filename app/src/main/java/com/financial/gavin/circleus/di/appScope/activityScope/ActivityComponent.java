package com.financial.gavin.circleus.di.appScope.activityScope;

import com.financial.gavin.circleus.ui.permission.PermissionsActivity;
import com.financial.gavin.circleus.ui.splash.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by gavin on 1/21/18.
 */

@ActivityScope
@Subcomponent(modules =
		{
			SplashModule.class,
			PermissionsModule.class
		})
public interface ActivityComponent {
	void inject(SplashActivity splashActivity);
	void inject(PermissionsActivity permissionsActivity);
}