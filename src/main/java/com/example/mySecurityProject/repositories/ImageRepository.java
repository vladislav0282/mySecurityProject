package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.shop.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
