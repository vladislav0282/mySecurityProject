package com.example.mySecurityProject.controllers.shop;

import com.example.mySecurityProject.domain.shop.Brand;
import com.example.mySecurityProject.domain.shop.Device;
import com.example.mySecurityProject.domain.shop.Type;
import com.example.mySecurityProject.dto.BrandDTO;
import com.example.mySecurityProject.dto.DeviceDTO;
import com.example.mySecurityProject.exeptions.*;
import com.example.mySecurityProject.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/device/create")
    public String createDevice(
            @RequestParam("file") MultipartFile file,
            @RequestParam("deviceName") String deviceName,
            @RequestParam("price") String price,
            @RequestParam("count") Integer count,
            @RequestParam("brand") Long brandId,
            @RequestParam("type") Long typeId,
            @RequestParam(value = "info", required = false) String info
    ) throws IOException {
        deviceService.saveProduct(deviceName, count, price, brandId, typeId, info, file);
        return "Device created";
    }

    @PostMapping("/type/create")
    public ResponseEntity<?> createType(@RequestBody Type type) {
        try {
            deviceService.saveType(type);
        } catch (TypeAlreadyExistExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
        return ResponseEntity.ok().body("Type created");
    }

    @PostMapping("/brand/create")
    public ResponseEntity<?> createBrand(@RequestBody Brand brand) {
        try {
            deviceService.saveBrand(brand);
        } catch (BrandAlreadyExistExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
        return ResponseEntity.ok().body("Type created");
    }

    @GetMapping("/device")
    public ResponseEntity<List<DeviceDTO>> getAllDevice() {
        try {
            List<Device> devices = deviceService.getDevices();
            List<DeviceDTO> deviceDTOs = devices.stream()
                    .map(this::toDeviceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(deviceDTOs);
        } catch (DevicesNotFounExeption e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    private DeviceDTO toDeviceDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setDeviceId(device.getDeviceId());
        dto.setDeviceName(device.getDeviceName());
        dto.setPrice(device.getPrice());
        dto.setRating(device.getRating());
        dto.setImg(device.getImg());
        dto.setBrandName(device.getBrandId().getBrandName());
        dto.setTypeName(device.getTypeId().getTypeName());
        dto.setPreviewImageId(device.getPreviewImageId());
//                dto.setImageUrls(device.getImages().stream()
//                .map(image -> "/images/" + image.getId() + ".jpg")
//                .collect(Collectors.toList()));
//        List<String> urls = device.getImages().stream()
//                .map(image -> "/images/" + image.getId() + ".jpg")
//                .collect(Collectors.toList());
//        dto.setImageUrls(urls);
        return dto;
    }

    @GetMapping("/brand")
    public ResponseEntity<?> getAllBrands() {
        try {
            return ResponseEntity.ok(deviceService.getBrands());
        } catch (BrandsNotFounExeptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/type")
    public ResponseEntity getAllTypes() {
        try {
            return ResponseEntity.ok(deviceService.getTypes());
        } catch (TypesNotFounExeptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity getDeviceId(@PathVariable long deviceId) {
        try {
            return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
        } catch (DeviceNotFoundExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/devices")
    public ResponseEntity<?> getDevicesByTypeBrand(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long typeId,
            @RequestParam int page,
            @RequestParam int limit
    ) {
        try {
            List<Device> devices = deviceService.getDevicesTypeBrand(brandId, typeId, page, limit);
            List<DeviceDTO> dtos = devices.stream()
                    .map(DeviceDTO::toDeviceDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (DevicesNotFounExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/devicespage")
    public ResponseEntity<?> getDevicesByPage(@RequestParam int page, @RequestParam int limit) {
        try {
            Page<Device> devicesPage = deviceService.getDevicesPage(page, limit);
            List<DeviceDTO> dtos = devicesPage.getContent().stream()
                    .map(DeviceDTO::toDeviceDTO)
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("rows", dtos);
            response.put("count", devicesPage.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (DevicesNotFounExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}