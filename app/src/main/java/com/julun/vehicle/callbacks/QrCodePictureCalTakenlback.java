package com.julun.vehicle.callbacks;

import android.hardware.Camera;

import com.julun.utils.ApplicationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015-11-29.
 */
public class QrCodePictureCalTakenlback implements Camera.PictureCallback {



    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        String tmpImgPath = ApplicationUtils.APP_BASE_EXTERNAL_STORAGE_PATH + "tmp.png";
        File tmp = new File(tmpImgPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tmp);
            fos.write(data);

            // TODO: 2015-11-29  
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != fos){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
