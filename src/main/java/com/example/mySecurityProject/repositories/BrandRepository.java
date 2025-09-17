package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.shop.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
