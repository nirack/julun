package com.julun.widgets.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.julun.commons.reflect.ReflectUtil;
import com.julun.datas.PageResult;
import com.julun.datas.beans.Adv;
import com.julun.exceptions.ConfigException;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.StringHelper;
import com.julun.utils.ToastHelper;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;
import com.julun.widgets.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;
import com.julun.widgets.viewpager.anims.DepthPageTransformer;
import com.julun.widgets.viewpager.anims.ZoomOutPageTransformer;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现的内容：</br>
 * <ul>
 * <li>远程加载 数据来初始化,一共有多少个子view，都有哪些内容etc... </li>
 * <li>自动轮播，k可以在使用的时候在xml里配置轮播的时间间隔</li>
 * <li>定义每个子view的点击事件的处理函数</li>
 * </ul>
 */
public class SimpleLoopViewPager extends FrameLayout {
    //constants
    private static final String LOG_TAG = SimpleLoopViewPager.class.getName();
    //    轮播标记
    private static final int MARCH_ON = 0;

    //停止轮播标记1
    private static final int STOP_LOOP_FLAG = -1;

    public static final int CHANGE_PAGE = 2;//vp页面变更

    /**
     * 使用 ZOOM_OUT 动画
     *
     * @see ZoomOutPageTransformer
     */
    public static final int ANI_ZOOM_OUT_PAGE = 1;
    /**
     * 使用 DEPTH_PAGE 动画.
     *
     * @see DepthPageTransformer
     */
    public static final int ANI_DEPTH_PAGE = 2;

    //constants over

    //xml文件声明的属性

    //点击停止自动轮播,默认为true
    private boolean stopLoopOnTouch = false;

    //轮播间隔的时间,单位 毫秒
    private int loopInterval = 3000;

    //    使用何种动画？
    private int animationTranslater = 0;
    //远程请求数据返回的结果集 object ,如果是Map类型而不是array类型,那么认为应该 object[$recordsField]为实际要取的结果集,如果该字段为空,则认为返回的是array
    private String remoteDataRecordsField;

    //请求数据的地址,可以省略http以及应用名称
    private String requestUrl;

    /**
     * 初始化组件使用的类名,需要全名.
     */
    private String initializerClass;

    /**
     * 每个子view的点击事件的处理
     */
    private String itemClickHandler;

    //xml文件声明的属性  voer ~

    private WeakReference<Context> context;

    private ViewPager viewPager;
    private SimpleGridViewIndicator indicator;

    private BaseListViewAdapter<Integer> gridAdapter;

    private Map<Integer, ViewPager.PageTransformer> aniTranslaterMap = new HashMap<>();

    private Map<Integer, View> childrenViews = new HashMap<>();

    //标记子View是否已分配点击事件
    private Map<Integer, Boolean> listenerAssignFlag = new HashMap<>();

    //与view绑定的数据
    private Map<Integer, Object> dataBinded = new HashMap<>();

    //点击事件
    private ItemClickListener itemClickListener;

    private int realItemCount;

    /**
     * 根据 initializerClass 获取的类对象.
     *
     * @see SimpleLoopViewPager#initializerClass
     * @see SimpleLoopViewPager.ViewItemInitializer
     */
    private ViewItemInitializer initializer;
    private LayoutInflater layoutInflater;
    private LoopAdapter adapter;

    //用户手按着屏幕的标记
    private Boolean isStopByTouch = false;

