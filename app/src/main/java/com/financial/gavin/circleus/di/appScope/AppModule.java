package com.financial.gavin.circleus.di.appScope;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gavin on 1/20/18.
 */


@Module
public class AppModule {
	private Context instance;
	
	public AppModule(Context instance) {
		this.instance = instance;
	}
	
	@Singleton
	@Provides
	Context providesApplicationContext() {
		return instance;
	}
}
