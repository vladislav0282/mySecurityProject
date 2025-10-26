package com.example.mySecurityProject.dto;

import com.example.mySecurityProject.domain.shop.Brand;
import com.example.mySecurityProject.domain.shop.Device;
import lombok.Data;


import java.util.List;
import java.util.stream.Collectors;



@Data
public class BrandDTO {
    private String brandName;
    private Long brandId;
//    private List<DeviceDTO> devices;

    public static BrandDTO toBrandDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandId(brand.getId());
        brandDTO.setBrandName(brand.getBrandName());
//        brandDTO.setDevices(brand.getDevices().stream().map(DeviceDTO::toDeviceDTO).collect(Collectors.toList()));
        return brandDTO;
    }
}

