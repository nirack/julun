package com.julun.vehicle.viewpagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.julun.datas.beans.Adv;
import com.julun.utils.ApplicationUtils;
import com.julun.vehicle.R;
import com.julun.volley.utils.Requests;
import com.julun.widgets.viewpager.SimpleLoopViewPager;

/**
 * Created by Administrator on 2015-11-09.
 */
public class SimpleHomeAdvInitializer implements SimpleLoopViewPager.ViewItemInitializer<Adv> {
    /**
     * 完全自主化,随便返回任意的View都可以.返回的View将会作为ViewPager的子view.
     *
     * @param context
     * @param data           在实现类里要自己转型为实际的对象.使用之前自己要知道.
     * @param layoutInflater 如果子view有定义好的布局文件...
     * @return
     */
    @Override
    public View initializeView(Context context, Adv data, LayoutInflater layoutInflater) {
        FrameLayout item = (FrameLayout) layoutInflater.inflate(R.layout.view_pager_item_layout,null);
        ImageView imageView = (ImageView) item.findViewById(R.id.image_view);
        //设置强制拉伸图片
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        String url = ApplicationUtils.BASE_URL_PREFIX + data.getImageUrl();
        TextView txt = (TextView) item.findViewById(R.id.title_info);
        txt.setText(data.getTitle());
        Requests.loadImage(imageView,url);
        return item;
    }

}
