package com.julun.vehicle.fragments.user;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.julun.annotations.business.BusinessBean;
import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.business.service.LoginService;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.StringHelper;
import com.julun.vehicle.R;
import com.julun.vehicle.activities.MainActivity;
import com.julun.vehicle.dialogs.ProgressDialogFragment;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by danjp on 2015/12/8.
 */
@ContentLayout(R.layout.fragment_login)
public class LoginFragment extends UserBackFragment {
    private static final String TAG = "LoginFragment";

    @Bind(R.id.username)
    AutoCompleteTextView userName;
    @Bind(R.id.password)
    AutoCompleteTextView password;
    @Bind(R.id.username_img)
    ImageView userNameImg;
    @Bind(R.id.password_img)
    ImageView passwordImg;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.retrieve_password)
    TextView retrievePassword;
    @Bind(R.id.dynamic_login)
    TextView dynamicLogin;
    @Bind(R.id.oauth_layout)
    LinearLayout oauthLayout;

    @Bind(R.id.captcha)
    EditText captcha;
    @Bind(R.id.captcha_progress_loading)
    ProgressBar captchaProgressLoading;
    @Bind(R.id.captcha_img)
    ImageView captchaImg;
    @Bind(R.id.change_captcha)
    TextView changeCaptcha;
    @Bind(R.id.captcha_zone)
    LinearLayout captchaZone;

    @BusinessBean
    LoginService loginService;

    private BaseListViewAdapter<String> adapter;
    private ProgressDialogFragment progressDialogFragment;

    private static final String FRAGMENT_PROGRESS_DIALOG = "progress_dialog";

    @AfterInitView
    public void initView() {
        Log.d(TAG, "initView");

        userName.requestFocus();
        new Handler().postDelayed(new Runnable() {
            //可能界面未加载完，延迟显示软键盘
            @Override
            public void run() {
                ((InputMethodManager) userName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(userName, 0);
            }
        }, 500L);

        adapter = new BaseListViewAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line) {

            @Override
            public void convert(ViewHolder vh, String s) {

            }
        };

        userName.setAdapter(adapter);
        userNameImg.setVisibility(View.INVISIBLE);
        passwordImg.setVisibility(View.INVISIBLE);
        captchaZone.setVisibility(View.GONE);  //默认不出来验证码

        userNameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setText("");
            }
        });
        passwordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_signup) {
            //跳转到注册页面
            updateFragment(FRAGMENT_SIGNUP_TAG);
        } else if (item.getItemId() == android.R.id.home) {
            jumpToMainActivity();
        }
        return true;
    }

    @OnTextChanged(value = R.id.username, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void userNameTextChanged() {
        if (userName.getText().length() > 0 && userNameImg.getVisibility() == View.INVISIBLE) {
            userNameImg.setVisibility(View.VISIBLE);
        } else if (userName.getText().length() == 0 && userNameImg.getVisibility() == View.VISIBLE) {
            userNameImg.setVisibility(View.INVISIBLE);
        }
    }

    @OnTextChanged(value = R.id.password, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void passwordTextChanged() {
        if (password.getText().length() > 0 && passwordImg.getVisibility() == View.INVISIBLE) {
            passwordImg.setVisibility(View.VISIBLE);
        } else if (password.getText().length() == 0 && passwordImg.getVisibility() == View.VISIBLE) {
            passwordImg.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.login, R.id.retrieve_password, R.id.dynamic_login, R.id.change_captcha})
    public void viewClick(View view) {
        Log.d(TAG,"View id : "+view.getId());
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.retrieve_password:
                updateFragment(FRAGMENT_GET_BACK_PASSWORD_TAG);
                break;
            case R.id.dynamic_login:
                updateFragment(FRAGMENT_PHONE_SIGNIN_TAG);
                break;
            case R.id.change_captcha:
                changeCaptcha();
                break;
        }
    }

    /**
     * 不同fragment切换添加
     *
     * @param fragmentTag
     */
    private void updateFragment(String fragmentTag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(fragmentTag);
        //隐藏当前登陆fragment，不希望重绘视图，保存用户输入的登录名密码
        transaction.hide(this);

        switch (fragmentTag) {
            case FRAGMENT_SIGNUP_TAG:
                fragment = new SignupFragment();
                break;
            case FRAGMENT_PHONE_SIGNIN_TAG:
                fragment = new PhoneLoginFragment();
                break;
            case FRAGMENT_GET_BACK_PASSWORD_TAG:
                fragment = new GetBackPasswordFragment();
                break;
        }

        transaction.replace(R.id.fragmentContainer, fragment, fragmentTag).addToBackStack(null).commit();
    }

    /**
     * 后台登陆
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void login() {
        Log.d(TAG, "login....show progress");
        progressDialogFragment = ProgressDialogFragment.newInstance(0);
        progressDialogFragment.show(getChildFragmentManager(), FRAGMENT_PROGRESS_DIALOG);
        Log.d(TAG, "调用后台登陆");
        loginService.login(userName.getText().toString().trim(), password.getText().toString().trim(), captcha.getText().toString().trim());
    }

    /**
     * 更换验证码
     */
    public void changeCaptcha() {
//        loginService.changeCaptcha();
        captchaImg.setVisibility(View.GONE);
        captchaProgressLoading.setVisibility(View.VISIBLE);
        // TODO: 2015/12/10  模拟后台生成验证码数据延时
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                captchaProgressLoading.setVisibility(View.GONE);
                captchaImg.setVisibility(View.VISIBLE);
                captchaImg.setImageBitmap(Code.getInstance().createBitmap());
            }
        }, 1000);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(DataChangeEvent event) {
        Log.d(TAG, "UI MAIN THREAD");
        if (event.isSuccess()) {
            Object data = event.getData();
            Log.d(TAG, "success"+ (data instanceof  String));
            if(data instanceof String){
                loginSuccess(String.valueOf(data));
            } else if(data instanceof  byte[]){
                setCaptchaImg((byte[])data);
            }
        }
    }

    /**
     * 登陆成功
     * @param data
     */
    private void loginSuccess(String data) {
        Log.d(TAG, "loginSuccess: " + data);
        if (StringHelper.isNotEmpty(data)) {
            new AlertDialog.Builder(getActivity()).setTitle("提示")
                    .setMessage(data)
                    .setPositiveButton("取消", null)
                    .setNegativeButton("手机号快捷登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转到手机快捷登陆
                        }
                    }).create();
        }
        //登陆成功，存储用户信息
        Log.d(TAG, "隐藏progress dialog");
        progressDialogFragment.dismiss();

        // TODO: 2015/12/10 根据后台数据data判断用户登陆有问题，需要选择验证码
        captchaZone.setVisibility(View.VISIBLE);
        captchaImg.setVisibility(View.VISIBLE);
        captchaProgressLoading.setVisibility(View.GONE);
        captchaImg.setImageBitmap(Code.getInstance().createBitmap());

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString("username", userName.getText().toString().trim());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置登陆验证码
     * @param data
     */
    private void setCaptchaImg(byte[] data) {
        if(data != null && data.length > 0) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            captchaImg.setImageBitmap(bm);
        }
    }

    public void jumpToMainActivity() {
        //登陆成功，跳转到"我的"界面
        // TODO: 2015/12/9 应该跳转到主界面中"我的"Fragment中，将”我的"面板checked
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}
