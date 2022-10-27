package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ContentDTO;

public interface FileService {
    String uploadFile(MultipartFile file, ContentDTO contentType );
}
