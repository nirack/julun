package com.julun.business.service;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.julun.business.BusiBaseService;
import com.julun.event.events.DataChangeEvent;
import com.julun.event.events.FailureEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danjp on 2015/12/9.
 */
public class LoginService extends BusiBaseService {

    /**
     * 请求后台登陆
     * @param userName  用户名
     * @param password  密码
     * @param captcha   验证码
     */
    public void login(String userName, String password, String captcha) {
        String url = ApplicationUtils.BASE_URL_PREFIX + "login";
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", userName);
        map.put("password", password);
        map.put("captcha", captcha);

        Context cxt = context != null ? context.get() : null;
        VolleyRequestCallback<String> callback = new VolleyRequestCallback<String>(cxt) {
            @Override
            public void doOnSuccess(String response) {
                DataChangeEvent<String> event = new DataChangeEvent<String>(response);
                dataLoadedAndTellUiThread(event);
//                UserInfoManager.saveUserInfo();
            }

            @Override
            public void doOnFailure(VolleyError error) {
                FailureEvent event = new FailureEvent(error.getMessage());
                dataLoadedAndTellUiThread(event);
            }
        };
        Requests.post(url, null, callback, map);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataLoadedAndTellUiThread(new DataChangeEvent<String>(""));
            }
        }, 1500);
    }

    /**
     * 请求新验证码
     */
    public void changeCaptcha() {
        String url = ApplicationUtils.BASE_URL_PREFIX + "changeCaptcha";
        Map<String, String> map = new HashMap<String, String>();

        Context cxt = context != null ? context.get() : null;
        VolleyRequestCallback<byte[]> callback = new VolleyRequestCallback<byte[]>(cxt) {
            @Override
            public void doOnSuccess(byte[] response) {
                DataChangeEvent<byte[]> event = new DataChangeEvent<byte[]>(response);
                dataLoadedAndTellUiThread(event);
            }

            @Override
            public void doOnFailure(VolleyError error) {
                FailureEvent event = new FailureEvent(error.toString());
                dataLoadedAndTellUiThread(event);
            }
        };
        Requests.postByte(url, null, callback, map);
    }

    /**
     * 根据用户ID检测用户登陆是否过期
     * @param userId
     * @return
     */
    public void checkLogin(String userId) {
        String url = ApplicationUtils.BASE_URL_PREFIX + "check_login";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);

        VolleyRequestCallback<String> callback = new VolleyRequestCallback<String>(getContext()) {
            @Override
            public void doOnSuccess(String response) {
                DataChangeEvent<String> event = new DataChangeEvent<String>(response);
                event.setCode(1);
                dataLoadedAndTellUiThread(event);
            }

            @Override
            public void doOnFailure(VolleyError error) {
                super.doOnFailure(error);
            }
        };
    }

    //第三方授权登陆
    public void authLogin() {

    }

}
