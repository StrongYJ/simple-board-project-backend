package com.myproject.simpleboard.domain.member.dto;

import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;

public record MemberPunishDto(MemberStatus status, String reason, Long days) {
}
