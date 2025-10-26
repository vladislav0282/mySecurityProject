package com.example.mySecurityProject.controllers.shop;

import com.example.mySecurityProject.domain.shop.Basket;
import com.example.mySecurityProject.domain.shop.Type;
import com.example.mySecurityProject.exeptions.BasketAlreadyExistExeption;
import com.example.mySecurityProject.exeptions.BrandAlreadyExistExeption;
import com.example.mySecurityProject.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/create")
    public ResponseEntity<?> createBasket(
            @RequestParam Long userId,
            @RequestParam Long deviceId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        return basketService.saveBasket(userId, deviceId, quantity);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBasket(@PathVariable Long userId) {
        try {
            return basketService.getUserBasket(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
