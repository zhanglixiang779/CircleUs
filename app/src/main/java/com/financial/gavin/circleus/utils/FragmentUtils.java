package com.financial.gavin.circleus.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by gavin on 1/21/18.
 */

public class FragmentUtils {
	
	public static void addFragmentToActivity(FragmentManager fragmentManager,
											 Fragment fragment, String tag) {
		fragmentManager.beginTransaction().add(fragment, tag).commit();
	}
	
	public static void replaceFragmentToActivity(FragmentManager fragmentManager, int containerResId, Fragment fragment,
									   String tag) {
		fragmentManager.beginTransaction().replace(containerResId, fragment, tag).commit();
	}
}
