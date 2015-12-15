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

/**
 * Created by Administrator on 2015-11-09.
 */
public class SimpleHomeAdvInitializer extends SimpleLoopViewPager.ViewItemInitializer<Adv> {

    /**
     * 完全自主化,随便返回任意的View都可以.返回的View将会作为ViewPager的子view.
     *
     * @param context
     * @param adv           在实现类里要自己转型为实际的对象.使用之前自己要知道.
     * @param layoutInflater
     * @return
     */
    @Override
    public View initializeView(Context context, Adv adv, LayoutInflater layoutInflater) {
        //布局文件自己定义好,最外围的layout就是实际返回的view
        FrameLayout item = (FrameLayout) layoutInflater.inflate(R.layout.view_pager_item_layout,null);
        ImageView imageView = (ImageView) item.findViewById(R.id.image_view);
        //设置强制拉伸图片
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        String url = ApplicationUtils.BASE_URL_PREFIX + adv.getImageUrl();
        TextView txt = (TextView) item.findViewById(R.id.title_info);
        txt.setText(adv.getTitle());
        Requests.loadImage(imageView, url);
        return item;
    }
}
