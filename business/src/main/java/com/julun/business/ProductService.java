package com.julun.business;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.julun.datas.PageResult;
import com.julun.datas.beans.Product;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 产品的业务类.
 */
public class ProductService extends BusiBaseService {

    public void listProduct() {

        Map<String, String> param = new HashMap<>();

        String url = ApplicationUtils.BASE_URL_PREFIX + "prod/query";
        VolleyRequestCallback<PageResult<Product>> callback = new VolleyRequestCallback<PageResult<Product>>(getContext()) {
            @Override
            public void doOnSuccess(PageResult<Product> response) {
                Log.i("线程信息", "返回:" + Thread.currentThread().getId());
                if (null == response) {
                    return;
                }

                DataChangeEvent<PageResult<Product>> event = new DataChangeEvent<PageResult<Product>>(response);
                dataLoadedAndTellUiThread(event);


            }

            @Override
            public void doOnFailure(VolleyError error) {
//                ToastHelper.showLong(context.getEventBus(), error.toString());
                DataChangeEvent<PageResult<Product>> event = new DataChangeEvent<PageResult<Product>>();
                event.setSuccess(false);
                dataLoadedAndTellUiThread(event);
            }
        };
        Log.i("线程信息", "发送请求:" + Thread.currentThread().getId() + ", name " + Thread.currentThread().getName());
        Requests.post(url, url, callback, param);
    }

    public ProductService() {
        super();
    }

    public ProductService(Context context) {
        this.context = new WeakReference<Context>(context);
    }
}
