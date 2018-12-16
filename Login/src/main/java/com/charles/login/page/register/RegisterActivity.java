package com.charles.login.page.register;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.charles.common.base.BaseActivity;
import com.charles.common.util.StringUtil;
import com.charles.common.util.ToastUtil;
import com.charles.login.R;
import com.yinglan.keyboard.HideUtil;

/**
 * @author charles
 */
public class RegisterActivity extends BaseActivity implements RegisterView, View.OnClickListener {
    private EditText accountEdt;
    private EditText codeEdt;
    private EditText passwordEdt;
    private Button getCodeBtn;
    private Button registerBtn;
    private CheckBox agreementCheckBox;

    private RegisterPresenter registerPresenter;

    private boolean accountOk = false;
    private boolean passwordOk = false;
    private boolean codeOk = false;
    private boolean agreementOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HideUtil.init(this);

        registerPresenter = new RegisterPresenter(this);
        initView();
    }

    private void initView() {
        accountEdt = findViewById(R.id.register_account_edt);
        codeEdt = findViewById(R.id.register_code_edt);
        passwordEdt = findViewById(R.id.register_password_edt);
        getCodeBtn = findViewById(R.id.register_getCode_btn);
        registerBtn = findViewById(R.id.register_register_btn);
        agreementCheckBox = findViewById(R.id.register_agreement_checkBox);

        accountEdt.addTextChangedListener(new MyTextWatcher(accountEdt));
        codeEdt.addTextChangedListener(new MyTextWatcher(codeEdt));
        passwordEdt.addTextChangedListener(new MyTextWatcher(passwordEdt));

        getCodeBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        findViewById(R.id.register_back_tv).setOnClickListener(this);

        agreementCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agreementOk = isChecked;
                setRegisterBtnState();
            }
        });

        setCodeBtnState();
        setRegisterBtnState();
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();

        int viewId = v.getId();
        if (viewId == R.id.register_back_tv) {
            finish();
        } else if (viewId == R.id.register_getCode_btn) {
            registerPresenter.sendCode();
        } else if (viewId == R.id.register_register_btn) {
            registerPresenter.register();
        }
    }

    /**
     * 设置登录按钮可用状态
     */
    private void setCodeBtnState() {
        boolean enable = accountOk;
        getCodeBtn.setEnabled(enable);
    }

    /**
     * 设置登录按钮可用状态
     */
    private void setRegisterBtnState() {
        boolean enable = accountOk && passwordOk && codeOk && agreementOk;
        registerBtn.setEnabled(enable);
        registerBtn.setBackground(enable ? getResources().getDrawable(R.drawable.shape_btn)
                : getResources().getDrawable(R.drawable.shape_btn_unable));
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
        ToastUtil.toast(message.isEmpty() ? "未能获取到验证码，请稍后再试" : message);
    }

    @Override
    public void registerSucceed() {

    }

    @Override
    public void registerFail(String message) {
        ToastUtil.toast(message.isEmpty() ? "注册失败，请稍后再试" : message);
    }

    /**
     * 监听账号输入
     *
     * @param s
     */
    private void accountEdtInputting(String s) {
        accountOk = StringUtil.isTel(s) || StringUtil.isEmail(s);
        setCodeBtnState();
        setRegisterBtnState();
    }

    /**
     * 监听密码输入
     *
     * @param s
     */
    private void passwordEdtInputting(String s) {
        passwordOk = s.length() >= 6 && s.length() <= 18;
        setRegisterBtnState();
    }

    /**
     * 监听验证码输入
     *
     * @param s
     */
    private void codeEdtInputting(String s) {
        codeOk = s.length() == 4;
        setRegisterBtnState();
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
            if (viewId == R.id.register_account_edt) {
                accountEdtInputting(s);
            } else if (viewId == R.id.register_password_edt) {
                passwordEdtInputting(s);
            } else if (viewId == R.id.register_code_edt) {
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
            bt.setText(String.format("%d秒后重试", millisUntilFinished / 1000));
            bt.setEnabled(false);
        }

        @Override
        public void onFinish() {//计时完毕时触发
            bt.setText(finishText);
            bt.setEnabled(true);
        }
    }
}
