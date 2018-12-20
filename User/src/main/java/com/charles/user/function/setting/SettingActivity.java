package com.charles.user.function.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.base.BaseActivity;
import com.charles.user.R;
import com.charles.user.function.feedback.FeedbackActivity;

/**
 * @author charles
 * @date 2018/10/17
 */

@Route(path = "/user/SettingActivity")
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_setting);

        initView();
    }

    private void initView() {
        findViewById(R.id.setting_back_tv).setOnClickListener(this);
        findViewById(R.id.setting_reset_info_btn).setOnClickListener(this);
        findViewById(R.id.setting_reset_password_btn).setOnClickListener(this);
        findViewById(R.id.setting_feedback_btn).setOnClickListener(this);
        findViewById(R.id.setting_about_btn).setOnClickListener(this);
        findViewById(R.id.setting_logout_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.setting_back_tv) {
            finish();
        } else if (viewId == R.id.setting_reset_info_btn) {

        } else if (viewId == R.id.setting_reset_password_btn) {
            ARouter.getInstance().build("/login/ResetPasswordActivity").navigation();
        } else if (viewId == R.id.setting_about_btn) {

        } else if (viewId == R.id.setting_feedback_btn) {
            startActivity(new Intent(this, FeedbackActivity.class));
        } else if (viewId == R.id.setting_logout_btn) {
            clickLogoutBtn();
        }
    }

    private void clickLogoutBtn() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("退出登录")
                .setPositiveButton("退出", (dialogInterface, i) ->
                    ARouter.getInstance().build("/login/LoginActivity").navigation()
                )
                .setNegativeButton("取消", null)
                .create().show();
    }
}
