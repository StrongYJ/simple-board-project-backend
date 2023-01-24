package com.myproject.simpleboard.comment;

import com.myproject.simpleboard.global.common.customtype.Deleted;
import com.myproject.simpleboard.global.common.customtype.Writer;
import com.myproject.simpleboard.global.common.entity.BaseTimeEntity;
import com.myproject.simpleboard.post.Post;

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
public class Comment extends BaseTimeEntity {
    
    @Id @GeneratedValue
    private Long id;

    private String content;
    
    @Embedded
    private Writer writer;

    @Embedded
    private Deleted isDeleted;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Comment(String content, Writer writer, Post post) {
        this.content = content;
        this.writer = writer;
        this.post = post;
    }
    
    @PrePersist
    private void deleteInit() {
        isDeleted = new Deleted(false, null);
    }
    
}
