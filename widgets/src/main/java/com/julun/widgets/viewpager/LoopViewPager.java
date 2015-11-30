package com.julun.widgets.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.julun.commons.reflect.ReflectUtil;
import com.julun.datas.PageResult;
import com.julun.datas.beans.Adv;
import com.julun.event.EventBusUtils;
import com.julun.event.events.BaseSimpleEvent;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.JsonHelper;
import com.julun.utils.StringHelper;
import com.julun.utils.ToastHelper;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;
import com.julun.widgets.R;
import com.julun.widgets.viewpager.anims.DepthPageTransformer;
import com.julun.widgets.viewpager.anims.ZoomOutPageTransformer;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 实现的内容：</br>
 * <ul>
 * <li>远程加载 数据来初始化,一共有多少个子view，都有哪些内容etc... </li>
 * <li>自动轮播，k可以在使用的时候在xml里配置轮播的时间间隔</li>
 * <li>定义每个子view的点击事件的处理函数</li>
 * </ul>
 */
public class LoopViewPager extends FrameLayout {
    //constants
    private static final String LOG_TAG = LoopViewPager.class.getName();
    //    轮播标记
    private static final int MARCH_ON = 0;

    //停止轮播标记1
    private static final int STOP_LOOP_FLAG = -1;

    //选中RadioButton 的标记
    private static final int CHECK_RADIO = 2;

    /**
     * 使用 ZOOM_OUT 动画
     *
     * @see com.julun.widgets.viewpager.anims.ZoomOutPageTransformer
     */
    public static final int ANI_ZOOM_OUT_PAGE = 1;
    /**
     * 使用 DEPTH_PAGE 动画.
     *
     * @see com.julun.widgets.viewpager.anims.DepthPageTransformer
     */
    public static final int ANI_DEPTH_PAGE = 2;

    //constants over

    //xml文件声明的属性

    //点击停止自动轮播,默认为true
    private boolean stopLoopOnTouch = true;
    //点击停止自动轮播,默认为true
    private boolean useIndicater = true;

    //是否点击RadioButton 与 viewpager联动
    private boolean indicaterReact = false;

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
    private Indicator indicator;

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
     * @see LoopViewPager#initializerClass
     * @see LoopViewPager.ViewItemInitializer
     */
    private ViewItemInitializer initializer;
    private LayoutInflater layoutInflater;
    private LoopAdapter adapter;

    //用户手按着屏幕的标记
    private Boolean isStopByTouch = false;

    EventBus mainBus = EventBusUtils.getNonDefaultEventBus();//viewpage触发事件

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = new WeakReference<Context>(context);

        extractAttrs(attrs);

        imitComponent();

