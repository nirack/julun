package com.julun.commons.images;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2015-11-20.
 */
public class ImageUtils {


    private void test(Bitmap source, int width, int height){
        String filepath = null;
        Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Images.Thumbnails.MINI_KIND);
        ThumbnailUtils.extractThumbnail(source, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        ThumbnailUtils.extractThumbnail(source, width, height);
    }

    public static Bitmap zoomImage(Bitmap source, int width, int height){
        return ThumbnailUtils.extractThumbnail(source, width, height);
    }

}
