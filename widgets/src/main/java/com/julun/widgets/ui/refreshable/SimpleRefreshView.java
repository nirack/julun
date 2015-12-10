package com.julun.widgets.ui.refreshable;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.julun.widgets.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015-12-09.
 */
public class SimpleRefreshView extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    private static final String TAG = SimpleRefreshView.class.getName();
    private WeakReference<Context> cxt;
    /**
     * 系统认为最小的可以认定为是滑动的偏移量.
     */
    private int touchSlop;
    /**
     * listview实例
     */
    private ListView listView;
    private BaseListViewAdapter<?> adapter;

    private String [] tipinfo = new String[4];

    public static final int PRE_LOADING_INFO = 0;
    public static final int DRAG_INFO = 1;
    public static final int LOADING_INFO = 2;
    public static final int NO_MORE_DATA_INFO = 3;


    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnRefresAndLoadListener refresAndLoadListener;

    private View footerView;//底部显示更新中的view

    private float startXindex;//起始x坐标 ,
    private float startYindex;//其实y坐标

    private float lastXindex;//最后的x坐标
    private float lastYindex;//最后的y坐标
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    public SimpleRefreshView(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: // 按下
                startXindex = event.getRawY();
                startYindex = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:  // 移动
                lastYindex = event.getRawY();
                lastXindex = event.getRawX();
                if (isPullUp() ) {
                    changeFooterInfo(DRAG_INFO);
                }
                break;

            case MotionEvent.ACTION_UP: // 抬起
                Log.d(TAG, "dispatchTouchEvent ACTION_UP called with: " + "event = [" + event + "]");
                lastYindex = event.getRawY();
                lastXindex = event.getRawX();

                if (canLoad() && isPullUp() ) {
                    changeFooterInfo(LOADING_INFO);
                    loadData();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                changeFooterInfo(PRE_LOADING_INFO);
                break;
            default:
                break;
        }



        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading ;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (startYindex - lastYindex) >= touchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (refresAndLoadListener == null) {
            throw new RuntimeException("需要设置刷新的Listener,调用  setRefresAndLoadListener ,而不是 setOnRefreshListener");
        }
        setLoading(true);// 设置状态
        refresAndLoadListener.loadMore();
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
    }

    /**
     * 同时实现 系统自带的下拉刷新和 扩展的上拽 加载更多.两个方法应对不同的情形.
     * @param refresAndLoadListener
     */
    public void setRefresAndLoadListener(OnRefresAndLoadListener refresAndLoadListener) {
        super.setOnRefreshListener(refresAndLoadListener);
        this.refresAndLoadListener = refresAndLoadListener;
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
//            listView.addFooterView(footerView);
//            changeFooterInfo(LOADING_INFO);
//            adapter.notifyDataSetChanged();
        } else {
            onLoadFinish();
        }
    }

    private void changeFooterInfo(int index) {
        String string = tipinfo[index];
        TextView txt = (TextView) footerView.findViewById(R.id.txt_load_info);
        txt.setText(string);
    }

    public void onLoadFinish(){
        setRefreshing(false);
        changeFooterInfo(PRE_LOADING_INFO);
        isLoading = false;
//        listView.removeFooterView(footerView);
//        adapter.notifyDataSetChanged();
        startYindex = 0;
        lastYindex = 0;
        startXindex = 0;
        lastXindex = 0;
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        boolean isButtom = adapter.getCount() /* - 1 */== listView.getLastVisiblePosition();
        Log.d(TAG, "isBottom() called with:  isButtom = " + isButtom + " ,adapter.getCount() = " + adapter.getCount() + " , lastVisible = " + listView.getLastVisiblePosition() );
        return isButtom;
    }


    private void init(Context context, AttributeSet attrs) {
        tipinfo[PRE_LOADING_INFO] = getResources().getString(R.string.load_more_tip);
        tipinfo[DRAG_INFO] = getResources().getString(R.string.prepare_loading_text);
        tipinfo[LOADING_INFO] = getResources().getString(R.string.loading_text);
        tipinfo[NO_MORE_DATA_INFO] = getResources().getString(R.string.nothing_more);


        // TODO: 2015-12-09 自定义属性的填充
        cxt = new WeakReference<Context>(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        footerView = new TextView(context);
        initAttrs();

        listView = new ListView(cxt.get());
        LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(layoutParam);

        footerView = LayoutInflater.from(cxt.get()).inflate(R.layout.refresh_view_footer,null);
        changeFooterInfo(PRE_LOADING_INFO);
        listView.addFooterView(footerView);

        addView(listView);

    }

    public void setAdapter(BaseListViewAdapter<?> adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    private void initAttrs() {
        // TODO: 2015-12-10
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, "onScrollStateChanged() called with: " + "view = [" + view + "], scrollState = [" + scrollState + "]");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll() called with: " + "view = [" + view + "], firstVisibleItem = [" + firstVisibleItem + "], visibleItemCount = [" + visibleItemCount + "], totalItemCount = [" + totalItemCount + "]");
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnRefresAndLoadListener extends OnRefreshListener {
        public void loadMore();
    }

}
