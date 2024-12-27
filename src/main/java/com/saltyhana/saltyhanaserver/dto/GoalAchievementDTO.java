package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalAchievementDTO {
    private Long id;
    private boolean achieved;
}
