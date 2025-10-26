package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.shop.Type;
import com.example.mySecurityProject.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByTypeName(String typeName);
}
