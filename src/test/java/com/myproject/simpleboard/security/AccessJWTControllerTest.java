package com.myproject.simpleboard.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MissingClaimException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccessJWTControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenUtils tokenUtils;

    private Long dummyMemberId;

    @BeforeAll
    void createDummyMember() {
        Member member = new Member("test1", "fkwemfkwefm", MemberRole.USER, MemberStatus.NORMAL);
        memberRepository.save(member);
        dummyMemberId = member.getId();
    }

    @DisplayName("유효한 토큰 테스트")
    @Test
    void jwtCookieTest() throws Exception {
        final String accessToken = tokenUtils.createAccessToken(dummyMemberId, MemberRole.USER);

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("AuthenticationArgumentResolver 테스트")
    @Test
    void jwtLoginMemberIdHandlerResolverTest() throws Exception {
        String accessToken = tokenUtils.createAccessToken(dummyMemberId, MemberRole.USER);

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("[유효한 토큰] memberId: " + dummyMemberId))
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
        String token = JwtProperties.ACCESS_PREFIX + Jwts.builder()
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

    @Test
    void 다른_sub로_검증시() {
        String accessToken = tokenUtils.createAccessToken(dummyMemberId, MemberRole.USER);
        Assertions.assertThatThrownBy(() -> {
            String token = tokenUtils.accessTokenResolve(accessToken);
            tokenUtils.verifyRefresh(token);
        }).isInstanceOf(IncorrectClaimException.class);

    }
}
