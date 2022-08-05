package com.devcourse.checkmoi.global.config;

import static org.springframework.http.HttpHeaders.LOCATION;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.devcourse.checkmoi.domain.file.service.S3Upload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("http://localhost:3000", "https://checkmoi.vercel.app")
            .allowedMethods("*")
            .allowedHeaders("*")
            .exposedHeaders(LOCATION);
    }

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
