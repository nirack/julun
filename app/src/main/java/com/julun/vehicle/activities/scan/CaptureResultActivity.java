package com.julun.vehicle.activities.scan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.julun.utils.ApplicationUtils;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureResultActivity extends AppCompatActivity {
    private static final String TAG = CaptureResultActivity.class.getName();
    private static int RESULT_LOAD_IMAGE = 1;
    @Bind(R.id.barcode_back)
    ImageView barcodeBack;
    @Bind(R.id.textview_title)
    TextView textviewTitle;
    @Bind(R.id.layout_top)
    RelativeLayout layoutTop;
    @Bind(R.id.scan_result)
    TextView scanResult;
    @Bind(R.id.qrcode_bitmap)
    ImageView qrcodeBitmap;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.camera)
    Button camera;
    @Bind(R.id.openPic)
    Button openPic;
    @Bind(R.id.layout_bottom)
    RelativeLayout layoutBottom;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_result);
        ButterKnife.bind(this);

        Bundle bundle = new Bundle();

        scanResult.setText(getIntent().getExtras().getString("result"));
        qrcodeBitmap.setImageBitmap((Bitmap) getIntent().getParcelableExtra("bitmap"));

    }


    @OnClick({R.id.barcode_back, R.id.save, R.id.camera, R.id.openPic})
    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.barcode_back:
                finish();
                break;
            //保存二维码
            case R.id.save:
                saveBarcode();
                break;

            case R.id.camera:
                openCamera();
                break;

            case R.id.openPic:
                openPic();
                break;

            default:
                break;
        }


    }

    //打开保存的二维码
    private void openPic() {
        startActivity(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }


    //保存生成的二维码图片
    private void saveBarcode() {
        bitmap = getIntent().getParcelableExtra("bitmap");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            Writer(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //将图片通过流的方式写入到文件中去
    private void Writer(byte[] byteArray) throws IOException {
        File cacheFile = null;
        long time = Calendar.getInstance().getTimeInMillis();
        String fileName = time + ".png";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheFile = new File(ApplicationUtils.APP_BASE_EXTERNAL_STORAGE_PATH, fileName);
        }

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(cacheFile));

            bos.write(byteArray, 0, byteArray.length);
            bos.close();
            Toast toast = Toast.makeText(getApplicationContext(), "图片保存在了内存卡下 :  " +
                    cacheFile.getAbsolutePath() +
                    " 文件夹下", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打开camera，实现拍照操作    
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.v("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }

            Bundle bundle = data.getExtras();
            //获得相机的返回的数据
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //获得sdcard路径
            File file = null;
            File saveDir = null;
            String pic = null;
            FileOutputStream fileoutputStream = null;
            try {
                long time = Calendar.getInstance().getTimeInMillis();
                pic = time + ".png";
                file = new File(ApplicationUtils.APP_BASE_EXTERNAL_STORAGE_PATH, pic);
                //保存文件的名称
                String picName = saveDir + "/" + pic;
                fileoutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileoutputStream);// 把数据写入文件

                ToastHelper.showLong(CaptureResultActivity.this, "图片已成功保存");
                fileoutputStream.flush();
                fileoutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    fileoutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
