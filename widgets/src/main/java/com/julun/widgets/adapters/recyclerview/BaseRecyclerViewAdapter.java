package com.julun.widgets.adapters.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julun.widgets.viewholder.recycler.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    /**
     * 数据源
     */
    List<T> datas = new ArrayList<>();

    /**
     * 每个子项用到的布局文件.
     */
    private int[] layoutResIds;


    /**
     *
     * 这里需要说明的是,
     * <ul>
     *     <li>
     *         如果所有的子view都是使用同一个布局文件,那么请传入一个布局文件的ID即可.
     *     </li>
     *     <li>
     *         如果传入多个，则认为,需要根据 {@link BaseRecyclerViewAdapter#getItemViewType} 方法判断使用哪个布局文件来实例化子View.
     *     </li>
     *     <li>
     *         如果多个布局文件,请<strong><font color="red">一定 </font></strong> 重写 {@link BaseRecyclerViewAdapter#getItemViewType}</br>
     *         实例化的时候，会根据 {@link BaseRecyclerViewAdapter#getItemViewType} 的返回值 取对应坐标的 布局文件.
     *     </li>
     *     <li>
     *         最后一点,如果有多个viewType ,也就是传入多个布局文件,那么,保证 {@link BaseRecyclerViewAdapter#getItemViewType} 的返回值是从 0 到 布局文件数组的长度-1
     *     </li>
     * </ul>
     *
     *
     * @param context
     * @param layoutIds
     */
    public BaseRecyclerViewAdapter(Context context, int ... layoutIds) {
        this.layoutResIds = layoutIds;
    }

    /**
     * @param parent   一言概之，就是RecylerView自己.
     * @param viewType 配合 #getItemViewType 来为不同类型的ViewType实例化不同的view.
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resId = this.layoutResIds[viewType];
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(resId, parent, false);
        ViewHolder vh = new ViewHolder(convertView);
        vh.setViewType(viewType);
        return vh;
    }

    @Override
    public  final void onBindViewHolder(ViewHolder vh, int position) {
        T t = datas.get(position);
        renderView(vh, t);
    }


    public void insertData(int position, T object) {
        if (null == object) {
            throw new RuntimeException(new IllegalArgumentException(" object should not be null!"));
        }
        datas.add(position, object);
        Log.i("数据变化", "现在数据集长度:" + datas.size());
        notifyItemInserted(position);
    }

    protected abstract void renderView(ViewHolder vh, T t);

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return datas.size();
    }

}
