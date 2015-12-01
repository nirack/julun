package com.julun.utils.sp;

import android.content.SharedPreferences;

/**
 * Created by danjp on 2015/12/1.
 */
public class SettintUtil {

    private static SharedPreferences sp = null;
    private static SharedPreferences.Editor editor = null;

    private static final String VERSION_NUMBER = "version_number";

    public static SharedPreferences getSp() {
        if(sp == null) {
            sp = SettingProperties.getInstance();
        }
        return sp;
    }

    public static SharedPreferences.Editor getEditor() {
        if(editor == null) {
            editor = getSp().edit();
        }
        return editor;
    }

    public static void removeKey(String key) {
        getEditor().remove(key).commit();
    }

    public static void saveVersionNumber(String versionNumber) {
        getEditor().putString(VERSION_NUMBER, versionNumber).commit();
    }

    public static String getVersionNumber() {
       return getSp().getString(VERSION_NUMBER, "-1");
    }

}
