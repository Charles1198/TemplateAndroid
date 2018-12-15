package com.charles.login.function.resetpwd;

/**
 * @author charles
 * @date 2018/10/9
 * @description
 */
public interface ResetPasswordView {
    /**
     * 获取账号，手机or邮箱取决于用户输入
     *
     * @return
     */
    String getAccount();

    /**
     * 获取验证码
     *
     * @return
     */
    String getCode();

    /**
     * 获取密码
     *
     * @return
     */
    String getPassword();

    /**
     * 向手机发送验证码成功
     */
    void sendCodeToTelSucceed();

    /**
     * 向邮箱发送验证码成功
     */
    void sendCodeToEmailSucceed();

    /**
     * 向手机/邮箱发送验证码失败
     *
     * @param message 失败信息
     */
    void sendCodeFail(String message);

    /**
     * 注册成功
     */
    void resetPasswordSucceed();

    /**
     * 注册失败
     * @param message 失败信息
     */
    void resetPasswordFail(String message);
}
