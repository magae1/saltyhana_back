package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByIdentifier(String identifier);
    Optional<User> findByIdentifier(String identifier);
}