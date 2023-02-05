package com.myproject.simpleboard.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MemberJoinDto(
    @NotBlank @Length(min = 4, max = 20) String username,
    @NotBlank @Length(min = 4, max = 16) String password
) {

}
