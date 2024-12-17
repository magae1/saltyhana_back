package com.saltyhana.saltyhanaserver.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;


    // 사용자 정보 조회
    public MyPageResponseDTO getUserInfo(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자"));
        return UserMapper.toMyPageResponseDTO(user);
    }

    // 사용자 정보 수정
    @Transactional
    public MyPageResponseDTO updateUserInfo(Long userId, MyPageUpdateForm updateForm) {
        if (authService.checkEmailExists(updateForm.getEmail())) {
            throw new AlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // ID 중복 체크
        if (authService.checkIdentifierExists(updateForm.getIdentifier())) {
            throw new AlreadyExistsException("이미 사용 중인 ID입니다.");
        }

        String password = updateForm.getPassword();
        String confirmPassword = updateForm.getConfirmPassword();
        if (password != null && confirmPassword != null && (
                !updateForm.getPassword().equals(updateForm.getConfirmPassword()))) {
            throw new WrongPasswordException();
        }

        User user = userRepo.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("유저");
        });

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

        user = UserMapper.toEntity(userDto);
        User updatedUser = userRepo.save(user);
        return UserMapper.toMyPageResponseDTO(updatedUser);
    }
}