package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Progress;
import com.saltyhana.saltyhanaserver.entity.Transfer;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.repository.ProgressRepository;
import com.saltyhana.saltyhanaserver.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.saltyhana.saltyhanaserver.util.DashboardUtil.calculateDailyAmount;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final TransferRepository transferRepository;

    public void initializeProgress(Goal goal){
        Progress progress = Progress.builder()
                .goal(goal)
                .addedAmount(0L)
                .afterAmount(0L)
                .addedAt(LocalDateTime.now())
                .build();

        progressRepository.save(progress);
    }

    @Transactional
    public void insertProgress(Goal goal){
        // 하루에 이체되어야 하는 금액
        LocalDateTime startAt = goal.getStartAt();
        LocalDateTime endAt = goal.getEndAt();
        Integer dailyAmount = calculateDailyAmount(startAt, endAt, goal.getAmount());

        Progress progress = progressRepository.findRecentProgressByGoalId(goal.getId())
                .orElseThrow(() -> {
                    throw new NotFoundException("progress");
                });

        System.out.println("가져온 progress 데이터:"+progress.getId());

        // 기존 Progress의 addedAt 이후의 거래만 가져오기
        Long accumulatedAmount = progress.getAfterAmount();
        LocalDateTime latestAddedAt = progress.getAddedAt();

        List<Transfer> newTransfers = transferRepository.findTransfersByGoalAndDate(
                latestAddedAt.plus(Duration.ofMillis(500)),
                endAt,
                goal.getName(),
                dailyAmount
        );

        if (!newTransfers.isEmpty()) {
            for (Transfer transfer : newTransfers) {
                System.out.println("가져온 transfer 데이터:"+transfer.getId());
                accumulatedAmount += transfer.getTranAmt();
                latestAddedAt = transfer.getTranTime();

                Progress updatedProgress = Progress.builder()
                        .goal(goal)
                        .addedAmount(Long.valueOf(dailyAmount))
                        .afterAmount(accumulatedAmount)
                        .addedAt(latestAddedAt)
                        .build();
                progressRepository.save(updatedProgress);
                progressRepository.flush();
                System.out.println("저장된 데이터:"+updatedProgress.getId()+" "+updatedProgress.getAfterAmount());
            }
        }
    }
}
