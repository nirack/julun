package com.julun.vehicle.fragments.user;

import android.os.Bundle;
import android.view.MenuItem;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

/**
 * Created by danjp on 2015/12/9.
 * 登陆、注册、手机快捷登陆几个fragment使用
 */
public class UserBackFragment extends BaseFragment {
    public static final String FRAGMENT_LOGIN_TAG = "login";                           //登陆
    public static final String FRAGMENT_SIGNUP_TAG = "signup";                         //注册
    public static final String FRAGMENT_PHONE_SIGNIN_TAG = "phone_login";             //手机快捷登陆
    public static final String FRAGMENT_GET_BACK_PASSWORD_TAG = "get_back_password";   //找回密码

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            //弹出当前fragment，回退到登陆fragment
            getFragmentManager().popBackStack();
            getActivity().setTitle(R.string.login);
        }
        return super.onOptionsItemSelected(item);
    }
}
