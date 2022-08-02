package com.devcourse.checkmoi.domain.file.service;

import static org.apache.commons.io.FilenameUtils.getName;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class S3Upload {

    private final AmazonS3 amazonS3;

    private final String url;

    private final String bucketName;

    public S3Upload(AmazonS3 amazonS3, String url, String bucketName) {
        this.amazonS3 = amazonS3;
        this.url = url;
        this.bucketName = bucketName;
    }

    public String upload(byte[] bytes, String key, String contentType,
        Map<String, String> metadata) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        objectMetadata.setContentType(contentType);

        if (metadata != null && !metadata.isEmpty()) {
            objectMetadata.setUserMetadata(metadata);
        }

        PutObjectRequest request = new PutObjectRequest(bucketName, key,
            new ByteArrayInputStream(bytes), objectMetadata);

        return put(request);
    }

    public void delete(String url) {
        String key = getName(url);

        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, key);

        executeDelete(request);
    }

    private String put(PutObjectRequest request) {
        amazonS3.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));

        StringBuilder sb = new StringBuilder(url);

        if (!url.endsWith("/")) {
            sb.append("/");
        }

        sb.append(bucketName);
        sb.append("/");
        sb.append(request.getKey());

        return sb.toString();
    }

    private void executeDelete(DeleteObjectRequest request) {
        amazonS3.deleteObject(request);
    }
}
