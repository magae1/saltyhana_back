package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.GoalResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalRequestDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.mapper.GoalMapper;
import com.saltyhana.saltyhanaserver.repository.GoalRepository;
import com.saltyhana.saltyhanaserver.repository.IconRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static com.saltyhana.saltyhanaserver.util.GoalUtil.calculatePercentage;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final S3FileUploadService s3FileUploadService;
    private final FileService fileService;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final IconRepository iconRepository;

    @Transactional
    public SetGoalResponseDTO createOrUpdateGoal(SetGoalRequestDTO goalDTO, Long goalId) {
        // 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 조회
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("사용자");
        });

        // 아이콘과 이미지 유효성 검사
        if (goalDTO.getIconId() != null && goalDTO.getGoalImg() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이콘과 이미지는 동시에 설정할 수 없습니다.");
        }

        // 아이콘 조회 (있는 경우)
        Icon icon = null;
        if (goalDTO.getIconId() != null) {
            icon = iconRepository.findById(goalDTO.getIconId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "아이콘을 찾을 수 없습니다."));
        }

        // 이미지 파일 저장
        String localFilePath = null; // 로컬 파일 경로

        if(goalDTO.getGoalImg() != null) {
            localFilePath = fileService.saveBase64File(goalDTO.getGoalImg());
            // S3 파일 업로드
            String contentType = "multipart/form-data"; // 이미지 타입 설정 (필요시 변경)
            File file = new File(localFilePath);
            Path filePath = file.toPath();
            String uploadedImageUrl = s3FileUploadService.uploadFileOnS3(filePath, contentType, file.length())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패"));
            goalDTO.setGoalImg(uploadedImageUrl);
        }

        Goal goal;
        // 목표 생성 시
        if (goalId == null) {
            // 새로운 목표 생성
            goal = Goal.builder()
                    .user(user)
                    .name(goalDTO.getGoalName())
                    .category(String.valueOf(goalDTO.getGoalType()))
                    .amount(Long.valueOf(goalDTO.getGoalMoney()))
                    .startAt(goalDTO.getStartDate().atStartOfDay())
                    .endAt(goalDTO.getEndDate().atStartOfDay())
                    .isEnded(false)
                    .icon(icon)
                    .customImage(goalDTO.getGoalImg())
                    .build();
        } else {
            // 목표 업데이
            goal = GoalMapper.toEntity(user, goalDTO, icon, goalId);
        }

        Goal updatedGoal = goalRepository.save(goal);

        if (localFilePath != null) {
            fileService.deleteFile(localFilePath);
        }

        // 응답 DTO 변환 및 반환
        return GoalMapper.toSetGoalResponse(updatedGoal);
    }

    @Transactional(readOnly = true)
    public List<GoalResponseDTO> getGoals(boolean activeOnly) {
        // 1. 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());

        // 2. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 3. 목표 조회
        List<Goal> goals = activeOnly ?
                goalRepository.findByUserAndIsEndedFalse(user) :
                goalRepository.findByUser(user);

        // 4. DTO 변환 및 반환
        return goals.stream()
                .map(goal -> GoalResponseDTO.builder()
                        .id(goal.getId())
                        .userName(user.getName())
                        .title(goal.getName())
                        .startAt(goal.getStartAt())
                        .endAt(goal.getEndAt())
                        .icon(goal.getIcon())
                        .connected_account(goal.getAccount() != null ? goal.getAccount().getId() : null)
                        .amount(goal.getAmount())
                        .percentage(calculatePercentage(goal))
                        .build())
                .collect(Collectors.toList());
    }
}