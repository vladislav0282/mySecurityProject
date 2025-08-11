package com.example.mySecurityProject.exeptions;

public class UserAlreadyExistExeption extends Exception{
    public UserAlreadyExistExeption(String message) {
        super(message);
    }
}
