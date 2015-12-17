package com.julun.vehicle.fragments.login;

import android.util.Log;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

/**
 * Created by danjp on 2015/12/9.
 * 手机快捷登陆
 */
@ContentLayout(R.layout.fragment_phone_login)
public class PhoneLoginFragment extends BaseFragment {

    private static final String TAG = "PhoneLoginFragment";

    @AfterInitView
    public void initView() {
        Log.d(TAG, "initView");
    }
}
