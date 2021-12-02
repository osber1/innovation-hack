package com.learning.security.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.security.model.AuthoritiesEntity;

@Repository
public interface AuthoritiesRepository extends JpaRepository<AuthoritiesEntity, Integer> {

    Set<AuthoritiesEntity> findByRole(String role);
}