    Handler uihandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_LOOP_FLAG:
                    ToastHelper.showLong(context.get(),"停止轮播....");
                    uihandler.removeMessages(MARCH_ON);
                    break;
                case MARCH_ON:
                    int currentItem = viewPager.getCurrentItem();
                    int position = currentItem + 1;
                    Integer realPosition = getRealPosition(position);
                    viewPager.setCurrentItem(position);
                    uihandler.sendEmptyMessageDelayed(MARCH_ON, 3000);
                    break;
            }
        }
    };

    public SimpleLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = new WeakReference<Context>(context);

        extractAttrs(attrs);

        imitComponent();

        initEvents();
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        //通过这个来实现手指没有离开之前,不继续轮播
        int action = MotionEventCompat.getActionMasked(ev);
        if (stopLoopOnTouch) {
            synchronized (isStopByTouch) {
                if ((action == MotionEvent.ACTION_DOWN)) {
                    isStopByTouch = true;
                } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                    isStopByTouch = false;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 抽取xml的配置属性.
     *
     * @param attrs
     */
    private void extractAttrs(AttributeSet attrs) {
        TypedArray ta = context.get().obtainStyledAttributes(attrs, R.styleable.SimpleLoopViewPager);
        //TODO 初始化自定义属性
        stopLoopOnTouch = ta.getBoolean(R.styleable.SimpleLoopViewPager_stopLoopOnTouch, false);
        loopInterval = ta.getInt(R.styleable.SimpleLoopViewPager_loopInterval, 3000);
        animationTranslater = ta.getInt(R.styleable.SimpleLoopViewPager_animationTranslater, 0);
        setRemoteDataRecordsField(ta.getString(R.styleable.SimpleLoopViewPager_remoteDataRecordsField));
        requestUrl = ta.getString(R.styleable.SimpleLoopViewPager_requestUrl);

        initializerClass = ta.getString(R.styleable.SimpleLoopViewPager_viewItemInitializer);
        try {
            initializer = (ViewItemInitializer) ReflectUtil.getClass(initializerClass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "获取初始化对象的接口出错,cause：\n" + e.getMessage());
            throw new RuntimeException(e);
        }

        itemClickHandler = ta.getString(R.styleable.SimpleLoopViewPager_itemClickHandler);
        if (StringHelper.isNotEmpty(itemClickHandler)) {
            try {
                itemClickListener = (ItemClickListener) ReflectUtil.getClass(itemClickHandler).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "获取 子view 的点击事件处理器 出错,cause：\n" + e.getMessage());
                throw new RuntimeException(e);
            }
        }else{
            if(!ItemClickListener.class.isAssignableFrom(context.get().getClass())){
                throw new RuntimeException(new ConfigException("需要为 " + this.getClass().getName() + "配置 itemClickHandler ,或者引入 该组件的 Activity 实现 " + ItemClickListener.class.getName() + "接口."));
            }
            itemClickListener = (ItemClickListener) context.get();
        }

        ta.recycle();
    }

    private void imitComponent() {

        layoutInflater = LayoutInflater.from(this.context.get());
        View realView = layoutInflater.inflate(R.layout.simple_loop_view_pager, null);
//        viewPager = (ViewPager) realView.findViewById(R.id.view_pager);
        viewPager = new ViewPager(context.get());

        //设置动画
        useInnerAnimation(animationTranslater);

        gridAdapter = new BaseListViewAdapter<Integer>(context.get(), R.layout.view_pager_indicator) {
            @Override
            public void convert(ViewHolder vh, Integer adv) {
            }
        };

        indicator = new SimpleGridViewIndicator(context.get());
        indicator.setBackgroundResource(R.color.ivory);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        layoutParams.gravity = Gravity.BOTTOM;
        indicator.setLayoutParams(layoutParams);
        indicator.setGravity(Gravity.CENTER_HORIZONTAL);

        indicator.setOrientation(LinearLayout.HORIZONTAL);
        indicator.setGravity(Gravity.BOTTOM);
        indicator.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

        this.addView(viewPager);
        this.addView(indicator);
        //请求处理数据
        requestAndProcessData(requestUrl);
    }

    private void initEvents() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("轮播---ViewPager选中事件", "onPageSelected() called with: " + "position = [" + position + "]" + context.get().getClass().getName());
                Integer realPosition = getRealPosition(position);
                indicator.check(realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * 添加RadioButton.
     *
     * @param index
     */
    private void addIndicators(Integer index) {
        gridAdapter.add(index);
        Button btn = new Button(context.get());
        btn.setBackgroundResource(R.drawable.app_title_bg_shape);
        btn.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        param.weight =1;
        param.leftMargin = 3;
        param.rightMargin = 3;
        btn.setLayoutParams(param);
        btn.setTag(index);
        this.indicator.addView(btn);
    }

    public void requestAndProcessData(String requestUrl) {
        requestUrl = StringHelper.ifEmpty(requestUrl, this.requestUrl);

        if (StringHelper.isEmpty(requestUrl)) {
            throw new IllegalArgumentException("缺少参数 [ requestUrl ]");
        }

        VolleyRequestCallback<PageResult<Adv>> callback = new VolleyRequestCallback<PageResult<Adv>>(this.getContext()) {
            @Override
            public void doOnSuccess(PageResult response) {
                realItemCount = response.getRecords().size();
                createChildrenViews(response.getRecords());
                adapter = new LoopAdapter();
                viewPager.setAdapter(adapter);
                if (realItemCount > 1) {
                    viewPager.setCurrentItem(getRealCount() * 200 - 1);
                    Log.d(this.getClass().getName(), "doOnSuccess() called with: " + "response = [" + response + "] , thread  := " + Thread.currentThread().getName());
                    startLoop();
                }
            }

            @Override
            public void doOnFailure(VolleyError error) {
                String message = error.getMessage();
                Throwable cause = error.getCause();
                message = StringHelper.ifEmpty(message, cause != null ? cause.getMessage() : "未能获取错误信息。。。");
                String msg = "错误发生：" + message + " , 本次请求 耗时： " + error.getNetworkTimeMs();
                ToastHelper.showLong(this.getContext().get(), msg);
            }
        };

        final String reqUrl = requestUrl;
        String url = StringHelper.isNotHttpUrl(reqUrl) ? ApplicationUtils.BASE_URL_PREFIX + reqUrl : reqUrl;
        Requests.post(url, url, callback);
    }

    /**
     * 避免滑动冲突/
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (viewPager != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    viewPager.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    viewPager.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    viewPager.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_MOVE:
                    viewPager.requestDisallowInterceptTouchEvent(true);
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void startLoop() {
        final long loopInterval = getLoopInterval();
        if (loopInterval < 1) {
            return;
        }
        this.uihandler.sendEmptyMessage(MARCH_ON);
    }

    public void stopLoop(){
        uihandler.sendEmptyMessage(STOP_LOOP_FLAG);
    }

    private <T> void createChildrenViews(List<T> array) {
        int size = array.size();
        for (Integer index = 0; index < size; index++) {
            Object obj = array.get(index);
            //初始化的时候保证了 initializer不为空,否则抛出运行时异常
            View view = initializer.initializeView(this.context.get(), obj, layoutInflater);
            childrenViews.put(index, view);
            dataBinded.put(index, obj);
            addIndicators(index);
        }
    }

    /**
     * 直接设置指定的，不在 这个类定义的范围内的动画效果.
     *
     * @param transformer
     */
    public void setViewPagerAnimation(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
    }

    /**
     * 设置ViewPager动画效果(使用内部动画效果),
     * 调用这个方法之前，一定要初始化好 ViewPager 对象
     *
     * @param translater
     * @see SimpleLoopViewPager#ANI_ZOOM_OUT_PAGE
     * @see SimpleLoopViewPager#ANI_DEPTH_PAGE
     */
    public void useInnerAnimation(int translater) {
        // TODO: 2015-11-08  使用原生的接口设置动画效果,兼容性稍后再做
        // TODO: 2015-11-25 不做兼容了
        Integer atWrapped = new Integer(translater);
        ViewPager.PageTransformer transformer = aniTranslaterMap.get(atWrapped);
        switch (translater) {
            case ANI_ZOOM_OUT_PAGE:
                if (null == transformer) {
                    transformer = new ZoomOutPageTransformer();
                }
                break;
            case ANI_DEPTH_PAGE:
                if (null == transformer) {
                    transformer = new DepthPageTransformer();
                }
                break;
        }
        if (transformer != null) {
            aniTranslaterMap.put(atWrapped, transformer);
            viewPager.setPageTransformer(true, transformer);
        }
    }

    private class LoopAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            int maxValue = Integer.MAX_VALUE;
            return maxValue;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Integer positionWrap = getRealPosition(position);
            View view = childrenViews.get(positionWrap);
            assignViewPagerItemOnclick(view, positionWrap);
            container.removeView(view);
            ViewParent parent = view.getParent();
            if (null != parent) {
                ViewGroup vg = (ViewGroup) parent;
                vg.removeView(view);
            }
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO: 2015-11-08 啥都不干
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private void assignViewPagerItemOnclick(View view, final Integer positionWrap) {
        boolean hasOnClickListeners = listenerAssignFlag.get(positionWrap) == Boolean.TRUE || false;
        if (hasOnClickListeners || StringHelper.isEmpty(itemClickHandler)) {
            return;
        }

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onViewPagerItemClick(dataBinded.get(positionWrap));
            }
        });
        listenerAssignFlag.put(positionWrap, Boolean.TRUE);
    }

    @NonNull
    private Integer getRealPosition(int position) {
        int realCount = getRealCount();
        Integer positionWrap = new Integer(position % realCount);
        if (position < 0) {
            positionWrap = realCount + position;
        }
        return positionWrap;
    }

    /**
     * 获取真实的子view的数目
     *
     * @return
     */
    public int getRealCount() {
        int size = childrenViews.size();
        return realItemCount;
    }

    /**
     * 实现类必须提供无参构造函数.以方便通过映射方式获取实例.
     */
    public static interface ViewItemInitializer<T> {
        /**
         * 完全自主化,随便返回任意的View都可以.返回的View将会作为ViewPager的子view.
         *
         * @param context
         * @param data           在实现类里要自己转型为实际的对象.使用之前自己要知道.
         * @param layoutInflater 如果子view有定义好的布局文件...
         * @return
         */
        public View initializeView(Context context, T data, final LayoutInflater layoutInflater);
    }

    /**
     * 每个子view点击的响应函数.
     */
    public static interface ItemClickListener<T> {
        /**
         * @param data 与当前view绑定的数据对象.
         */
        public void onViewPagerItemClick(T data);
    }

    public long getLoopInterval() {
        return loopInterval;
    }

    /**
     * 设置远程请求的地址.
     *
     * @param remoteDataRecordsField
     */
    public void setRemoteDataRecordsField(String remoteDataRecordsField) {
        this.remoteDataRecordsField = remoteDataRecordsField;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

}
