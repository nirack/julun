package com.julun.widgets.ui.refreshable;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.julun.utils.ToastHelper;
import com.julun.widgets.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.popwin.LoadProgressWin;
import com.julun.widgets.utils.PopWinHelper;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

/**
 * <p>
 * 无奈的只能先实现子view里有 ListView的时候上拉刷新,因为<b>获取是否到底view底部太困难</b>,RecyclerView也可较容易的实现,先过了这个版本在做，其他更多组件再往后排.
 * </p>
 */
public class ExtendedSwiperRefreshView extends SwipeRefreshLayout {
    private static final String TAG = ExtendedSwiperRefreshView.class.getName();
    private WeakReference<Context> cxt;
    /**
     * 系统认为最小的可以认定为是滑动的偏移量.
     */
    private int touchSlop;
    /**
     * listview实例
     */
    private ListView listView;

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
    private LoadProgressWin loadingPopWin;
    private boolean loadPopNotShow = true;
    private boolean buttom;


    public ExtendedSwiperRefreshView(Context context) {
        super(context);
        cxt = new WeakReference<Context>(context);
    }

    public ExtendedSwiperRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cxt = new WeakReference<Context>(context);
    }

    /**
     * 重写这个方法就是为了获取子view.
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout() called with: " + "changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
        if(null == listView){
            int childCount = this.getChildCount();
            if(0 == childCount){
                throw new RuntimeException("使用<" + TAG + ">需要有一个子view,目前仅仅支持ListView及其子类...对此表示非常抱歉 :< ");
            }
            for (int index = 0; index < childCount; index++) {
                View child = getChildAt(index);
                if(ListView.class.isAssignableFrom(child.getClass())){
                    listView = (ListView) child;
                    break;
                }
            }
        }
        if(listView == null){
            throw new RuntimeException("使用<" + TAG + ">需要有一个ListView或者其子类...对此非常抱歉 :< 后续对其他组件的支持即将开始...");
        }


        if(footerView == null){
            footerView = LayoutInflater.from(cxt.get()).inflate(R.layout.refresh_view_footer,null);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void loadFinish(){
        setRefreshing(false);
//        listView.removeFooterView(footerView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: // 按下
                startXindex = event.getRawY();
                startYindex = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:  // 移动
                lastYindex = event.getRawY();
                lastXindex = event.getRawX();
                if(isPullUp() && loadPopNotShow){
                    // TODO: 2015-12-10 需要增加一些效果 使得看起来更好看
                    loadPopNotShow = false;
                }

                break;

            case MotionEvent.ACTION_UP: // 抬起
                Log.d(TAG, "dispatchTouchEvent ACTION_UP called with: " + "event = [" + event + "]");
                lastYindex = event.getRawY();
                lastXindex = event.getRawX();

                if(loadPopNotShow){
                    // TODO: 2015-12-10 需要增加一些效果 使得看起来更好看
                    loadPopNotShow = false;
                }

                TextView text = (TextView) footerView.findViewById(R.id.txt_load_info);
                text.setText(getResources().getString(R.string.loading_text));

                if (canLoadMore()) {
                    listView.addFooterView(this.footerView);
                    ToastHelper.showLong(cxt.get(),"可以加载数据了！！！！");
                    if(null == refresAndLoadListener){
                        throw new RuntimeException("刷新和加载更多的监听 函数没有设置.查看 <" + TAG + ".setRefresAndLoadListener " + "> 方法 , 不要调用 setOnRefreshListener");
                    }
                    refresAndLoadListener.loadMore();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多数据...
     *  已经到达底部，并且没有正在加载...
     * @return
     */
    private boolean canLoadMore() {
        return isButtom() && !isLoading;
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
     * 是否已经到底底部.
     * @return
     */
    public boolean isButtom() {
        if(listView != null){
           return listView.getLastVisiblePosition() == (listView.getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 同时实现 系统自带的下拉刷新和 扩展的上拽 加载更多.两个方法应对不同的情形.
     * @param refresAndLoadListener
     */
    public void setRefresAndLoadListener(OnRefresAndLoadListener refresAndLoadListener) {
        this.refresAndLoadListener = refresAndLoadListener;
        setOnRefreshListener(refresAndLoadListener);
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnRefresAndLoadListener extends OnRefreshListener {
        /**
         * 加载更多数据.
         * 更多更方便的方法后续提供,比如制定url自动加载和更新UI，以及其他 。。。 暂时先做这么多.
         * 需要的listView要么在这个方法里传入要么在构造函数里传入,哪个更好，看以后的使用情况再做进一步处理.
         */
        public void loadMore();
    }

}
