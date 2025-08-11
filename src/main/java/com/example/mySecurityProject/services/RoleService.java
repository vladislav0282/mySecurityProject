package com.example.mySecurityProject.services;

import com.example.mySecurityProject.domain.user.Role;
import com.example.mySecurityProject.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
