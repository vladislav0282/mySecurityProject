package com.example.mySecurityProject.domain.shop;

import jakarta.persistence.*;
import lombok.Data;

    @Entity
    @Data
    @Table(name="brands")
    public class Brand {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) //при добавлении новой записи будет автоматически генерироваться новый id и добавляться колонка
        @Column(name = "id")
        private Integer id;

        @Column(name = "name")
        private String brandName;
    }

