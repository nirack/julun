package com.julun.vehicle.activities.examples;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.vehicle.listeners.examples.refresh.RefreshLoadListenerForTestActivity;
import com.julun.widgets.ui.refreshable.SimpleRefreshView;

import butterknife.Bind;
import butterknife.OnClick;

@ContentLayout(R.layout.activity_refresh_view_test)
public class RefreshViewTestActivity extends BaseActivity {

    @Bind(R.id.btn_reload)
    Button reloadBtn;
    @Bind(R.id.btn_load_more)
    Button loadMoreBtn;
    @Bind(R.id.btn_add_footer)
    Button addFooter;

    @Bind(R.id.simpleRefreshView)
    SimpleRefreshView simpleRefreshView;

    RefreshLoadListenerForTestActivity refreshLoadListener;

    int dataCount = 0;

    private static final int countEachTime = 2;
    private android.view.View footer;


    boolean footerIsVisiable = false;
    private RefreshTestAdapter adapter;

    @AfterInitView
    public void init() {

        footer = LayoutInflater.from(this).inflate(R.layout.refresh_view_footer,null);

        adapter = new RefreshTestAdapter(this);

        loadOriginalData();

        simpleRefreshView.setAdapter(adapter);
        simpleRefreshView.setRefresAndLoadListener(new SimpleRefreshView.OnRefresAndLoadListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.add("重置 --》 " + System.currentTimeMillis());
                adapter.add("重置 --》 " + System.currentTimeMillis() + 200);
//                adapter.notifyDataSetChanged();
                simpleRefreshView.onLoadFinish();
            }

            @Override
            public void loadMore() {
                int count = adapter.getCount();
                for (int index = 0; index < countEachTime; index++) {
                    adapter.add("重置 --》 " + index + ", 下标 : = " + (count + index));
                }
                simpleRefreshView.onLoadFinish();
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

    @OnClick({R.id.btn_reload, R.id.btn_load_more,R.id.btn_add_footer})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load_more:
                loadOriginalData();
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_reload:
                refresh();
                break;
            case R.id.btn_add_footer:
//                footer.setVisibility(footerIsVisiable ? View.INVISIBLE:View.VISIBLE);
                if(footerIsVisiable){
//                    listView.removeFooterView(footer);
                }else {
//                    listView.addFooterView(footer);
                }
//                listView.deferNotifyDataSetChanged();
                footerIsVisiable = !footerIsVisiable;

                break;
        }
    }

    private void refresh() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        loadOriginalData();
        adapter.notifyDataSetChanged();
    }


}