        initEvents();
        mainBus.register(this);
    }

    /**
     * 抽取xml的配置属性.
     *
     * @param attrs
     */
    private void extractAttrs(AttributeSet attrs) {
        TypedArray ta = context.get().obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        //TODO 初始化自定义属性
        stopLoopOnTouch = ta.getBoolean(R.styleable.LoopViewPager_stopLoopOnTouch, true);
        useIndicater = ta.getBoolean(R.styleable.LoopViewPager_useIndicater, true);
        loopInterval = ta.getInt(R.styleable.LoopViewPager_loopInterval, 3000);
        animationTranslater = ta.getInt(R.styleable.LoopViewPager_animationTranslater, 0);
        setRemoteDataRecordsField(ta.getString(R.styleable.LoopViewPager_remoteDataRecordsField));
        requestUrl = ta.getString(R.styleable.LoopViewPager_requestUrl);

        indicaterReact = ta.getBoolean(R.styleable.LoopViewPager_indicaterReact,false);

        initializerClass = ta.getString(R.styleable.LoopViewPager_viewItemInitializer);
        try {
            initializer = (ViewItemInitializer) ReflectUtil.getClass(initializerClass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "获取初始化对象的接口出错,cause：\n" + e.getMessage());
            throw new RuntimeException(e);
        }

        itemClickHandler = ta.getString(R.styleable.LoopViewPager_itemClickHandler);
        if (StringHelper.isNotEmpty(itemClickHandler)) {
            try {
                itemClickListener = (ItemClickListener) ReflectUtil.getClass(itemClickHandler).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "获取 子view 的点击事件处理器 出错,cause：\n" + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        ta.recycle();
    }

    private void imitComponent() {

        viewPager = new ViewPager(this.context.get()) {
            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                //通过这个来实现手指没有离开之前,不继续轮播
                int action = MotionEventCompat.getActionMasked(ev);
                if (stopLoopOnTouch) {
                    synchronized (isStopByTouch){
                        if ((action == MotionEvent.ACTION_DOWN)) {
                            isStopByTouch = true;
                        } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                            isStopByTouch = false;
                        }
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
        };

        //设置动画
        useInnerAnimation(animationTranslater);

        this.addView(viewPager);

        if (useIndicater) {
//            indicator = new RadioGroup(this.context.get());
            indicator = new Indicator(context.get());
            indicator.setOrientation(LinearLayout.HORIZONTAL);
            indicator.setGravity(Gravity.BOTTOM);
            indicator.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            this.addView(indicator);
        }

        layoutInflater = LayoutInflater.from(this.context.get());

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
                if (indicaterReact) {
//                        indicator.check(indicator.getChildAt(realPosition).getId());
                    ViewPageMoveEvent stickyEvent = mainBus.getStickyEvent(ViewPageMoveEvent.class);
                    int checkedRadioButtonId = indicator.getCheckid();
                    Log.d("轮播---VP选中--stickyEvent ", " stickyEvent :  [" + JsonHelper.toJson(stickyEvent) + "] ,checkedRadioButtonId  is : " + checkedRadioButtonId + " , and will check : " + realPosition);
                    if (checkedRadioButtonId == realPosition) {
                        Log.d("轮播---VP选中--stickyEvent ", " 按钮点击触发的事件，返回，不再继续....");
                        return;
                    }
                    if (stickyEvent == null) {
                        Log.d("轮播---VP选中--stickyEvent ", " 不是   按钮点击触发的事件，继续....");
                        mainBus.post(ViewPageMoveEvent.get(ViewPageMoveEvent.CHANGE_PAGE, realPosition));
                    } else {
                        mainBus.removeAllStickyEvents();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = false)
    public void onEventMainThread(ViewPageMoveEvent env) {
        Log.d("轮播-", "onEventMainThread() called with: " + "env = [" + JsonHelper.toJson(env) + "] , currentThread 当前ID " + Thread.currentThread().getName());
        int action = env.actionSource;
        switch (action) {
            case ViewPageMoveEvent.CHECK_RADIO:
                Log.d("轮播-点击了Button ，将要变更VP", "onEventMainThread 当前 VP ID : " + env.position + " , 将要被点击的ID：　" + env.position + " ,isStopByTouch := " + isStopByTouch );
                if(!isStopByTouch){
                    viewPager.setCurrentItem(env.position);
                }
                break;
            case ViewPageMoveEvent.CHANGE_PAGE:
                int checked = indicator.getCheckid();
                Log.d("轮播将要点击按钮", "onEventMainThread 当前ID : " + checked + " , 将要被点击的ID：　" + env.position + "");
                if(checked == env.position){
                    return;
                }
                indicator.check(env.position);
                break;
            case ViewPageMoveEvent.LOOP_PLAY:
                Log.d("自动轮播，将要变更VP", "当前ID : " + env.position   + " , 将要被点击的ID：　" + env.position + "");
                if(!isStopByTouch){//被停止了
                    viewPager.setCurrentItem(env.position);
                }
                break;
        }
    }

    /**
     * 添加RadioButton.
     *
     * @param index
     */
    private void addIndicators(int index) {
        ImageButton btn = new ImageButton(this.context.get());
        btn.setImageResource(R.drawable.ic_circle_radio_btn_off);
        btn.getBackground().setAlpha(100);//设置背景透明
//        btn.setBackgroundResource(R.drawable.ic_circle_radio_btn_off);
        btn.setPadding(12, 0, 12, 2);//设置一些编剧，不要让按钮靠的太近
        btn.setTag(index);

        if(indicaterReact){
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("---选中事件", "RadioButton onClick() called with: " + "v = [" + v + "]");
                    int realCount = getRealCount();
                    int currentItem = viewPager.getCurrentItem();
                    int index = (int) v.getTag();
                    if (index == getRealPosition(currentItem)) {
                        return;
                    }
                    // TODO: 2015-11-25 测试过后可以删掉这个变量
                    final Integer next = currentItem / realCount * realCount + index;
//                    mainBus.postSticky(ViewPageMoveEvent.get(ViewPageMoveEvent.CHECK_RADIO, next,true));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(next,true);
                        }
                    });
                    indicator.check((Integer) v.getTag());
                }
            });
        }

        indicator.addView(btn);
    }

    Handler handler = new Handler();

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
                    viewPager.setCurrentItem(getRealCount() * 200);
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

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.d("移动下一步", "run() called with: " + " , now := " + System.currentTimeMillis());
                        Thread.sleep(loopInterval);
                        mainBus.post(ViewPageMoveEvent.get(ViewPageMoveEvent.LOOP_PLAY, viewPager.getCurrentItem() + 1));
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();


    }

    private <T> void createChildrenViews(List<T> array) {
        int size = array.size();
        for (Integer index = 0; index < size; index++) {
            Object obj = array.get(index);
            //初始化的时候保证了 initializer不为空,否则抛出运行时异常
            View view = initializer.initializeView(this.context.get(), obj, layoutInflater);
            childrenViews.put(index, view);
            dataBinded.put(index, obj);
            if (useIndicater && size > 1) {
                addIndicators(index);
            }
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
     * @see LoopViewPager#ANI_ZOOM_OUT_PAGE
     * @see LoopViewPager#ANI_DEPTH_PAGE
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
                itemClickListener.doOnClick(dataBinded.get(positionWrap));
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
    public static interface ItemClickListener {
        /**
         * @param data 与当前view绑定的数据对象.
         */
        public void doOnClick(Object data);
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

    private static class ViewPageMoveEvent extends BaseSimpleEvent {
        public int actionSource;// 事件的触发源头,1:点击radionButton,2:滑动,3:自动轮播
        public int position;// viewpage的目标item 或者 RadioButton的ID
        public static final int CHECK_RADIO = 1;//点击按钮
        public static final int CHANGE_PAGE = 2;//vp页面变更
        public static final int LOOP_PLAY = 3;//轮播Vp
        public boolean stop = false;//是否就此打住事件的传播

        public ViewPageMoveEvent(int actionSource, int position) {
            super(MARCH_ON);
            this.actionSource = actionSource;
            this.position = position;
        }

        /**
         * @param action
         * @param position
         * @return
         */
        public static ViewPageMoveEvent get(int action, int position,boolean ... stopHere) {
            ViewPageMoveEvent env = new ViewPageMoveEvent(action, position);
            if(stopHere !=null && stopHere.length > 0 ){
                env.stop = stopHere[0];
            }
            return env;
        }
    }

    private static class Indicator extends LinearLayout{
        public Indicator(Context context) {
            super(context);
        }
        private int checkid = -1;
        public void check(int id ){
            int lastCheckedId = this.getCheckid();
            Log.d(this.getClass().getName(), "Indicator check() " + "id = [" + id + "]  ， "+ " lastCheckedId = [" + lastCheckedId + "]");
            if(id == lastCheckedId){
                return;
            }
            ImageButton btn = (ImageButton) this.getChildAt(lastCheckedId);
            if(lastCheckedId != -1){
                btn.setImageResource(R.drawable.ic_circle_radio_btn_off);
            }
            this.checkid = id;
            btn = (ImageButton) this.getChildAt(id);
            btn.setImageResource(R.drawable.ic_circle_radio_btn_on);
        }

        public int getCheckid() {
            return checkid;
        }
    }
}
