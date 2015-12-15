package com.julun.volley.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.julun.commons.R;
import com.julun.commons.images.BitMapCache;
import com.julun.commons.images.ImageUtils;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.ToastHelper;
import com.julun.volley.ByteArrayRequest;
import com.julun.volley.GenericTypedRequest;
import com.julun.volley.VolleyRequestCallback;

import java.util.HashMap;
import java.util.Map;


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

    public static void postByte(@NonNull String url, String requestTag, @NonNull VolleyRequestCallback callback, Map<String, String>... params) {
        requestTag = requestTag == null ? url : requestTag;
        ApplicationUtils.getGlobeRequestQueue().cancelAll(requestTag);
        Map<String, String> map = new HashMap<>();
        if (params != null) {
            for (Map<String, String> each : params) {
                map.putAll(each);
            }
        }
        ByteArrayRequest request = new ByteArrayRequest(url, callback, map);
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
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultResId, errorImageResId);
        ApplicationUtils.getGlobeImageLoader().get(url, listener);
    }

    /**
     * 加载图片并调整到指定的大小.
     *  @param view
     * @param url
     * @param width
     * @param height
     */
    public static void loadImageAndResize(@NonNull final ImageView view, @NonNull String url, final int width, final int height) {
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
        ApplicationUtils.getGlobeImageLoader().get(url, listener);
    }

    /**
     * 为 NetworkImageView 加载图片.
     *
     * @param niv
     * @param url
     */
    public static void loadImage4NetImageView(@NonNull NetworkImageView niv, @NonNull String url) {
        ImageLoader loader = ApplicationUtils.getGlobeImageLoader();
        niv.setDefaultImageResId(defaultResId);
        niv.setErrorImageResId(errorImageResId);
        niv.setImageUrl(url, loader);
    }

}
