package com.saltyhana.saltyhanaserver.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
public class GoalAchievement {
    @Id
    private Long id;

    @Indexed
    private boolean achieved;

    @Indexed
    private boolean checked;
}
