package com.julun.utils;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by danjp on 2015/12/14.
 * SharePreferences工具类
 */
public final class SharedPreferencesUtils {
    private static SharedPreferences preferences;

    private SharedPreferencesUtils(){}

    public static SharedPreferences getSharePreferences() {
        if(preferences == null) {
            synchronized (SharedPreferences.class) {
                if (preferences == null) {
                    preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationUtils.getGlobalApplication());
                }
            }
        }
        return preferences;
    }

    public static String getString(String key, String value) {
        return SharedPreferencesUtils.getSharePreferences().getString(key, value);
    }

    public static boolean getBoolean(String key, boolean value) {
        return SharedPreferencesUtils.getSharePreferences().getBoolean(key, value);
    }

    public static int getInt(String key, int value) {
        return SharedPreferencesUtils.getSharePreferences().getInt(key, value);
    }

    public static long getLong(String key, long value) {
        return SharedPreferencesUtils.getSharePreferences().getLong(key, value);
    }

    public static float getFloat(String key, float value) {
        return SharedPreferencesUtils.getSharePreferences().getFloat(key, value);
    }

    public static Set<String> getStringSet(String key, Set<String> value) {
        return SharedPreferencesUtils.getSharePreferences().getStringSet(key, value);
    }

    public static void commitString(String key, String value) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor().putString(key, value);
        commit(editor);
    }

    public static void commitInt(String key, int value) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor().putInt(key, value);
        commit(editor);
    }

    public static void commitFloat(String key, float value) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor().putFloat(key, value);
        commit(editor);
    }

    public static void commitLong(String key, long value) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor().putLong(key, value);
        commit(editor);
    }

    public static void commitBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor().putBoolean(key, value);
        commit(editor);
    }

    public static void commitStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getEditor().putStringSet(key, value);
        commit(editor);
    }

    public static void remove(String key) {
        SharedPreferencesUtils.getEditor().remove(key);
    }

    private static void commit(SharedPreferences.Editor editor) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }else {
            editor.commit();
        }
    }

    private static SharedPreferences.Editor getEditor() {
        return SharedPreferencesUtils.getSharePreferences().edit();
    }

}
