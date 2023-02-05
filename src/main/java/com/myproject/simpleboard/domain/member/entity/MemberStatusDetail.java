package com.myproject.simpleboard.domain.member.entity;

import com.myproject.simpleboard.domain.member.entity.model.MemberStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberStatusDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    private String reason;

    private Long period;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public MemberStatusDetail(MemberStatus status, String reason, Member member) {
        this.status = status;
        this.reason = reason;
        this.member = member;
    }

    @PrePersist
    private void pausePeriod() {
        if(status == MemberStatus.PAUSE)
            this.period = LocalDateTime.now().plusWeeks(1L).toEpochSecond(ZoneOffset.UTC);
    }
}
