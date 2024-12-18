package com.saltyhana.saltyhanaserver.service;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


@Log4j2
@Service
@RequiredArgsConstructor
public class S3FileUploadService {

  @Value("${aws.s3.bucketName}")
  private String bucketName;

  private final S3AsyncClient s3AsyncClient;


  public Optional<String> uploadFileOnS3(Path filePath, String contentType, Long contentLength) {
    log.info("Uploading file {} to S3", filePath.getFileName());
    PutObjectRequest putObjRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(filePath.getFileName().toString())
        .contentType(contentType)
        .contentLength(contentLength)
        .build();
    CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(putObjRequest,
        filePath);
    try {
      future.join();
      log.info("File uploaded successfully");
      return Optional.of(
          String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, Region.AP_NORTHEAST_2,
              filePath.getFileName()));
    } catch (Exception e) {
      log.error(e);
      return Optional.empty();
    }
  }
}


