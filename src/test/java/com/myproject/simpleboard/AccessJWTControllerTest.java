package com.myproject.simpleboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.TokenUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessJWTControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenUtils tokenUtils;

    @DisplayName("유효한 토큰 테스트")
    @Test
    void jwtCookieTest() throws Exception {
        String accessToken = tokenUtils.createAccessToken(1L, MemberRole.USER);

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("LoginMemberIdHandlerResolver 테스트")
    @Test
    void jwtLoginMemberIdHandlerResolverTest() throws Exception {
        long nextLong = new Random().nextLong();
        String accessToken = tokenUtils.createAccessToken(nextLong, MemberRole.USER);

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("[유효한 토큰] memberId: " + nextLong))
                .andDo(print());
    }

    @DisplayName("액세스토큰없이 로그인 필요 서비스 접근시")
    @Test
    void jwtNonCookieTest() throws Exception {

        mockMvc.perform(post("/members/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @DisplayName("다른 키값으로 생성한 액세스 토큰을 보냈을 시")
    @Test
    void jwtNotHttpOnlyCookieTest() throws Exception {
        String token = Jwts.builder()
                .setSubject("board_project")
                .claim("id", 1L)
                .claim("role", MemberRole.USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRE_TIME))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
                .compact();

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
