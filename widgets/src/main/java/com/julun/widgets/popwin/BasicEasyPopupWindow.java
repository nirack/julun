package com.julun.widgets.popwin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.julun.widgets.utils.LayoutManagerHelper;

/**
 * 全屏，半透明，的popwin.
 */
public class BasicEasyPopupWindow extends PopupWindow {

    public BasicEasyPopupWindow(@NonNull View contentView) {
        super(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }

    public BasicEasyPopupWindow(@NonNull Context context,@NonNull int contentView) {
        super(LayoutInflater.from(context).inflate(contentView,null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    }

    /**
     * 显示在屏幕正中间.
     */
    public void center(){
        this.showAsDropDown(getContentView());
    }

}
