package com.saltyhana.saltyhanaserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.entity.Progress;
import com.saltyhana.saltyhanaserver.entity.User;
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

    @JsonIgnore
    private Icon icon;
    private Long connected_account;
    private Long amount;
    private Long percentage;
}
