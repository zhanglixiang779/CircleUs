package com.financial.gavin.circleus.di.activityScope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by gavin on 1/21/18.
 */

@Scope
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
