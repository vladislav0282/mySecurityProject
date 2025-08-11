package com.example.mySecurityProject.domain.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //при добавлении новой записи будет автоматически генерироваться новый id и добавляться колонка
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
