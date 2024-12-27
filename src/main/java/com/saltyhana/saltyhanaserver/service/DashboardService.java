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
import java.util.*;
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
    private final GoalAchievementRepository goalAchievementRepository;

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
        Boolean isEnded = false;
        Icon icon = goal.getIcon();

        if (icon != null) {
            iconImage = icon.getIconImage();
        }

        // Progress 처리
        initializeProgress(goal);

        Long dailyAmount = progressRepository.findByGoalId(goal.getId()).get().getAfterAmount();
        Long percentage = calculatePercentage(dailyAmount, goal.getAmount());
        String goalPeriod = formatGoalPeriod(goal.getStartAt(), goal.getEndAt());

        if(dailyAmount >= goal.getAmount()) {
            isEnded = true;
            goalRepository.updateIsEnded(goal.getId(), true);
        }

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
                .isEnded(isEnded)
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
                    progressRepository.save(initialProgress); // 저장
                    return initialProgress; // 저장한 객체 반환
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
                            .productLink(product.getLinkPrd())
                            .build();
                })
                .collect(Collectors.toList());
    }


    public List<GoalAchievementDTO> getUncheckedAchievements() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getPrincipal().toString());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자"));

        List<GoalSummaryResponseDTO> goals = getGoals(user);

        return goals.stream()
                .filter(goal -> {
                    GoalAchievement achievement = goalAchievementRepository.findById(goal.getId()).orElse(null);
                    return achievement == null || !achievement.isChecked();   // Redis에 없거나 `checked`가 false인 목표만
                })
                .map(goal -> {
                    Optional<GoalAchievement> achievement = goalAchievementRepository.findById(goal.getId());
                    if (!achievement.isPresent()) {
                        Long dailyAmount = progressRepository.findByGoalId(goal.getId()).get().getAfterAmount();
                        Long percentage = calculatePercentage(dailyAmount, goal.getTotalMoney());
                        boolean achieved = percentage == 100L;

                        goalAchievementRepository.save(
                                GoalAchievement.builder()
                                        .id(goal.getId())
                                        .achieved(achieved)
                                        .checked(false) // 초기값
                                        .build()
                        );
                        return new GoalAchievement(goal.getId(), achieved, false); // 새로 생성된 목표 리턴
                    }
                    return achievement.get();
                })
                .filter(Objects::nonNull) // null 값 제외
                .map(this::toDTO)         // GoalAchievement를 GoalAchievementDTO로 변환
                .collect(Collectors.toList());
    }

    public void confirmAchievements(List<Long> goalIds) {
        goalIds.forEach(goalId -> {
            // Redis에서 목표 조회
            Optional<GoalAchievement> achievementOpt = goalAchievementRepository.findById(goalId);

            // 목표가 Redis에 존재하면 확인 상태 업데이트
            if (achievementOpt.isPresent()) {
                GoalAchievement achievement = achievementOpt.get();
                if (!achievement.isChecked()) {
                    achievement.setChecked(true); // 확인 상태를 true로 업데이트
                    goalAchievementRepository.save(achievement); // 업데이트된 목표 저장
                }
            }
        });
    }

    private GoalAchievementDTO toDTO(GoalAchievement achievement) {
        return GoalAchievementDTO.builder()
                .id(achievement.getId())
                .achieved(achievement.isAchieved())
                .build();
    }
}
