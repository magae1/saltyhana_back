package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.component.RedisMessageQueue;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.dto.GoalMessageDTO;
import com.saltyhana.saltyhanaserver.dto.GoalResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalDTO;
import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.mapper.GoalMapper;
import com.saltyhana.saltyhanaserver.repository.AccountRepository;
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

import static com.saltyhana.saltyhanaserver.mapper.GoalMapper.toGoalResponseDTO;


@Service
@RequiredArgsConstructor
public class GoalService {
    private final S3FileUploadService s3FileUploadService;
    private final FileService fileService;
    private final RedisMessageQueue<GoalMessageDTO> goalMessageQueue;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final IconRepository iconRepository;
    private final AccountRepository accountRepository;


    @Transactional
    public SetGoalDTO createOrUpdateGoal(SetGoalDTO goalDTO, Long goalId) {
        // 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 조회
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("사용자");
        });

        // 계좌 조회 및 검증
        Account account = null;
        if (goalDTO.getConnectedAccount() != null) {
            // 먼저 사용자의 계좌 목록을 조회
            List<AccountDTO> userAccounts = accountRepository.findAllByUserId(userId);

            // 선택된 계좌가 사용자의 계좌 목록에 있는지 확인
            boolean isValidAccount = userAccounts.stream()
                    .anyMatch(acc -> acc.getId().equals(goalDTO.getConnectedAccount()));

            if (!isValidAccount) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 계좌에 대한 권한이 없습니다.");
            }

            // 권한이 확인되면 실제 Account 엔티티 조회
            account = accountRepository.findById(goalDTO.getConnectedAccount())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "계좌를 찾을 수 없습니다."));
        }

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
                    .account(account)
                    .name(goalDTO.getGoalName())
                    .category(String.valueOf(goalDTO.getGoalType()))
                    .amount(Long.valueOf(goalDTO.getGoalMoney()))
                    .startAt(goalDTO.getStartDate().atStartOfDay())
                    .endAt(goalDTO.getEndDate().atStartOfDay())
                    .icon(icon)
                    .customImage(goalDTO.getGoalImg())
                    .build();
        } else {
            // 목표 업데이트
            goal = GoalMapper.toEntity(user, account, goalDTO, icon, goalId);
        }

        Goal updatedGoal = goalRepository.save(goal);

        if (localFilePath != null) {
            fileService.deleteFile(localFilePath);
        }

        // 응답 DTO 변환 및 반환
        return GoalMapper.toSetGoalDTO(updatedGoal);
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
                .map(goal -> toGoalResponseDTO(goal, user))
                .collect(Collectors.toList());
    }

    public List<GoalMessageDTO> getGoalMessages(Long userId) {
        String keyName = Long.toString(userId);
        return goalMessageQueue.popAllMessages(keyName);
    }
}