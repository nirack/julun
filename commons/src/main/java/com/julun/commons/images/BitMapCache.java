package com.julun.commons.images;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2015-10-21.
 */
public class BitMapCache implements ImageLoader.ImageCache {
    public LruCache<String,Bitmap> cache = null;
    public int max_size = 10 * 1024 * 1024;//10M

    public BitMapCache() {
        this.cache = new LruCache<String,Bitmap>(max_size){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() ;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url,bitmap);
    }
}
