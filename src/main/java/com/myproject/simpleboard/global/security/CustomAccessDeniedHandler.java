package com.myproject.simpleboard.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendError(403, "Access Denied");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("timestamp", Instant.now().toEpochMilli());
        errorJson.put("status", 403);
        errorJson.put("message", "Forbidden");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorJson));
    }
}
