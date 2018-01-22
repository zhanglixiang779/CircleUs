package com.financial.gavin.circleus.ui.main;

import com.financial.gavin.circleus.ui.base.BasePresenter;
import com.financial.gavin.circleus.ui.base.BaseView;

/**
 * Created by gavin on 1/21/18.
 */

public interface MainContract {
	
	interface View extends BaseView<Presenter> {
	
	}
	
	interface Presenter extends BasePresenter<View> {
	
	}
}
