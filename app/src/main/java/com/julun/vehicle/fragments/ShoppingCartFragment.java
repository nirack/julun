package com.julun.vehicle.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.julun.annotations.business.BusinessBean;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.ProductService;
import com.julun.business.TestService;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.recyclerview.BaseRecyclerViewAdapter;
import com.julun.widgets.viewholder.recycler.ViewHolder;
import com.julun.widgets.views.recycler.decoration.DividerItemDecoration;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentLayout(R.layout.fragment_shopping_cart)
public class ShoppingCartFragment extends BaseFragment {

//    @Bind(R.id.list_view)
//    RecyclerView listView;

    @BusinessBean(serviceClass = TestService.class)
    TestService testService;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @AfterInitView
    public void afterInitViews() {

//        testService.test();

    }

}
