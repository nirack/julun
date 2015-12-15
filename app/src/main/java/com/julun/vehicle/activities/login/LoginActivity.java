package com.julun.vehicle.activities.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.vehicle.fragments.user.LoginFragment;

/**
 * Created by danjp on 2015/12/8.
 */
@ContentLayout(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //设置自定义返回图标
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_home_back);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(savedInstanceState == null) {
            /*FragmentManager fm = getFragmentManager();
            Fragment fragment = fm.findFragmentByTag(LoginFragment.FRAGMENT_LOGIN_TAG);
            FragmentTransaction trans = fm.beginTransaction();
            if (fragment == null) {
                fragment = new LoginFragment();
                trans.add(R.id.fragmentContainer, fragment, LoginFragment.FRAGMENT_LOGIN_TAG).commit();
            } else {
                trans.replace(R.id.fragmentContainer, fragment, LoginFragment.FRAGMENT_LOGIN_TAG).commit();
            }*/
            transaction.add(R.id.fragmentContainer, new LoginFragment(), LoginFragment.FRAGMENT_LOGIN_TAG).commit();
        }else{
            transaction.show(getFragmentManager().findFragmentByTag(LoginFragment.FRAGMENT_LOGIN_TAG));
        }
        setTitle(R.string.login);
    }

    @Override
    public void setTitle(CharSequence paramCharSequence){
        super.setTitle(paramCharSequence);
        getSupportActionBar().setTitle(paramCharSequence);
    }


}
