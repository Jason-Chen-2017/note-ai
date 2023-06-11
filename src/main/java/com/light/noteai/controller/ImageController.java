package com.light.noteai.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/upload")
    public String upload(@RequestParam("editormd-image-file") MultipartFile file) throws IOException {
        String imageName = UUID.randomUUID() + ".jpg";
        String path = "/home/me/static/images/" + imageName;

        Files.copy(file.getInputStream(), new File(path).toPath());

        return path;
    }
}