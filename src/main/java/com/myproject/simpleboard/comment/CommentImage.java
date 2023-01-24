package com.myproject.simpleboard.comment;

import com.myproject.simpleboard.global.common.entity.File;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "file_id")
public class CommentImage extends File {
    
    @JoinColumn(name = "comment_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Comment comment;
}
