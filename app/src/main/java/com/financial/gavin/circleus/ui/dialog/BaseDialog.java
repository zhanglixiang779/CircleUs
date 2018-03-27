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

import java.util.Map;

/**
 * Created by gavin on 2/26/18.
 */

public class BaseDialog extends DialogFragment {
	
	protected static final String LAYOUT_RES_ID = "layout_resId";
	protected static final String POS_RES_ID = "pos_resId";
	protected static final String NEG_RES_ID = "neg_resId";
	protected NoticeListener mListener;
	
	public interface NoticeListener{
		void onDialogPositiveClick(Map<String, Object> map);
		void onDialogNegativeClick(BaseDialog dialog);
	}
	
	public BaseDialog() {
	
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
	
	protected static Bundle createBundle(int layoutResId, int posResId, int negResId) {
		Bundle args = new Bundle();
		args.putInt(LAYOUT_RES_ID, layoutResId);
		args.putInt(POS_RES_ID, posResId);
		args.putInt(NEG_RES_ID, negResId);
		return args;
	}
}
