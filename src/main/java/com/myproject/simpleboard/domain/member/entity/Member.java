package com.myproject.simpleboard.domain.member.entity;

import com.myproject.simpleboard.domain.member.entity.model.MemberRole;
import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import com.myproject.simpleboard.domain.shared.model.BaseTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public Member(String username, String pwd, MemberRole role, MemberStatus memberStatus) {
        this.username = username;
        this.pwd = pwd;
        this.role = role;
        this.status = memberStatus;
    }

    public void updateUsernameOrPwd(String username, String pwd) {
        if(StringUtils.hasText(username)) this.username = username;
        if(StringUtils.hasText(pwd)) this.pwd = pwd;
    }

    public void punishPause(String reason) {
        this.status = MemberStatus.PAUSE;
        this.memberStatusDetail = new MemberStatusDetail(MemberStatus.PAUSE, reason, this);
    }
    public void punishSuspend(String reason) {
        this.status = MemberStatus.SUSPEND;
        this.memberStatusDetail = new MemberStatusDetail(MemberStatus.SUSPEND, reason, this);
    }
}
