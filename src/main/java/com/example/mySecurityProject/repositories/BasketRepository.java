package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.shop.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByUserId(Long userId);
    List<Basket> findAllByUserId(Long userId);
}
