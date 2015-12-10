package com.julun.vehicle.activities.examples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;
import com.julun.vehicle.listeners.examples.refresh.RefreshLoadListenerForTestActivity;
import com.julun.widgets.ui.refreshable.SimpleRefreshView;

import butterknife.Bind;

@ContentLayout(R.layout.activity_refresh_view_test2)
public class RefreshViewTest2Activity extends BaseActivity {

    @Bind(R.id.simpleRefreshView)
    SimpleRefreshView simpleRefreshView;

    RefreshLoadListenerForTestActivity refreshLoadListener;

    int dataCount = 0;

    private static final int countEachTime = 5;


    boolean footerIsVisiable = false;
    private RefreshTestAdapter adapter;

    private static final String TAG = RefreshViewTest2Activity.class.getName();

    @AfterInitView
    public void init() {
        adapter = new RefreshTestAdapter(this);
        loadOriginalData();
        simpleRefreshView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        simpleRefreshView.setRefresAndLoadListener(new SimpleRefreshView.OnRefresAndLoadListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onRefresh!  loadMore () called with: " + "");
                adapter.add("重置 --》 " + System.currentTimeMillis());
                adapter.add("重置 --》 " + System.currentTimeMillis() + 200);
                adapter.add("重置 --》 " + System.currentTimeMillis() + 200);
                adapter.add("重置 --》 " + System.currentTimeMillis() + 200);
                adapter.add("重置 --》 " + System.currentTimeMillis() + 200);
//                adapter.notifyDataSetChanged();
                simpleRefreshView.onLoadFinish();
            }

            @Override
            public void loadMore() {
                Log.d(TAG, " onRefresh loadMore!() called with: " + "");
                int count = adapter.getCount();
                for (int index = 0; index < countEachTime; index++) {
                    adapter.add("重置 --》 " + index + ", 下标 : = " + (count + index));
                }

                simpleRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simpleRefreshView.onLoadFinish();
                    }
                },2000L);

            }
        });



    }


    private void loadOriginalData() {
        for (int index = 0; index < countEachTime; index++) {
            String str = "生成字符串<" + (dataCount) + ">";
            dataCount++;
            adapter.add(str);
        }

    }


}
