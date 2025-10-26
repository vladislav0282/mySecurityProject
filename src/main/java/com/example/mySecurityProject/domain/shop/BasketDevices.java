package com.example.mySecurityProject.domain.shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "basket_devices")
@AllArgsConstructor
@NoArgsConstructor
public class BasketDevices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @PrePersist
    private void init(){
        addedDate = LocalDateTime.now();
    }
}
