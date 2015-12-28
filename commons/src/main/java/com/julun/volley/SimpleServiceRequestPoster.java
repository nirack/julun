package com.julun.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.julun.business.BusiBaseService;
import com.julun.event.events.DataChangeEvent;
import com.julun.event.events.FailureEvent;
import com.julun.utils.ApplicationUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 *  更简洁的请求方式，在Services里请求之后，直接调用  dataLoadedAndTellUiThread 方法。
 */
public abstract class SimpleServiceRequestPoster<T> {
    private final Type type;
    WeakReference<Context> cxt;
    private VolleyRequestCallback<T> requestCallback;
    private BusiBaseService service;
    private String url;
    private Map<String,String> params;

    public SimpleServiceRequestPoster (Context context, String url, BusiBaseService service, Map<String, String> params) {
        type = getSuperclassTypeParameter(getClass());

        init(context,url, service, params);
    }

    private void init(Context context, String url, final BusiBaseService baseService, Map<String, String> params) {
        this.service = baseService;
        this.url = url;
        this.params = params;
        cxt = new WeakReference<Context>(context);

        requestCallback = new VolleyRequestCallback<T>(cxt,type) {
            @Override
            public void doOnSuccess(T response) {
                Log.i(SimpleServiceRequestPoster.class.getName(),response.getClass().getName());
                service.dataLoadedAndTellUiThread(new DataChangeEvent<T>(response));
            }

            @Override
            public void doOnFailure(VolleyError error) {
                service.dataLoadedAndTellUiThread (new FailureEvent (error.toString ()));
                Log.d(SimpleServiceRequestPoster.class.getName(), "doOnFailure() called with: " + "error = [" + error + "]");
            }
        };
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type actualType = parameterized.getActualTypeArguments()[0];//本类的定义限制了一定只有一个泛型
        return actualType;
    }

    public void post() {
        ApplicationUtils.getGlobalRequestQueue().cancelAll(url);
        GenericTypedRequest request = new GenericTypedRequest(url, this.requestCallback, this.params);
        request.setTag(url);
        ApplicationUtils.getGlobalRequestQueue().add(request);
        ApplicationUtils.getGlobalRequestQueue().start();
    }
}
