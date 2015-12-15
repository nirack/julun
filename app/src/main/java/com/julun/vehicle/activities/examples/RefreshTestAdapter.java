package com.julun.vehicle.activities.examples;

import android.content.Context;

import com.julun.vehicle.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

/**
 * Created by Administrator on 2015-12-10.
 */
public class RefreshTestAdapter extends BaseListViewAdapter<String> {

    public RefreshTestAdapter(Context context) {
        super(context, R.layout.addr_item);
    }

    /**
     * 具体的adapter只需要实现这个方法即可.
     * 里面有具体要做的操作.
     *
     * @param vh
     * @param s
     */
    @Override
    public void convert(ViewHolder vh, String s) {
        vh.setTextViewText(R.id.addr_name,s);
    }
}
