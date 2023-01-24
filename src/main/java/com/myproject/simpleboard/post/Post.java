package com.myproject.simpleboard.post;

import com.myproject.simpleboard.global.common.customtype.Deleted;
import com.myproject.simpleboard.global.common.entity.BaseTimeEntity;
import com.myproject.simpleboard.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Lob
    private String content;
    
    @Embedded
    private Deleted deleted;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member namedWriter;
    private String writer;
}
