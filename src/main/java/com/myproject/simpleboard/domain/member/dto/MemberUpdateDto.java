package com.myproject.simpleboard.domain.member.dto;

import org.hibernate.validator.constraints.Length;

public record MemberUpdateDto(
        @Length(min = 4, max = 20) String username,
        @Length(min = 4, max = 16) String pwd
) {
}
