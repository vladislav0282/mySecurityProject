package com.example.mySecurityProject.dto;


import lombok.Data;

@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String name;

}
