package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import com.saltyhana.saltyhanaserver.dto.GoalResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalRequestDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalResponseDTO;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final IconRepository iconRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public SetGoalResponseDTO createGoal(SetGoalRequestDTO goalDTO) {
        // 1. 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 2. 사용자 조회
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 3. 아이콘과 이미지 유효성 검사
        if (goalDTO.getIconId() != null && goalDTO.getGoalImg() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이콘과 이미지는 동시에 설정할 수 없습니다.");
        }

        // 4. 아이콘 조회 (있는 경우)
        Icon icon = null;
        if (goalDTO.getIconId() != null) {
            icon = iconRepository.findById(goalDTO.getIconId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "아이콘을 찾을 수 없습니다."));
        }

        // 5. 계좌 조회 및 검증
        Account account = null;
        if (goalDTO.getConnectedAccount() != null) {
            // 먼저 사용자의 계좌 목록을 조회
            List<AccountDTO> userAccounts = accountRepository.findByUserId(userId);

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

        // 6. Goal 엔티티 생성
        Goal goal = Goal.builder()
                .user(user)
                .name(goalDTO.getGoalName())
                .category(String.valueOf(goalDTO.getGoalType()))
                .amount(Long.valueOf(goalDTO.getGoalMoney()))
                .startAt(goalDTO.getStartDate().atStartOfDay())
                .endAt(goalDTO.getEndDate().atStartOfDay())
                .isEnded(false)
                .icon(icon)
                .customImage(goalDTO.getGoalImg())
                .account(account)  // 계좌 정보 설정
                .build();

        // 7. Goal 저장
        Goal savedGoal = goalRepository.save(goal);

        // 8. 응답 DTO 반환
        return SetGoalResponseDTO.builder()
                .goalName(savedGoal.getName())
                .goalMoney(savedGoal.getAmount().intValue())
                .startDate(savedGoal.getStartAt().toLocalDate())
                .endDate(savedGoal.getEndAt().toLocalDate())
                .goalType(Integer.parseInt(savedGoal.getCategory()))
                .iconId(savedGoal.getIcon() != null ? savedGoal.getIcon().getId() : null)
                .goalImg(savedGoal.getCustomImage())
                .build();
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

    private Long calculatePercentage(Goal goal) {
        LocalDateTime now = LocalDateTime.now();

        // 시작 전인 경우
        if (now.isBefore(goal.getStartAt())) {
            return 0L;
        }

        // 이미 종료된 경우
        if (now.isAfter(goal.getEndAt()) || goal.isEnded()) {
            return 100L;
        }

        // 총 기간(일수) 계산
        long totalDays = ChronoUnit.DAYS.between(goal.getStartAt(), goal.getEndAt());
        if (totalDays == 0) { // 시작일과 종료일이 같은 경우
            return 100L;
        }

        // 경과 기간(일수) 계산
        long elapsedDays = ChronoUnit.DAYS.between(goal.getStartAt(), now);

        // 진행률 계산 (소수점 반올림)
        return Math.round((double) elapsedDays / totalDays * 100);
    }

    @Transactional
    public SetGoalResponseDTO updateGoal(Long goalId, SetGoalRequestDTO goalDTO) {
        // 1. 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("유저");
        });

        // 2. 목표 조회
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "목표를 찾을 수 없습니다."));

        // 3. 목표의 소유자 확인
        if (!goal.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 목표에 대한 권한이 없습니다.");
        }

        // 4. 아이콘과 이미지 유효성 검사
        if (goalDTO.getIconId() != null && goalDTO.getGoalImg() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이콘과 이미지는 동시에 설정할 수 없습니다.");
        }

        // 5. 아이콘 조회 (있는 경우)
        Icon icon = null;
        if (goalDTO.getIconId() != null) {
            icon = iconRepository.findById(goalDTO.getIconId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "아이콘을 찾을 수 없습니다."));
        }

        // 6. 목표 업데이트
        goal = GoalMapper.toEntity(user, goalDTO, icon, goalId);
        Goal updatedGoal = goalRepository.save(goal);

        // 7. 응답 DTO 변환 및 반환
        return GoalMapper.toSetGoalResponse(updatedGoal);
    }

    @Transactional(readOnly = true)
    public List<Goal> getGoalsByUser(User user) {
        return goalRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Goal> getActiveGoalsByUser(User user) {
        return goalRepository.findByUserAndIsEndedFalse(user);
    }
}