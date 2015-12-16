package com.julun.vehicle.viewpagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.julun.commons.reflect.ReflectUtil;
import com.julun.datas.beans.Adv;
import com.julun.utils.ApplicationUtils;
import com.julun.utils.DensityUtils;
import com.julun.utils.JsonHelper;
import com.julun.utils.ScreenUtils;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;
import com.julun.widgets.viewpager.SimpleLoopViewPager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-11-09.
 */
public class SimpleHomeAdvInitializer extends SimpleLoopViewPager.ViewItemInitializer<Adv> {

    /**
     * 完全自主化,随便返回任意的View都可以.返回的View将会作为ViewPager的子view.
     *
     * @param context
     * @param advs     在实现类里要自己转型为实际的对象.使用之前自己要知道.
     * @return
     */
    @Override
    public List<View> initializeView(Context context, List<Adv> advs) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        List<View> list = new ArrayList<>();

        for (int index = 0; index < advs.size(); index++) {
            //布局文件自己定义好,最外围的layout就是实际返回的view
            FrameLayout item = (FrameLayout) layoutInflater.inflate(R.layout.view_pager_item_layout, null);
            ImageView imageView = (ImageView) item.findViewById(R.id.image_view);
            //设置强制拉伸图片
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            Adv adv = advs.get(index);
            String url = ApplicationUtils.BASE_URL_PREFIX + adv.getImageUrl();
            TextView txt = (TextView) item.findViewById(R.id.title_info);
            txt.setText(adv.getTitle());
            int height = DensityUtils.dp2px(200);//布局文件里是200dp,这里转换为 px值
            Requests.loadImageAndResize(imageView,url, ScreenUtils.getScreenWidth() ,height);
            list.add(item);
        }

        return list;
    }

    @Override
    public VolleyRequestCallback<List<Adv>> getRequestCallback() {

        VolleyRequestCallback<List<Adv>> callback = new VolleyRequestCallback<List<Adv>>(this.cxt.get()) {
            @Override
            public void doOnSuccess(List<Adv> response) {
                List<View> views = initializeView(cxt.get(), response);
                for (Integer index = 0; index < views.size(); index++) {
                    viewMap.put(index,views.get(index));
                }
                looperView.afterLoadData(response);
            }

            @Override
            public void doOnFailure(VolleyError error) {
                onLoadError(error);
            }
        };
        return callback;
    }

}
