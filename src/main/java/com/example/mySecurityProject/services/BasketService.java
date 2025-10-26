package com.example.mySecurityProject.services;

import com.example.mySecurityProject.domain.shop.Basket;
import com.example.mySecurityProject.domain.shop.BasketDevices;
import com.example.mySecurityProject.domain.shop.Brand;
import com.example.mySecurityProject.domain.shop.Device;
import com.example.mySecurityProject.domain.user.User;
import com.example.mySecurityProject.dto.BasketDTO;
import com.example.mySecurityProject.dto.BasketDeviceDTO;
import com.example.mySecurityProject.exeptions.BasketAlreadyExistExeption;
import com.example.mySecurityProject.exeptions.BrandAlreadyExistExeption;
import com.example.mySecurityProject.repositories.BasketRepository;
import com.example.mySecurityProject.repositories.DeviceRepository;
import com.example.mySecurityProject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketService {

    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    public ResponseEntity<?> saveBasket(Long userId, Long deviceId, Integer quantity) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

            Basket existingBasket = basketRepository.findByUserId(userId).orElse(null);

            Basket basket;
            if (existingBasket == null) {
                basket = new Basket();
                basket.setUser(user);
                basket = basketRepository.save(basket);
                log.info("Created new basket for user: {}", userId);
            } else {
                basket = existingBasket;
                log.info("Using existing basket for user: {}", userId);
            }

            Optional<BasketDevices> existingBasketDevice = basket.getBasketDevices().stream()
                    .filter(bd -> bd.getDevice().getDeviceId().equals(deviceId))
                    .findFirst();

            if (existingBasketDevice.isPresent()) {
                BasketDevices basketDevice = existingBasketDevice.get();
                basketDevice.setQuantity(basketDevice.getQuantity() + quantity);
                log.info("Updated quantity for device {} in basket {}", deviceId, basket.getId());
            } else {
                BasketDevices basketDevice = new BasketDevices();
                basketDevice.setBasket(basket);
                basketDevice.setDevice(device);
                basketDevice.setQuantity(quantity);
                basket.getBasketDevices().add(basketDevice);
                log.info("Added device {} to basket {}", deviceId, basket.getId());
            }

            basketRepository.save(basket);
            return ResponseEntity.ok().body("Device added to basket successfully");

        } catch (Exception e) {
            log.error("Error saving basket: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getUserBasket(Long userId) {
        try {
            Optional<Basket> basket = basketRepository.findByUserId(userId);
            if (basket.isPresent()) {
                BasketDTO response = convertToBasketResponseDTO(basket.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok().body("Basket is empty");
            }
        } catch (Exception e) {
            log.error("Error getting user basket: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private BasketDTO convertToBasketResponseDTO(Basket basket) {
        BasketDTO response = new BasketDTO();
        response.setId(basket.getId());
        response.setUserId(basket.getUser().getId());
        response.setUsername(basket.getUser().getUsername());
        response.setCreatedDate(basket.getCreatedDate());

        List<BasketDeviceDTO> deviceDTOs = basket.getBasketDevices().stream()
                .map(this::convertToBasketDeviceDTO)
                .collect(Collectors.toList());

        response.setBasketDevices(deviceDTOs);
        return response;
    }

    private BasketDeviceDTO convertToBasketDeviceDTO(BasketDevices basketDevice) {
        BasketDeviceDTO dto = new BasketDeviceDTO();
        dto.setId(basketDevice.getId());
        dto.setDeviceId(basketDevice.getDevice().getDeviceId());
        dto.setDeviceName(basketDevice.getDevice().getDeviceName());
        dto.setPrice(basketDevice.getDevice().getPrice());
        dto.setImg(basketDevice.getDevice().getImg());
        dto.setQuantity(basketDevice.getQuantity());
        dto.setAddedDate(basketDevice.getAddedDate());
        return dto;
    }
}
