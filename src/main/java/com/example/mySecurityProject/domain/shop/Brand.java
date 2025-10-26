package com.example.mySecurityProject.domain.shop;

import com.example.mySecurityProject.dto.DeviceDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
    @Data
    @Table(name="brands")
    public class Brand {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) //при добавлении новой записи будет автоматически генерироваться новый id и добавляться колонка
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String brandName;

//@OneToMany(cascade = CascadeType.ALL, mappedBy = "brand")

//private List<Device> devices = new ArrayList<>();
    }

