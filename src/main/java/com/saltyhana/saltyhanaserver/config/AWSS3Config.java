package com.saltyhana.saltyhanaserver.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;


@Configuration
public class AWSS3Config {

  @Value("${aws.s3.accessKey}")
  private String accessKey;
  @Value("${aws.s3.secretKey}")
  private String secretKey;


  @Bean
  public S3AsyncClient amazonS3AsyncClient() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    return S3AsyncClient.builder()
        .multipartEnabled(true)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.AP_NORTHEAST_2)
        .build();
  }
}
