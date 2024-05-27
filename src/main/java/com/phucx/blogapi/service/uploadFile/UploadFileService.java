package com.phucx.blogapi.service.uploadFile;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.blogapi.model.FileFormat;

public interface UploadFileService {
    public FileFormat uploadImage(MultipartFile files);
    public byte[] getImage(String filename) throws IOException;
    public String getMimeType (String filename) throws IOException;
}
