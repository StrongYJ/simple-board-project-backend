package com.myproject.simpleboard.domain.post.entity;

import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.shared.model.BaseTime;
import com.myproject.simpleboard.domain.shared.model.Deleted;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTime {
    
    @Id @GeneratedValue
    private Long id;

    private String content;
    
    
    @Embedded
    private Deleted isDeleted;
    
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String writer;

    public Comment(String content, Member member, Post post) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.writer = member.getUsername();
    }

    @PrePersist
    private void deleteInit() {
        isDeleted = new Deleted(false);
    }
    
}
