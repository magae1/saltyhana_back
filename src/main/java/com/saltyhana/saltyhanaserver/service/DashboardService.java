package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.*;
import com.saltyhana.saltyhanaserver.entity.*;
import com.saltyhana.saltyhanaserver.enums.ProductEnum;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
import com.saltyhana.saltyhanaserver.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.saltyhana.saltyhanaserver.util.DashboardUtil.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardService {
    private final GoalRepository goalRepository;
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;
    private final ProductRepository productRepository;
    private final RateRepository rateRepository;

    public List<DashBoardResponseDTO> getGoalsAndWeekdays() {
        // 인증 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 조회
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("사용자");
        });

        List<GoalSummaryResponseDTO> goals = getGoals(user);

        List<DashBoardResponseDTO> dashboardList = goals.stream()
                .map(goal -> {
                    WeekdayCalendarResponseDTO weekdayCalendar = getWeekdayCalendar(goal);
                    List<BestProductListResponseDTO> bestProductList = getBestProductList();
                    return DashBoardResponseDTO.builder()
                            .goal(goal)
                            .weekdayCalendar(weekdayCalendar)
                            .bestProductList(bestProductList)
                            .build();
                })
                .collect(Collectors.toList());

        return dashboardList;

    }

    private List<GoalSummaryResponseDTO> getGoals(User user) {
        List<Goal> goals = goalRepository.findAllByUser(user);

        // 목표가 없을 경우 예외 처리
        if(goals == null || goals.isEmpty()) {
            throw new NotFoundException("설정한 목표");
        }

        return goals.stream()
                .map(goal -> {
                    String iconImage = null;
                    Icon icon = goal.getIcon();
                    if (icon != null) {
                        try {
                            Hibernate.initialize(icon);
                            iconImage = icon.getIconImage();
                        } catch (Exception e) {
                            throw new RuntimeException("Icon 초기화 중 오류 발생: " + goal.getId(), e);
                        }
                    }

                    // Progress
                    Long dailyAmount = 0L;
                    try {
                        Long latestAmount = progressRepository.findLatestAfterAmountByGoalId(goal.getId());
                        if (latestAmount == null) {
                            Progress progress = Progress.builder()
                                    .goal(goal)
                                    .addedAmount(0L)
                                    .afterAmount(0L)
                                    .addedAt(LocalDateTime.now())
                                    .build();
                            progressRepository.save(progress);
                        } else {
                            dailyAmount = latestAmount;
                        }
                    } catch (Exception e) {
                        System.err.println("Progress 처리 중 에러 발생: " + e.getMessage());
                    }

                    //계산 및 DTO 생성
                    Long percentage = calculatePercentage(dailyAmount, goal.getAmount());
                    String goalPeriod = formatGoalPeriod(goal.getStartAt(), goal.getEndAt());

                    return GoalSummaryResponseDTO.builder()
                            .id(goal.getId())
                            .title(goal.getName())
                            .userName(user.getName())
                            .goalPeriod(goalPeriod)
                            .iconImage(iconImage)
                            .currentMoney(dailyAmount)
                            .totalMoney(goal.getAmount())
                            .percentage(percentage)
                            .build();
                })
                .collect(Collectors.toList());
    }


    private WeekdayCalendarResponseDTO getWeekdayCalendar(GoalSummaryResponseDTO goalDTO){

        // 목표가 없을 경우 예외 처리
        Goal goal = goalRepository.findById(goalDTO.getId())
                .orElseThrow(() -> new NotFoundException("목표 및 달력"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime elevenDaysAgo = now.minusDays(11);

        Integer dailyAmount = calculateDailyAmount(goal.getStartAt(),goal.getEndAt(),goalDTO.getTotalMoney());
        List<Transfer> transfers = transferRepository.findTransfersByAccountAndDateRange(elevenDaysAgo, now, goal.getAccount().getId());
        List <WeekDayType> weekDayTypes = new ArrayList<>();

        for(LocalDateTime date = elevenDaysAgo.plusDays(1); !date.isAfter(now); date = date.plusDays(1)) {
            boolean isAchieved = checkIfAchieved(transfers, dailyAmount, date);
            weekDayTypes.add(WeekDayType.builder()
                    .date(date.toLocalDate())
                    .isAchieve(isAchieved)
                    .build());
        }

        return WeekdayCalendarResponseDTO.builder()
                .weekday(weekDayTypes)
                .build();
    }

    private List<BestProductListResponseDTO> getBestProductList() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> bestProductList = productRepository.findBestProductList(pageable);

        // 추천 상품이 없을 경우 예외 처리
        if (bestProductList == null || bestProductList.isEmpty()) {
            throw new NotFoundException("추천상품");
        }

        return bestProductList.stream()
                .map(product -> {
                    Rate rate = rateRepository.findByProductId(product.getId());

                    return BestProductListResponseDTO.builder()
                            .id(product.getId())
                            .type(ProductEnum.ASSET)
                            .title(product.getFinPrdtNm())
                            .subtitle(product.getJoinMember())
                            .imageUrl("https://example.com/image/" + product.getId())
                            .description(RecommendationMapper.formatDescription(rate))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
