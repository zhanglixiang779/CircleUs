package com.financial.gavin.circleus.di.appScope;

import com.financial.gavin.circleus.di.activityScope.ActivityComponent;
import com.financial.gavin.circleus.di.activityScope.SplashModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gavin on 1/21/18.
 */


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
	ActivityComponent getActivityComponent(SplashModule splashModule);
}
