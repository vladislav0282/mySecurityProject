package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
//    boolean existsByName(String name);
}
