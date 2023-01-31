package com.myproject.simpleboard.global.security;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (JwtException e) {
            responseJwtExJsonBuilder(response, e);
        } catch(RuntimeException e) {
            responseRuntimeExJsonBuilder(response, e);
        } 
    }

    private void responseRuntimeExJsonBuilder(HttpServletResponse response, RuntimeException e) throws IOException {
        errorResponseSetting(response);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("error_type", e.toString());
        errorJson.put("error_message", e.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(errorJson)); 
    }
    private void responseJwtExJsonBuilder(HttpServletResponse response, JwtException e) throws IOException {
        errorResponseSetting(response);
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("error_type", e.toString());
        errorJson.put("error_message", e.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorJson)); 
    }

    private void errorResponseSetting(HttpServletResponse response) throws IOException {
        response.sendError(401, "Invalid Token");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
