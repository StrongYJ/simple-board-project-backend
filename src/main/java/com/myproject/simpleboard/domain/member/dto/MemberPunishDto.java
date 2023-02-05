package com.myproject.simpleboard.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberPunishDto(@NotBlank String status, String reason) {
}
