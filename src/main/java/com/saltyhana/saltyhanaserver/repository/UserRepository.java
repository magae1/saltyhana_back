package com.saltyhana.saltyhanaserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.saltyhana.saltyhanaserver.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByIdentifier(String identifier);
    @Query("SELECT u FROM uuser u WHERE u.identifier = :identifier AND u.active = true")
    Optional<User> findByIdentifier(String identifier);
    Optional<User> findByEmail(String email);

    @Query("SELECT u "
        + "FROM uuser u "
        + "WHERE u.active=false AND u.identifier=:identifier AND u.email=:email")
    Optional<User> findInactiveUserByIdentifierAndEmail(String identifier, String email);
}