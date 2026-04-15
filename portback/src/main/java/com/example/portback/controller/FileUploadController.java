package com.example.portback.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class FileUploadController {

    @Value("${upload.dir:/app/uploads}")
    private String uploadDir;

    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB
    private static final java.util.Set<String> ALLOWED_TYPES = java.util.Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어있습니다."));
        }
        if (file.getSize() > MAX_SIZE) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일 크기는 10MB 이하여야 합니다."));
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미지 파일만 업로드 가능합니다. (JPG, PNG, GIF, WEBP)"));
        }

        String ext = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + ext;

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), dir.resolve(filename));

        String url = "/uploads/" + filename;
        return ResponseEntity.ok(Map.of("url", url));
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf('.'));
    }
}
