package com.julun.vehicle.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.julun.vehicle.R;

/**
 * Created by danjp on 2015/12/10.
 */
public class ProgressDialogFragment extends DialogFragment {
    private final static String PROGRESS_LOADING = "progress_loading";

    public static ProgressDialogFragment newInstance(int message) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PROGRESS_LOADING, message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getString(R.string.loading_text));
        return progressDialog;
    }
}
