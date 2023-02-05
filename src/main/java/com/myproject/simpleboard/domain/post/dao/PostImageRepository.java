package com.myproject.simpleboard.domain.post.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.simpleboard.domain.post.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    
}
