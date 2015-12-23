package com.julun.container.uicontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julun.container.BaseContainerInitializer;
import com.julun.container.UIContainerEvnProvider;
import com.julun.event.events.BaseSimpleEvent;
import com.julun.event.events.FailureEvent;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 */
public class BaseFragment extends Fragment implements UIContainerEvnProvider {
    protected EventBus mainBus;
    protected static String LOG_TAG_CLASS_NAME;
    protected LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutInflater = inflater;
        LOG_TAG_CLASS_NAME = this.getClass().getName();
        return BaseContainerInitializer.initFragment(this, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainBus.unregister(this);
    }

    /**
     * 一些手工做的后台耗时操作执行完毕之后,可能需要更新UI的时候,会有一个是事件触发从而调用这个方法.
     * 如果需要,请覆盖这个方法.
     *
     * @param event
     * @param <T>
     * @see BaseSimpleEvent
     * @see com.julun.event.EventBusUtils
     */
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = false)
    public <T extends BaseSimpleEvent> void onEventMainThread(T event) {
        // TODO: 如果需要,请覆盖本方法
        Log.d(LOG_TAG_CLASS_NAME, "onEventMainThread() called with: " + "event = [" + event + "]");
    }

    /**
     * 跳转到activity
     *
     * @param next
     * @param extra 实际只取第一个
     */
    public void jump2Activity(Class<? extends Activity> next, Bundle ... extra){
        Activity activity = this.getActivity();
        Intent intent = new Intent(activity, next);
        if(extra!=null && extra.length > 0){
            intent.putExtras(extra[0]);
        }
        activity.startActivity(intent);
    }

    /**
     * 跳转到activity
     *
     * @param klass
     * @param requestCode
     * @param options
     */
    public void startActivityForResult(Class<? extends Activity> klass, int requestCode, Bundle... options) {
        Activity activity = this.getActivity();
        activity.startActivity(new Intent(activity, klass));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(new Intent(activity, klass), requestCode, options != null ? options[0] : null);
        } else {
            activity.startActivityForResult(new Intent(activity, klass), requestCode);
        }

    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public Context getContextActivity() {
        return this.getActivity();
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
    public Handler getHandler(){
        return null;
    }

    /**
     * 请求异常处理类 doOnFailure
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(FailureEvent event) {
        //请求失败，网络异常等等情况
        String errorInfo = event.getExtraMessage();
        new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("数据获取失败, 请稍后重试")
                .setPositiveButton("知道了", null)
                .create();
    }

}