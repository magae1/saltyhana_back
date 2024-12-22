package com.saltyhana.saltyhanaserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saltyhana.saltyhanaserver.entity.Icon;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GoalResponseDTO {
    private Long id;
    private String userName;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String iconImage;
    private String iconColor;
    private String customImage;
    private String category;

    @JsonIgnore
    private Icon icon;
    private Long connected_account;
    private Long currentMoney;
    private Long totalMoney;
    private Long percentage;
}
