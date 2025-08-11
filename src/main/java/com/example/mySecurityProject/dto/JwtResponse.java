package com.example.mySecurityProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data//Что бы автоматически создавались геттеры и сеттеры
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
}
