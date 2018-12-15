package com.charles.login.function.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.base.BaseActivity;
import com.charles.common.kv.Kv;
import com.charles.common.network.response.UpdateResp;
import com.charles.common.util.StringUtil;
import com.charles.login.LoginConst;
import com.charles.login.R;
import com.charles.login.function.register.RegisterActivity;
import com.charles.login.function.resetpwd.ResetPasswordActivity;
import com.yinglan.keyboard.HideUtil;


/**
 * @author charles
 */
@Route(path = "/login/LoginActivity")
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {
    private EditText accountEdt;
    private EditText passwordEdt;
    private TextInputLayout passwordInput;
    private Button loginBtn;

    private LoginPresenter loginPresenter;

    private boolean accountOk = false;
    private boolean passwordOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HideUtil.init(this);

        initView();
        loginPresenter = new LoginPresenter(this);
    }

    private void initView() {
        accountEdt = findViewById(R.id.login_account_edt);
        passwordEdt = findViewById(R.id.login_password_edt);
        passwordInput = findViewById(R.id.login_password_input);
        loginBtn = findViewById(R.id.login_login_btn);

        accountEdt.addTextChangedListener(new MyTextWatcher(accountEdt));
        passwordEdt.addTextChangedListener(new MyTextWatcher(passwordEdt));

        loginBtn.setOnClickListener(this);
        findViewById(R.id.login_register_btn).setOnClickListener(this);
        findViewById(R.id.login_forget_password_btn).setOnClickListener(this);

        // TODO: 2018/11/20 为了演示
        accountEdt.setText("18501646696");
        passwordEdt.setText("Password123");
        accountEdt.setEnabled(false);
        passwordEdt.setEnabled(false);

        CheckBox rememberAccountCheckBox = findViewById(R.id.login_remember_checkBox);
        rememberAccountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Kv.setBool(LoginConst.REMEMBER_ACCOUNT, isChecked);
            }
        });
        if (Kv.getBool(LoginConst.REMEMBER_ACCOUNT, true)) {
            String account = Kv.getString(LoginConst.ACCOUNT);
            String password = Kv.getString(LoginConst.PASSWORD);
            if (!account.isEmpty() && !password.isEmpty()) {
                accountEdt.setText(account);
                passwordEdt.setText(password);
            }
        }

//        loginBtnEnable();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_login_btn) {

//            clickLoginBtn();
            ARouter.getInstance().build("/app/MainActivity").navigation();
        } else if (viewId == R.id.login_register_btn) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (viewId == R.id.login_forget_password_btn) {
            startActivity(new Intent(this, ResetPasswordActivity.class));
        }
    }

    private void clickLoginBtn() {
        loginPresenter.login();
        hideKeyboard();
    }

    @Override
    public String getAccount() {
        return accountEdt.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordEdt.getText().toString();
    }

    @Override
    public void loginSucceed() {

    }

    @Override
    public void loginFail(String message) {
        passwordInput.setError(message);
    }

    @Override
    public void alertUpdate(UpdateResp updateResp) {

    }

    /**
     * 监听账号输入
     *
     * @param s
     */
    private void accountEdtInputting(String s) {
        accountOk = StringUtil.isTel(s) || StringUtil.isEmail(s);
        loginBtnEnable();
    }

    /**
     * 设置登录按钮可用状态
     */
    private void loginBtnEnable() {
        boolean enable = accountOk && passwordOk;
        loginBtn.setEnabled(enable);
        loginBtn.setBackground(enable ? AppCompatResources.getDrawable(this, R.drawable.shape_btn)
                : AppCompatResources.getDrawable(this, R.drawable.shape_btn_unable));
    }

    /**
     * 监听密码输入
     *
     * @param s
     */
    private void passwordEdtInputting(String s) {
        passwordOk = s.length() >= 6 && s.length() <= 18;
        loginBtnEnable();
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
            if (viewId == R.id.login_account_edt) {
                accountEdtInputting(s);
            } else if (viewId == R.id.login_password_edt) {
                passwordEdtInputting(s);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onBackPressed() {
    }
}
