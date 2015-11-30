package com.julun.widgets.viewholder.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.julun.volley.utils.Requests;

import java.lang.ref.WeakReference;

/**
 *
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    //当前view里面的子view的集合.  SparseArray是比 HashMap更快的容器.
    private SparseArray<View> children;
    private WeakReference<Context> context;
    private int viewType;

    public ViewHolder(View itemView) {
        super(itemView);
        children = new SparseArray<>();
    }

    /**
     * 通过 viewId  获取控件.
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends  View> T getView(Integer viewId){
        View view = children.get(viewId);
        if(null == view ){
            view = itemView.findViewById(viewId);
            children.put(viewId, view);
        }
        return (T)view;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    /**
     * 给TextView 设置文本内容.
     * @param viewId
     * @param txt
     * @return
     */
    public ViewHolder  setTextViewText(int viewId,CharSequence txt){
        TextView view = getView(viewId);
        view.setText(txt);
        return this;
    }

    /**
     * 设置ImageView 的resource的ID.
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResourceId(int viewId, int resId){
        ImageView img = getView(viewId);
        img.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitMap(int viewId, Bitmap bitmap){
        ImageView img = getView(viewId);
        img.setImageBitmap(bitmap);
        return this;
    }


    public void hideView(int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
    }

    public ViewHolder setImageUrl(int viewId, String url){
        ImageView imageView = getView(viewId);
        Requests.loadImage(imageView, url);
        return this;
    }

    public  ViewHolder loadImage4NetImageView(Integer imageViewid, String url) {
        Requests.loadImage4NetImageView((NetworkImageView) getView(imageViewid), url);
        return this;
    }

}
