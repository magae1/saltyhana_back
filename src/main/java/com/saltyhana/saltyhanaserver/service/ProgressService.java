package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Progress;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.repository.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;

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
    public void insertProgress(Goal goal, Integer dailyAmount, LocalDateTime tranTime) {

        //가장 최근에 추가된 progress
        Progress progress = progressRepository.findRecentProgressByGoalId(goal.getId())
                .orElseThrow(() -> {
                    throw new NotFoundException("progress");
                });

        Long accumulatedAmount = progress.getAfterAmount()+ dailyAmount;
        LocalDateTime latestAddedAt = tranTime;

        Progress updatedProgress = Progress.builder()
                .goal(goal)
                .addedAmount(Long.valueOf(dailyAmount))
                .afterAmount(accumulatedAmount)
                .addedAt(latestAddedAt)
                .build();

        progressRepository.save(updatedProgress);
        progressRepository.flush();
    }
}
