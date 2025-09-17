package com.example.mySecurityProject.services;

import com.example.mySecurityProject.domain.user.Role;
import com.example.mySecurityProject.domain.user.User;
import com.example.mySecurityProject.dto.*;
import com.example.mySecurityProject.exeptions.AppError;
import com.example.mySecurityProject.repositories.RoleRepository;
import com.example.mySecurityProject.repositories.UserRepository;
import com.example.mySecurityProject.util.JwtTokenUtils;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager ;//что бы автоматически проверялось пришедший логин и пароль существует

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                    authRequest.getPassword()));
        } catch (BadCredentialsException e){
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Некорректный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, refreshToken));
    }

    public ResponseEntity<?> refreshTokens(String refreshToken) {
        if (!jwtTokenUtils.validateRefreshToken(refreshToken)) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token"), HttpStatus.UNAUTHORIZED);
        }
        String username = jwtTokenUtils.getUsernameFromRefreshToken(refreshToken);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String newAccessToken = jwtTokenUtils.generateToken(userDetails);
        String newRefreshToken = jwtTokenUtils.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        // Проверяем, есть ли роль ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });
//        Role userRole = roleRepository.findByName("ROLE_ADMIN")
//                .orElseGet(() -> {
//                    Role newRole = new Role();
//                    newRole.setName("ROLE_ADMIN");
//                    return roleRepository.save(newRole);
//                });
        if(!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getName()));
    }

    public ResponseEntity<?> checkToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Токен невалиден или отсутствует"), HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Пользователь не найден"), HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getName()));
    }

    public ResponseEntity<?> checkRole()  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Токен невалиден или отсутствует"), HttpStatus.UNAUTHORIZED);
        }
        // Получаем роли пользователя
        var authorities = authentication.getAuthorities();
        var roles = authorities.stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();
        return ResponseEntity.ok(roles);
    }
}
