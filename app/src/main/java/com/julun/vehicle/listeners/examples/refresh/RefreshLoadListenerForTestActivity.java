package com.julun.vehicle.listeners.examples.refresh;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.ui.refreshable.ExtendedSwiperRefreshView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015-12-10.
 */
public class RefreshLoadListenerForTestActivity<T> implements ExtendedSwiperRefreshView.OnRefresAndLoadListener {
    private WeakReference<BaseListViewAdapter<T>> adapter;
    private WeakReference<ExtendedSwiperRefreshView> refreshLayout;


    public RefreshLoadListenerForTestActivity(BaseListViewAdapter<T> adapter, ExtendedSwiperRefreshView refresh_layout){
        this.adapter = new WeakReference<BaseListViewAdapter<T>>(adapter);
        refreshLayout = new WeakReference<ExtendedSwiperRefreshView>(refresh_layout);
    }

    @Override
    public void onRefresh() {
        adapter.get().clear();
        loadMore();
    }

    /**
     * 加载更多数据.
     * 更多更方便的方法后续提供,比如制定url自动加载和更新UI，以及其他 。。。 暂时先做这么多.
     * 需要的listView要么在这个方法里传入要么在构造函数里传入,哪个更好，看以后的使用情况再做进一步处理.
     *
     */
    @Override
    public void loadMore() {
        for (int index = 1; index <= 3; index++) {
            adapter.get().add("test(" +
                    (adapter.get().getCount() + index) +
                    ")");
        }

        adapter.get().notifyDataSetChanged();
        refreshLayout.get().loadFinish();
    }
}
