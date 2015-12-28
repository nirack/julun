package com.julun.volley.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.julun.commons.R;
import com.julun.commons.images.ImageUtils;
import com.julun.event.events.DataChangeEvent;
import com.julun.event.events.FailureEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.JsonHelper;
import com.julun.utils.ToastHelper;
import com.julun.volley.ByteArrayRequest;
import com.julun.volley.GenericTypedRequest;
import com.julun.volley.VolleyRequestCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * HTTP请求的帮助类.
 */
public class Requests {

    //图片默认的照片
    private static final int defaultResId = R.drawable.no_image_foundpng;
    //图片加载失败时候用的照片
    private static final int errorImageResId = R.drawable.no_image_foundpng;

    private ImageLoader.ImageListener listener;

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
        ApplicationUtils.getGlobalRequestQueue().cancelAll(requestTag);
        Map<String, String> map = new HashMap<>();
        if (params != null) {
            for (Map<String, String> each : params) {
                map.putAll(each);
            }
        }
        GenericTypedRequest request = new GenericTypedRequest(url, callback, map);
        request.setTag(requestTag);
        ApplicationUtils.getGlobalRequestQueue().add(request);
        ApplicationUtils.getGlobalRequestQueue().start();
    }

    public static void postByte(@NonNull String url, String requestTag, @NonNull VolleyRequestCallback callback, Map<String, String>... params) {
        requestTag = requestTag == null ? url : requestTag;
        ApplicationUtils.getGlobalRequestQueue().cancelAll(requestTag);
        Map<String, String> map = new HashMap<>();
        if (params != null) {
            for (Map<String, String> each : params) {
                map.putAll(each);
            }
        }
        ByteArrayRequest request = new ByteArrayRequest(url, callback, map);
        request.setTag(requestTag);
        ApplicationUtils.getGlobalRequestQueue().add(request);
        ApplicationUtils.getGlobalRequestQueue().start();
    }

    /**
     * 加载普通ImageView 的图片.
     *
     * @param view
     * @param url
     */
    public static void loadImage(@NonNull ImageView view, @NonNull String url) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultResId, errorImageResId);
        ApplicationUtils.getGlobalImageLoader().get(url, listener);
    }

    /**
     * 加载图片，压缩图片至最大宽度和高度
     *
     * @param view
     * @param url
     * @param maxWidth
     * @param maxHeight
     */
    public static void loadImage(@NonNull ImageView view, @NonNull String url, int maxWidth, int maxHeight) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultResId, errorImageResId);
        ApplicationUtils.getGlobalImageLoader().get(url, listener, maxWidth, maxHeight);
    }

    /**
     * 加载图片并调整到指定的大小.
     *
     * @param view
     * @param url
     * @param width
     * @param height
     */
    public static void loadImageAndResize(@NonNull final ImageView view, @NonNull String url, final int width, final int height) {
        ImageLoader.ImageListener listener2 = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap rawImage = response.getBitmap();
                if (rawImage == null && errorImageResId != 0) {
                    rawImage = BitmapFactory.decodeResource(ApplicationUtils.getGlobalApplication().getResources(), errorImageResId);
                }
                Bitmap newImg = ImageUtils.createBitmapBySize(rawImage, width, height);
                view.setImageBitmap(newImg);

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastHelper.showShort(view.getContext(), "加载图片失败," + error.toString());
                view.setImageResource(errorImageResId);
            }
        };
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, defaultResId, errorImageResId);
        ApplicationUtils.getGlobalImageLoader().get(url, listener2);
    }

    /**
     * 为 NetworkImageView 加载图片.
     *
     * @param niv
     * @param url
     */
    public static void loadImage4NetImageView(@NonNull NetworkImageView niv, @NonNull String url) {
        ImageLoader loader = ApplicationUtils.getGlobalImageLoader();
        niv.setDefaultImageResId(defaultResId);
        niv.setErrorImageResId(errorImageResId);
        niv.setImageUrl(url, loader);
    }

}
