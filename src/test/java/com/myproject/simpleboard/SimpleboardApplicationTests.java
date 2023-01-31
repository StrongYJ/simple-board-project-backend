package com.myproject.simpleboard;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.domain.Member;
import com.myproject.simpleboard.domain.member.domain.MemberRole;
import com.myproject.simpleboard.domain.member.domain.MemberStatus;
import com.myproject.simpleboard.domain.member.domain.Status;
import com.myproject.simpleboard.global.security.JwtProperties;
import com.myproject.simpleboard.global.security.TokenUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SimpleboardApplicationTests {

    @Autowired private MemberRepository memberRepository;
    @Autowired private TokenUtils tokenUtils;

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
	@Test
	void createMember() {
        Member m = new Member("ddd", "1234", MemberRole.USER, new MemberStatus(Status.NORMAL));
        memberRepository.save(m);
    }
 
}
