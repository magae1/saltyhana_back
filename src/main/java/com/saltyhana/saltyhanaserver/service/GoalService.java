package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.SetGoalRequestDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.entity.User;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final IconRepository iconRepository;

    @Transactional
    public SetGoalResponseDTO createGoal(SetGoalRequestDTO goalDTO) {
        // 1. 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal().toString())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

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
            System.out.println("hello");
            icon = iconRepository.findById(goalDTO.getIconId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "아이콘을 찾을 수 없습니다."));
        }
        System.out.println(icon);

        // 5. Goal 엔티티 생성
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
                .build();

        // 6. Goal 저장
        Goal savedGoal = goalRepository.save(goal);

        // 7. 응답 DTO 변환 및 반환
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
    public List<Goal> getGoalsByUser(User user) {
        return goalRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Goal> getActiveGoalsByUser(User user) {
        return goalRepository.findByUserAndIsEndedFalse(user);
    }
}