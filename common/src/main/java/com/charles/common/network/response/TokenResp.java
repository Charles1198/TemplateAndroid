package com.charles.common.network.response;

/**
 *
 * @author charles
 * @date 16/9/29
 */

public class TokenResp {
    private String refreshToken;
    private String token;

    public String getRefreshToken() {
        return refreshToken == null ? "" : refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
