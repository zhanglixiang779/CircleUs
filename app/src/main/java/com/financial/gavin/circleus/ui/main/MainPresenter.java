package com.financial.gavin.circleus.ui.main;

import android.location.Location;
import android.util.Log;

import com.financial.gavin.circleus.CircleUsApplication;
import com.financial.gavin.circleus.data.DataManager;
import com.financial.gavin.circleus.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by gavin on 1/21/18.
 */

public class MainPresenter implements MainContract.Presenter {
	
	private MainContract.View mMainView;
	private DatabaseReference mRef;
	private DatabaseReference mGroupRef;
	
	public MainPresenter() {
		getDataManager().getGoogleApis().setObserver(getObserver());
	}
	
	private DataManager getDataManager() {
		return CircleUsApplication.getInstance().getDataManager();
	}
	
	@Override
	public void addView(MainContract.View view) {
		mMainView = view;
	}
	
	@Override
	public void dropView() {
		mMainView = null;
	}
	
	@Override
	public void initLocationSettingsRequest() {
		getDataManager().getGoogleApis().initLocationSettingsRequest();
	}
	
	@Override
	public void startLocationUpdates() {
		getDataManager().getGoogleApis().startLocationUpdates();
	}
	
	@Override
	public void getUsers() {
		final List<User> users = new ArrayList<>();
		mRef = CircleUsApplication.getInstance().getFirebase().getReference();
		mGroupRef = mRef.child("groups").child("group1");
		final ValueEventListener groupListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot ref : dataSnapshot.getChildren()) {
					User user = ref.getValue(User.class);
					users.add(user);
				}
				mMainView.updateSlider(users);
				mMainView.setUsers(users);
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
			
			}
		};
		
		mGroupRef.addListenerForSingleValueEvent(groupListener);
	}
	
	private  <T> Observer<T> getObserver() {
		return new Observer<T>() {
			@Override
			public void onSubscribe(Disposable d) {
			
			}
			
			@Override
			public void onNext(T t) {
				if (t instanceof Exception) {
					mMainView.handleCurrentLocationError((Exception) t);
				}
				
				if (t instanceof Location) {
				
//					mMainView.setCurrentLocation((Location) t);
					//TODO: Send the real time location to firebase
//					mRef = CircleUsApplication.getInstance().getFirebase().getReference();
//					String groupName = mRef.child("groups").child("first group");
				}
			}
			
			@Override
			public void onError(Throwable e) {
			
			}
			
			@Override
			public void onComplete() {
			
			}
		};
	}
}
