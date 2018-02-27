package com.financial.gavin.circleus.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.financial.gavin.circleus.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by gavin on 2/26/18.
 */

public class CustomDialog extends DialogFragment {
	
	private static final String LAYOUT_RES_ID = "layout_resId";
	private static final String POS_RES_ID = "pos_resId";
	private static final String NEG_RES_ID = "neg_resId";
	private NoticeListener mListener;
	
	public interface NoticeListener{
		void onDialogPositiveClick(String groupName);
		void onDialogNegativeClick(CustomDialog dialog);
	}
	
	public CustomDialog() {
	
	}
	
	public static CustomDialog newInstance(int layoutResId, int posResId, int negResId) {
		CustomDialog dialog = new CustomDialog();
		Bundle args = new Bundle();
		args.putInt(LAYOUT_RES_ID, layoutResId);
		args.putInt(POS_RES_ID, posResId);
		args.putInt(NEG_RES_ID, negResId);
		dialog.setArguments(args);
		return dialog;
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mListener = (NoticeListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement NoticeListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view =  inflater.inflate(getArguments().getInt(LAYOUT_RES_ID), null);
		builder.setView(view)
				.setPositiveButton(getArguments().getInt(POS_RES_ID), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						EditText groupName = view.findViewById(R.id.group_name);
						mListener.onDialogPositiveClick(groupName.getText().toString());
					}
				})
				.setNegativeButton(getArguments().getInt(NEG_RES_ID), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDialogNegativeClick(CustomDialog.this);
					}
				});
		return builder.create();
	}
}
