package com.myproject.simpleboard.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record JoinDto(
    @NotBlank
    String username, 
    @NotBlank
    String password
) {

}
