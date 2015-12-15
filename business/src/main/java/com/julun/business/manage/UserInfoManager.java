package com.julun.business.manage;

import com.julun.business.bean.User;
import com.julun.utils.SharedPreferencesUtils;

/**
 * Created by danjp on 2015/12/15.
 */
public class UserInfoManager {

    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String USER_ID = "USER_ID";
    public static final String LOGIN_NAME = "LOGIN_NAME";
    public static final String USER_TYPE = "USER_TYPE";

    private static UserInfoManager userInfoManager;

    private UserInfoManager(){}

    public static UserInfoManager getInstance() {
        if(userInfoManager == null) {
            userInfoManager = new UserInfoManager();
        }
        return userInfoManager;
    }

    /**
     * 保存是否登陆
     * @param isLogin
     */
    public static void setIsLogin(boolean isLogin) {
        SharedPreferencesUtils.commitBoolean(IS_LOGIN, isLogin);
    }

    /**
     * 是否登陆
     * @return
     */
    public static boolean getIsLogin() {
        return SharedPreferencesUtils.getBoolean(IS_LOGIN, false);
    }

    /**
     * 获取保存的用户信息
     * @return
     */
    public static User getUserInfo() {
        User user = new User();
        user.setSession_id(SharedPreferencesUtils.getString(SESSION_ID, null));
        user.setUser_id(SharedPreferencesUtils.getString(USER_ID, null));
        return user;
    }

    /**
     * 清除用户信息
     */
    public static void clearUserInfo() {
        SharedPreferencesUtils.remove(SESSION_ID);
        SharedPreferencesUtils.remove(USER_ID);
    }

    /**
     * 保存用户信息
     * @param user
     */
    public static void saveUserInfo(User user) {
        SharedPreferencesUtils.commitString(SESSION_ID, user.getSession_id());
        SharedPreferencesUtils.commitString(USER_ID, user.getUser_id());
    }

}
