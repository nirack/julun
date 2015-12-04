package com.julun.vehicle.activities.examples;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.utils.ScreenUtils;
import com.julun.vehicle.R;
import com.julun.widgets.popwin.LoadProgressWin;
import com.julun.widgets.utils.PopWinHelper;

import butterknife.Bind;
import butterknife.OnClick;

@ContentLayout(R.layout.activity_pop_win_test)
public class PopWinTestActivity extends BaseActivity {
    public static final String TAG = PopWinTestActivity.class.getName();
    @Bind(R.id.show_loading)
    Button showLoading;
    @Bind(R.id.stop_anmi)
    Button stop_anmi;
    private LoadProgressWin loadingPopWin;


    @OnClick({R.id.show_loading,R.id.stop_anmi})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.show_loading:
                showLoading(view);
                break;
            case R.id.stop_anmi:
                loadingPopWin.stop();
                break;
        }

    }

    private void showLoading(final View view) {
        loadingPopWin = PopWinHelper.getLoadingPopWin(this,new FrameLayout(this), 200);
        loadingPopWin.showAsDropDown(stop_anmi);
        loadingPopWin.start();
//        win.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_title_bg_shape));//背景如果不设置，则弹出框不会 dismiss

    }

}
