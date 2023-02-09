package com.myproject.simpleboard.domain.member.dto;

import com.myproject.simpleboard.domain.member.entity.Member;

import java.time.LocalDateTime;

public record MemberDetailDto(Long id, String username, LocalDateTime createdDate, String role, String status, LocalDateTime statusModifiedDate) {
    public MemberDetailDto(Member member) {
        this(member.getId(), member.getUsername(), member.getCreatedDate(), member.getRole().getTitle(), member.getStatus().getTitle(), member.getModifiedDate());
    }
}
