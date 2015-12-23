package com.julun.vehicle.activities.examples;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.beans.Area;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.business.beans.Adv;
import com.julun.event.EventBusUtils;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.DateHelper;
import com.julun.utils.JsonHelper;
import com.julun.vehicle.R;
import com.julun.volley.utils.Requests;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

@ContentLayout(R.layout.activity_list_view_example)
public class ListViewExampleActivity extends BaseActivity {

    private static final String TAG = ListViewExampleActivity.class.getName();

    private List<Adv> datas = new ArrayList<>();

    @Bind(R.id.list_view)
    ListView listView;
    private BaseListViewAdapter<Adv> adapter;

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
//    @Bind(R.id.swipe_refresh_layout)
//    SimpleRefreshView srl;


    @AfterInitView
    private void afterInitView() {

//        srl.setSize(SwipeRefreshLayout.LARGE);
//        srl.setSize(SwipeRefreshLayout.DEFAULT);
        initEvents();

        bind();


//        srl.setColorSchemeColors();
    }


    private void initEvents() {

/*

        srl.setOnLoadListener(new SimpleRefreshView.OnLoadListener() {
            @Override
            public void loadMore() {

            }

            @Override
            public void onRefresh() {
                addDatas();
            }
        });
*/

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick() called with: " + "parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
            }
        });

    }


    private void addDatas() {
//        adapter.clear();
//        datas.clear();
        int counts = ((int) (Math.random() * 100) % 10) + 5;
        for (int index = 0; index < counts; index++) {
            int id = adapter.getCount() + index;
            Adv ad = new Adv();
            ad.setId(id);
            ad.setTitle("标题<" + id + ">");
            datas.add(ad);
            adapter.add(ad);
        }
        adapter.notifyDataSetChanged();
//        srl.setRefreshing(false);


    }

    boolean btn1Clicked = false;
    boolean btn2Clicked = false;


    int btn1change = 0;
    int btn2change = 0;

    @OnClick({R.id.btn1, R.id.btn2})
    public void onclick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn1:
                doRequest();
                break;
            case R.id.btn2:
                break;
        }
    }

    EventBus eventbus = EventBusUtils.getNonDefaultEventBus();

    private void doRequest() {
        String url = ApplicationUtils.BASE_URL_PREFIX + "index/area";
        String tag = url;


        DataChangeEvent<List<Area>> event = new DataChangeEvent<>();
        Requests.post0(url, tag, event, eventbus);
    }

    @Subscribe
    public void subscript(DataChangeEvent<List<Area>> event) {
        Log.i(TAG, JsonHelper.toJson(event));
    }


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
        addDatas();


    }


}
