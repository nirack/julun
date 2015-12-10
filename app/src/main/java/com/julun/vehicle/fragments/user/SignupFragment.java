package com.julun.vehicle.fragments.user;

import android.util.Log;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.vehicle.R;

/**
 * Created by danjp on 2015/12/10.
 * 注册
 */
@ContentLayout(R.layout.fragment_signup)
public class SignupFragment extends UserBackFragment {

    private static final String TAG = "GetBackPasswordFragment";

    @AfterInitView
    public void initView() {
        Log.d(TAG, "initView");
        getActivity().setTitle(R.string.signup);
    }
}
