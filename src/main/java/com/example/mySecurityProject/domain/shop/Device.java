package com.example.mySecurityProject.domain.shop;

import com.example.mySecurityProject.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "devices")
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates a new ID when a new record is added
    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "img")
    private String img;

    @Column(name = "count")
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "brand_id")

    private Brand brandId;

    @ManyToOne
    @JoinColumn(name = "type_id")

    private Type typeId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "device")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    private LocalDateTime dateOfCreated;

    public void addImageToDevice(Image image){
        image.setDevice(this);
        images.add(image);
    }

    @ManyToMany
    @JoinTable(
            name = "basket_devices",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "basket_id")
    )
    private List<Basket> baskets = new ArrayList<>();

    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }
}
