package com.julun.widgets.viewpager.anims;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2015-11-03.
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                view.setAlpha(0);
            }else {
//                ViewHelper.setAlpha(view,0);
            }

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                float translationX = horzMargin - vertMargin / 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    view.setTranslationX(translationX);
                }else {
//                    ViewHelper.setTranslationX(view, translationX);
                }
            } else {
                float translationX = -horzMargin + vertMargin / 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    view.setTranslationX(translationX);
                }else {
//                    ViewHelper.setTranslationX(view, translationX);
                }
            }

            // Scale the page down (between MIN_SCALE and 1)
            float alpha = MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(alpha);
            }else {
//                ViewHelper.setAlpha(view, alpha);
            }

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                view.setAlpha(0);
            }else {
//                ViewHelper.setAlpha(view, 0);
            }
        }
    }
}