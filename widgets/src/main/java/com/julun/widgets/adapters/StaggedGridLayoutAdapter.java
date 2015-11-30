package com.julun.widgets.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.julun.widgets.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-10-22.
 */
public class StaggedGridLayoutAdapter extends SimpleAdapter{

    public interface OnClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    private List<Integer> heights;

    private Integer eachHeight;

    private OnClickListener listener;

    public StaggedGridLayoutAdapter(Context context, List<String> datas) {
        super(context, datas);
        heights = new ArrayList<>();
        for (int index = 0; index < datas.size(); index++) {
            int height  = (int) (100 + Math.random() * 300);
            heights.add(height);
        }
        eachHeight =  (int) (100 + Math.random() * 300);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.d("数据信息", "调用 adapter 的 onBindViewHolder() with: " + "holder = [" + holder + "], position = [" + position + "]");
        if(heights.size() ==0){
            return;
        }
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//        layoutParams.height = heights.get(position);
        layoutParams.height= eachHeight;
        layoutParams.height = 60;
        holder.itemView.setLayoutParams(layoutParams);
        holder.tv.setText(datas.get(position));
//        holder.tv.setBackgroundResource(R.drawable.rectangle_with_corner_without_buttom);
        if(null != this.listener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    listener.onClick(holder.itemView, adapterPosition);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    listener.onLongClick(holder.itemView, adapterPosition);
                    return true;
                }
            });
        }
    }

    public void insertData(int position,String str ) {
        if(null==str){
            str = "新增";
        }
        datas.add(position, str);
        Log.i("数据变化","现在数据集长度:" + datas.size());
        notifyItemInserted(position);
    }


    public void removeData(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }


    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}


