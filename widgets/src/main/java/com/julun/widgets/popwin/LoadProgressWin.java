package com.julun.widgets.popwin;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.julun.utils.ScreenUtils;
import com.julun.widgets.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015-12-03.
 */
public class LoadProgressWin extends BasicEasyPopupWindow {

    private static final String TAG = LoadProgressWin.class.getName();
    public static final int STOP = -1;
    public static final int GO = 1;
    private static long ANIINTERVAL = 200;//动画每帧播放间隔

    private static final int[] ANI_RES_IDS = {R.drawable.ic_go_1, R.drawable.ic_go_2};
    private int currentIndex = -1;//当前播放的图片的下标

    static Animation anima;


    private int SCREEN_OFFSET = ScreenUtils.getScreenHeight();

    private int nextImg() {
        int next = (currentIndex + 1 + ANI_RES_IDS.length) % ANI_RES_IDS.length;
        currentIndex = next;
        return ANI_RES_IDS[next];
    }

    public LoadProgressWin(@NonNull Context cxt, @NonNull View contentView) {
        super(cxt, contentView);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO: 2015-12-13  stop()????
            }
        });
    }

    public void start() {

        final View contentView = this.getContentView();

        if (anima == null) {
            anima = new RotateAnimation(0f, 365f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anima.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Log.d(TAG + "_ AnimationListener ", "onAnimationStart() called with: " + "animation = [" + animation + "]");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d(TAG + "_ AnimationListener ", "onAnimationStart() called with: " + "animation = [" + animation + "]");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    Log.d(TAG + "_ AnimationListener ", "onAnimationStart() called with: " + "animation = [" + animation + "]");
                }
            });
            anima.setDuration(1000L);// 设置动画的运行时长
            anima.setFillAfter(true); // 动画运行结束之后，保存结束之后的状态
            anima.setRepeatCount(Integer.MAX_VALUE); // 设置重复的次数
            //设置重复模式为逆运动
//                        anima.setRepeatMode(Animation.REVERSE);
        }
        contentView.findViewById(R.id.wheel_image).startAnimation(anima);

    }

}
