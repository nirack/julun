package com.julun.business;

import android.util.Log;

import com.android.volley.VolleyError;
import com.julun.datas.PageResult;
import com.julun.datas.beans.Product;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.JsonHelper;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015-11-24.
 */
public class TestService extends BusiBaseService {

    /**
     * 测试下复杂参数的传递,
     * 结果，OK.
     */
    public void test(){
        Map<String, String> param = new HashMap<>();
        param.put("id","1");
        param.put("title","测试标题");
        param.put("imageUrl","imageUrl-------------------");
        Product product = new Product();
        product.setLocation("location_________");
        product.setPrice("price_-~!@#");
        param.put("product", JsonHelper.toJson(product));


        String url = ApplicationUtils.BASE_URL_PREFIX + "test/test";
        VolleyRequestCallback<List<Object>> callback = new VolleyRequestCallback<List<Object>>(getContext()) {
            @Override
            public void doOnSuccess(List<Object> response) {
                Log.i("线程信息", "返回:" + Thread.currentThread().getId());
                if (null == response) {
                    return;
                }

                DataChangeEvent<List<Object>> event = new DataChangeEvent<List<Object>>(response);
//                dataLoadedAndTellUiThread(event);


            }

            @Override
            public void doOnFailure(VolleyError error) {
//                ToastHelper.showLong(context.getEventBus(), error.toString());
                DataChangeEvent<PageResult<Product>> event = new DataChangeEvent<PageResult<Product>>();
                event.setSuccess(false);
//                dataLoadedAndTellUiThread(event);
            }
        };
        Log.i("线程信息", "发送请求:" + Thread.currentThread().getId() + ", name " + Thread.currentThread().getName());
        Requests.post(url, url, callback, param);
    }

}
