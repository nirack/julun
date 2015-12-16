package com.julun.widgets.filterbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.julun.datas.beans.Area;
import com.julun.utils.ApplicationUtils;
import com.julun.volley.VolleyRequestCallback;
import com.julun.volley.utils.Requests;
import com.julun.widgets.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供给弹出窗口使用的查询过滤条件.
 * <p/>
 * 一行按钮,点击之后弹出下来菜单.
 */
public class SearchFilterView extends LinearLayout {
    private WeakReference<Context> cxt;
    /**
     * 在 SearchFilterBar里的位置.
     */
    private int position;

    //两边的数据请求对象
    private VolleyRequestCallback<List<Area>> leftLoader;
    private VolleyRequestCallback<List<Area>> rightLoader;

    //数据请求url
    private String leftUrl;
    private String rightUrl;

    private BaseListViewAdapter<Area> leftAdapter;
    private BaseListViewAdapter<Area> rightAdapter;
    private String baseUrl = ApplicationUtils.BASE_URL_PREFIX + "index/area";

    private ListView leftList ;
    private ListView rightList ;


    public SearchFilterView(Context context) {
        super(context);
        init(context, null);
    }

    public SearchFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        cxt = new WeakReference<Context>(context);
        if (null != attrs) {
            initAttrs(attrs);
        }

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        this.setLayoutParams(layoutParam);


        LinearLayout.LayoutParams listViewLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        listViewLayoutParam.gravity = Gravity.TOP;
        listViewLayoutParam.weight = 1;

        leftList = new ListView(context);
        rightList = new ListView(context);
        leftList.setLayoutParams(listViewLayoutParam);
        rightList.setLayoutParams(listViewLayoutParam);


        leftLoader = new VolleyRequestCallback<List<Area>>(cxt.get()) {
            @Override
            public void doOnSuccess(List<Area> response) {
                if(leftAdapter ==null){
                    leftAdapter = new BaseListViewAdapter<Area>(cxt.get(), R.layout.addr_item) {
                        @Override
                        public void convert(ViewHolder vh, Area area) {
                            vh.setTextViewText(R.id.addr_name,area.getAreaname());
                        }
                    };
                    leftList.setAdapter(leftAdapter);
                }
                leftAdapter.clear();
                leftAdapter.addAll(response);
                leftAdapter.notifyDataSetChanged();
                leftList.setSelection(0);
            }
        };


        this.addView(leftList);

        Requests.post(baseUrl, baseUrl, leftLoader);

        rightLoader = new VolleyRequestCallback<List<Area>>(cxt.get()) {
            @Override
            public void doOnSuccess(List<Area> response) {

                if(rightAdapter ==null){
                    rightAdapter = new BaseListViewAdapter<Area>(cxt.get(), R.layout.addr_item) {
                        @Override
                        public void convert(ViewHolder vh, Area area) {
                            vh.setTextViewText(R.id.addr_name,area.getAreaname());
                        }
                    };
                    rightList.setAdapter(rightAdapter);
                }
                rightAdapter.clear();
                rightAdapter.addAll(response);
                rightAdapter.notifyDataSetChanged();
            }
        };

        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Area area = leftAdapter.getItem(position);
                String url = baseUrl + "?parent=" + area.getId();
                Map<String,String> param = new HashMap<String, String>();
                param.put("parent",area.getId());
                Requests.post(baseUrl, baseUrl,rightLoader,param);
            }
        });
        this.addView(rightList);
    }

    /**
     * 加载自定义的属性值.
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {

    }

    /**
     * 获取过滤的条件.
     *
     * @return
     */
    public Map<String, String> getFilterValues() {
        return null;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
