package com.myproject.simpleboard;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.global.security.TokenUtils;

import io.jsonwebtoken.JwtException;

@SpringBootTest
@Slf4j
class SimpleBoardApplicationTests {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void contextLoads() throws InterruptedException {
        String compact = tokenUtils.createToken(1L, MemberRole.USER);

        try {
            Thread.sleep(2000L);
            tokenUtils.verify(compact);
        } catch (JwtException e) {
            log.error("type = {}", e.toString());
            log.error("message = {}", e.getMessage());
        }
    }
}
