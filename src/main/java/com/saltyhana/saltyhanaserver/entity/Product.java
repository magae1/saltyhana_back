package com.saltyhana.saltyhanaserver.entity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dclsMonth;
    private Integer finCoNo;
    private Integer finPrdtCd;
    private String korCoNm;
    private String finPrdtNm;
    private String mtrtInt;
    private String spclCnd;
    private String joinDeny;
    private String joinMember;
    private String etcNote;
    private String maxLimit;
    private LocalDateTime dclsStrtDay;
    private LocalDateTime dclsEndDay;
    private LocalDateTime finCoSubmDay;
    private String description; // 상품 설명
    private String linkPrd; // 하나은행 상품 링크


    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Icon icon;

    @ManyToMany(mappedBy = "recommendedProducts")
    private Set<User> recommended;
}
