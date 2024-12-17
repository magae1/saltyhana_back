package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.MyPageResponseDTO;
import com.saltyhana.saltyhanaserver.dto.MyPageUpdateDTO;
import com.saltyhana.saltyhanaserver.entity.UserMyPage;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 정보 조회
    public MyPageResponseDTO getUserInfo(String id) {
        UserMyPage user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return MyPageResponseDTO.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .birth(user.getBirth())
                .password(user.getPassword())
                .profileImg(user.getProfileImg())
                .build();
    }

    // 사용자 정보 수정
    @Transactional
    public MyPageResponseDTO updateUserInfo(String id, MyPageUpdateDTO updateDTO) {
        UserMyPage user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. "));

        // 이메일 중복 체크
        if (!user.getEmail().equals(updateDTO.getEmail()) &&
                userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // ID 중복 체크
        if (!user.getId().equals(updateDTO.getId()) &&
                userRepository.existsById(updateDTO.getId())) {
            throw new RuntimeException("이미 사용 중인 ID입니다.");
        }

        // 정보 업데이트
        user.setEmail(updateDTO.getEmail());
        user.setId(updateDTO.getId());
        user.setName(updateDTO.getName());
        user.setBirth(updateDTO.getBirth());

        // 비밀번호가 제공된 경우에만 업데이트
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        // 프로필 이미지가 제공된 경우에만 업데이트
        if (updateDTO.getProfileImg() != null) {
            user.setProfileImg(updateDTO.getProfileImg());
        }

        userRepository.save(user);

        return MyPageResponseDTO.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .birth(user.getBirth())
                .password(user.getPassword())
                .profileImg(user.getProfileImg())
                .build();
    }
}