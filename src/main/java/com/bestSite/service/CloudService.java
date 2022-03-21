package com.bestSite.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class CloudService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Autowired
    public CloudService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String uuidFile = UUID.randomUUID().toString();
        String fileName = uuidFile + "." + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        new ResponseEntity<>(fileName, HttpStatus.OK);
        fileObj.delete();
        return fileName;
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }

    public boolean fileIsPresent(MultipartFile file){
        return Objects.equals(file.getContentType(), "image/png")
                || Objects.equals(file.getContentType(), "image/jpeg");
    }
}
