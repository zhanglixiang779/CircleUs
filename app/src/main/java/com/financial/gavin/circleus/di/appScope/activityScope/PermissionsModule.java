package com.financial.gavin.circleus.di.appScope.activityScope;

import com.financial.gavin.circleus.ui.permission.PermissionsContract;
import com.financial.gavin.circleus.ui.permission.PermissionsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gavin on 1/21/18.
 */

@Module
public class PermissionsModule {
	@Provides
	@ActivityScope
	PermissionsContract.Presenter providesPermissionsPresenter() {
		return new PermissionsPresenter();
	}
}
