package com.julun.vehicle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentLayout(R.layout.fragment_user_info)
public class UserInfoFragment extends BaseFragment {

    public UserInfoFragment() {
    }

    @AfterInitView
    public void afterInitViews() {
    }

}
