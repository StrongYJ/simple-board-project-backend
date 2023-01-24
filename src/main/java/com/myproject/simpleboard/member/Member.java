package com.myproject.simpleboard.member;

import com.myproject.simpleboard.global.common.entity.BaseTimeEntity;
import com.myproject.simpleboard.member.customtype.MemberStatus;
import com.myproject.simpleboard.member.customtype.Role;

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
public class Member extends BaseTimeEntity {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String pwd;

    @Enumerated(EnumType.STRING)
    private Role Role;

    @Embedded
    private MemberStatus status;
}
