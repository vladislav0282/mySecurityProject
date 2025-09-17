package com.example.mySecurityProject.domain.shop;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates a new ID when a new record is added
    @Column(name = "id")
    private Long deviceId;

    @Column(name = "name")
    private String devicename;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "img")
    private String img;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
}
