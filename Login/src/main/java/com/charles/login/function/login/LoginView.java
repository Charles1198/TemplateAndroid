package com.charles.login.function.login;

import com.charles.common.base.BaseView;
import com.charles.common.network.response.UpdateResp;

/**
 * @author charles
 * @date 2018/10/5
 * @description
 */
public interface LoginView extends BaseView {
    /**
     * 获取账号
     *
     * @return
     */
    String getAccount();

    /**
     * 获取密码
     *
     * @return
     */
    String getPassword();

    /**
     * 登录成功
     */
    void loginSucceed();

    /**
     * 登录失败
     *
     * @param message
     */
    void loginFail(String message);

    /**
     * 提示用户版本更新
     * @param updateResp
     */
    void alertUpdate(UpdateResp updateResp);
}
