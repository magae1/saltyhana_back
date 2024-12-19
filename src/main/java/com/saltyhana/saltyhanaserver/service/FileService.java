package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileService {
    private static final String UPLOAD_DIR = "uploads"; // 저장 디렉토리 설정

    public String saveBase64File(String base64String) {
        try {
            // 저장 경로 설정
            createUploadDirIfNotExists(); // 디렉토리 생성

            // 랜덤 파일 이름 생성
            String randomFileName = UUID.randomUUID() + ".png";
            String outputPath = UPLOAD_DIR + "/" + randomFileName;

            // Base64 문자열로 파일 생성
            createFileFromBase64(base64String, outputPath);

            return outputPath;
        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new FileUploadException();
        }
    }

    private void createUploadDirIfNotExists() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdir();
            if (!created) {
                throw new FileUploadException("파일 업로드 디렉토리를 생성할 수 없습니다.");
            }
        }
    }

    private void createFileFromBase64(String base64String, String outputPath) throws IOException {
        // "data:image/png;base64," 제거
        String base64Data = base64String.substring(base64String.indexOf(",") + 1);

        // Base64 디코딩
        byte[] fileBytes = Base64.getDecoder().decode(base64Data);

        // 파일 생성
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(fileBytes);
        }
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (!deleted) {
                log.warn("Failed to delete file: {}", filePath);
            } else {
                log.info("Successfully deleted file: {}", filePath);
            }
        } else {
            log.warn("File does not exist or is not a valid file: {}", filePath);
        }
    }
}
