package com.myproject.simpleboard.domain.member.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberRole {
    USER("일반유저"), MANAGER("관리자"), ADMIN("슈퍼관리자");

    private final String code;
    private final String title;
    private MemberRole(String title) {
        this.code = name();
        this.title = title;
    }
}
