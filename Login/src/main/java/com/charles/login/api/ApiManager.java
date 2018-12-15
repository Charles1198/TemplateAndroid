package com.charles.login.api;

import com.charles.common.network.response.BaseResp;
import com.charles.common.network.response.TokenResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * @author charles
 * @date 2018/10/5
 * @description
 */
public interface ApiManager {

    /**
     * 登录
     *
     * @param loginKey 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("api/authentication")
    Call<BaseResp<TokenResp>> login(@Field("loginKey") String loginKey,
                                    @Field("password") String password);

    /**
     * 向手机发送验证码
     *
     * @param tel 手机号
     * @param tag 验证码类型 1:电话注册 2:邮箱注册 3:密码重置(电话) 4:密码重置(邮箱) 5:重置电话； 6:重置邮箱
     * @return
     */
    @FormUrlEncoded
    @POST("api/tel/code")
    Call<BaseResp> sendCodeToTel(@Field("tel") String tel,
                                 @Field("tag") int tag);

    /**
     * 用手机注册
     *
     * @param tel      手机
     * @param password 密码
     * @param code     验证码
     * @return
     */
    @FormUrlEncoded
    @POST("api/register/tel")
    Call<BaseResp> registerByTel(@Field("tel") String tel,
                                 @Field("password") String password,
                                 @Field("code") String code);

    /**
     * 用手机修改密码
     *
     * @param tel      手机
     * @param password 密码
     * @param code     验证码
     * @return
     */
    @POST("api/password/tel")
    Call<BaseResp> resetPasswordByTel(@Field("tel") String tel,
                                      @Field("password") String password,
                                      @Field("code") String code);

    /**
     * 向邮箱发送验证码
     *
     * @param email 邮箱
     * @param tag   验证码类型 1:电话注册 2:邮箱注册 3:密码重置(电话) 4:密码重置(邮箱) 5:重置电话； 6:重置邮箱
     * @return
     */
    @FormUrlEncoded
    @POST("api/email/code")
    Call<BaseResp> sendCodeToEmail(@Field("email") String email,
                                   @Field("tag") int tag);

    /**
     * 用邮箱注册
     *
     * @param email    邮箱
     * @param password 密码
     * @param code     验证码
     * @return
     */
    @FormUrlEncoded
    @POST("api/register/email")
    Call<BaseResp> registerByEmail(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("code") String code);

    /**
     * 用邮箱修改密码
     *
     * @param email    邮箱
     * @param password 密码
     * @param code     验证码
     * @return
     */
    @PUT("api/password/email")
    Call<BaseResp> resetPasswordByEmail(@Field("email") String email,
                                        @Field("password") String password,
                                        @Field("code") String code);
}
