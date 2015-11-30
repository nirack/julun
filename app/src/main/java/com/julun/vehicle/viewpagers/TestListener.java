package com.julun.vehicle.viewpagers;

import android.util.Log;

import com.julun.widgets.viewpager.LoopViewPager;


/**
 * Created by Administrator on 2015-11-08.
 */
public class TestListener implements LoopViewPager.ItemClickListener {
    /**
     * @param data 与当前view绑定的数据对象.
     */
    @Override
    public void doOnClick(Object data) {
        Log.d(TestListener.class.getName(), "doOnClick() called with: " + "data = [" + data + "]");
    }
}
