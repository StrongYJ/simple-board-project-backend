package com.myproject.simpleboard.domain.member.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Setter(value = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public final class PunishDetail {
    @Column(name = "punish_reason")
    private String reason;

    @Column(name = "punish_deadline")
    private LocalDate deadline;
}
