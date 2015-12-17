package com.julun.vehicle.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.manage.UserInfoManager;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.utils.SharedPreferencesUtils;
import com.julun.vehicle.R;
import com.julun.vehicle.activities.login.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by danjp on 2015/12/16.
 * "我的"界面中上半部分 关于是否登录的界面
 */
@ContentLayout(R.layout.fragment_user_header)
public class UserMainHeaderFragment extends BaseFragment {

    @Bind(R.id.not_login_container)
    LinearLayout notLoginContainer;
    @Bind(R.id.user_icon)
    ImageView userIcon;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.user_level_icon)
    ImageView userLevelIcon;
    @Bind(R.id.logined_container)
    RelativeLayout loginedContainer;

    public static UserMainHeaderFragment newInstance() {
        return new UserMainHeaderFragment();
    }

    @AfterInitView
    public void initView() {

        boolean isLogined = UserInfoManager.getIsLogin();
        if(isLogined) {
            notLoginContainer.setVisibility(View.GONE);
            loginedContainer.setVisibility(View.VISIBLE);
        } else {
            notLoginContainer.setVisibility(View.VISIBLE);
            loginedContainer.setVisibility(View.GONE);
        }

    }


    @OnClick({R.id.not_login_container, R.id.logined_container})
    public void viewClick(View view ) {
        switch (view.getId()) {
            case R.id.not_login_container:
                //未登录，跳转到登陆页面
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.logined_container:
                //已登录，跳转到显示个人信息页面
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
