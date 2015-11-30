package com.julun.widgets.adapters.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.julun.widgets.viewholder.listview.ViewHolder;

import java.lang.ref.WeakReference;


/**
 * 有一个默认的前提,就是一定要有一个用于渲染子View的layout文件.简单的使用一个组件的情况在实际应用中也几乎看不到,忽略这种情况了.
 * Created by Administrator on 2015-10-21.
 */
public abstract class BaseListViewAdapter<T> extends ArrayAdapter {
    protected WeakReference<Context> context;
    protected LayoutInflater inflater;
    protected int resourceId;

    public BaseListViewAdapter(Context context, int itemResId) {
        super(context,itemResId);
        this.resourceId = itemResId;
        this.context = new WeakReference<Context>(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public T getItem(int position) {
        Object item = super.getItem(position);
        return (T)item;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder vh = ViewHolder.get(context,convertView,parent,this.resourceId,position);
        convert(vh, getItem(position));
        return vh.getConvertView();
    }

    /**
     * 具体的adapter只需要实现这个方法即可.
     * 里面有具体要做的操作.
     * @param vh
     * @param t
     */
    public abstract void convert(ViewHolder vh,T t) ;

}
