package com.myproject.simpleboard.global.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (JwtException e) {
            responseJwtExJsonBuilder(response, e);
        }
    }

    private void responseJwtExJsonBuilder(HttpServletResponse response, JwtException e) throws IOException {
        jwtExResponseSetting(response);
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("error_type", e.toString());
        errorJson.put("error_message", e.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorJson)); 
    }

    private void jwtExResponseSetting(HttpServletResponse response) {
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
