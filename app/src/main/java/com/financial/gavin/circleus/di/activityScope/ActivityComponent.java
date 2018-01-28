package com.financial.gavin.circleus.di.activityScope;

import com.financial.gavin.circleus.data.api.GoogleApisImpl;
import com.financial.gavin.circleus.ui.main.MainActivity;
import com.financial.gavin.circleus.ui.permission.PermissionsActivity;
import com.financial.gavin.circleus.ui.splash.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by gavin on 1/21/18.
 */

@ActivityScope
@Subcomponent(modules =
		{
			PresentersModule.class,
			GoogleServiceModule.class
		})
public interface ActivityComponent {
	void inject(SplashActivity splashActivity);
	void inject(PermissionsActivity permissionsActivity);
	void inject(MainActivity mainActivity);
	void inject(GoogleApisImpl googleApis);
}
