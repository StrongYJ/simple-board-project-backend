package com.myproject.simpleboard.global.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    
    public JwtFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
                
                
        doFilter(request, response, filterChain);
    }
    
}
