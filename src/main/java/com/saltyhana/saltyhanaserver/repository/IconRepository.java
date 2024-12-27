package com.saltyhana.saltyhanaserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saltyhana.saltyhanaserver.entity.Icon;


@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {
    Optional<Icon> findByName(String name);

    List<Icon> findByNameStartsWith(String name);
}