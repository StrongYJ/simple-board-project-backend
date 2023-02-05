package com.myproject.simpleboard.domain.member.dto;

import com.myproject.simpleboard.domain.member.entity.Member;

import java.time.LocalDateTime;

public record MemberBasicDto(String username, LocalDateTime createdDate, String status, String role) {
    public MemberBasicDto(Member member) {
        this(member.getUsername(), member.getCreatedDate(), member.getStatus().getStatus().getTitle(), member.getRole().getTitle());
    }
}
