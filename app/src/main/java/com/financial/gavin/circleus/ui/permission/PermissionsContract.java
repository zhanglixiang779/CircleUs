package com.financial.gavin.circleus.ui.permission;

import com.financial.gavin.circleus.ui.base.BasePresenter;
import com.financial.gavin.circleus.ui.base.BaseView;

/**
 * Created by gavin on 1/21/18.
 */

public interface PermissionsContract {
	
	interface View extends BaseView<Presenter> {
		void startMainActivity();
		void requestPermissions();
	}
	
	interface Presenter extends BasePresenter<View> {
		void startMainActivity();
		void requestPermissions();
	}
}
