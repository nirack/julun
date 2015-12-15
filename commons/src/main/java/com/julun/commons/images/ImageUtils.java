package com.julun.commons.images;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2015-11-20.
 */
public class ImageUtils {

    public static Bitmap zoomImage(Bitmap source, int width, int height){
        return ThumbnailUtils.extractThumbnail(source, width, height);
    }

}
