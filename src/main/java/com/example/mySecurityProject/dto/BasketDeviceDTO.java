package com.example.mySecurityProject.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketDeviceDTO {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private Integer price;
    private String img;
    private Integer quantity;
    private LocalDateTime addedDate;
}