package com.saltyhana.saltyhanaserver.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.saltyhana.saltyhanaserver.enums.ConsumptionTypeEnum;
import com.saltyhana.saltyhanaserver.enums.ConsumptionMBTIEnum;


@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsumptionTendency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ConsumptionTypeEnum type;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ConsumptionMBTIEnum mbti;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private String emoji;

    @Column(nullable = false)
    private String description;
}
