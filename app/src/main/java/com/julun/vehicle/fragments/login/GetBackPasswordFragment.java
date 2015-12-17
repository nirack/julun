package com.julun.vehicle.fragments.login;

import android.util.Log;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

/**
 * Created by danjp on 2015/12/9.
 * 找回密码
 */
@ContentLayout(R.layout.fragment_get_back_password)
public class GetBackPasswordFragment extends BaseFragment {

    private static final String TAG = "GetBackPasswordFragment";

    @AfterInitView
    public void initView() {
        Log.d(TAG, "initView");
    }
}
