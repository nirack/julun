package com.julun.vehicle.activity.examples;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;

/**
 * Created by wqy on 2015/12/14.
 */
@ContentLayout(R.layout.example_recycle_view)
public class BuyRecyclerExampleActivity extends BaseActivity {
    @Bind(R.id.my_recycler)
    RecyclerView listView;
    @Bind(R.id.change_recycler_view_button)
    Button button;
    
//    private List<TestBean> datas = new ArrayList<>();
//    private BaseRecyclerViewAdapter<TestBean> adapter;
    @AfterInitView
    private void initDatas(){
        new Thread(runnable).start();
        
//        LinearLayoutManager list = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        int spanCount = 2;
        RecyclerView.LayoutManager list = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
        
        listView.setLayoutManager(list);
        /*
        adapter =new BaseRecyclerViewAdapter<TestBean>(this,R.layout.example_buy_item){
            @Override
            protected void renderView(ViewHolder vh, TestBean adv) {
                vh.setTextViewText(R.id.car_name, adv.getTitle());
                vh.setTextViewText(R.id.car_info, adv.getInfo());
                vh.setTextViewText(R.id.market_price, Integer.toString(adv.getMarketPrice()));
                vh.setTextViewText(R.id.buy_price, Integer.toString(adv.getBuyPrice()));
                vh.setImageUrl(R.id.car_image, adv.getImageUrl());
//                Bitmap bitmap = downloadImage(vh,adv.getImageUrl(), new onImageLoaderListener() {
//                    @Override
//                    public void onImageLoader(com.julun.widgets.viewholder.listview.ViewHolder vh,Bitmap bitmap) {
//                        vh.setImageBitMap(R.id.car_image, bitmap);
//                    }
//                });
            }
        };
        listView.setAdapter(adapter);
*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(BuyListViewExampleActivity.class);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 3:
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("http://www.baidu.com/"));
//                        startActivity(intent);
//                    default:
//                        Toast.makeText(BuyRecyclerExampleActivity.this, "cs------" + position + "--" + id, Toast.LENGTH_SHORT).show();
//                }
////                new Btn1Listener();
//            }
//        });
    }



    Handler handler = new Handler(){/*
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
//            Log.i("CS:----","请求结果:" + val);

            JSONArray myJsonArray;
            try
            {
                myJsonArray = new JSONArray(val);
                for(int i=0 ; i < myJsonArray.length() ;i++)
                {
                    //获取每一个JsonObject对象
                    JSONObject myjObject = myJsonArray.getJSONObject(i);

                    //获取每一个对象中的值
                    String prodImageUrl = myjObject.getString("prodImageUrl");
                    String selectedDesc = myjObject.getString("selectedDesc");
                    String name = myjObject.getString("prodName");
                    int prodId = myjObject.getInt("prodId");
                    int marketPrice = myjObject.getInt("marketPrice");
                    int buyPrice = myjObject.getInt("buyPrice");
                    TestBean bean = new TestBean();
                    bean.setImageUrl(prodImageUrl);
                    bean.setInfo(selectedDesc);
                    bean.setId(prodId);
                    bean.setTitle(name);
                    bean.setBuyPrice(buyPrice);
                    bean.setMarketPrice(marketPrice);

//                    Bitmap bitmap = downloadImage(bean.getImageUrl());
                    datas.add(bean);
                }
               
                for (TestBean adv : datas) {
                    adapter.insertData(0,adv);
                    adapter.notifyItemChanged(0);
                }

            }
            catch (JSONException e)
            {
            }
        }*/
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            InputStream input = null;
            OutputStream output = null;
            StringBuilder resultData = new StringBuilder("");
            HttpURLConnection connection = null;
            try {
                java.net.CookieManager manager = new java.net.CookieManager();
                manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                CookieHandler.setDefault(manager);

                URL url = new URL("http://120.26.67.181:8088/icar/data/myCart.jsp");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer  
                //getInputStream是真正去连接网络获取数据  
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());

                //使用缓冲一行行的读入，加速InputStreamReader的速度  
                BufferedReader buffer = new BufferedReader(isr);
                String inputLine = null;

                while((inputLine = buffer.readLine()) != null){
                    resultData.append(inputLine);
                    resultData.append("\n");
                }

                buffer.close();
                isr.close();
                connection.disconnect();


                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", resultData.toString());
                msg.setData(data);
                handler.sendMessage(msg);

            }catch(Exception e){
                e.printStackTrace();
            }


        }
    };

}
