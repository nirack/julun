package com.julun.widgets.filterbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * 搜索查询的过滤栏.
 */
public class SearchFilterBar extends LinearLayout {
    private WeakReference<Context> cxt;

    public SearchFilterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initThis(context,attrs);
    }

    private void initThis(Context context, AttributeSet attrs) {
        cxt = new WeakReference<Context>(context);
        this.setOrientation(HORIZONTAL);
        if(null != attrs){
            initAttrs(attrs);
        }
    }

    private void initAttrs(AttributeSet attrs) {

    }

    public SearchFilterBar(Context context) {
        super(context);
        initThis(context,null);
    }
}
