package com.example.mySecurityProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {
    private Long id;
    private Long userId;
    private String username;
    private LocalDateTime createdDate;
    private List<BasketDeviceDTO> basketDevices;
}