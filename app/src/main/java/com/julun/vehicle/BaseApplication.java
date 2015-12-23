package com.julun.vehicle;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.julun.utils.ApplicationUtils;


public class BaseApplication extends Application {

    private LocationClient locationClient;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(BaseApplication.class.getName(), "应用启动！！！！！！！！！！！！！");
        ApplicationUtils.init(this);
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Log.d(BaseApplication.class.getName(), "onTerminate() called with: " + "");
        super.onTerminate();
    }

}