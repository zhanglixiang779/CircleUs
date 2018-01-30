package com.financial.gavin.circleus.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.financial.gavin.circleus.R;
import com.financial.gavin.circleus.data.model.User;

import java.util.List;

/**
 * Created by gavin on 1/28/18.
 */

public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.UserViewHolder> {
	
	private List<User> users;
	private Context mContext;
	
	public UserViewAdapter(List<User> users, Context context) {
		this.users = users;
		mContext = context;
	}
	
	@Override
	public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
		return new UserViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(UserViewHolder holder, int position) {
		int newPosition = position % users.size();
		holder.name.setText(users.get(newPosition).getName());
		holder.phone.setText(users.get(newPosition ).getPhoneNumber());
		Glide.with(mContext)
				.load(users.get(newPosition).getThumbNailUrl())
				.apply(RequestOptions.circleCropTransform())
				.into(holder.thumbNail);
	}
	
	@Override
	public int getItemCount() {
		return Integer.MAX_VALUE;
	}
	
	class UserViewHolder extends RecyclerView.ViewHolder {
		
		ImageView thumbNail;
		TextView name;
		TextView phone;
		public UserViewHolder(View itemView) {
			super(itemView);
			thumbNail = itemView.findViewById(R.id.thumb_nail);
			name = itemView.findViewById(R.id.name);
			phone = itemView.findViewById(R.id.phone);
		}
	}
}
