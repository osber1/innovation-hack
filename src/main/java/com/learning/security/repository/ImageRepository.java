package com.learning.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.security.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    boolean existsByPath(String path);
}