package com.phucx.blogapi.service.uploadFile;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    public String uploadImage(MultipartFile files);
    public byte[] getImage(String filename) throws IOException;
    public String getMimeType (String filename) throws IOException;
}
