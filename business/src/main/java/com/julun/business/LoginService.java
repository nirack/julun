package com.julun.business;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danjp on 2015/12/9.
 */
public class LoginService extends BusiBaseService{

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
            }

            @Override
            public void doOnFailure(VolleyError error) {
                DataChangeEvent<String> event = new DataChangeEvent<String>(null);
                event.setSuccess(false);
                event.setExtraMessage(error.toString());
                dataLoadedAndTellUiThread(event);
            }
        };
        Requests.post(url, null, callback, map);
    }

    /**
     * 请求新验证码
     */
    public void changeCaptcha() {
        String url = ApplicationUtils.BASE_URL_PREFIX + "changeCaptcha";
        Map<String, String> map = new HashMap<String, String>();

        Context cxt = context != null ? context.get() : null;
        VolleyRequestCallback<String> callback = new VolleyRequestCallback<String>(cxt) {
            @Override
            public void doOnSuccess(String response) {
                DataChangeEvent<String> event = new DataChangeEvent<String>(response);
                event.setCode(1);
                dataLoadedAndTellUiThread(event);
            }

            @Override
            public void doOnFailure(VolleyError error) {
                DataChangeEvent<String> event = new DataChangeEvent<String>(null);
                event.setSuccess(false);
                event.setCode(1);
                event.setExtraMessage(error.toString());
                dataLoadedAndTellUiThread(event);
            }
        };
        Requests.post(url, null, callback, map);
    }

}
