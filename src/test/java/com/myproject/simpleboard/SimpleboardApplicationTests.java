package com.myproject.simpleboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myproject.simpleboard.domain.member.MemberRepository;
import com.myproject.simpleboard.domain.member.domain.Member;
import com.myproject.simpleboard.domain.member.domain.MemberRole;
import com.myproject.simpleboard.domain.member.domain.MemberStatus;
import com.myproject.simpleboard.domain.member.domain.Status;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SimpleboardApplicationTests {

    @Autowired MemberRepository memberRepository;
    
	@Test
	void contextLoads() {
	}
	@Test
	void createMember() {
        Member m = new Member("ddd", "1234", MemberRole.USER, new MemberStatus(Status.NORMAL));
        memberRepository.save(m);
    }   
}
