package com.julun.vehicle.fragments.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.julun.business.LoginService;
import com.julun.event.events.DataChangeEvent;
import com.julun.utils.StringHelper;
import com.julun.vehicle.R;
import com.julun.vehicle.activities.MainActivity;
import com.julun.widgets.adapters.listview.BaseListViewAdapter;
import com.julun.widgets.viewholder.listview.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

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
        }, 1000L);

        //判断是否有第三方账号登陆
        adapter = new BaseListViewAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line) {

            @Override
            public void convert(ViewHolder vh, String s) {

            }
        };

        userName.setAdapter(adapter);
        userNameImg.setVisibility(View.INVISIBLE);
        passwordImg.setVisibility(View.INVISIBLE);
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
        Log.d(TAG, "删除用户名图标是否显示判断");
        if (userName.getText().length() > 0 && userNameImg.getVisibility() == View.INVISIBLE) {
            Log.d(TAG, "显示删除用户名图标");
            userNameImg.setVisibility(View.VISIBLE);
        } else if (userName.getText().length() == 0 && userNameImg.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "隐藏删除用户名图标");
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

        transaction.add(R.id.fragmentContainer, fragment, fragmentTag).addToBackStack(null).commit();
    }

    /**
     * 后台登陆
     */
    private void login() {
        loginService.login(userName.getText().toString(), password.getText().toString(), captcha.getText().toString());
    }

    /**
     * 更换验证码
     */
    public void changeCaptcha() {
        loginService.changeCaptcha();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(DataChangeEvent<String> event) {
        boolean success = event.isSuccess();
        if (success) {
            String msg = event.getData();
            if (StringHelper.isNotEmpty(msg)) {
                new AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setMessage(msg)
                        .setPositiveButton("取消", null)
                        .setNegativeButton("手机号快捷登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //跳转到手机快捷登陆
                            }
                        }).create();
            }else{
                int code = event.getCode();
                if(code == 0) {
                    jumpToMainActivity();
                }else if(code == 1) {
//                    captchaImg.setImageDrawable(Drawable.);
                }
            }
        } else {
            //请求失败，网络异常等情况
            String errorInfo = event.getExtraMessage();
            Log.d(TAG, errorInfo);
            new AlertDialog.Builder(getActivity()).setTitle("提示")
                    .setMessage("数据获取失败, 请稍后重试")
                    .setPositiveButton("知道了", null)
                    .create();
        }
    }

    public void jumpToMainActivity() {
        //登陆成功，跳转到"我的"界面
        // TODO: 2015/12/9 应该跳转到主界面中"我的"Fragment中，将”我的"面板checked
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}