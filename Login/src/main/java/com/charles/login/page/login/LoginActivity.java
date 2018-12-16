package com.charles.login.page.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.base.BaseActivity;
import com.charles.common.network.response.UpdateResp;
import com.charles.login.R;
import com.charles.login.page.register.RegisterActivity;
import com.charles.login.page.resetpwd.ResetPasswordActivity;
import com.yinglan.keyboard.HideUtil;


/**
 * @author charles
 */
@Route(path = "/login/LoginActivity")
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {
    private EditText accountEdt;
    private EditText passwordEdt;
    private TextInputLayout passwordInput;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        HideUtil.init(this);

        initView();
        loginPresenter = new LoginPresenter(this);
    }

    private void initView() {
        accountEdt = findViewById(R.id.login_account_edt);
        passwordEdt = findViewById(R.id.login_password_edt);
        passwordInput = findViewById(R.id.login_password_input);

        findViewById(R.id.login_login_btn).setOnClickListener(this);
        findViewById(R.id.login_register_btn).setOnClickListener(this);
        findViewById(R.id.login_forget_password_btn).setOnClickListener(this);

        accountEdt.setText(LoginPresenter.TEST_USERNAME);
        passwordEdt.setText(LoginPresenter.TEST_PASSWORD);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.login_login_btn) {
            clickLoginBtn();
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
        ARouter.getInstance().build("/app/MainActivity").navigation();
    }

    @Override
    public void loginFail(String message) {
        passwordInput.setError(message);
    }

    @Override
    public void alertUpdate(UpdateResp updateResp) {

    }

    @Override
    public void onBackPressed() {
    }
}
