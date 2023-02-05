package com.myproject.simpleboard.domain.post.entity;

import java.util.ArrayList;
import java.util.List;

import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.shared.model.BaseTime;
import com.myproject.simpleboard.domain.shared.model.Deleted;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTime {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Lob
    private String body;
    
    @Embedded
    private Deleted isDeleted;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String writer;
    @OneToMany(mappedBy = "post")
    List<PostImage> images = new ArrayList<>();

    public Post(String title, String body, Member member) {
        this.title = title;
        this.body = body;
        this.member = member;
        this.writer = member.getUsername();
    }

    @PrePersist
    private void deleteInit() {
        isDeleted = new Deleted(false);
    }
}
