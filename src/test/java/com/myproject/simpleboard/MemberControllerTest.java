package com.myproject.simpleboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.TokenUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TokenUtils tokenUtils;
    
    @DisplayName("유효한 토큰 테스트")
    @Test
    void jwtCookieTest() throws Exception {
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, tokenUtils.createToken(1L, MemberRole.USER));
        cookie.setHttpOnly(true);
        
        mockMvc.perform(post("/members/test").cookie(cookie))
        .andExpect(status().isOk())
        .andDo(print());
    }

    @DisplayName("LoginMemberIdHandlerResolver 테스트")
    @Test
    void jwtLoginMemberIdHandlerResolverTest() throws Exception {
        long nextLong = new Random().nextLong();
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, tokenUtils.createToken(nextLong, MemberRole.USER));
        cookie.setHttpOnly(true);
        
        mockMvc.perform(post("/members/test").cookie(cookie))
        .andExpect(status().isOk())
        .andExpect(content().string("[유효한 토큰] memberId: " + nextLong))
        .andDo(print());
    }

    @DisplayName("쿠키값없이 로그인 필요 서비스 접근시")
    @Test
    void jwtNonCookieTest() throws Exception {
        
        mockMvc.perform(post("/members/test"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
    }

    @DisplayName("다른 키값으로 토큰 생성 후 HttpOnly가 아닌 쿠키로 보냈을 시")
    @Test
    void jwtNotHttpOnlyCookieTest() throws Exception {
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, Jwts.builder()
        .setSubject("board_project")
        .claim("id", 1L)
        .claim("role", MemberRole.USER)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.TOKEN_EXPIRE_TIME))
        .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
        .compact());
        
        mockMvc.perform(post("/members/test").cookie(cookie))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
    }

    @DisplayName("다른 키값으로 토큰 생성 후 HttpOnly 쿠키로 보냈을 시")
    @Test
    void jwtHttpOnlyCookieTest() throws Exception {
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, Jwts.builder()
        .setSubject("board_project")
        .claim("id", 1L)
        .claim("role", MemberRole.USER)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.TOKEN_EXPIRE_TIME))
        .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
        .compact());

        cookie.setHttpOnly(true);
        
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("error_type", "io.jsonwebtoken.security.SignatureException: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
        errorJson.put("error_message", "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");

        mockMvc.perform(post("/members/test").cookie(cookie))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(errorJson), false))
        .andDo(print());
    }

    @DisplayName("만료 토큰")
    @Test
    void jwtHttpOnlyCookieExpiredTest() throws Exception {
        Cookie cookie = new Cookie(JwtProperties.JWT_NANE, tokenUtils.createToken(1L, MemberRole.USER));

        cookie.setHttpOnly(true);
        
        Thread.sleep(JwtProperties.TOKEN_EXPIRE_TIME + 1L);

        mockMvc.perform(post("/members/test").cookie(cookie))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
    }
    
}
