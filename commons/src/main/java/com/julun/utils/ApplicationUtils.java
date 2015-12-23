package com.julun.utils;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.telephony.TelephonyManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.julun.commons.R;
import com.julun.commons.images.BitMapCache;
import com.julun.constants.PreferencesConstans;
import com.julun.constants.SystemConstants;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 跟App相关的辅助类
 */
public class ApplicationUtils {
    //外部存储的跟路径
    public static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getPath();
    public static String APP_BASE_EXTERNAL_STORAGE_PATH ;

    //全局的  volley 请求队列
    public static RequestQueue requestQueue;
    public static ImageLoader imageLoader;
    private static String deviceId;
/*

    private static final String ipAddress = "120.26.67.181";
    private static final String webAppName = "vehicle";
    private static final String portNumber = "8088";
*/

//    private static final String ipAddress = "192.168.1.128";
//        private static final String ipAddress = "192.168.2.105";

    private static final String ipAddress = "192.168.1.159";
//    private static final String ipAddress = "192.168.0.101";
//    private static final String webAppName = "sales";
    private static final String webAppName = "";
    private static final String portNumber = "8080";


    public static final String BASE_URL_PREFIX = "http://" + ipAddress + ":" + portNumber + "/" + webAppName + "/";

    private static WeakReference<Application> application;

    private static boolean hasCamera = true;

    private ApplicationUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是否已添加快捷方式：
     * 暂时没有方法能够准确的判断到快捷方式，原因是，
     * 1、不同厂商的机型他的快捷方式uri不同，我遇到过HTC的他的URI是content://com.htc.launcher.settings/favorites?notify=true
     * 2、桌面不只是android自带的，可能是第三方的桌面，他们的快捷方式uri都不同
     * <p/>
     * 提供一个解决办法，创建快捷方式的时候保存到preference，或者建个文件在SD卡上，下次加载的时候判断不存在就先发删除广播，再重新创建
     * <p/>
     * 添加权限:<uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" ></uses-permission>
     */
    public static boolean hasInstallShortcut(Context context) {
        boolean hasInstall = false;

        String AUTHORITY = "com.android.launcher.settings";
        int systemversion = Build.VERSION.SDK_INT;
         /*大于8的时候在com.android.launcher2.settings 里查询（未测试）*/
        if (systemversion >= 8) {
            AUTHORITY = "com.android.launcher2.settings";
        }
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                new String[]{"title"}, "title=?",
                new String[]{context.getString(R.string.app_name)}, null);

        if (cursor != null && cursor.getCount() > 0) {
            hasInstall = true;
        }

        return hasInstall;
    }

    /**
     * 删除快捷方式
     */
    public static void deleteShortCut(String appName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        /**删除和创建需要对应才能找到快捷方式并成功删除**/
        Intent intent = new Intent();
        intent.setClass(application.get(), application.get().getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//        context.sendBroadcast(shortcut);
    }

    /**
     * 创建快捷方式.
     *
     * @param shuortcutName
     * @param launcherResId
     */
    public static void createShorts(String shuortcutName, int launcherResId, Class cls) {
        //创建快捷方式的Intent
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shuortcutName);
        //快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(application.get(), launcherResId);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        Intent intent = new Intent(application.get(), cls);
        //下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        //点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        //发送广播。OK
        application.get().sendBroadcast(shortcutIntent);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageName(){
        return application.get().getPackageName();
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isNewVersion() {
        Float oldversion = null;
        try {
            String versionNumber = SharedPreferencesUtils.getSharePreferences().getString(PreferencesConstans.VERSION_NUMBER, "0");
            oldversion = Float.parseFloat(versionNumber);
        } catch (Exception e) {
            return true;
        }
        try {
            String ver = getAppVersionName(application.get());
            Float newVewsion = Float.parseFloat(ver);
            return newVewsion > oldversion;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 判断系统是否有相机.
     * @return
     */
    public static boolean hasCamera(){
        return hasCamera;
    }

    /**
     * 创建快捷方式.
     * @param appName
     * @param ic_launcher
     */
    public static void reCreateShortCut(String appName, int ic_launcher, Class cls) {
        if(isNewVersion()){
            ApplicationUtils.deleteShortCut(appName);
            createShorts(appName, ic_launcher, cls);
            SharedPreferencesUtils.commitString(PreferencesConstans.VERSION_NUMBER,
                    getAppVersionName(application.get()));
        }

    }

    public static void init(Application app) {
        application = new WeakReference<Application>(app);
        TelephonyManager tm = (TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE);
        //获取本机ID
        deviceId = tm.getDeviceId();
        //初始化Volley的请求队列.
        requestQueue = Volley.newRequestQueue(app);
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
        //是否有相机
        hasCamera = app.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        APP_BASE_EXTERNAL_STORAGE_PATH = EXTERNAL_STORAGE_PATH + File.separator + application.get().getPackageName() + File.separator;
        File base = new File(APP_BASE_EXTERNAL_STORAGE_PATH);
        if(base.exists() && !base.isDirectory()){
            base.delete();
        }
        if(!base.exists()){
            base.mkdir();
        }
    }

    public static ImageLoader getGlobalImageLoader() {
        return imageLoader;
    }

    public static RequestQueue getGlobalRequestQueue() {
        return requestQueue;
    }

    public static Application getGlobalApplication() {
        return application.get();
    }
}
