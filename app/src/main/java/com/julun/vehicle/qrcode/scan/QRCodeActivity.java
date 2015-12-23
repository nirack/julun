package com.julun.vehicle.qrcode.scan;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.vehicle.qrcode.CaptureActivityHandler;
import com.julun.widgets.qrscan.view.ViewfinderView;
import com.julun.zxing.CameraManager;
import com.julun.zxing.decoding.InactivityTimer;

import java.io.IOException;
import java.util.Timer;
import java.util.Vector;

import butterknife.Bind;

@ContentLayout(R.layout.activity_qrcode)
public class QRCodeActivity extends BaseActivity {

    @Bind(R.id.preview_view)
    SurfaceView previewView;//预览的SurfaceVIew

    @Bind(R.id.ImageView01)
    ImageView ImageView01;
    @Bind(R.id.centerView)
    View centerView;
    @Bind(R.id.txtScanResult)
    TextView txtScanResult;
    @Bind(R.id.RelativeLayout01)
    RelativeLayout RelativeLayout01;
    @Bind(R.id.FrameLayout01)
    FrameLayout FrameLayout01;

    @Bind(R.id.viewfinder_view)
    ViewfinderView viewfinderView;

    @Bind(R.id.details_textview_title)
    TextView title;
    @Bind(R.id.details_imageview_gohome)
    ImageView goHome;

    /**
     * 是否有预览的SurfaceVIew
     */
    private boolean hasSurface = false;
    private Vector<BarcodeFormat> decodeFormats;
    private Timer timer;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;//是否震动

    private CaptureActivityHandler captureActivityHandler;


    @AfterInitView
    public void afterInitViews() {
        Log.d(QRCodeActivity.class.getName(), "afterInitViews() called with: " + "");
        CameraManager.init(getApplication());

        title.setText(R.string.scan_title);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onPause() {
        Log.d(QRCodeActivity.class.getName(), "onPause() called with: " + "");
        super.onPause();
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            captureActivityHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        Log.d(QRCodeActivity.class.getName(), "onDestroy() called with: " + " ， captureActivityHandler != null ？  " + ( captureActivityHandler != null ) + "" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(new SurfaceCallback());
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /**
     * 扫描正确后的震动声音,如果感觉apk大了,可以删除
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,  characterSet);
        }
    }


    public void drawViewfinder() {
        // TODO: 2015-11-29
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(Result result, Bitmap barcode) {

        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(QRCodeActivity.this, "Scan failed,Please have a try!", Toast.LENGTH_SHORT).show();
        }else {
            Intent resultIntent = new Intent(QRCodeActivity.this,CaptureResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            bundle.putParcelable("bitmap", barcode);
            resultIntent.putExtras(bundle);
            //this.setResult(RESULT_OK, resultIntent);

            jumpActivity(CaptureResultActivity.class,bundle);;

//            startActivity(resultIntent);
        }
        QRCodeActivity.this.finish();
    }

    private static final long VIBRATE_DURATION = 200L;
    
    private void playBeepSoundAndVibrate() {

        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    /**
     * Created by Administrator on 2015-11-29.
     */
    public class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            hasSurface = false;
        }
    }

}
