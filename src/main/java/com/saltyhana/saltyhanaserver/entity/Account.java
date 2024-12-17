package com.saltyhana.saltyhanaserver.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private Integer fintechUseNum;

    @Column(length = 50)
    private String accountAlias;

    private Integer bankCodeStd;

    private Integer bankCodeSub;

    @Column(length = 20)
    private String bankName;

    private Long accountNum;

    @Column(length = 1)
    @Comment("1: 수시입출금, 2: 예적금")
    private String accountType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean main;
}
