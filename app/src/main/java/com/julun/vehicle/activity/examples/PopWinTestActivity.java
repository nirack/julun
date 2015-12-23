package com.julun.vehicle.activity.examples;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.utils.ScreenUtils;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.popwin.BasicEasyPopupWindow;
import com.julun.widgets.popwin.LoadProgressWin;
import com.julun.widgets.filterbar.SearchFilterView;
import com.julun.widgets.refreshable.RefreshableView;
import com.julun.widgets.utils.PopWinHelper;
import com.julun.widgets.viewholder.listview.ViewHolder;

import butterknife.Bind;
import butterknife.OnClick;

@ContentLayout(R.layout.activity_pop_win_test)
public class PopWinTestActivity extends BaseActivity implements RefreshableView{
    public static final String TAG = PopWinTestActivity.class.getName();
    @Bind(R.id.show_loading)
    Button showLoading;
    @Bind(R.id.stop_anmi)
    Button stop_anmi;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.show_filter_win)
    Button filterWin;

    private LoadProgressWin loadingPopWin;
    private BaseListViewAdapter<Integer> listAdapter;

    private Integer added = 0;

    private int countsEachTime = 10;


    @AfterInitView
    public void afterInitView(){
        refreshMe();

        listAdapter = new BaseListViewAdapter<Integer>(this, R.layout.addr_item) {
            /**
             * 具体的adapter只需要实现这个方法即可.
             * 里面有具体要做的操作.
             *
             * @param vh
             * @param intVal
             */
            @Override
            public void convert(ViewHolder vh, Integer intVal) {
                vh.setTextViewText(R.id.addr_name, "数字：" + intVal);
            }
        };
        initDatas();
        listView.setAdapter(listAdapter);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG + listView, "onTouch called with: " + "v = [" + v + "], event = [" + event + "]");
                return false;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //鼠标点击为 1，放开为 0
                Log.d(TAG + listView, "onScrollStateChanged called with: " + "view = [" + view + "], scrollState = [" + scrollState + "]");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d(TAG + listView, "onScroll called with: " + "view = [" + view + "], firstVisibleItem = [" + firstVisibleItem + "], visibleItemCount = [" + visibleItemCount + "], totalItemCount = [" + totalItemCount + "]");
            }
        });


    }

    private void initDatas() {
        for (int index = 0; index < countsEachTime; index++) {
            listAdapter.add(added + index);
        }
    }


    @OnClick({R.id.show_loading, R.id.stop_anmi,R.id.show_filter_win})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.show_loading:
                showLoading(view);
                break;
            case R.id.stop_anmi:
                if (loadingPopWin != null) {
//                    loadingPopWin.stop();
                }
                break;
            case R.id.show_filter_win:
                showFilterWin(view);
                break;
        }

    }

    private void showFilterWin(final View view) {
        SearchFilterView contentView = new SearchFilterView(this);

        BasicEasyPopupWindow popWin = PopWinHelper.getPopWin(this, contentView).width(ScreenUtils.getScreenWidth());
        int color = R.color.turquoise;//各种颜色试一下
        color = R.color.lightskyblue;
        color = R.color.darkseagreen;
        color = R.color.lightblue;//lightcyan
        color = R.color.lightcyan;//
        popWin.setBackgroundDrawable(getResources().getDrawable(color));
        popWin.showAsDropDown(view);
    }

    private void showLoading(final View view) {
        FrameLayout contentView = new FrameLayout(this);

        View inflate = LayoutInflater.from(this).inflate(R.layout.refresh_pop_win_content, null);

        loadingPopWin = PopWinHelper.getLoadingPopWin(this, inflate, 0);

        loadingPopWin.showAsDropDown(stop_anmi);
//        loadingPopWin.getBackground().setAlpha(10);
        loadingPopWin.start();
//        animation.setDuration(1000);
//        inflate.startAnimation(animation);


//        win.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_title_bg_shape));//背景如果不设置，则弹出框不会 dismiss

    }

    @Override
    public void refreshMe() {

    }
}
