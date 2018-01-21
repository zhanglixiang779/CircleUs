package com.financial.gavin.circleus.ui.splash;

import com.financial.gavin.circleus.ui.base.BasePresenter;
import com.financial.gavin.circleus.ui.base.BaseView;

/**
 * Created by gavin on 1/20/18.
 */

public interface SplashContract {
	
	interface View extends BaseView<Presenter> {
		void startMainActivity();
	}
	
	interface Presenter extends BasePresenter<View> {
		void startMainActivity();
	}
}
