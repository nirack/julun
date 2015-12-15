package com.julun.vehicle.activities.examples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.volley.VolleyError;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.datas.PageResult;
import com.julun.datas.beans.Product;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;
import com.julun.vehicle.listeners.examples.refresh.RefreshLoadListenerForTestActivity;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;
import com.julun.widgets.ui.refreshable.SimpleRefreshView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Integer currentPage = 0;

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
                currentPage = 1;
            }

            @Override
            public void loadMore() {
                Log.d(TAG, " onRefresh loadMore!() called with: " + "");
                int count = adapter.getCount();
                for (int index = 0; index < countEachTime; index++) {
//                    adapter.add("重置 --》 " + index + m ", 下标 : = " + (count + index));
                }

                Map<String, String> param = new HashMap<String, String>();
                currentPage++;

                param.put("pageNumber",""+currentPage);
                param.put("pageSize","10");


                String url = ApplicationUtils.BASE_URL_PREFIX +  "prod/query";
                VolleyRequestCallback<PageResult<Product>> callback = new VolleyRequestCallback<PageResult<Product>>(RefreshViewTest2Activity.this) {
                    @Override
                    public void doOnSuccess(PageResult<Product> response) {
                        List<Product> records = response.getRecords();
                        for(Product item : records){
                            adapter.add(item.getName());
                        }
                        adapter.notifyDataSetChanged();
                        simpleRefreshView.onLoadFinish();

                    }

                    /**
                     * 请求失败之后.
                     * 默认是tost一下.
                     *
                     * @param error
                     */
                    @Override
                    public void doOnFailure(VolleyError error) {
                        super.doOnFailure(error);
                    }
                };

                Requests.post(url,url,callback,param);

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
