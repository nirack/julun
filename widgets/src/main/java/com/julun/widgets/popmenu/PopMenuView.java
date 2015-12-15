package com.julun.widgets.popmenu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.ref.WeakReference;

/**
 * 最多有两个listView互动.也可以只有一个ListView.
 */
public class PopMenuView extends LinearLayout {
    /**
     * 是否只有一个view.
     */
    private boolean singleView = true;

    private WeakReference<Context> cxt;

    /**
     * 标题.
     */
    private String title;

    private void init(Context context) {
        cxt = new WeakReference<Context>(context);
        this.setOrientation(singleView ? VERTICAL : HORIZONTAL);
        RecyclerView listView = new RecyclerView(context);

        LinearLayoutManager list = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(list);



    }

    public PopMenuView(Context context) {
        super(context);
        init(context);
    }

    public PopMenuView(Context context, boolean single) {
        super(context);
        this.singleView = single;
        init(context);
    }


    public void setSingleView(boolean singleView) {
        this.singleView = singleView;
    }
}
