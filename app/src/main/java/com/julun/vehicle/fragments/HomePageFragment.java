package com.julun.vehicle.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.julun.annotations.business.BusinessBean;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.IndexService;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.datas.beans.County;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.CollectionHelper;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;
import com.julun.vehicle.activities.scan.QRCodeActivity;
import com.julun.vehicle.activities.search.SearchProdActivity;
import com.julun.widgets.adapters.StaggedGridLayoutAdapter;
import com.julun.widgets.popwin.BasicEasyPopupWindow;
import com.julun.widgets.utils.PopWinHelper;
import com.julun.widgets.viewpager.SimpleLoopViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentLayout(R.layout.fragment_home_page)
public class HomePageFragment extends BaseFragment {
    //位置相关
    private MyLocationConfiguration.LocationMode model;//模式
    private LocationClient locationClient;

    //子view
    @Bind(R.id.home_tool_bar)
    LinearLayout homeToolBar;
    @Bind(R.id.location_city_btn)
    TextView cityBtn;
    @Bind(R.id.search_btn)
    EditText searchBtn;
    @Bind(R.id.scan_qr_code_btn)
    TextView scanQrCodeBtn;
    @Bind(R.id.show_my_msg_btn)
    TextView showMsgBtn;
    @Bind(R.id.view_pager)
    SimpleLoopViewPager viewPager;

    private boolean locationInited = false;

    @BusinessBean
    private IndexService indexService;
    private BDLocationListener bdLocationListener;

    public HomePageFragment() {
    }

    @AfterInitView
    public void afterInitViews() {
        cityBtn.setClickable(true);
        if(!locationInited){
            Log.d(LOG_TAG_CLASS_NAME + " 现在地址 ", "afterInitViews() called with: " + "");
            cityBtn.setText("请选择");
            initLocation();
            locationInited = true;
        }
    }

    /**
     * 初始化地址信息.
     */
    private void initLocation() {
        model = MyLocationConfiguration.LocationMode.NORMAL;
        final Context context = getContextActivity().getApplicationContext();
        locationClient = new LocationClient(context);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setOpenGps(true);
        locationClientOption.setScanSpan(1000);

        locationClient.setLocOption(locationClientOption);

        bdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.i(LOG_TAG_CLASS_NAME, "定位完成,现在地址： " + bdLocation.getAddrStr());
                Address address = bdLocation.getAddress();
                String cityCode = address.cityCode;
                String country = address.country;//中国
                String city = bdLocation.getCity();
                // TODO: 可以成功获取地址，配置没问题了
                try {
                    Context contextActivity = getContextActivity();
                    ToastHelper.showLong(contextActivity, "空指针 ？？？  " + (cityBtn == null) + "  city: " + city);
                } catch (Exception e) {
                    Log.e("错误错误",e.getMessage());
                    e.printStackTrace();
                }
                cityBtn.setText(city);
//                ToastHelper.showLong(context, "county := " + country + " , city := " + city + " , cityCode := " + cityCode + "\n address: " + address.toString());
                locationClient.stop();
            }
        };

        locationClient.registerLocationListener(bdLocationListener);
        locationClient.start();
        Log.i(LOG_TAG_CLASS_NAME, "已经开始定位： ");
    }

    @OnClick({R.id.location_city_btn, R.id.search_btn, R.id.scan_qr_code_btn, R.id.show_my_msg_btn})
    public void toolsClick(View view) {
        switch (view.getId()) {
            case R.id.location_city_btn:
                showPopupWindow(view);
                break;
            case R.id.search_btn:
                //                jump2Activity();
                jump2Activity(SearchProdActivity.class);
                break;
            case R.id.scan_qr_code_btn:
                jump2Activity(QRCodeActivity.class);
                break;
            case R.id.show_my_msg_btn:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(HomePageFragment.class.getName(), "onResume() called with: " + "");
    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO: 2015-12-01 需要在暂停的时候,让viewpager不再继续循环播放
        ToastHelper.showLong(getContextActivity(), "暂停，即将前往另外一个 Activity");
        viewPager.stopLoop();
        if(locationClient.isStarted()){
            locationClient.unRegisterLocationListener(bdLocationListener);
            locationClient.stop();
        }

    }


    public void changeCity(final View view) {
        final LinearLayout contentView = (LinearLayout) layoutInflater.inflate(R.layout.city_select_pop_win, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);


        //设置系统 默认的动画效果...
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_eara_item_selector));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }

    StaggedGridLayoutAdapter adapter = null;

    private void showPopupWindow(final View selectedCityView) {
        final RelativeLayout contentView = (RelativeLayout) layoutInflater.inflate(R.layout.addr_float, null);
        final BasicEasyPopupWindow popupWindow = PopWinHelper.getPopWin(this.getContextActivity(),contentView);

        RecyclerView list = (RecyclerView) contentView.findViewById(R.id.addr_list);
        List<String> dataList = new ArrayList<>();
        dataList.add(getContextActivity().getString(R.string.str_plz_select));

        View switchCity = contentView.findViewById(R.id.switch_city);
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                changeCity(selectedCityView);
            }
        });


        adapter = new StaggedGridLayoutAdapter(getContextActivity(), dataList);

        adapter.notifyDataSetChanged();

        adapter.setListener(new StaggedGridLayoutAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToastHelper.showLong(getContextActivity(), "click 点击 ： " + position);
                TextView tv = (TextView) selectedCityView;
                TextView tv2 = (TextView) view.findViewById(R.id.addr_name);
                tv.setText(tv2.getText());
                popupWindow.dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {
                ToastHelper.showLong(getContextActivity(), "click 长按 ： " + position);
                adapter.removeData(position);
            }
        });

        list.setAdapter(adapter);

        //设置布局管理器
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        list.setLayoutManager(layoutManager);

        //设置系统 默认的动画效果...
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        list.setItemAnimator(animator);

        indexService.fetchCities();

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_eara_item_selector, getActivity().getTheme()));
        } else {
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_eara_item_selector));
        }

        // 设置好参数之后再show
        popupWindow.showAsDropDown(selectedCityView);

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(DataChangeEvent<List<County>> event) {
        boolean success = event.isSuccess();
        if (!success) {
            ToastHelper.showLong(getContextActivity(), event.getExtraMessage());
            return;
        }
        adapter.removeData(0);
        adapter.notifyItemChanged(0);
        List<County> data = event.getData();
        data = CollectionHelper.reverse(data);
        for (County c : data) {
            adapter.insertData(0, c.getCountyName());
            adapter.notifyItemChanged(0);
        }
    }
}
