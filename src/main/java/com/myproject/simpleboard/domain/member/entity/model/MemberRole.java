package com.myproject.simpleboard.domain.member.entity.model;

import lombok.Getter;

@Getter
public enum MemberRole {
    USER("일반유저"), MANAGER("관리자"), ADMIN("슈퍼관리자");

    private final String title;
    private MemberRole(String title) {
        this.title = title;
    }
}
