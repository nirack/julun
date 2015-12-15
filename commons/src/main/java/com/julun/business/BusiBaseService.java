package com.julun.business;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.julun.datas.PageResult;
import com.julun.datas.beans.Product;
import com.julun.event.events.BaseSimpleEvent;
import com.julun.event.events.DataChangeEvent;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;

import java.lang.ref.WeakReference;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 */
public abstract class BusiBaseService implements BusiBaseServiceInterface{
    protected WeakReference<Context> context;
    /**
     * 用来与UI线程交互数据的,不要自己设置这个属性.
     */
    protected EventBus mainEventBus4Post;

    public void setMainEventBus4Post(EventBus bus){
        this.mainEventBus4Post = bus;
    }

    protected Context getContext() {
        return context != null ? context.get() : null;
    }

    /**
     * 数据操作执行完毕之后,通知UI,可以更新了
     * @param event
     * @param <T>
     */
    protected <T extends BaseSimpleEvent> void dataLoadedAndTellUiThread(T event){
        Log.i(getClass().getName(), "dataLoadedAndTellUiThread() called with: " + "event = [" + event + "]");
        mainEventBus4Post.post(event);
    }

}