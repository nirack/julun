package com.julun.volley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.julun.utils.JsonHelper;
import com.julun.utils.ToastHelper;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 *  网络请求的监听函数.
 * </p>
 * <span>
 * 配合 GenericTypedRequest 使用.
 * 请求网络的时候,通常只需呀实现这个类就欧了.
 * </span>
 *
 * <span>
 *     VolleyRequestCallback.doOnSuccess 方法一定要实现, 提供了默认的 VolleyRequestCallback.doOnFailure ,如果需要其他的处理错误的方式,覆盖这个方法即可.
 * </span>
 *
 * @see VolleyRequestCallback#doOnSuccess(Object)
 * @see VolleyRequestCallback#doOnFailure(VolleyError)
 * @see GenericTypedRequest
 * @param <T>
 */
public abstract class VolleyRequestCallback<T>{
    private final Type type;
    protected WeakReference<Context> context;
    protected Response.Listener<String> successListener;

    protected Response.ErrorListener errorListener;

    public VolleyRequestCallback(Context context) {
        this(new WeakReference<Context>(context));
    }

    public VolleyRequestCallback(WeakReference<Context> context) {
        this.type = getSuperclassTypeParameter(getClass());
        this.context = context;
        this.successListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                T response = JsonHelper.fromJson(json,type);
                doOnSuccess(response);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                doOnFailure(error);
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

    /**
     * 请求加载成功之后.
     * @param response
     */
    public abstract void doOnSuccess(T response ) ;

    /**
     * 请求失败之后.
     * 默认是tost一下.
     * @param error
     */
    public void doOnFailure(VolleyError error){
        ToastHelper.showLong(getContext().get(),error.toString());
    }

    public WeakReference<Context> getContext() {
        return context;
    }

    public Response.Listener<String> getSuccessListener() {
        return successListener;
    }

    public Response.ErrorListener getErrorListener() {
        return errorListener;
    }
}
