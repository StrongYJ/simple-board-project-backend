package com.myproject.simpleboard.global.security;

public interface JwtProperties {
    String JWT_NANE = "token";
    long TOKEN_EXPIRE_TIME = 1000 * 60 * 10;
    long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
}
