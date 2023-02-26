package com.myproject.simpleboard.domain.member.dto;

import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.domain.member.entity.model.PunishDetail;

import java.time.LocalDateTime;

public record MemberDto(Long id, String username, MemberRole role, MemberStatus status, PunishDetail punishDetail, LocalDateTime createdDate, LocalDateTime modifiedDate) {
    public MemberDto(Member member) {
        this(member.getId(), member.getUsername(), member.getRole(), member.getStatus(), member.getPunishDetail(), member.getCreatedDate(), member.getModifiedDate());
    }

    public MemberDto removeId() {
        return new MemberDto(null, username, role, status, punishDetail, createdDate, modifiedDate);
    }
}
