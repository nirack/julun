package com.julun.vehicle.activities.examples;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.datas.beans.Adv;
import com.julun.utils.DateHelper;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@ContentLayout(R.layout.activity_list_view_example)
public class ListViewExampleActivity extends BaseActivity {

    private List<Adv> datas = new ArrayList<>();

    @Bind(R.id.list_view)
    ListView listView;
    private BaseListViewAdapter<Adv> adapter;

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;


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

    boolean btn1Clicked = false;
    boolean btn2Clicked = false;


    int btn1change = 0;
    int btn2change = 0;

//    @OnClick({R.id.btn1, R.id.btn2})
    public void start(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (!btn1Clicked) {
                    btn1Clicked = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                            try {
                                Thread.sleep(100L);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String msg = "改变文本：" + DateHelper.formatNowTime() + "  , 次数： " + btn1change;
                                        btn1change++;
                                        btn2.setText(msg);
                                    }
                                }, 100L);
                            } catch (Exception e) {

                            }

                            }
                        }
                    }.start();
                }
                break;
            case R.id.btn2:
                if (!btn2Clicked) {
                    btn2Clicked = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(100L);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String msg = "btn1 改变文本：" + DateHelper.formatNowTime() + "  , 次数： " + btn2change;
                                            btn2change++;
                                            btn1.setText(msg);
                                        }
                                    }, 100L);
                                } catch (Exception e) {

                                }
                            }
                        }
                    }.start();
                }
                break;
        }
    }

    Handler handler = new Handler();

    private void bind() {
        adapter = new BaseListViewAdapter<Adv>(this, R.layout.test_listview_item) {
            @Override
            /**
             * 仅仅需要实现这个方法,处理布局文件里的每个子view
             */
            public void convert(ViewHolder vh, Adv adv) {
                vh.setTextViewText(R.id.tv1, adv.getId().toString()).setTextViewText(R.id.tv2, adv.getTitle());
            }
        };
        listView.setAdapter(adapter);
        for (Adv adv : datas) {
            adapter.add(adv);
        }
        adapter.notifyDataSetChanged();
    }

}
