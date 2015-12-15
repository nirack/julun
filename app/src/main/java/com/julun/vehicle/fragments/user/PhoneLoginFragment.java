package com.julun.vehicle.fragments.user;

import android.util.Log;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.vehicle.R;

/**
 * Created by danjp on 2015/12/9.
 * 手机快捷登陆
 */
@ContentLayout(R.layout.fragment_phone_login)
public class PhoneLoginFragment extends UserBackFragment {

    private static final String TAG = "PhoneLoginFragment";

    @AfterInitView
    public void initView() {
        Log.d(TAG, "initView");
        getActivity().setTitle(R.string.phone_login);
    }
}
