package com.saltyhana.saltyhanaserver.entity;

import com.saltyhana.saltyhanaserver.listener.GoalEntityListener;
import com.saltyhana.saltyhanaserver.listener.TransferEntityListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@EntityListeners(TransferEntityListener.class)
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;

    @Column(columnDefinition = "TIMESTAMP")  // TIMESTAMP로 명시
    private LocalDateTime tranTime;

    @Column(length = 1)
    private Integer inOutType;

    @Column(length = 20)
    private String printedContent;

    private Integer tranAmt;
    private Integer afterBalanceAmt;

    @Column(length = 20)
    private String branchName;



}
