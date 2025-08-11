package com.example.mySecurityProject.exeptions;

import lombok.Data;

import java.util.Date;

@Data//Что бы автоматически создавались геттеры и сеттеры
public class AppError {
    private int status;
    private String message;
    private Date timestamp;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
