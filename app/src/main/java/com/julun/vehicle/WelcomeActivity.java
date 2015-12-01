package com.julun.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.utils.ApplicationUtils;
import com.julun.vehicle.activities.MainActivity;

@ContentLayout(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationUtils.reCreateShortCut(getString(R.string.app_name), R.mipmap.ic_launcher, WelcomeActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        }, 1500L);
    }

    //    @OnClick(R.id.go_home)
    public void goHome() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
        this.finish();
    }

}