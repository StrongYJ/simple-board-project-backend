package com.myproject.simpleboard.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
}
