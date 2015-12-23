package com.julun.vehicle.user.login;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.julun.annotations.views.AfterInitView;
import com.julun.annotations.views.ContentLayout;
import com.julun.container.uicontroller.BaseFragment;
import com.julun.vehicle.R;

import butterknife.Bind;

/**
 * Created by danjp on 2015/12/10.
 * 注册
 */
@ContentLayout(R.layout.fragment_signup)
public class SignupFragment extends BaseFragment implements View.OnClickListener {

    protected String TAG = "SignupFragment";
    @Bind(R.id.step1)
    RadioButton step1;
    @Bind(R.id.step2)
    RadioButton step2;
    @Bind(R.id.step3)
    RadioButton step3;
    @Bind(R.id.step_tip)
    RadioGroup stepTip;
    @Bind(R.id.container)
    FrameLayout container;

    EditText mobile;
    Button mGetSecurityCodeBtn;
    CheckBox term;

    EditText smsCode;
    Button sendCodeBtn;
    Button submitCodeBtn;

    EditText password;
    EditText confirmPassword;
    Button submit;

    @AfterInitView
    public void initView() {
        Log.d(TAG, "initView");

        step1.setChecked(true);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_signup_step1, null);

        container.addView(view);

        mobile = (EditText) view.findViewById(R.id.mobile);

        mobile.requestFocus();
        ((InputMethodManager) mobile.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mobile, 0);


        mGetSecurityCodeBtn = (Button)view.findViewById(R.id.get_security_code);
        mGetSecurityCodeBtn.setOnClickListener(this);

        term = (CheckBox) view.findViewById(R.id.term);
        term.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGetSecurityCodeBtn.setEnabled(true);
                } else {
                    mGetSecurityCodeBtn.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_security_code:
                Log.d(TAG, "Click button");
                container.removeAllViews();
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_signup_step2, null);
                container.addView(view);
                step2.setChecked(true);

                smsCode = (EditText)view.findViewById(R.id.sms_code);
                sendCodeBtn = (Button)view.findViewById(R.id.send_code_btn);
                submitCodeBtn = (Button)view.findViewById(R.id.submit_btn);
                sendCodeBtn.setOnClickListener(this);
                submitCodeBtn.setOnClickListener(this);

                TimeCount timeCount = new TimeCount(60000, 1000);
                timeCount.start();
                break;
            case R.id.submit_btn:
                container.removeAllViews();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_signup_step3, null);
                container.addView(view);
                step3.setChecked(true);

                password = (EditText)view.findViewById(R.id.password);
                confirmPassword = (EditText)view.findViewById(R.id.confirm_password);
                submit = (Button)view.findViewById(R.id.submit);
                submit.setOnClickListener(this);

                break;
            case R.id.submit:
                //注册成功,跳转到主界面
                break;
        }
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeBtn.setClickable(false);
            sendCodeBtn.setText("重新获取("+millisUntilFinished/1000+")");
        }

        @Override
        public void onFinish() {
            sendCodeBtn.setText("发送验证码");
            sendCodeBtn.setClickable(true);
        }
    }
}
