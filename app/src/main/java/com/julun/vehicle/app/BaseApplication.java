package com.julun.vehicle.app;

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
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(BaseApplication.class.getName(), "应用启动！！！！！！！！！！！！！");
        ApplicationUtils.init(this);
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

}