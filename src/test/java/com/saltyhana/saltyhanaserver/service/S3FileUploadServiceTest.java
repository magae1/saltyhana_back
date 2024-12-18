package com.saltyhana.saltyhanaserver.service;


import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class S3FileUploadServiceTest {

  @Autowired
  private S3FileUploadService s3FileUploadService;

  public void testUploadFileOnS3() {
    File file = new File("src/test/resources/test123.png");
    Path filePath = file.toPath();
    Optional<String> result = s3FileUploadService.uploadFileOnS3(filePath, "multipart/form-data",
        file.length());
    if (result.isPresent()) {
      System.out.println("File uploaded: " + result.get());
    } else {
      System.out.println("File not uploaded");
    }
  }

}
