package com.julun.vehicle.activities.examples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.datas.beans.Adv;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.recyclerview.BaseRecyclerViewAdapter;
import com.julun.widgets.viewholder.recycler.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@ContentLayout(R.layout.activity_recycler_view_example)
public class RecyclerViewExampleActivity extends BaseActivity {

    @Bind(R.id.list_view)
    RecyclerView listView;
    @Bind(R.id.grid_view)
    RecyclerView gridView;

    private List<Adv> datas = new ArrayList<>();

    private void bind() {
        //首先是类似listView的效果
//        adapter 只需要实现一个方法
        LinearLayoutManager list = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(list);
        BaseRecyclerViewAdapter<Adv> adapter = new BaseRecyclerViewAdapter<Adv>(this, R.layout.test_listview_item) {
            @Override
            protected void renderView(ViewHolder vh, Adv adv) {
                vh.setTextViewText(R.id.tv1, "QWE_" + adv.getId()).setTextViewText(R.id.tv2, "RTY--：" + adv.getTitle());
            }
        };
        listView.setAdapter(adapter);
        for (Adv adv: datas) {
            adapter.insertData(0,adv);
            adapter.notifyItemChanged(0);
        }
        //每行四列
        RecyclerView.LayoutManager grid = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        gridView.setLayoutManager(grid);
        BaseRecyclerViewAdapter<Adv> adapter2 = new BaseRecyclerViewAdapter<Adv>(this, R.layout.test_listview_item) {
            @Override
            protected void renderView(ViewHolder vh, Adv adv) {
                vh.setTextViewText(R.id.tv1, "格子|" + adv.getId()).setTextViewText(R.id.tv2, "格子| ：" + adv.getTitle());
            }
        };
        gridView.setAdapter(adapter2);
        for (Adv adv: datas) {
            adapter2.insertData(0,adv);
            adapter2.notifyItemChanged(0);
        }
    }

    @AfterInitView
    private void initDatas() {
        int counts = ((int) (Math.random() * 100) % 10) + 5;
        for (int index = 0; index < counts; index++) {
            Adv ad = new Adv();
            ad.setId(index);
            ad.setTitle("标题<" + index + ">");
            datas.add(ad);
        }
        bind();
    }

}
