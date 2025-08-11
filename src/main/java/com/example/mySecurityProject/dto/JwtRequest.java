package com.example.mySecurityProject.dto;

import lombok.Data;

@Data//Что бы автоматически создавались геттеры и сеттеры
public class JwtRequest {
    private String username;
    private  String password;
}
