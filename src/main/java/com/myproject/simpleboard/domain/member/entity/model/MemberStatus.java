package com.myproject.simpleboard.domain.member.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberStatus {
    NORMAL("정상"), PAUSE("일시정지"), SUSPEND("영구정지");

    private final String code;
    private final String title;
    private MemberStatus(String title) {
        this.code = name();
        this.title = title;
    }
}
