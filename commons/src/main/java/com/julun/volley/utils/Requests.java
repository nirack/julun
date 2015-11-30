package com.julun.volley.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.julun.commons.R;
import com.julun.commons.images.BitMapCache;
import com.julun.commons.images.ImageUtils;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.JsonHelper;
import com.julun.utils.ToastHelper;
import com.julun.volley.GenericTypedRequest;
import com.julun.volley.VolleyRequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * HTTP请求的帮助类.
 */
public class Requests {

    //图片默认的照片
    private static final int defaultResId = R.drawable.no_image_foundpng;
    //图片加载失败时候用的照片
    private static final int errorImageResId = R.drawable.no_image_foundpng;

    //百度搜索图片的地址,使用的时候后面跟关键词.现在服务器没有图片,测试的时候,用这个随便从百度图片里搜图片
    private static final String BAIDU_IMG_SEARCH_PREFIX = "http://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=error&sf=1&fmq=&pv=&ic=0&nc=1&z=&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=";

    /**
     * 发送post请求.
     *
     * @param url
     * @param requestTag
     * @param callback
     * @param params
     */
    public static void post(@NonNull String url, String requestTag, @NonNull VolleyRequestCallback callback, Map<String, String>... params) {
        requestTag = requestTag == null ? url : requestTag;
        ApplicationUtils.getGlobeRequestQueue().cancelAll(requestTag);
        Map<String, String> map = new HashMap<>();
        if (params != null) {
            for (Map<String, String> each : params) {
                map.putAll(each);
            }
        }
        GenericTypedRequest request = new GenericTypedRequest(url, callback, map);
        request.setTag(requestTag);
        ApplicationUtils.getGlobeRequestQueue().add(request);
        ApplicationUtils.getGlobeRequestQueue().start();
    }

    /**
     * 加载普通ImageView 的图片.
     *
     * @param view
     * @param url
     */
    public static void loadImage(@NonNull ImageView view, @NonNull String url) {
        ImageLoader loader = new ImageLoader(ApplicationUtils.getGlobeRequestQueue(), new BitMapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultResId, errorImageResId);
        loader.get(url, listener);
    }

    /**
     * 加载图片并调整到指定的大小.
     *  @param view
     * @param url
     * @param width
     * @param height
     */
    public static void loadImageAndResize(@NonNull final ImageView view, @NonNull String url, final int width, final int height) {
        ImageLoader loader = new ImageLoader(ApplicationUtils.getGlobeRequestQueue(), new BitMapCache());
        ImageLoader.ImageListener listener2 = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                Bitmap newImg = ImageUtils.zoomImage(bitmap, width, height);
                view.setImageBitmap(newImg);
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastHelper.showShort(view.getContext(),"加载图片失败," + error.toString());
                view.setImageResource(errorImageResId);
            }
        };
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultResId, errorImageResId);
        loader.get(url, listener);
    }

    /**
     * 为 NetworkImageView 加载图片.
     *
     * @param niv
     * @param url
     */
    public static void loadImage4NetImageView(@NonNull NetworkImageView niv, @NonNull String url) {
        ImageLoader loader = new ImageLoader(ApplicationUtils.getGlobeRequestQueue(), new BitMapCache());
        niv.setDefaultImageResId(defaultResId);
        niv.setErrorImageResId(errorImageResId);
        niv.setImageUrl(url, loader);
    }

    public static List<String> getRandomImageUrl(String keyword, final int skip, final int size) {
        if (null == keyword) {
            keyword = "" + Math.random();
        }
        String url = BAIDU_IMG_SEARCH_PREFIX + keyword;

        String page = "1";
        url = "http://photo.bitauto.com/serialmore/2045/2015/11/" + page + "/";//易车网保时捷911 官方图
        url = "http://car.autohome.com.cn/pic/series/703-1-p2.html";//汽车之家保时捷图片
//        url = "http://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fr=&sf=1&fmq=&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=汽车高清壁纸&oq=汽车高清壁纸&rsp=-1#z=0&pn=&ic=0&st=-1&face=0&s=0&lm=-1";
        Log.i("获取图片的地址： ", url);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> imgSrcList = getImgSrcList(response, skip, size);
                Log.i("获取图片", JsonHelper.toJson(imgSrcList));
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("请求出错", error.toString());
            }
        };
        ApplicationUtils.getGlobeRequestQueue().cancelAll(url);
        StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener);
        request.setTag(url);
        ApplicationUtils.getGlobeRequestQueue().add(request);
        ApplicationUtils.getGlobeRequestQueue().start();
        return null;
    }

    /**
     * 得到网页中图片的地址
     *
     * @param htmlStr 请求获得的HTML页面.
     * @param size    数目
     * @return
     */
    public static List<String> getImgSrcList(String htmlStr, int skip, int size) {
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<img.*?src=\"http://(.*?).jpg\""; // 图片链接地址
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(htmlStr);
        while (m_image.find() && pics.size() < size) {
            if (skip != 0) {
                skip--;
                Log.i("查看skip", "getImgSrcList() called with: " + " skip = [" + skip + "], size = [" + size + "]");
                continue;
            }
            String src = m_image.group(1);
            if (src.length() < 100) {
                pics.add("http://" + src + ".jpg");
            }
        }
        return pics;
    }

}
