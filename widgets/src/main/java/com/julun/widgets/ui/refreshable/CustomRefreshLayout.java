package com.julun.widgets.ui.refreshable;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.julun.datas.localDataBase.LocalDatabaseOperator;
import com.julun.widgets.R;

/**
 * Created by danjp on 2015/12/18.
 * 添加上拉加载功能
 */
public class CustomRefreshLayout extends SwipeRefreshLayout implements OnScrollListener {

    private static final String TAG = CustomRefreshLayout.class.getSimpleName();

    private boolean mIsLoading = false;         //默认列表View未加载中

    private ListView mListView;
    private OnLoadListener mOnLoadListener;     //上拉加载监听器
    private View mListViewFooterView;
    private TextView mLoadInfoTv;
    private ImageView mWheelIv;


    public CustomRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs, ListView listView) {
        super(context, attrs);
        init(context);
        addListView(listView);
    }

    private void init(Context context) {
        //可手动调整下拉刷新框
        setColorSchemeColors(new int[]{R.color.green, R.color.transparent, R.color.green, R.color.transparent});

        mListViewFooterView = LayoutInflater.from(context).inflate(R.layout.refresh_view_footer, null);
        mLoadInfoTv = (TextView)mListViewFooterView.findViewById(R.id.txt_load_info);
        mWheelIv = (ImageView)mListViewFooterView.findViewById(R.id.img_wheel_like);
    }

    public void setMainColor(int colorResId) {
        setColorSchemeColors(colorResId, R.color.transparent, colorResId, R.color.transparent);
    }

    public void addListView(ListView listView) {
        this.mListView = listView;
        this.mListView.setOnScrollListener(this);
        this.mListView.addFooterView(mListViewFooterView);
        hideFooterView();
    }

    /**
     * 下拉加载数据
     */
    private void loadData() {
        Log.d(TAG, "loadData");
        if(mOnLoadListener != null) {
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    /**
     * 设置下拉加载footerview
     * @param isLoading
     */
    public void setLoading(boolean isLoading) {
        Log.d(TAG, "setLoading");
        if(mListView == null) {
            Log.e(TAG, "请添加ListView或GridView...");
            return;
        }
        mIsLoading = isLoading;
        if(mIsLoading) {
            mLoadInfoTv.setVisibility(VISIBLE);
            mWheelIv.setVisibility(VISIBLE);
        } else {
            hideFooterView();
        }
    }

    /**
     * 首次加载，由于listView.addFooterView，下部加载栏会显示，需要在首次加载完数据后隐藏
     */
    public void hideFooterView() {
        mLoadInfoTv.setVisibility(GONE);
        mWheelIv.setVisibility(GONE);
        mListViewFooterView.setMinimumHeight(0);
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        this.mOnLoadListener = loadListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, "onScrollStateChanged");
        //滑动离开屏幕
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && !mIsLoading && mListView.getAdapter().getCount() - 1 <= mListView.getLastVisiblePosition()) {
            loadData();
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }
}
