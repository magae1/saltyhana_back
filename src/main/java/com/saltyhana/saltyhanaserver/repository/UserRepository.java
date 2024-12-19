package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByIdentifier(String identifier);
    Optional<User> findByIdentifier(String identifier);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM uuser u LEFT JOIN FETCH u.consumptionTendency WHERE u.id = :userId")
    Optional<User> findByIdWithConsumptionTendency(@Param("userId") Long userId);
}