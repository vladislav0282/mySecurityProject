package com.example.mySecurityProject.domain.shop;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="types")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //при добавлении новой записи будет автоматически генерироваться новый id и добавляться колонка
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String typeName;

// @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
//@OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
//
//private List<Device> devices = new ArrayList<>();

}
