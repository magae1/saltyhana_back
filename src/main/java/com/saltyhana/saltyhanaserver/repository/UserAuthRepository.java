package com.saltyhana.saltyhanaserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saltyhana.saltyhanaserver.entity.User;


public interface UserAuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentifier(String identifier);
}
