package com.julun.vehicle.activities.examples;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * Created by wqy on 2015/12/4.
 */
@ContentLayout(R.layout.example_buy_list_view)
public class BuyListViewExampleActivity extends BaseActivity{
//    private List<TestBean> datas = new ArrayList<>();
    
    @Bind(R.id.car_list_view)
    ListView listView;
//    private BaseListViewAdapter<TestBean> adapter;
    @Bind(R.id.change_list_view_button)
    Button button;

    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 下载Image的线程池 
     */
    private ExecutorService mImageThreadPool = null;

    private static final int MSG_UPDATE_TEXT = 1;
    private String mStrContent = null;
    MainHandler mMainHanlder = null;
    TextView mTv1 = null;
    Button mBtn1 = null;
    
    @AfterInitView
    private void initData(){
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }



        // 获取虚拟机可用内存（内存占用超过该值的时候，将报OOM异常导致程序崩溃）。最后除以1024是为了以kb为单位
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // 使用可用内存的1/8来作为Memory Cache
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写sizeOf()方法，使用Bitmap占用内存的kb数作为LruCache的size
                return bitmap.getByteCount() / 1024;
            }
        };
        
        new Thread(runnable).start();
/*

        adapter = new BaseListViewAdapter<TestBean>(this, R.layout.example_buy_item) {
            @Override
            public void convert(ViewHolder vh, TestBean adv) {
                vh.setTextViewText(R.id.car_name, adv.getTitle());
                vh.setTextViewText(R.id.car_info, adv.getInfo());
                vh.setTextViewText(R.id.market_price, Integer.toString(adv.getMarketPrice()));
                vh.setTextViewText(R.id.buy_price, Integer.toString(adv.getBuyPrice()));
//                vh.setImageUrl(R.id.car_image, adv.getImageUrl());
//                vh.setImageBitMap(R.id.car_image,getEasyBitmap(adv.getImageUrl()));
//                downloadImage(vh, R.id.car_image, adv.getImageUrl());

                Bitmap bitmap = downloadImage(vh,adv.getImageUrl(), new onImageLoaderListener() {
                    @Override
                    public void onImageLoader(ViewHolder vh,Bitmap bitmap) {
                        vh.setImageBitMap(R.id.car_image, bitmap);
                    }
                });

                
            }

        };
*/

//        mMainHanlder = new MainHandler();
//        listView.setOnItemClickListener(new Btn1Listener());
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 3:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.baidu.com/"));
                        startActivity(intent);
                    default:
                        Toast.makeText(BuyListViewExampleActivity.this, "cs------"+position+"--"+id, Toast.LENGTH_SHORT).show();        
                }
