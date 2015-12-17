package com.julun.vehicle.activities.user;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.vehicle.fragments.user.UserInfoFragment;

/**
 * Created by danjp on 2015/12/17.
 * 用户登陆后的个人信息界面
 */
@ContentLayout(R.layout.activity_base_fragment)
public class UserInfoActivity extends BaseActivity {

    @AfterInitView
    public void viewInit() {

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_base_container, UserInfoFragment.newsInstance()).commit();

    }

}
