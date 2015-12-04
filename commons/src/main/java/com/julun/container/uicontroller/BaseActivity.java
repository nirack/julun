package com.julun.container.uicontroller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.julun.container.BaseContainerInitializer;
import com.julun.container.UIContainerEvnProvider;
import com.julun.event.events.BaseSimpleEvent;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * activity 基类,提供默认的onCreate 和 onDestory 方法,如果覆盖,请一定先调用 super.xxx
 */
public class BaseActivity extends AppCompatActivity implements UIContainerEvnProvider {
    protected EventBus mainBus;
    protected static String LOG_TAG_CLASS_NAME;
    protected Handler handler;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_TAG_CLASS_NAME = this.getClass().getName();
        BaseContainerInitializer.initActivity(this, savedInstanceState);
    }

    /**
     *
     * @param next
     * @param extra 实际只取第一个
     */
    public void jumpActivity(Class<? extends FragmentActivity> next, Bundle ... extra){
        Intent intent = new Intent(this, next);
        if(extra!=null && extra.length > 0){
            intent.putExtras(extra[0]);
        }
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(this.getClass().getName(), "onRestart() called with: " + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    public <T extends View> T viewById(int id) {
        T viewById = (T) super.findViewById(id);
        return viewById;
    }

    /**
     * 一些手工做的后台耗时操作执行完毕之后,可能需要更新UI的时候,会有一个是事件触发从而调用这个方法.
     * 如果需要,请覆盖这个方法.
     * @see BaseSimpleEvent
     * @see com.julun.event.EventBusUtils
     * @param event
     * @param <T>
     */
    @Subscribe(threadMode= ThreadMode.MainThread,sticky = false)
    public <T extends BaseSimpleEvent> void onEventMainThread(T event ){
        // TODO: 如果需要,请覆盖本方法
        Log.d(LOG_TAG_CLASS_NAME, "onEventMainThread() called with: " + "event = [" + event + "]" + "  ，Thread ：   " + Thread.currentThread().getName());
    }

    @Override
    public Context getContextActivity() {
        return this;
    }

    /**
     * @param bus
     */
    @Override
    public void setMainEventBus(EventBus bus) {
        mainBus = bus;
    }
    @Override
    public EventBus getMainEventBus() {
        return mainBus;
    }

    @Override
    public Handler getHandler() {
        return this.handler;
    }
}
