package com.julun.vehicle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.utils.FragmentUtils;
import com.julun.vehicle.activity.examples.BuyListViewExampleActivity;
import com.julun.vehicle.activity.examples.ListViewExampleActivity;
import com.julun.vehicle.activity.examples.PopWinTestActivity;
import com.julun.vehicle.activity.examples.RecyclerViewExampleActivity;
import com.julun.vehicle.activity.examples.RefreshViewTest2Activity;
import com.julun.vehicle.activity.examples.RefreshViewTestActivity;
import com.julun.vehicle.cargo.CargoFragment;
import com.julun.vehicle.home.HomePageFragment;
import com.julun.vehicle.shopping.ShoppingCartFragment;
import com.julun.vehicle.user.UserMainFragment;

import butterknife.Bind;
import butterknife.OnCheckedChanged;

@ContentLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private String LOG_TAG = MainActivity.class.getName();

    @Bind(R.id.btn_home_page)
    RadioButton buttonHomePage;
    @Bind(R.id.btn_machine_parts)
    RadioButton buttonMachineParts;
    @Bind(R.id.btn_shopping_cart)
    RadioButton buttonShoppingCart;
    @Bind(R.id.btn_mime_stuff)
    RadioButton buttonMimeStuff;

    @Bind(R.id.main_nav_radio)
    RadioGroup mainNavRadio;

    private HomePageFragment homePageFragment;
    private UserMainFragment userMainFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private CargoFragment cargoFragment;
    private FragmentManager fragmentManager;

    private Fragment mCurrentFragment;

    @AfterInitView
    public void afterInitViews() {
        // TODO: fragment直接new，如果遇到问题 再改作其他的方式
        /*homePageFragment = new HomePageFragment();
        userMainFragment = new UserMainFragment();
        shoppingCartFragment = new ShoppingCartFragment();
        cargoFragment = new CargoFragment();
        */
        fragmentManager = this.getFragmentManager();
        mCurrentFragment = new HomePageFragment();
        mainNavRadio.check(R.id.btn_home_page);
    }

    @OnCheckedChanged({R.id.btn_machine_parts, R.id.btn_home_page, R.id.btn_mime_stuff, R.id.btn_shopping_cart})
    public void radioButtonCheckChange(RadioButton button, boolean checked) {
        if(!checked) return;
        int checkedId = button.getId();
        Class newFragmentClass = null;
        switch (checkedId) {
            case R.id.btn_home_page:
                newFragmentClass = HomePageFragment.class;
                break;
            case R.id.btn_machine_parts:
                newFragmentClass = CargoFragment.class;
                break;
            case R.id.btn_shopping_cart:
                newFragmentClass = ShoppingCartFragment.class;
                break;
            case R.id.btn_mime_stuff:
                newFragmentClass = UserMainFragment.class;
                break;
        }
        mCurrentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.mainContentCtr, mCurrentFragment, newFragmentClass, null);
    }

    // TODO: 2015-11-25 暂时用作展示一些测试用例

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_4_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.show_list_view:
                // TODO: 2015-11-10  测试
                jumpActivity( ListViewExampleActivity.class);
                break;
            case R.id.show_rec_view:
                jumpActivity(RecyclerViewExampleActivity.class);
                break;
            case R.id.show_pop_win:
                jumpActivity(PopWinTestActivity.class);
                break;
            case R.id.show_buy_list_view:
                jumpActivity(BuyListViewExampleActivity.class);
                break;
            case R.id.refresh_test:
                jumpActivity(RefreshViewTestActivity.class);
                break;

            case R.id.refresh_test2:
                jumpActivity(RefreshViewTest2Activity.class);
                break;
        }
        return true;
    }
}
