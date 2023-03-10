package com.myproject.simpleboard.domain.shared.api;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.myproject.simpleboard.domain.post.repository.PostImageRepository;
import com.myproject.simpleboard.domain.post.entity.PostImage;
import com.myproject.simpleboard.global.util.file.FileUtility;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    
    private final FileUtility fileUtility;
    private final PostImageRepository postImgRepo;
    
    @GetMapping("/{filename}")
    public Resource getImages(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileUtility.getFullPath(filename));
    }

    @GetMapping("/{image-id}")
    public ResponseEntity<Resource> downloadPostImages(@PathVariable("image-id") Long id) throws MalformedURLException {
        PostImage postImg = postImgRepo.findById(id).orElseThrow();
        UrlResource urlResource = new UrlResource("file:" + fileUtility.getFullPath(postImg.getSavedName()));
        String encodeOriginalName = UriUtils.encode(postImg.getOriginalName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeOriginalName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(urlResource);
    }
}
