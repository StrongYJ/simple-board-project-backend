package com.myproject.simpleboard.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    Optional<Member> findMemberByUsername(String username);
}
