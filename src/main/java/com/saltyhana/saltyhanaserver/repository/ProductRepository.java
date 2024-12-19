package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Product;
import com.saltyhana.saltyhanaserver.entity.Rate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p")
    List<Product> findBestProductList(Pageable pageable);

    // ID 범위로 상품 조회 (1~6번 상품)
    List<Product> findAllByIdBetween(Long startId, Long endId);

    // 상품명 리스트에 따라 상품 조회
    List<Product> findAllByFinPrdtNmIn(List<String> finPrdtNmList);


    // 이율 조회
    @Query("SELECT r FROM Rate r WHERE r.product.id IN :productIds")
    List<Rate> findRatesByProductIds(@Param("productIds") List<Long> productIds);

}
