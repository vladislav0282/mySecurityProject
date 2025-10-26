package com.example.mySecurityProject.domain.shop;

import com.example.mySecurityProject.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "baskets")
@AllArgsConstructor
@NoArgsConstructor
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BasketDevices> basketDevices = new ArrayList<>();

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    private void init(){
        createdDate = LocalDateTime.now();
    }
}
