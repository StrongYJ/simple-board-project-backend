package com.myproject.simpleboard.global.security;

public interface JwtProperties {
    String JWT_PREFIX = "Bearer ";
    long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
}
