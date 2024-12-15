package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Recommended;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedRepository extends JpaRepository<Recommended, Long> {
    List<Recommended> findAllByUserId(Long userId);

    List<Recommended> findById_User_Id(Long idUserId);
}