//                new Btn1Listener();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(BuyRecyclerExampleActivity.class);
            }
        });

    }
    public Bitmap downloadImage(final ViewHolder vh, final String url,final onImageLoaderListener listener){

        final String subUrl = url.replaceAll("[^\\w]", "");
        Bitmap bitmap = showCacheBitmap(subUrl);
//        final String subUrl = url;
//        Bitmap bitmap= null;
        if(bitmap != null){
//            return bitmap;
            listener.onImageLoader(vh,bitmap);
        }else{

            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Bitmap bitmap =  (Bitmap)msg.obj;
//                            vh.getConvertView()
                    listener.onImageLoader(vh,bitmap);
                }
            };

            getThreadPool().execute(new Runnable() {

                @Override
                public void run() {
                    Bitmap bup = getEasyBitmap(url);
                    Message msg = handler.obtainMessage();
                    msg.obj = bup;
                    handler.sendMessage(msg);

//                            try {
//                                //保存在SD卡或者手机目录  
//                                fileUtils.savaBitmap(subUrl, bitmap);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                    //将Bitmap 加入内存缓存  
                    addBitmapToMemoryCache(subUrl, bup);
                }
            });
        }

        return null;
    }
    /**
     * 异步下载图片的回调接口 
     * @author len
     *
     */
    public interface onImageLoaderListener{
        void onImageLoader(ViewHolder vh,Bitmap bitmap);
    }

    public ExecutorService getThreadPool(){
        if(mImageThreadPool == null){
            synchronized(ExecutorService.class){
                if(mImageThreadPool == null){
                    //为了下载图片更加的流畅，我们用了2个线程来下载图片  
                    mImageThreadPool = Executors.newFixedThreadPool(2);
                }
            }
        }

        return mImageThreadPool;

    }
    public Bitmap getEasyBitmap(String url){
        URL imageUrl = null;
        try {
            imageUrl = new URL(url);

        }catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
//                    bitmap = BitmapFactory.decodeStream(imageUrl.openStream());

            Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openStream());
            int rawHeight = bmp.getHeight();
            int rawWidth = bmp.getWidth();
            // 设定图片新的高宽 
            int newHeight = 300;
            int newWidth = 300;
            // 计算缩放因子 
            float heightScale = ((float) newHeight) / rawHeight;
            float widthScale = ((float) newWidth) / rawWidth;
            // 新建立矩阵 
            Matrix matrix = new Matrix();
            matrix.postScale(heightScale, widthScale);
            // 设置图片的旋转角度 
            //matrix.postRotate(-30); 
            // 设置图片的倾斜 
            //matrix.postSkew(0.1f, 0.1f); 
            //将图片大小压缩 
            //压缩后图片的宽和高以及kB大小均会变化 
            bitmap = Bitmap.createBitmap(bmp, 0, 0, rawWidth,rawHeight, matrix, true);

        }catch (IOException e){
            e.printStackTrace();
        }
        return  bitmap;
    }

    /**
     * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步 
     * @param url
     * @return
     */
    public Bitmap showCacheBitmap(String url){
        if(getBitmapFromMemCache(url) != null){
            return getBitmapFromMemCache(url);
//        }else if(fileUtils.isFileExists(url) && fileUtils.getFileSize(url) != 0){
//            //从SD卡获取手机里面获取Bitmap  
//            Bitmap bitmap = fileUtils.getBitmap(url);
//
//            //将Bitmap 加入内存缓存  
//            addBitmapToMemoryCache(url, bitmap);
//            return bitmap;
        }

        return null;
    }
    /**
     * 添加Bitmap到内存缓存 
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap 
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    Handler handler = new Handler(){
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
                    /*
                    TestBean bean = new TestBean();
                    bean.setImageUrl(prodImageUrl);
                    bean.setInfo(selectedDesc);
                    bean.setId(prodId);
                    bean.setTitle(name);
                    bean.setBuyPrice(buyPrice);
                    bean.setMarketPrice(marketPrice);

//                    Bitmap bitmap = downloadImage(bean.getImageUrl());
                    datas.add(bean);
                    */
                }
/*

                listView.setAdapter(adapter);
                for (TestBean adv : datas) {
                    adapter.add(adv);
                }
                adapter.notifyDataSetChanged();
*/

            }
            catch (JSONException e)
            {
            }
        }
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


    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TEXT:
                    if(mStrContent != null)
                        mTv1.setText(mStrContent);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    class Btn1Listener implements AdapterView.OnClickListener{
        @Override
        public void onClick(View v) {
            new Thread(){
                public void run() {
                    String httpUrl = "http://www.baidu.com";
                    StringBuilder resultData = new StringBuilder("");
                    URL url = null;
                    try {
                        url = new URL(httpUrl);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block  
                        e.printStackTrace();
                    }

                    try {
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setRequestMethod("GET");
                        //inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer  
                        //getInputStream是真正去连接网络获取数据  
                        InputStreamReader isr = new InputStreamReader(urlConn.getInputStream());

                        //使用缓冲一行行的读入，加速InputStreamReader的速度  
                        BufferedReader buffer = new BufferedReader(isr);
                        String inputLine = null;

                        while((inputLine = buffer.readLine()) != null){
                            resultData.append(inputLine);
                            resultData.append("\n");
                        }
                        buffer.close();
                        isr.close();
                        urlConn.disconnect();
                        mStrContent = resultData.toString();
                        mMainHanlder.sendEmptyMessage(MSG_UPDATE_TEXT);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block  
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

}
