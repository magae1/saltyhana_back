package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.*;
import com.saltyhana.saltyhanaserver.entity.*;
import com.saltyhana.saltyhanaserver.enums.ProductEnum;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
import com.saltyhana.saltyhanaserver.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

        // 목표가 없을 경우 예외 처리
        if (goals == null || goals.isEmpty()) {
            List<DashBoardResponseDTO> dashboardList = new ArrayList<>();
            List<BestProductListResponseDTO> bestProductList = getBestProductList();
            dashboardList.add(DashBoardResponseDTO.builder()
                    .goal(null)
                    .weekdayCalendar(null)
                    .bestProductList(bestProductList)
                    .build());

            return dashboardList;
        }


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

    @Transactional
    protected List<GoalSummaryResponseDTO> getGoals(User user) {
        // Fetch Join
        List<Goal> goals = goalRepository.findAllByUserWithIcons(user);

        // 목표가 없을 경우 예외 처리
        if(goals == null || goals.isEmpty()) {
            return Collections.emptyList();
        }

        return goals.stream()
                .map(goal -> GoalToGoalSummaryResponseDTO(goal, user))
                .collect(Collectors.toList());
    }

    @Transactional
    protected GoalSummaryResponseDTO GoalToGoalSummaryResponseDTO(Goal goal, User user) {

        String iconImage = null;
        Icon icon = goal.getIcon();

        if (icon != null) {
            iconImage = icon.getIconImage();
        }

        // Progress 처리
        initializeProgress(goal);
        Long dailyAmount = progressRepository.findByGoalId(goal.getId()).get().getAfterAmount();
        Long percentage = calculatePercentage(dailyAmount, goal.getAmount());
        String goalPeriod = formatGoalPeriod(goal.getStartAt(), goal.getEndAt());

        return GoalSummaryResponseDTO.builder()
                .id(goal.getId())
                .title(goal.getName())
                .userName(user.getName())
                .goalPeriod(goalPeriod)
                .iconImage(iconImage)
                .customImage(goal.getCustomImage())
                .currentMoney(dailyAmount)
                .totalMoney(goal.getAmount())
                .percentage(percentage)
                .build();
    }


    private void initializeProgress(Goal goal) {

        LocalDateTime startAt = goal.getStartAt();
        LocalDateTime endAt = goal.getEndAt();
        // 하루에 이체되어야 하는 금액
        Integer dailyAmount = calculateDailyAmount(startAt, endAt, goal.getAmount());

        Progress progress = progressRepository.findByGoalId(goal.getId())
                .orElseGet(() -> {
                    Progress initialProgress = Progress.builder()
                            .goal(goal)
                            .addedAmount(0L)
                            .afterAmount(0L)
                            .addedAt(goal.getStartAt())
                            .build();
                    return progressRepository.save(initialProgress);
                });

        Long accumulatedAmount = progress.getAfterAmount();
        LocalDateTime latestAddedAt = progress.getAddedAt();

        // 기존 Progress의 addedAt 이후의 거래만 가져오기
        List<Transfer> newTransfers = transferRepository.findTransfersByGoalAndDate(
                latestAddedAt.plusSeconds(1),
                endAt,
                goal.getAccount().getId(),
                dailyAmount
        );

        if (!newTransfers.isEmpty()) {
            for (Transfer transfer : newTransfers) {
                accumulatedAmount += transfer.getTranAmt();
                latestAddedAt = transfer.getTranTime();
            }

            progress.setAddedAmount(Long.valueOf(dailyAmount));
            progress.setAfterAmount(accumulatedAmount);
            progress.setAddedAt(latestAddedAt);

            progressRepository.save(progress);
        }
    }


    private WeekdayCalendarResponseDTO getWeekdayCalendar(GoalSummaryResponseDTO goalDTO){

        Goal goal = goalRepository.findById(goalDTO.getId()).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime elevenDaysAgo = now.minusDays(11);

        //하루에 이체되어야 하는 금액
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
                            .subtitle(product.getSpclCnd())
                            .imageUrl("https://example.com/image/" + product.getId())
                            .description(RecommendationMapper.formatDescription(rate))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
