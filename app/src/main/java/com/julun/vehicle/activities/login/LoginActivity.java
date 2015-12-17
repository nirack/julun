package com.julun.vehicle.activities.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.vehicle.activities.MainActivity;
import com.julun.vehicle.fragments.login.GetBackPasswordFragment;
import com.julun.vehicle.fragments.login.LoginFragment;
import com.julun.vehicle.fragments.login.PhoneLoginFragment;
import com.julun.vehicle.fragments.login.SignupFragment;

import butterknife.Bind;

/**
 * Created by danjp on 2015/12/8.
 */
@ContentLayout(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginFragment.LoginCallBack {
    private static final String TAG = "LoginActivity";

    public static final String FRAGMENT_LOGIN_TAG = "login";                           //登陆
    public static final String FRAGMENT_SIGNUP_TAG = "signup";                         //注册
    public static final String FRAGMENT_PHONE_SIGNIN_TAG = "phone_login";             //手机快捷登陆
    public static final String FRAGMENT_GET_BACK_PASSWORD_TAG = "get_back_password";   //找回密码

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_center_tv)
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "savedInstanceState: "+savedInstanceState);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_LOGIN_TAG);
        if(savedInstanceState == null) {
            transaction.add(R.id.fragmentContainer, LoginFragment.newInstance(""), FRAGMENT_LOGIN_TAG).commit();
        }else{
            transaction.show(fragment);
        }

        mTitleTv.setText(R.string.login);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_home_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_LOGIN_TAG);
                if (fragment == null || fragment.isHidden()) {
                    getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction().show(fragment);
                    mTitleTv.setText(R.string.login);
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTv.setText(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "outState: " + outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showFragment(String fragmentTag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(fragmentTag);
        //隐藏当前登陆fragment，不希望重绘视图，保存用户输入的登录名密码
        transaction.hide(fm.findFragmentByTag(FRAGMENT_LOGIN_TAG));
        switch (fragmentTag) {
            case FRAGMENT_SIGNUP_TAG:
                fragment = new SignupFragment();
                setTitle(R.string.signup);
                break;
            case FRAGMENT_PHONE_SIGNIN_TAG:
                fragment = new PhoneLoginFragment();
                setTitle(R.string.phone_login);
                break;
            case FRAGMENT_GET_BACK_PASSWORD_TAG:
                fragment = new GetBackPasswordFragment();
                setTitle(R.string.get_back_password);
                break;
        }
        //都是从loginFragment中切换到其他fragment，保存loginFragment实例，销毁其他fragment
        transaction.add(R.id.fragmentContainer, fragment, fragmentTag).addToBackStack(null).commit();
    }
}
