package com.example.mySecurityProject.exeptions;

public class DeviceNotFoundExeption extends Exception{
    public DeviceNotFoundExeption(String message) {
        super(message);
    }
}