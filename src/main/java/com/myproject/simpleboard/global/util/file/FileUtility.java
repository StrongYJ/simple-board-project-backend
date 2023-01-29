package com.myproject.simpleboard.global.util.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileUtility {
    
    @Value("${file.path}")
    private String path;
    
    public Path getFullPath(String filename) {
        return Paths.get(path).resolve(filename);
    }
    public List<UploadFile> saveFile(MultipartFile... files) {
        List<UploadFile> savedFiles = new ArrayList<>();
        Arrays.asList(files).stream()
            .filter(file -> !file.isEmpty())
            .forEach(f -> {
                try {
                    savedFiles.add(saveSingleFile(f));
                } catch (IllegalStateException | IOException e) {
                    log.error("[FileUtiliy] Error", e.getMessage());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
                }
            });
        return savedFiles;
    }

    private UploadFile saveSingleFile(MultipartFile file) throws IllegalStateException, IOException {
        if(file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();
        String savedFilename = createStoreFilename(originalName);
        file.transferTo(getFullPath(savedFilename));
        return new UploadFile(originalName, savedFilename);
    }


    private String createStoreFilename(String originalFilename) {
        return UUID.randomUUID() + "." + extractExt(originalFilename);
    }

    private String extractExt(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
