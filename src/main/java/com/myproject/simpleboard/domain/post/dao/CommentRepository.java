package com.myproject.simpleboard.domain.post.dao;

import com.myproject.simpleboard.domain.post.entity.Comment;
import com.myproject.simpleboard.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
