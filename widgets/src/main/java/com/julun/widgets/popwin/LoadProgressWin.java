package com.julun.widgets.popwin;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.julun.widgets.R;

/**
 * Created by Administrator on 2015-12-03.
 */
public class LoadProgressWin extends BasicEasyPopupWindow {

    private static final String TAG = LoadProgressWin.class.getName();
    public static final  int STOP = -1;
    public static final  int GO = 1;
    private static long ANIINTERVAL = 200;//动画每帧播放间隔

    private static final int [] ANI_RES_IDS = {R.drawable.ic_go_1,R.drawable.ic_go_2};
    private int currentIndex = -1;//当前播放的图片的下标


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage() called with: " + "msg = [" + msg + "]");
            switch (msg.what){
                case GO:
                    getContentView().setBackgroundResource(nextImg());
                    handler.sendEmptyMessageDelayed(GO, ANIINTERVAL);
                    break;
                case STOP:
                    handler.removeMessages(GO);
                    break;
            }
        }
    };

    private int nextImg() {
        int next = (currentIndex + 1 + ANI_RES_IDS.length) % ANI_RES_IDS.length;
        currentIndex = next;
        return ANI_RES_IDS[next];
    }

    public LoadProgressWin(@NonNull Context cxt,@NonNull View contentView) {
        super(cxt, contentView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                stop();
            }
        });
    }

    public void start(){
        handler.sendEmptyMessage(GO);
    }

    public void stop(){
        handler.sendEmptyMessage(STOP);
    }

}
