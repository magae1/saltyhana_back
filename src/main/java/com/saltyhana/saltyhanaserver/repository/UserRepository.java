package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.entity.UserMyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserMyPage, Long> {
    Optional<UserMyPage> findById(String id);  // PK로 찾기
    Optional<UserMyPage> findByEmail(String email);
    Optional<UserMyPage> findByIdEquals(String id);  // id 필드로 찾기
    boolean existsByEmail(String email);
    boolean existsById(String id);
}