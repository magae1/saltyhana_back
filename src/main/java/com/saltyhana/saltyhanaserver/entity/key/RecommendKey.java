package com.saltyhana.saltyhanaserver.entity.key;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.saltyhana.saltyhanaserver.entity.Product;
import com.saltyhana.saltyhanaserver.entity.User;


@Embeddable
@EntityListeners(AuditingEntityListener.class)
@Getter
public class RecommendKey implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}