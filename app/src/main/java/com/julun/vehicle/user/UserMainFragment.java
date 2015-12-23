package com.julun.vehicle.user;


import android.os.Bundle;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

/**
 * 我的面板
 */
@ContentLayout(R.layout.fragment_user_main)
public class UserMainFragment extends BaseFragment {

    public static UserMainFragment newInstance() {
        return new UserMainFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            getChildFragmentManager().beginTransaction()
                    .add(R.id.user_main_login_container, UserMainHeaderFragment.newInstance()).commit();
        }
    }

    @AfterInitView
    public void afterInitViews() {

    }

}
