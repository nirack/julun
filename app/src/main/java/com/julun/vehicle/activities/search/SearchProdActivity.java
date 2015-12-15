package com.julun.vehicle.activities.search;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.datas.localdata.SearchHistory;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;
import com.julun.vehicle.viewpagers.SimpleLoopViewPager;

import butterknife.Bind;
import butterknife.OnClick;

@ContentLayout(R.layout.activity_search_prod)
public class SearchProdActivity extends BaseActivity implements SimpleLoopViewPager.ItemClickListener<SearchHistory> {

    @Bind(R.id.search_tool_bar)
    LinearLayout linearLayout;
    @Bind(R.id.home_tool_bar)
    ImageView homeBtn;
    @Bind(R.id.search_edit)
    EditText searchWord;
    @Bind(R.id.search_button)
    ImageButton searchBtn;


    @OnClick({R.id.home_tool_bar, R.id.search_button})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.home_tool_bar:
                this.finish();
                break;
            case R.id.search_button:
                ToastHelper.showShort(this,"打开查询结果.....");
                String keyword = searchWord.getText().toString();
                storeSearchHistory(keyword);
                Intent intent = new Intent(this,SearchResultActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                break;
        }
    }

    private void storeSearchHistory(String keyword) {
        // TODO: 2015-12-01 要不要把搜索历史写入本地数据库

    }

    /**
     * @param data 与当前view绑定的数据对象.
     */
    @Override
    public void onViewPagerItemClick(SearchHistory data) {

    }
}
