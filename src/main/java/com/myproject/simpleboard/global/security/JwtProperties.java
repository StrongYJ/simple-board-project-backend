package com.myproject.simpleboard.global.security;

public interface JwtProperties {
    String ACCESS_PREFIX = "Bearer ";
    String REFRESH_NANE = "token";
    long ACCESS_EXPIRE_TIME = 1000 * 60 * 10;
    long REFRESH_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
}
