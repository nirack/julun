package com.julun.widgets.popwin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.julun.widgets.R;
import com.julun.widgets.utils.LayoutManagerHelper;

import java.lang.ref.WeakReference;

/**
 * 全屏，半透明，的popwin.
 */
public class BasicEasyPopupWindow extends PopupWindow {

    protected WeakReference<Context> cxt;

    public BasicEasyPopupWindow(@NonNull Context context,@NonNull View contentView) {
        super(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        cxt = new WeakReference<Context>(context);
        setDismissable();
    }

    public BasicEasyPopupWindow(@NonNull Context context,@NonNull int contentView) {
        this(context,LayoutInflater.from(context).inflate(contentView, null));
        setDismissable();
    }

    public void setDismissable(){
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;// 这里如果返回true的话，touch事件将被拦截 ， 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        this.setBackgroundDrawable( cxt.get().getResources().getDrawable(R.color.main_bg) );
    }

    public BasicEasyPopupWindow width(int width) {
        this.setWidth(width);
        return this;
    }

    public BasicEasyPopupWindow height(int height) {
        this.setHeight(height);
        return this;
    }

    public BasicEasyPopupWindow focusable(boolean focusable) {
        this.setFocusable(focusable);
        return this;
    }

    /**
     * 显示在屏幕正中间.
     */
    public void center(){
        this.showAsDropDown(getContentView());
    }

}
