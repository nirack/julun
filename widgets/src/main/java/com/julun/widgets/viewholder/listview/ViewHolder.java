package com.julun.widgets.viewholder.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.julun.volley.utils.Requests;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 可以通用的 ViewHolder,快速组装并提供一些方便的方法.
 */
public class ViewHolder {
    private View convertView;
    //当前view里面的子view的集合.  SparseArray是比 HashMap更快的容器.
    private SparseArray<View> views;
    //当前组件所处的位置
    private int currentPosition;
    /**
     *
     */
    private Map<String, Object> extraInfo;

    private ViewHolder(WeakReference<Context> context, ViewGroup parent, int layoutId, int position) {
        this.views = new SparseArray<>();
        convertView = LayoutInflater.from(context.get()).inflate(layoutId, parent, false);
        this.currentPosition = position;
        this.convertView.setTag(this);
        extraInfo = new HashMap<>();
        convertView.setTag(this);
    }

    public static ViewHolder get(WeakReference<Context> context, View convertView, ViewGroup parent, int layoutId, int position) {
        ViewHolder vh = null;
        if (null == convertView) {
            vh = new ViewHolder(context, parent, layoutId, position);
        } else {
            vh = (ViewHolder) convertView.getTag();
            vh.currentPosition = position;
        }
        return vh;
    }

    public View getConvertView() {
        return convertView;
    }

    /**
     * 通过 viewId  获取控件.
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (null == view) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给TextView 设置文本内容.
     *
     * @param viewId
     * @param txt
     * @return
     */
    public ViewHolder setTextViewText(int viewId, CharSequence txt) {
        TextView view = getView(viewId);
        view.setText(txt);
        return this;
    }

    /**
     * 设置ImageView 的resource的ID.
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageResourceId(int viewId, int resId) {
        ImageView img = getView(viewId);
        img.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitMap(int viewId, Bitmap bitmap) {
        ImageView img = getView(viewId);
        img.setImageBitmap(bitmap);
        return this;
    }


    public void hideView(int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
    }

    public ViewHolder setImageUrl(int viewId, String url) {
        ImageView imageView = getView(viewId);
        Requests.loadImage(imageView, url);
        return this;
    }

    public ViewHolder requestByNetworkImg(NetworkImageView imageView, String url) {
//        NetworkImageView view = getView(imageView);
        Requests.loadImage4NetImageView(imageView, url);
        return this;
    }

    public void attachClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
    }

    public void setExtra(String key, Object value) {
        if (extraInfo == null) {
            extraInfo = new HashMap<>();
        }
        extraInfo.put(key, value);
    }

    public Object getExtraInfo(String key) {
        return extraInfo.get(key);
    }

    public ViewHolder setRatingStart(int rating_bar, Integer rate) {
        RatingBar rb = getView(rating_bar);
        rb.setNumStars(rate != null ? rate : 3);
        return this;
    }
}
