package com.phucx.blogapi.service.uploadFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.phucx.blogapi.config.StoredFileProperties;
import com.phucx.blogapi.model.FileFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadFileServiceImp implements UploadFileService{
    // @Autowired
    // private ResourceLoader resourceLoader;
    @Autowired
    private StoredFileProperties storedFileProperties;
    @Override
    public FileFormat uploadImage(MultipartFile file) {
        log.info("uploadImage({})", file.getOriginalFilename());
        try {
            // get stored direction
            // String filePath = storedUploadDir();
            // get filename
            String filename = file.getOriginalFilename();
            // set newfilename
            int lastIndexOfDot = filename.lastIndexOf(".");
            String extension = filename.substring(lastIndexOfDot+1);
            String randomFilename = UUID.randomUUID().toString();
            String newfileName = randomFilename + "." + extension;

            Path targetPath = Path.of(storedFileProperties.getImageStoredDir(), newfileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            String currentUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            log.info("{}/posts/image/{}", currentUrl, newfileName);
            return new FileFormat(currentUrl + "/posts/image/" +newfileName);
            // return filePath;
        } catch (IOException e) {
            log.error("Error uploading file: " + e.getMessage());
            return new FileFormat("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public byte[] getImage(String filename) throws IOException {
        log.info("getImage({})", filename);
        String storedpath = storedFileProperties.getImageStoredDir();
        if(storedpath.charAt(storedpath.length()-1)!='/') storedpath+='/';
        Path path = Paths.get(storedpath + filename);
        byte[] imageBytes = Files.readAllBytes(path);
        return imageBytes;
    }

    @Override
    public String getMimeType (String filename) throws IOException{
        log.info("getMimeType({})", filename);
        // String filePath = storedUploadDir();
        String storedpath = storedFileProperties.getImageStoredDir();
        if(storedpath.charAt(storedpath.length()-1)!='/') storedpath+='/';
        // String filePath = 
        Path path = Paths.get(storedpath + filename);
        String mimeType = Files.probeContentType(path);
        return mimeType;
    }

    // private String storedUploadDir() throws IOException{
    //     final Resource fileResource = resourceLoader.getResource("classpath:application.properties");
    //     String filePath = fileResource.getURL().getPath().substring(1, fileResource.getURL().getPath().length()-"application.properties".length());
    //     return filePath;
    // }
    
}
