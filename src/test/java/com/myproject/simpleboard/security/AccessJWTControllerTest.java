package com.myproject.simpleboard.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.global.security.AuthMember;
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
        Member member = new Member("test1", "fkwemfkwefm", MemberRole.USER);
        memberRepository.save(member);
        dummyMemberId = member.getId();
    }

    @DisplayName("????????? ?????? ?????????")
    @Test
    void jwtCookieTest() throws Exception {
        final String accessToken = tokenUtils.createAccessToken(dummyMemberId, MemberRole.USER);

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("AuthenticationArgumentResolver ?????????")
    @Test
    void jwtLoginMemberIdHandlerResolverTest() throws Exception {
        String accessToken = tokenUtils.createAccessToken(dummyMemberId, MemberRole.USER);

        mockMvc.perform(post("/members/test").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("[????????? ??????] memberId: " + new AuthMember(dummyMemberId, MemberRole.USER)))
                .andDo(print());
    }

    @DisplayName("????????????????????? ????????? ?????? ????????? ?????????")
    @Test
    void jwtNonCookieTest() throws Exception {

        mockMvc.perform(post("/members/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @DisplayName("?????? ???????????? ????????? ????????? ????????? ????????? ???")
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
    void ??????_sub???_?????????() {
        String accessToken = tokenUtils.createAccessToken(dummyMemberId, MemberRole.USER);
        Assertions.assertThatThrownBy(() -> {
            String token = tokenUtils.accessTokenResolve(accessToken);
            tokenUtils.verifyRefresh(token);
        }).isInstanceOf(IncorrectClaimException.class);

    }
}
