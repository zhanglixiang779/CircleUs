package com.financial.gavin.circleus.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.financial.gavin.circleus.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gavin on 3/3/18.
 */

public class CreateGroupDialog extends BaseDialog {
	
	public CreateGroupDialog() {
	}
	
	public static CreateGroupDialog newInstance(int layoutResId, int posResId, int negResId) {
		CreateGroupDialog dialog = new CreateGroupDialog();
		Bundle args = createBundle(layoutResId, posResId, negResId);
		dialog.setArguments(args);
		return dialog;
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
						Map<String, Object> data = new HashMap<>();
						data.put("group_name", groupName.getText().toString());
						mListener.onDialogPositiveClick(data);
					}
				})
				.setNegativeButton(getArguments().getInt(NEG_RES_ID), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDialogNegativeClick(CreateGroupDialog.this);
					}
				});
		return builder.create();
	}
}
