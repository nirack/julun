package com.julun.vehicle.activities.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;

@ContentLayout(R.layout.activity_search_result)
public class SearchResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String keyword = getIntent().getStringExtra("keyword");
        ToastHelper.showLong(this,"要查询的关键词是  " + keyword);
    }
}
