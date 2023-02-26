package com.myproject.simpleboard.domain.post.entity;

import com.myproject.simpleboard.domain.shared.model.File;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "file_id")
@Entity
public class PostImage extends File {
    
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public PostImage(String originalName, String savedName, Post post) {
        this.originalName = originalName;
        this.savedName = savedName;
        this.post = post;
    }
}
