package com.myproject.simpleboard.domain.post.entity;

import com.myproject.simpleboard.domain.member.entity.Member;
import com.myproject.simpleboard.domain.post.dto.post.PostUpdateDto;
import com.myproject.simpleboard.domain.shared.model.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String board;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Post(String board, String type, String title, String body, Member member) {
        this.board = board;
        this.type = type;
        this.title = title;
        this.body = body;
        this.member = member;
    }

    @OneToMany(mappedBy = "post")
    List<PostImage> images = new ArrayList<>();

    /**
     * 게시글을 쓴 회원의 이름을 반환합니다.
     * @return 회원이 존재한다면 회원의 이름을 반환하고, 존재하지 않는다면 "탈퇴한 유저"를 반환합니다.
     */
    public String getAuthor() {
        return Optional.ofNullable(member).map(Member::getUsername).orElse("탈퇴한 유저");
    }

    public void updateTextInfo(PostUpdateDto data) {
        if(StringUtils.hasText(data.type())) this.type = data.type();
        if(StringUtils.hasText(data.title())) this.title = data.title();
        if(StringUtils.hasText(data.body())) this.body = data.body();
    }
}
