package com.example.mySecurityProject.dto;

import com.example.mySecurityProject.domain.shop.Device;
import lombok.Data;

@Data
public class DeviceDTO {
    private Long deviceId;
    private String deviceName;
    private Integer price;
    private Integer count;
    private Integer rating;
    private String img;
    private String brandName;
    private String typeName;
    private Long previewImageId;


    public static DeviceDTO toDeviceDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setDeviceId(device.getDeviceId());
        dto.setDeviceName(device.getDeviceName());
        dto.setPrice(device.getPrice());
        dto.setCount(device.getCount());
        dto.setRating(device.getRating());
        dto.setImg(device.getImg());
        dto.setBrandName(device.getBrandId().getBrandName());
        dto.setTypeName(device.getTypeId().getTypeName());
        dto.setPreviewImageId(device.getPreviewImageId());
        return dto;
    }
}

