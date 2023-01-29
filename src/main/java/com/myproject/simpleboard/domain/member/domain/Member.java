package com.myproject.simpleboard.domain.member.domain;

import com.myproject.simpleboard.domain.shared.model.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String pwd;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Embedded
    private MemberStatus status;

    public Member(String username, String pwd, MemberRole role, MemberStatus status) {
        this.username = username;
        this.pwd = pwd;
        this.role = role;
        this.status = status;
    }
}
