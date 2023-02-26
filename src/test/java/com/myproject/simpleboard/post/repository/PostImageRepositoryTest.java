package com.myproject.simpleboard.post.repository;

import com.myproject.simpleboard.domain.post.entity.Post;
import com.myproject.simpleboard.domain.post.entity.PostImage;
import com.myproject.simpleboard.domain.post.repository.PostImageRepository;
import com.myproject.simpleboard.domain.post.repository.PostRepository;
import com.myproject.simpleboard.global.util.file.FileUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
class PostImageRepositoryTest {

    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FileUtility fileUtility;

    @BeforeEach
    void init() {
        postRepository.save(new Post("sports", "공지", "test", "testBody", null));
    }

    @Test
    void notNullpointerEx() {
        Post post = postRepository.findAll().get(0);
        List<PostImage> images = postImageRepository.findByPost(post);
        Assertions.assertEquals(0, images.size());
        Assertions.assertNotNull(images);
        Assertions.assertDoesNotThrow(() -> {
            images.forEach(img -> new File(fileUtility.getFullPath(img.getSavedName()).toString()).delete());
            postImageRepository.deleteByPost(post);
        });

    }
}
