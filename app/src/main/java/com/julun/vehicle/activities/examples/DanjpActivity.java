package com.julun.vehicle.activities.examples;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.JsonHelper;
import com.julun.vehicle.R;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.ui.refreshable.CustomRefreshLayout;
import com.julun.widgets.viewholder.listview.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by danjp on 2015/12/18.
 */
@ContentLayout(R.layout.activity_danjp)
public class DanjpActivity extends BaseActivity {

    private static final String TAG = DanjpActivity.class.getSimpleName();

    @Bind(R.id.listview)
    ListView mListview;
    @Bind(R.id.swipe_layout)
    CustomRefreshLayout mSwipeLayout;
    @Bind(R.id.textview)
    TextView mTextView;

    private MyAdapter myAdapter;
    private RefreshHandler mRefreshHandler;
    private LoadHandler mLoadHandler;

    private static final String URL = "http://120.26.67.181:8088/icar/data/myCart.jsp";

    @AfterInitView
    public void initView() {

//        mRefreshHandler = new RefreshHandler();
//        mLoadHandler = new LoadHandler();

        myAdapter = new MyAdapter(this, R.layout.example_danjp_listview_item);
        mListview.setAdapter(myAdapter);
        mSwipeLayout.addListView(mListview);

        //下拉刷新
        View footerView = getLayoutInflater().inflate(R.layout.activity_danjp_footer, null);
//        mSwipeLayout.setFooterViewLayout(R.layout.activity_danjp_footer);
        mSwipeLayout.addFooterView(footerView);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                /*new Thread() {
                    @Override
                    public void run() {
                        httpLoadData(mRefreshHandler);
                    }
                }.start();*/
                requestHttpData(1);
                mSwipeLayout.setRefreshing(false);
            }
        });

        //上拉加载
        mSwipeLayout.setOnLoadListener(new CustomRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Log.d(TAG, "onLoad");
                /*new Thread() {
                    @Override
                    public void run() {
                        httpLoadData(mLoadHandler);
                    }
                }.start();*/

                requestHttpData(2);
                mSwipeLayout.setLoading(false);
            }
        });

        requestHttpData(2);

