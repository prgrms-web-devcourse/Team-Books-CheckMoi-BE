package com.devcourse.checkmoi.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.devcourse.checkmoi.domain.file.service.S3Upload;
import com.devcourse.checkmoi.global.config.property.AwsConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Bean
    public AmazonS3 amazonS3Client(AwsConfigProperties awsConfigure) {
        return AmazonS3ClientBuilder.standard()
            .withRegion(Regions.fromName(awsConfigure.getRegion()))
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                        awsConfigure.getAccessKey(),
                        awsConfigure.getSecretKey())
                )
            )
            .build();
    }

    @Bean
    public S3Upload s3Upload(AmazonS3 amazonS3, AwsConfigProperties awsProperties) {
        return new S3Upload(amazonS3, awsProperties.getUrl(), awsProperties.getBucketName());
    }
}
