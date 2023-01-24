package com.myproject.simpleboard.comment;

import com.myproject.simpleboard.global.common.customtype.Deleted;
import com.myproject.simpleboard.global.common.customtype.Writer;
import com.myproject.simpleboard.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    
    @Id @GeneratedValue
    private Long id;

    private String content;
    
    @Embedded
    private Writer writer;

    @Embedded
    private Deleted isDeleted;
}
