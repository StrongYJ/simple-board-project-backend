package com.myproject.simpleboard.domain.member.entity;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.domain.shared.model.BaseTime;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberStatusDetail memberStatusDetail;

    public Member(String username, String pwd, MemberRole role, MemberStatus memberStatus) {
        this.username = username;
        this.pwd = pwd;
        this.role = role;
        this.status = memberStatus;
    }

    public void modifyUsername(String username) {
        this.username = username;
    }

    public void modifyPwd(String pwd) {
        this.pwd = pwd;
    }

    public void modifyStatus(MemberStatus memberStatus) {
        this.status = memberStatus;
    }

    public void setMemberStatusDetail(MemberStatus status, String reason) {
        this.memberStatusDetail = new MemberStatusDetail(status, reason, this);
    }
}
