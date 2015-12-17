package com.julun.vehicle.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;
import com.julun.vehicle.activities.examples.BuyListViewExampleActivity;
import com.julun.vehicle.activities.examples.ListViewExampleActivity;
import com.julun.vehicle.activities.examples.PopWinTestActivity;
import com.julun.vehicle.activities.examples.RecyclerViewExampleActivity;
import com.julun.vehicle.fragments.CargoFragment;
import com.julun.vehicle.fragments.HomePageFragment;
import com.julun.vehicle.fragments.ShoppingCartFragment;
import com.julun.vehicle.fragments.user.UserMainFragment;

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
    private UserMainFragment userInfoFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private CargoFragment cargoFragment;
    private FragmentManager fragmentManager;

    @AfterInitView
    public void afterInitViews() {
        // TODO: fragment直接new，如果遇到问题 再改作其他的方式
        homePageFragment = new HomePageFragment();
        userInfoFragment = new UserMainFragment();
        shoppingCartFragment = new ShoppingCartFragment();
        cargoFragment = new CargoFragment();
        fragmentManager = this.getFragmentManager();
        mainNavRadio.check(R.id.btn_home_page);
    }

    @OnCheckedChanged({R.id.btn_machine_parts, R.id.btn_home_page, R.id.btn_mime_stuff, R.id.btn_shopping_cart})
    public void radioButtonCheckChange(RadioButton button, boolean checked) {
        FragmentTransaction trans = fragmentManager.beginTransaction();
        int checkedId = button.getId();
        int drawAbleResId = 0;
        BaseFragment frag = null;
        switch (checkedId) {
            case R.id.btn_home_page:
                drawAbleResId = checked ? R.drawable.ic_home_green_a700_24dp : R.drawable.ic_home_black_24dp;
                if (checked) {
                    frag = homePageFragment;
                }
                break;
            case R.id.btn_machine_parts:
                if (checked) {
                    frag = cargoFragment;
                }
                drawAbleResId = checked ? R.drawable.ic_search_green_a700_24dp : R.drawable.ic_search_black_24dp;
                break;
            case R.id.btn_shopping_cart:
                if (checked) {
                    frag = shoppingCartFragment;
                }
                drawAbleResId = checked ? R.drawable.ic_shopping_cart_green_a700_24dp : R.drawable.ic_shopping_cart_black_24dp;
                break;
            case R.id.btn_mime_stuff:
                if (checked) {
                    frag = userInfoFragment;
                }
                drawAbleResId = checked ? R.drawable.ic_account_circle_green_a700_24dp : R.drawable.ic_account_circle_black_24dp;
                break;
        }

        if (checked) {
            trans.replace(R.id.mainContentCtr, frag).commit();
        }
        button.setCompoundDrawablesWithIntrinsicBounds(0, drawAbleResId, 0, 0);

        int colorResId = checked ? R.color.green_a700 : R.color.black_totally;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.setTextColor(getResources().getColor(colorResId, getTheme()));
        } else {
            button.setTextColor(getResources().getColor(colorResId));
        }

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
        }
        return true;
    }
}
