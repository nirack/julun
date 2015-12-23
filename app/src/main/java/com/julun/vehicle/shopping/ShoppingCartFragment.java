package com.julun.vehicle.shopping;


import android.support.v4.app.Fragment;

import com.julun.annotations.business.BusinessBean;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.service.TestService;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

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
