package com.myproject.simpleboard.post;

import com.myproject.simpleboard.global.common.customtype.Deleted;
import com.myproject.simpleboard.global.common.customtype.Writer;
import com.myproject.simpleboard.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
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
    private Deleted isDeleted;
    
    @Embedded
    private Writer writer;

    public Post(String title, String content, Writer writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    @PrePersist
    private void deleteInit() {
        isDeleted = new Deleted(false, null);
    }
}
