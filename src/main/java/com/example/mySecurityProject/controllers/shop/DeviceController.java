package com.example.mySecurityProject.controllers.shop;

import com.example.mySecurityProject.exeptions.BrandsNotFounExeptions;
import com.example.mySecurityProject.exeptions.DeviceNotFoundExeption;
import com.example.mySecurityProject.exeptions.DevicesNotFounExeption;
import com.example.mySecurityProject.exeptions.TypesNotFounExeptions;
import com.example.mySecurityProject.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/device")
    public ResponseEntity getAllDevice() {
        try {
            return ResponseEntity.ok(deviceService.getDevices());
        } catch (DevicesNotFounExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/brand")
    public ResponseEntity getAllBrands() {
        try {
            return ResponseEntity.ok(deviceService.getBrands());
        } catch (BrandsNotFounExeptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/type")
    public ResponseEntity getAllTypes() {
        try {
            return ResponseEntity.ok(deviceService.getTypes());
        } catch (TypesNotFounExeptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity getDeviceId(@PathVariable long deviceId) {
        try {
            return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
        } catch (DeviceNotFoundExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

//    @GetMapping("/devices")
//    public ResponseEntity getDevicesByTypeBrand(@RequestParam long brandId, @RequestParam long typeId,
//                                                @RequestParam int page, @RequestParam int limit) {
//        try {
//            return ResponseEntity.ok(deviceService.getDevicesTypeBrand(brandId, typeId, page, limit));
//        } catch (DevicesNotFounExeption e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error");
//        }
//    }

    @GetMapping("/devices")
    public ResponseEntity<?> getDevicesByTypeBrand(
            @RequestParam("brandId") long brandId,
            @RequestParam("typeId") long typeId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        try {
            return ResponseEntity.ok(deviceService.getDevicesTypeBrand(brandId, typeId, page, limit));
        } catch (DevicesNotFounExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }




}
