package com.julun.vehicle.activities.examples;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import butterknife.Bind;
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

    private static final String URL = "http://120.26.67.181:8088/icar/data/myCart.jsp";

    @AfterInitView
    public void initView() {


        mSwipeLayout.addListView(mListview);

        myAdapter = new MyAdapter(this, R.layout.example_danjp_listview_item);
        mListview.setAdapter(myAdapter);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                requestHttpData(1);
            }
        });

        //上拉加载
        mSwipeLayout.setOnLoadListener(new CustomRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Log.d(TAG, "onLoad");
                requestHttpData(2);
            }
        });

        requestHttpData(1);

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
//        if(str.equals("1")){
//            mTextView.setText("网络异常");
//        }
        mSwipeLayout.setRefreshing(false);
        mSwipeLayout.setLoading(false);
    }

    int a = 1;
    int b = 1;
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
            mSwipeLayout.setRefreshing(false);
        }
    }

    private void appendData(ArrayList<Prod> prodList){
        int size = prodList == null ? 0 : prodList.size();
        Log.d(TAG, "appendData size : "+size);
        for(int i=0;i<size;i++) {
            prodList.get(i).prodName += " bottom" + b;
            b += 1;
        }
        myAdapter.addAll(prodList);
        myAdapter.notifyDataSetChanged();
        mSwipeLayout.setLoading(false);
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
        public void convert(final ViewHolder vh, final Prod prod) {
            //直接加载图片，肯定错误
//            vh.setImageUrl(R.id.prod_image, prod.prodImageUrl);
            Log.d(TAG, "convert image url : "+prod.prodName);
            vh.setImageResourceId(R.id.prod_image, com.julun.commons.R.drawable.no_image_foundpng);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, " handler run image url");
                    Requests.loadImage(((ImageView) vh.getView(R.id.prod_image)), prod.prodImageUrl);
                }
            }, 3000);
//            Requests.loadImage(((ImageView) vh.getView(R.id.prod_image)), prod.prodImageUrl);
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
