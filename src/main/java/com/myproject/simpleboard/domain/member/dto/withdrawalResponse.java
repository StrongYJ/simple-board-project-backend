package com.myproject.simpleboard.domain.member.dto;

import java.time.LocalDate;

public record withdrawalResponse(String username, LocalDate deleteDate) {
}
