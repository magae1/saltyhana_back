package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.*;
import com.saltyhana.saltyhanaserver.entity.*;
import com.saltyhana.saltyhanaserver.enums.ProductEnum;
import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
import com.saltyhana.saltyhanaserver.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<DashBoardResponseDTO> getGoalsAndWeekdays(DashBoardRequestDTO dashBoardRequestDTO) {
        User user = userRepository.findById(dashBoardRequestDTO.getUserId()).orElse(null);

        List<GoalResponseDTO> goals = getGoals(user);

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

    private List<GoalResponseDTO> getGoals(User user) {
        return goalRepository.findAllByUser(user).stream()
                .map(goal -> {
                    Icon icon = goal.getIcon();
                    Hibernate.initialize(icon);

                    Long dailyAmount = progressRepository.findLatestAfterAmountByGoalId(goal.getId());
                    Long percentage = calculatePercentage(dailyAmount, goal.getAmount());

                    return GoalResponseDTO.builder()
                            .id(goal.getId())
                            .title(goal.getName())
                            .userName(user.getName())
                            .startAt(goal.getStartAt())
                            .endAt(goal.getEndAt())
                            .icon(icon)
                            .amount(goal.getAmount())
                            .connected_account(goal.getAccount().getId())
                            .percentage(percentage)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private WeekdayCalendarResponseDTO getWeekdayCalendar(GoalResponseDTO goalDTO){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime elevenDaysAgo = now.minusDays(11);
        Integer dailyAmount = calculateDailyAmount(goalDTO.getStartAt(),goalDTO.getEndAt(),goalDTO.getAmount());
        List<Transfer> transfers = transferRepository.findTransfersByAccountAndDateRange(elevenDaysAgo, now, goalDTO.getConnected_account());
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

        return bestProductList.stream()
                .map(product -> {
                    Rate rate = rateRepository.findByProductId(product.getId()); // Product ID로 Rate 조회

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
