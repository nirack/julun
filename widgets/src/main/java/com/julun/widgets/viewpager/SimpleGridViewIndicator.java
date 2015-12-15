package com.julun.widgets.viewpager;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.julun.widgets.R;

import java.lang.ref.WeakReference;

/**
 * 配合 SimpleLoopViewPager 使用.
 *
 * @see SimpleLoopViewPager
 */
public class SimpleGridViewIndicator extends LinearLayout {
    private int lastCheckItem = -1;
    private WeakReference<Context> cxt;

    public SimpleGridViewIndicator(Context context) {
        super(context);
        this.cxt = new WeakReference<Context>(context);
        this.setOrientation(HORIZONTAL);
    }

    public void check(int id) {
        if (lastCheckItem != -1) {
            View childAt = getChildAt(lastCheckItem);
            childAt.setBackgroundResource(R.drawable.app_title_bg_shape);
        }
        this.lastCheckItem = id;
        View childAt = getChildAt(id);
        if (null != childAt) {
            childAt.setBackgroundResource(R.drawable.app_title_bg_red);
        }
        // TODO: 2015-12-02
    }

    public int getLastCheckItem() {
        return lastCheckItem;
    }
}
