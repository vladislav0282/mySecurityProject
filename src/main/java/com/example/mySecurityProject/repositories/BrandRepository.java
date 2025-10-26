package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.shop.Brand;
import com.example.mySecurityProject.domain.shop.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandName(String brandName);
}
