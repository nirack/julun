package com.julun.vehicle.activity.examples;


import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
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

/**
 * Created by danjp on 2015/12/17.
 */
@ContentLayout(R.layout.example_danjp_listview)
public class DanjpRemoteListViewExampleActivity extends BaseActivity {

    @Bind(R.id.listview)
    ListView listView;
    private MyAdapter myAdapter = null;
    private ArrayList<Prod> items = new ArrayList<>();


    @AfterInitView
    public void vewiInit() {
        myAdapter = new MyAdapter(this, R.layout.example_danjp_listview_item);
        myAdapter.addAll(items);
        listView.setAdapter(myAdapter);

        final MyHandler myHandler = new MyHandler();
        new Thread() {
            public void run() {
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
                    Log.d("DANJP", sb.toString());
//                    Prod prod = JsonHelper.fromJson(sb.toString(), Prod.class);

                    JSONArray arr = new JSONArray(sb.toString());
                    Prod prod = null;
                    for(int i=0,length=arr.length();i<length;i++){
                        JSONObject obj = arr.getJSONObject(i);
                        prod = new Prod();
                        prod.prodId = obj.getString("prodId");
                        prod.prodName = obj.getString("prodName");
                        prod.prodImageUrl = obj.getString("prodImageUrl");
                        prod.marketPrice = obj.getDouble("marketPrice");
                        prod.buyPrice = obj.getDouble("buyPrice");
                        prod.buyCount = obj.getInt("buyCount");
                        prod.selectedDesc = obj.getString("selectedDesc");
                        items.add(prod);
                    }

                    Message message = myHandler.obtainMessage();
                    myHandler.sendEmptyMessage(1);

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
        }.start();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                myAdapter.addAll(items);
                myAdapter.notifyDataSetChanged();
            }
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
            vh.setImageUrl(R.id.prod_image, prod.prodImageUrl);
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
