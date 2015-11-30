package com.julun.widgets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.julun.widgets.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015-10-22.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {
    protected List<String> datas;
    protected Context context;
    protected LayoutInflater inflater;

    public SimpleAdapter(Context context, List<String> datas) {
        this.context = context;
        if(datas == null){
            datas = new ArrayList<>();
        }
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: 2015-11-09
        View view =  inflater.inflate(R.layout.addr_item, parent, false);;//inflater.inflate(R.layout.test_recycle_linear, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.tv.setBackgroundResource(R.drawable.normal_rectangle);
        holder.tv.setText(datas.get(position));
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.datas.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            // TODO: 2015-11-09  
            tv = (TextView) itemView.findViewById(R.id.addr_name);
        }
    }
}


