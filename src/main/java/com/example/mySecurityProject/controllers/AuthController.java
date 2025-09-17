package com.example.mySecurityProject.controllers;

import com.example.mySecurityProject.dto.JwtRequest;
import com.example.mySecurityProject.dto.RefreshTokenRequest;
import com.example.mySecurityProject.dto.RegistrationUserDto;
import com.example.mySecurityProject.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshTokens(request.getRefreshToken());
    }

    @GetMapping("/checkauth")
    public ResponseEntity<?> checkAuthToken() {
        return authService.checkToken();
    }

    @GetMapping("/checkrole")
    public ResponseEntity<?> checkAuthRole() {
        return authService.checkRole();
    }
}
