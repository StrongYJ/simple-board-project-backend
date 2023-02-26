package com.myproject.simpleboard.domain.member.entity;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.domain.member.entity.model.PunishDetail;
import com.myproject.simpleboard.domain.shared.model.BaseTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String pwd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Embedded
    private PunishDetail punishDetail;

    @Builder
    public Member(String username, String pwd, MemberRole role) {
        this.username = username;
        this.pwd = pwd;
        this.role = role;
        this.status = MemberStatus.NORMAL;
    }

    public void updateUsernameOrPwd(String username, String pwd) {
        if(StringUtils.hasText(username)) this.username = username;
        if(StringUtils.hasText(pwd)) this.pwd = pwd;
    }

    public void paused(String reason, LocalDate deadline) {
        this.status = MemberStatus.PAUSE;
        this.punishDetail = new PunishDetail(reason, deadline);
    }
    public void suspended(String reason) {
        this.status = MemberStatus.SUSPEND;
        this.punishDetail = new PunishDetail(reason, null);
    }
}
