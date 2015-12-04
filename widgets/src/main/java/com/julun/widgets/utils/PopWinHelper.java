package com.julun.widgets.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.julun.widgets.popwin.BasicEasyPopupWindow;
import com.julun.widgets.popwin.LoadProgressWin;

/**
 */
public class PopWinHelper {

    public static BasicEasyPopupWindow getPopWin(int viewid){
        BasicEasyPopupWindow basicEasyPopupWindow = new BasicEasyPopupWindow(null, viewid);
        return basicEasyPopupWindow.focusable(true);
    }

    /**
     * 获取一个弹出窗口,并设置外部可点击
     * @param contentView
     * @return
     */
    public static BasicEasyPopupWindow getPopWin(@NonNull Context cxt,View contentView){
        BasicEasyPopupWindow basicEasyPopupWindow = new BasicEasyPopupWindow(cxt,contentView);
        return basicEasyPopupWindow.focusable(true).width(ViewGroup.LayoutParams.MATCH_PARENT).height(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static LoadProgressWin getLoadingPopWin(@NonNull Context cxt,View contentView,int height){
        LoadProgressWin win = new LoadProgressWin(cxt,contentView);
        win.height(height);
        return win;
    }

}
