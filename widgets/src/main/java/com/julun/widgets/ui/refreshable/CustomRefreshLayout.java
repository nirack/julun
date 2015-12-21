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
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by danjp on 2015/12/18.
 * 添加上拉加载功能
 */
public class CustomRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    private static final String TAG = CustomRefreshLayout.class.getSimpleName();

    private int mTouchSlop;
    private float mStartY;
    private float mEndY;
    private boolean mIsLoading = false;         //默认列表View未加载中

    private View mListViewFooterView;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;     //上拉加载监听器

//    private Context mContext;


    public CustomRefreshLayout(Context context) {
        super(context);
//        mContext = context;
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        mContext = context;
    }

//    public void setFooterViewLayout(int resLayoutId) {
//        mListViewFooterView = LayoutInflater.from(mContext).inflate(resLayoutId, null, false);
//    }

    public void addFooterView(View view) {
        mListViewFooterView = view;
    }

    public void addListView(ListView listView) {
        this.mListView = listView;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //这里可以重写下拉刷新的动画操作和文字显示,待续...
        /*if(mListView == null) {
            initListView();
        }*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:     //离开
//                mEndY = event.getRawY();
                if(canLoad()){
                    loadData();
                }
                break;
            case MotionEvent.ACTION_MOVE:   //滑动
                mEndY = event.getRawY();
                break;
            case MotionEvent.ACTION_DOWN:   //触摸
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /*private void initListView() {
        Log.d(TAG, "initListView");
        int count = this.getChildCount();
        if(count > 0) {
            View childView = this.getChildAt(0);
            if(childView instanceof ListView) {
                mListView = (ListView) childView;
                mListView.setOnScrollListener(this);
            }else if(childView instanceof GridView) {
                //待续 或者就直接用RecyleView
            }
        }else{
            Log.e(TAG, "请给CustomRefreshLayout添加子组件");
        }
    }*/

    /**
     * 是否上拉操作
     * @return
     */
    private boolean isPullUp() {
        Log.d(TAG, "isPullUp");
        return ( mStartY - mEndY ) >= mTouchSlop;
    }

    /**
     * 是否是列表最下面
     * @return
     */
    private boolean isBottom() {
        Log.d(TAG, "isBottom");
        if(mListView != null && mListView.getAdapter() != null){
            return mListView.getAdapter().getCount() - 1 == mListView.getLastVisiblePosition();
        }
        return false;
    }

    /**
     * 下拉加载 是否能操作
     * @return
     */
    private boolean canLoad() {
        Log.d(TAG, "canLoad");
        return isPullUp() && isBottom() && !mIsLoading;
    }

    private void loadData() {
        Log.d(TAG, "loadData");
        if(mOnLoadListener != null) {
            Log.d(TAG, "loadData2");
            this.setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean isLoading) {
        if(mListView == null) {
            Log.e(TAG, "请添加ListView或GridView...");
            return;
        }
        if(mListViewFooterView == null) {
            Log.e(TAG, "请添加上拉加载Layout");
        }

        mIsLoading = isLoading;
        if(mIsLoading) {
            mListView.addFooterView(mListViewFooterView, null, false);
        } else {
            mListView.removeFooterView(mListViewFooterView);
            mStartY = mEndY = 0;
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        this.mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "OnScroll");
        if(canLoad()) {
            loadData();
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }
}
