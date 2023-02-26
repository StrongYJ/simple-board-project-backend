package com.myproject.simpleboard.domain.post.repository;

import com.myproject.simpleboard.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPost(Post post);
    @Modifying(clearAutomatically = true)
    void deleteByPost(Post post);
}
