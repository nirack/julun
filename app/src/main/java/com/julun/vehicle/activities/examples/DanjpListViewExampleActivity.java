package com.julun.vehicle.activities.examples;


import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by danjp on 2015/12/17.
 */
@ContentLayout(R.layout.example_danjp_listview)
public class DanjpListViewExampleActivity extends BaseActivity {

    @Bind(R.id.listview)
    ListView listView;
    private ArrayList<Prod> items = new ArrayList<>();


    @AfterInitView
    public void vewiInit() {

        Prod prod = new Prod("固铂轮胎Lifeliner GLS 205/55R 16 91H cooper[全球替换专家1]", 360, 540, 65, 37);
        items.add(prod);

        prod = new Prod("固铂轮胎Lifeliner GLS 205/55R 16 91H cooper[全球替换专家2]", 380, 590, 42, 22);
        items.add(prod);

        prod = new Prod("固铂轮胎Lifeliner GLS 205/55R 16 91H cooper[全球替换专家3]", 420, 650, 15, 5);
        items.add(prod);

        MyAdapter adapter = new MyAdapter(this, R.layout.example_danjp_listview_item);
        adapter.addAll(items);
        listView.setAdapter(adapter);

    }

    class Prod {
        String prod_name;
        String prod_img_url;
        float prod_price;
        float market_price;
        int buy_count;
        int pingjia_count;
        Prod(){}
        Prod(String prod_name, float prod_price, float market_price, int buy_count, int pingjia_count){
            this.prod_name = prod_name;
            this.prod_price = prod_price;
            this.market_price = market_price;
            this.buy_count = buy_count;
            this.pingjia_count = pingjia_count;
        }
    }

    class MyAdapter extends BaseListViewAdapter<Prod> {
        public MyAdapter(Context context, int itemResId) {
            super(context, itemResId);
        }

        @Override
        public void convert(ViewHolder vh, Prod prod) {
            vh.setTextViewText(R.id.prod_name, prod.prod_name);
            vh.setTextViewText(R.id.prod_price, "¥" + prod.prod_price);

            TextView tv = vh.getView(R.id.prod_price);
            tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            vh.setTextViewText(R.id.market_price, "¥"+prod.market_price+"");
            vh.setTextViewText(R.id.buy_count, prod.buy_count+"人购买");
            vh.setTextViewText(R.id.pingjia, prod.pingjia_count+"人评价");
        }
    }

}
