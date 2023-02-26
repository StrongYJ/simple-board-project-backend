package com.myproject.simpleboard.domain.post.entity;

import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.shared.model.BaseTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTime {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Comment(String content, Member member, Post post) {
        this.content = content;
        this.post = post;
        this.member = member;
    }

    /**
     * 게시글을 쓴 회원의 이름을 반환합니다.
     * @return 회원이 존재한다면 회원의 이름을 반환하고, 존재하지 않는다면 "탈퇴한 유저"를 반환합니다.
     */
    public String getAuthor() {
        return Optional.ofNullable(member).map(Member::getUsername).orElse("탈퇴한 유저");
    }
}
