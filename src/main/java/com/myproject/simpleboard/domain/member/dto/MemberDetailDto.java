package com.myproject.simpleboard.domain.member.dto;

import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;

import java.time.LocalDateTime;

public record MemberDetailDto(Long id, String username, LocalDateTime createdDate, MemberRole role, String status, LocalDateTime statusModifiedDate) {
    public MemberDetailDto(Member member) {
        this(member.getId(), member.getUsername(), member.getCreatedDate(), member.getRole(), member.getStatus().getTitle(), member.getModifiedDate());
    }
}
