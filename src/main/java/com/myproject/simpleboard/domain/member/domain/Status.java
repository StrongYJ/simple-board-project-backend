package com.myproject.simpleboard.domain.member.domain;

import lombok.Getter;

@Getter
public enum Status {
    NORMAL("정상"), PAUSE("일시정지"), SUSPEND("영구정지"), WITHDRAWAL("탈퇴");

    private final String kor;
    private Status(String kor) {
        this.kor = kor;
    }
}
