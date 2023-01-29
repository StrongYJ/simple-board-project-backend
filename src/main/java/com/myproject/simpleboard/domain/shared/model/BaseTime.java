package com.myproject.simpleboard.domain.shared.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseTime {
    
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdDT;

    @LastModifiedDate
    private LocalDateTime updatedDT;

}