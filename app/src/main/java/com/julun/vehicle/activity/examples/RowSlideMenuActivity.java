package com.julun.vehicle.activity.examples;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.beans.Cargo;
import com.julun.container.uicontroller.BaseActivity;
import com.julun.utils.ScreenUtils;
import com.julun.utils.ToastHelper;
import com.julun.vehicle.R;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import butterknife.Bind;
import butterknife.OnClick;

@ContentLayout (R.layout.activity_row_slide_menu)
public class RowSlideMenuActivity extends BaseActivity {

    public static final String TAG = RowSlideMenuActivity.class.getName ();

    @Bind (R.id.btn1)
    Button button;
    @Bind (R.id.list_view)
    ListView listView;

    @Bind (R.id.btn_right)
    Button btn_right;


    BaseListViewAdapter<Cargo> adapter;
    private int count = 0;

    float start = 0f;


    @AfterInitView
    public void afterViewInited () {

        final int screenWidth = ScreenUtils.getScreenWidth ();


        adapter = new BaseListViewAdapter<Cargo> (this, R.layout.addr_item) {
            @Override
            public void convert (ViewHolder vh, Cargo cargo) {
                vh.setExtra ("data",cargo);
                vh.setTextViewText(R.id.addr_name,cargo.getProductName ());

                View convertView = vh.getConvertView ();
                convertView.setOnTouchListener (new View.OnTouchListener () {
                    @Override
                    public boolean onTouch (View v, MotionEvent event) {

                        if(event.getAction () == MotionEvent.ACTION_DOWN){
                            start = event.getX ();
                        }else if(event.getAction () == MotionEvent.ACTION_UP){
                            float posi = event.getX ();
                            float diff = start - posi ;
                            Log.d (TAG, "convertView onTouch() called with: screenWidth : =[ " + screenWidth  + " ] , posi = [" + posi + "], diff = [" + diff + "]");
                            if(  diff > (screenWidth / 5)){
                                ToastHelper.showLong (RowSlideMenuActivity.this,"可以现实菜单了。。。。");
                            }

                        }
                        return true;
                    }
                });

            }
        };
        Cargo cargo = new Cargo ();
        cargo.setProductName ("sssssssssssss");
        adapter.add (cargo);
        listView.setAdapter (adapter);

        listView.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                Log.d (TAG, "listView onTouch() called with: " + "v = [" + v + "], event = [" + event + "]");
                return false;
            }
        });

    }


    @OnClick ({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn_right})
    public void btnClick (View view) {
        int id = view.getId ();
        switch (id) {
            case R.id.btn1:
                adapter.clear ();
                break;
            case R.id.btn2:
                addData ();
                break;
            case R.id.btn3:
                Log.d (TAG, "btnClick() called with: " + "view = [" + view + "]");
                btn_right.animate ().scaleX (200).setDuration (1000).start ();
                break;
        }
    }

    private void addData () {
        Cargo cargo = new Cargo ();
        int seqId = count + 1;
        cargo.setSeqId (seqId);
        cargo.setProductName ("测试商品-<" + seqId +  ">");
        adapter.add (cargo);
        count ++;
        adapter.notifyDataSetChanged ();
    }

}
