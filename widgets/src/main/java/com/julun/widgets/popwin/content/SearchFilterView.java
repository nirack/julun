package com.julun.widgets.popwin.content;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * 提供给弹出窗口使用的查询过滤条件.
 *
 * 一行按钮,点击之后弹出下来菜单.
 *
 */
public class SearchFilterView extends LinearLayout{
    private WeakReference<Context> cxt;
    public SearchFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        cxt = new WeakReference<Context>(context);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {

    }
}
