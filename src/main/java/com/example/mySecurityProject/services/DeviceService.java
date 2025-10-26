package com.example.mySecurityProject.services;

import com.example.mySecurityProject.domain.shop.Brand;
import com.example.mySecurityProject.domain.shop.Device;
import com.example.mySecurityProject.domain.shop.Image;
import com.example.mySecurityProject.domain.shop.Type;
import com.example.mySecurityProject.domain.user.User;
import com.example.mySecurityProject.dto.UserDto;
import com.example.mySecurityProject.exeptions.*;
import com.example.mySecurityProject.repositories.BrandRepository;
import com.example.mySecurityProject.repositories.DeviceRepository;
import com.example.mySecurityProject.repositories.TypeRepository;
import com.example.mySecurityProject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;

    public void saveProduct(String deviceName, Integer count, String price, Long brandId, Long typeId, String info, MultipartFile file) throws IOException {
        Device device = new Device();
        device.setDeviceName(deviceName);
        device.setPrice((int) Double.parseDouble(price));
        device.setCount(count);

        // Находим и устанавливаем Brand
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + brandId));
        device.setBrandId(brand);

        // Находим и устанавливаем Type
        Type type = typeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Type not found with id: " + typeId));
        device.setTypeId(type);

        // Обработка дополнительной информации
        if (info != null && !info.isEmpty()) {
            // Парсим JSON info и сохраняем в device
            // Это зависит от структуры вашей Device сущности
        }

        if(device.getCount() == null || device.getCount() <= 0){
            device.setCount(0);
        }

        Image image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            image.setPreviewImage(true);
            device.addImageToDevice(image);
        }

        log.info("Saving device: {}", device.getDeviceName());
        Device deviceFromDb = deviceRepository.save(device);
        if (!device.getImages().isEmpty()) {
            deviceFromDb.setPreviewImageId(deviceFromDb.getImages().get(0).getId());
            deviceRepository.save(deviceFromDb);
        }
    }


    public ResponseEntity<?> saveType(Type type) throws TypeAlreadyExistExeption {
        if (typeRepository.findByTypeName(type.getTypeName()).isPresent()) {
            throw new TypeAlreadyExistExeption("Тип с указанным названием уже существует");
        }
        if (type.getTypeName() == "") {
            throw new TypeAlreadyExistExeption("нет названия");
        }
        return ResponseEntity.ok(typeRepository.save(type));
    }

    public ResponseEntity<?> saveBrand(Brand brand) throws BrandAlreadyExistExeption {
        if (brandRepository.findByBrandName(brand.getBrandName()).isPresent()) {
            throw new BrandAlreadyExistExeption("Бренд с указанным названием уже существует");
        }
        if (brand.getBrandName() == "") {
            throw new BrandAlreadyExistExeption("нет названия");
        }
        return ResponseEntity.ok(brandRepository.save(brand));
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

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
        Page<Device> devicesPage;

        boolean hasBrand = (brandId != null && brandId > 0);
        boolean hasType = (typeId != null && typeId > 0);

        if (hasBrand && hasType) {
            // оба фильтра
            devicesPage = deviceRepository.findByBrandId_IdAndTypeId_Id(brandId, typeId, pageable);
        } else if (hasBrand) {
            // только бренд
            devicesPage = deviceRepository.findByBrandId_Id(brandId, pageable);
        } else if (hasType) {
            // только тип
            devicesPage = deviceRepository.findByTypeId_Id(typeId, pageable);
        } else {
            // без фильтров — все устройства постранично
            devicesPage = deviceRepository.findAll(pageable);
        }

        if (devicesPage == null) {
            throw new DevicesNotFounExeption("Query returned null Page object");
        }

        List<Device> devices = devicesPage.getContent();
        if (devices.isEmpty()) {
            // По желанию: можешь не кидать исключение, а вернуть пустой список
//            throw new DevicesNotFounExeption(
//                    "Devices not found for brandId: " + brandId + " and typeId: " + typeId
//            );
            return List.of();
        }

        return devices;
    }

    public Page<Device> getDevicesPage(int page, int limit) throws DevicesNotFounExeption {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Device> devicesPage = deviceRepository.findAll(pageable);
        if (devicesPage == null) {
            throw new DevicesNotFounExeption("Query returned null Page object");
        }
        return devicesPage;
    }
}