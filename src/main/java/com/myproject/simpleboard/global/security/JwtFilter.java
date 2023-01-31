package com.myproject.simpleboard.global.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    
    public JwtFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        Cookie[] cookies = request.getCookies();
        if(!ObjectUtils.isEmpty(cookies)) {
            String token = tokenUtils.getToken(cookies);
            tokenUtils.verify(token);
            log.info("[JwtFilter] 유효한 토큰");
            Authentication authentication = tokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } 
        doFilter(request, response, filterChain);
    }
    
}
