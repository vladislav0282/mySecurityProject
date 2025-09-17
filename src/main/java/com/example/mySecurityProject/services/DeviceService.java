package com.example.mySecurityProject.services;

import com.example.mySecurityProject.domain.shop.Brand;
import com.example.mySecurityProject.domain.shop.Device;
import com.example.mySecurityProject.domain.shop.Type;
import com.example.mySecurityProject.domain.user.User;
import com.example.mySecurityProject.dto.UserDto;
import com.example.mySecurityProject.exeptions.*;
import com.example.mySecurityProject.repositories.BrandRepository;
import com.example.mySecurityProject.repositories.DeviceRepository;
import com.example.mySecurityProject.repositories.TypeRepository;
import com.example.mySecurityProject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;

    public List<Device> getDevices() throws DevicesNotFounExeption {
        List<Device> devices = deviceRepository.findAll();
        if (devices.isEmpty()) {
            throw new DevicesNotFounExeption("Devices not found");
        }
        return devices;
    }

    public List<Brand> getBrands() throws BrandsNotFounExeptions {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            throw new BrandsNotFounExeptions("Brands not found");
        }
        return brands;
    }

    public List<Type> getTypes() throws TypesNotFounExeptions {
        List<Type> types = typeRepository.findAll();
        if (types.isEmpty()) {
            throw new TypesNotFounExeptions("Types not found");
        }
        return types;
    }

    public Device getDeviceById(Long deviceId) throws DeviceNotFoundExeption {
        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new DeviceNotFoundExeption("Device not found with ID: " + deviceId));
        return device;
    }

    public List<Device> getDevicesTypeBrand(Long brandId, Long typeId, int page, int limit) throws DevicesNotFounExeption {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Device> devicesPage = deviceRepository.findByBrand_IdAndType_Id(brandId, typeId, pageable);
        List<Device> devices = devicesPage.getContent();
        if (devices.isEmpty()) {
            throw new DevicesNotFounExeption("Devices not found for brandId: " + brandId + " and typeId: " + typeId);
        }
        return devices;
    }


}