//        new Thread() {
//            @Override
//            public void run() {
//                httpLoadData(mRefreshHandler);
//            }
//        }.start();
    }

    private void requestHttpData(final int code) {
        VolleyRequestCallback<ArrayList<Prod>> callback = new VolleyRequestCallback<ArrayList<Prod>>(DanjpActivity.this) {
            @Override
            public void doOnSuccess(ArrayList<Prod> response) {
                Log.d(TAG, "doOnSuccess: "+response);
//                ArrayList<Prod> prodList = toProd(response);
                DataChangeEvent<ArrayList<Prod>> event = new DataChangeEvent<ArrayList<Prod>>(response);
                event.setCode(code);
                getMainEventBus().post(event);
            }

            @Override
            public void doOnFailure(VolleyError error) {
                Log.d(TAG, "fail info : "+JsonHelper.toJson(error));
                getMainEventBus().post("1");
            }
        };
        Requests.post(URL, URL, callback);
    }

    private ArrayList<Prod> toProd(String json) {
        Log.d(TAG,"JSON : "+json);
        ArrayList<Prod> prodList = new ArrayList<Prod>();
        try{
            JSONArray arr = new JSONArray(json);
            Prod prod = null;
            for(int i=0,length=arr.length();i<length;i++){
                JSONObject obj = arr.getJSONObject(i);
                prod = new Prod();
                prod.prodId = obj.getString("prodId");

                /*if(handler instanceof RefreshHandler)
                    prod.prodName = obj.getString("prodName")+" 刷新"+(a++);
                else if(handler instanceof LoadHandler)
                    prod.prodName = obj.getString("prodName")+" 加载"+(a++);*/
                prod.prodName = obj.getString("prodName");
                prod.prodImageUrl = obj.getString("prodImageUrl");
                prod.marketPrice = obj.getDouble("marketPrice");
                prod.buyPrice = obj.getDouble("buyPrice");
                prod.buyCount = obj.getInt("buyCount");
                prod.selectedDesc = obj.getString("selectedDesc");
                prodList.add(prod);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return prodList;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(DataChangeEvent<ArrayList<Prod>> event) {
        Log.d(TAG, "已获取到数据");
        int code = event.getCode();
        ArrayList<Prod> prodList = event.getData();
        if(prodList == null || prodList.size() == 0){
            mTextView.setText("没有数据");
            return;
        }
        if(code == 1){           //下拉刷新
            insertData(event.getData());
        }else if(code == 2) {    //上拉加载
            appendData(event.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(String str) {
        Log.d(TAG, "网络异常");
        if(str.equals("1")){
            mTextView.setText("网络异常");
        }
    }

    int a = 1;
    int b = 1;
    private void httpLoadData(Handler handler) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://120.26.67.181:8088/icar/data/myCart.jsp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6 * 1000);
            conn.connect();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
            Log.d(TAG, sb.toString());
//                    Prod prod = JsonHelper.fromJson(sb.toString(), Prod.class);

            JSONArray arr = new JSONArray(sb.toString());
            Prod prod = null;
            ArrayList<Prod> prodList = new ArrayList<Prod>();
            for(int i=0,length=arr.length();i<length;i++){
                JSONObject obj = arr.getJSONObject(i);
                prod = new Prod();
                prod.prodId = obj.getString("prodId");

                if(handler instanceof RefreshHandler)
                    prod.prodName = obj.getString("prodName")+" 刷新"+(a++);
                else if(handler instanceof LoadHandler)
                    prod.prodName = obj.getString("prodName")+" 加载"+(a++);

                prod.prodImageUrl = obj.getString("prodImageUrl");
                prod.marketPrice = obj.getDouble("marketPrice");
                prod.buyPrice = obj.getDouble("buyPrice");
                prod.buyCount = obj.getInt("buyCount");
                prod.selectedDesc = obj.getString("selectedDesc");
                prodList.add(prod);
            }

            Log.d(TAG, "prod size : "+prodList.size());
            Message message = handler.obtainMessage();
            message.obj = prodList;
            handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void insertData(ArrayList<Prod> prodList) {
        Log.d(TAG, "insert product info");
        if(prodList != null && prodList.size() > 0) {
            for(int i=0,size=prodList.size();i<size;i++){
                prodList.get(i).prodName += " header"+a;
                myAdapter.insert(prodList.get(i), 0);
                a += 1;
            }
            Log.d(TAG, "insert product info!!!!");
            myAdapter.notifyDataSetChanged();
//            mSwipeLayout.setRefreshing(false);
        }
    }

    private void appendData(ArrayList<Prod> prodList){
        for(int i=0,size=prodList.size();i<size;i++) {
            prodList.get(i).prodName += " bottom" + b;
            b += 1;
        }
        myAdapter.addAll(prodList);
        myAdapter.notifyDataSetChanged();
//        mSwipeLayout.setLoading(false);
    }

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "prod size11 : "+msg.obj);
            ArrayList<Prod> prodList = (ArrayList<Prod>)msg.obj;
            Log.d(TAG, "prod size222 : "+prodList.size());
            insertData(prodList);
        }
    }

    class LoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<Prod> prodList = (ArrayList<Prod>)msg.obj;
            appendData(prodList);
        }
    }

    class Prod {
        String prodId;
        String prodName;
        String prodImageUrl;
        double marketPrice;
        double buyPrice;
        int buyCount;
        String selectedDesc;
        Prod(){}

        @Override
        public String toString() {
            return "ProdId="+prodId+"  ProdName="+prodName;
        }
    }

    class MyAdapter extends BaseListViewAdapter<Prod> {

        public MyAdapter(Context context, int itemResId) {
            super(context, itemResId);
        }

        @Override
        public void convert(ViewHolder vh, Prod prod) {
            //直接加载图片，肯定错误
//            vh.setImageUrl(R.id.prod_image, prod.prodImageUrl);
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Requests.loadImage(((ImageView) vh.getView(R.id.prod_image)), prod.prodImageUrl);
                }
            }, 2000);*/
            Requests.loadImage(((ImageView) vh.getView(R.id.prod_image)), prod.prodImageUrl);
            vh.setTextViewText(R.id.prod_name, prod.prodName);
            vh.setTextViewText(R.id.prod_price, "¥" + prod.buyPrice);

            TextView tv = vh.getView(R.id.prod_price);
            tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            vh.setTextViewText(R.id.market_price, "¥"+prod.marketPrice+"");
            vh.setTextViewText(R.id.buy_count, prod.buyCount+"人购买");
            vh.setTextViewText(R.id.pingjia, "5人评价");
        }
    }
}
