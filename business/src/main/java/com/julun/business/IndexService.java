package com.julun.business;

import android.content.Context;

import com.android.volley.VolleyError;
import com.julun.datas.beans.County;
import com.julun.event.events.DataChangeEvent;
import com.julun.event.events.FailureEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015-11-24.
 */
public class IndexService extends BusiBaseService {

    public void fetchCities() {
        String url = ApplicationUtils.BASE_URL_PREFIX + "index/addr";
        Map<String, String> map = new HashMap<String, String>();
        map.put("parent", new Integer(1).toString());

        Context cxt = context != null ? context.get():null;
        VolleyRequestCallback<List<County>> callback = new VolleyRequestCallback<List<County>>(cxt) {
            @Override
            public void doOnSuccess(List<County> response) {
                DataChangeEvent<List<County>> event = new DataChangeEvent<>(response);
                dataLoadedAndTellUiThread(event);
            }

            @Override
            public void doOnFailure(VolleyError error) {
                FailureEvent event = new FailureEvent(error.toString());
                dataLoadedAndTellUiThread(event);
            }
        };
        Requests.post(url,url,callback,map);

    }
}
