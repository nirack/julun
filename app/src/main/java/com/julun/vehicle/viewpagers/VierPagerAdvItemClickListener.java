package com.julun.vehicle.viewpagers;

import android.util.Log;

import com.julun.datas.beans.Adv;
import com.julun.widgets.viewpager.SimpleLoopViewPager;


/**
 * Created by Administrator on 2015-11-08.
 */
public class VierPagerAdvItemClickListener implements SimpleLoopViewPager.ItemClickListener<Adv> {
    /**
     * @param data 与当前view绑定的数据对象.
     */
    @Override
    public void onViewPagerItemClick(Adv data) {
        Log.d(VierPagerAdvItemClickListener.class.getName(), "onViewPagerItemClick() called with: " + "data = [" + data + "]");
    }
}
