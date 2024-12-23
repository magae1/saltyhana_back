package com.saltyhana.saltyhanaserver.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saltyhana.saltyhanaserver.dto.MyPageResponseDTO;
import com.saltyhana.saltyhanaserver.dto.UserDTO;
import com.saltyhana.saltyhanaserver.dto.form.MyPageUpdateForm;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.AlreadyExistsException;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.exception.WrongPasswordException;
import com.saltyhana.saltyhanaserver.mapper.UserMapper;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Path;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final FileService fileService;
    private final S3FileUploadService s3FileUploadService;

    // 사용자 정보 조회
    public MyPageResponseDTO getUserInfo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자"));
        return UserMapper.toMyPageResponseDTO(user);
    }

    // 사용자 정보 수정
    @Transactional
    public MyPageResponseDTO updateUserInfo(Long userId, MyPageUpdateForm updateForm) {
        // 이메일 중복 체크
        if (authService.checkEmailExists(updateForm.getEmail())) {
            throw new AlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // ID 중복 체크
        if (authService.checkIdentifierExists(updateForm.getIdentifier())) {
            throw new AlreadyExistsException("이미 사용 중인 ID입니다.");
        }

        // 비밀번호 확인
        String password = updateForm.getPassword();
        String confirmPassword = updateForm.getConfirmPassword();
        if (password != null && confirmPassword != null && (
                !updateForm.getPassword().equals(updateForm.getConfirmPassword()))) {
            throw new WrongPasswordException();
        }

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("유저");
        });

        // UserDTO로 변환 및 필드 업데이트
        UserDTO userDto = UserMapper.toDTO(user);
        if (updateForm.getEmail() != null) {
            userDto.setEmail(updateForm.getEmail());
        }
        if (updateForm.getIdentifier() != null) {
            userDto.setIdentifier(updateForm.getIdentifier());
        }
        if (updateForm.getPassword() != null) {
            userDto.setPassword(passwordEncoder.encode(updateForm.getPassword()));
        }
        if (updateForm.getName() != null) {
            userDto.setName(updateForm.getName());
        }
        if (updateForm.getBirth() != null) {
            userDto.setBirth(updateForm.getBirth());
        }

        // 프로필 이미지 처리
        if (updateForm.getProfileImage() == null) {
            userDto.setProfileImage(null); // 프로필 이미지 명시적으로 null 설정
        } else if (updateForm.getProfileImage() != null) {
            // 로컬에 Base64 이미지 파일 저장
            String localFilePath = fileService.saveBase64File(updateForm.getProfileImage());

            try {
                // S3 파일 업로드
                String contentType = "multipart/form-data";
                File file = new File(localFilePath);
                Path filePath = file.toPath();
                String uploadedImageUrl = s3FileUploadService.uploadFileOnS3(filePath, contentType, file.length())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패"));

                // 업로드된 이미지 URL 설정
                userDto.setProfileImage(uploadedImageUrl);
            } finally {
                // 로컬 임시 파일 삭제
                if (localFilePath != null) {
                    fileService.deleteFile(localFilePath);
                }
            }
        }

        // Entity로 변환 및 저장
        user = UserMapper.toEntity(userDto);
        User updatedUser = userRepository.save(user);

        return UserMapper.toMyPageResponseDTO(updatedUser);
    }

    public boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public String getTmpPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String newPassword = "";

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            newPassword += charSet[idx];
        }

        return newPassword;
    }

    @Transactional
    public void updateTmpPassword(String tmpPassword, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new NotFoundException(email);
        }

        UserDTO userDto = UserMapper.toDTO(user);
        userDto.setPassword(passwordEncoder.encode(tmpPassword));

        user = UserMapper.toEntity(userDto);
        userRepository.save(user);
    }
}