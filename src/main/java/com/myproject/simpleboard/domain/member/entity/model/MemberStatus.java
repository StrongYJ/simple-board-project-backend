package com.myproject.simpleboard.domain.member.entity.model;

import lombok.Getter;

@Getter
public enum MemberStatus {
    NORMAL("정상"), PAUSE("일시정지"), SUSPEND("영구정지"), WITHDRAWAL("탈퇴");

    private final String title;
    private MemberStatus(String title) {
        this.title = title;
    }
}
