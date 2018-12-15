package com.charles.login.function.resetpwd;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.charles.common.base.BaseActivity;
import com.charles.common.util.StringUtil;
import com.charles.login.R;
import com.yinglan.keyboard.HideUtil;

/**
 * @author charles
 */
@Route(path = "/login/ResetPasswordActivity")
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener, ResetPasswordView {
    private EditText accountEdt;
    private EditText codeEdt;
    private EditText passwordEdt;
    private Button resetBtn;
    private Button getCodeBtn;

    private boolean accountOk = false;
    private boolean passwordOk = false;
    private boolean codeOk = false;

    private ResetPasswordPresenter resetPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HideUtil.init(this);

//        resetPasswordPresenter = new ResetPasswordPresenter()

        initView();
    }

    private void initView() {
        accountEdt = findViewById(R.id.reset_account_edt);
        codeEdt = findViewById(R.id.reset_code_edt);
        passwordEdt = findViewById(R.id.reset_password_edt);
        resetBtn = findViewById(R.id.reset_btn);
        getCodeBtn = findViewById(R.id.reset_getCode_tv);

        accountEdt.addTextChangedListener(new MyTextWatcher(accountEdt));
        codeEdt.addTextChangedListener(new MyTextWatcher(codeEdt));
        passwordEdt.addTextChangedListener(new MyTextWatcher(passwordEdt));

        resetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.reset_getCode_tv) {
            resetPasswordPresenter.sendCode();
        } else if (viewId == R.id.reset_btn) {
            resetPasswordPresenter.resetPassword();
        }
    }

    /**
     * 监听账号输入
     *
     * @param s
     */
    private void accountEdtInputting(String s) {
        accountOk = StringUtil.isTel(s) || StringUtil.isEmail(s);
        resetBtnEnable();
    }

    /**
     * 设置登录按钮可用状态
     */
    private void resetBtnEnable() {
        boolean enable = accountOk && passwordOk && codeOk;
        resetBtn.setEnabled(enable);
        resetBtn.setBackground(enable ? getResources().getDrawable(R.drawable.shape_btn)
                : getResources().getDrawable(R.drawable.shape_btn_unable));
    }

    /**
     * 监听密码输入
     *
     * @param s
     */
    private void passwordEdtInputting(String s) {
        passwordOk = s.length() >= 6 && s.length() <= 18;
        resetBtnEnable();
    }

    /**
     * 监听验证码输入
     *
     * @param s
     */
    private void codeEdtInputting(String s) {
        codeOk = s.length() == 4;
        resetBtnEnable();
    }

    @Override
    public String getAccount() {
        return accountEdt.getText().toString();
    }

    @Override
    public String getCode() {
        return codeEdt.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordEdt.getText().toString();
    }

    @Override
    public void sendCodeToTelSucceed() {
        TimeCount timeCount = new TimeCount(60000, 1000, getCodeBtn, "没有收到验证码?点击重试");
        timeCount.start();
    }

    @Override
    public void sendCodeToEmailSucceed() {
        TimeCount timeCount = new TimeCount(60000, 1000, getCodeBtn, "没有收到验证码?点击重试");
        timeCount.start();
    }

    @Override
    public void sendCodeFail(String message) {

    }

    @Override
    public void resetPasswordSucceed() {

    }

    @Override
    public void resetPasswordFail(String message) {

    }

    private class MyTextWatcher implements TextWatcher {
        private TextView tv;

        /**
         * 绑定 TextView
         *
         * @param tv
         */
        MyTextWatcher(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int viewId = tv.getId();
            String s = tv.getText().toString();
            if (viewId == R.id.reset_account_edt) {
                accountEdtInputting(s);
            } else if (viewId == R.id.reset_password_edt) {
                passwordEdtInputting(s);
            } else if (viewId == R.id.reset_code_edt) {
                codeEdtInputting(s);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class TimeCount extends CountDownTimer {
        private Button bt;
        private String finishText;

        /**
         * @param millisInFuture    计时总时长
         * @param countDownInterval 时间间隔
         * @param bt                目标
         * @param finishText        结束计时显示的文字
         */
        public TimeCount(long millisInFuture, long countDownInterval, Button bt, String finishText) {
            //为毛是 * 1.001，因为timeCount貌似计时不准确，大概2-3秒多1毫秒
            super((long) (millisInFuture * 1.001), countDownInterval);
            this.bt = bt;
            this.finishText = finishText;

        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            bt.setEnabled(false);
            bt.setText((millisUntilFinished / 1000) + "秒后重试");
        }

        @Override
        public void onFinish() {//计时完毕时触发
            bt.setText(finishText);
            bt.setEnabled(true);
        }
    }
}
