package com.example.mySecurityProject.repositories;

import com.example.mySecurityProject.domain.shop.Device;
import com.example.mySecurityProject.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDevicename(String devicename);
    Optional<Device> findByDeviceId(Long deviceId);
    List<Device> findByType_Id(Long typeId);
    List<Device> findByBrand_Id(Long brandId);
    List<Device> findByBrand_IdAndType_Id(Long brandId, Long typeId);
    Page<Device> findByBrand_IdAndType_Id(Long brandId, Long typeId, Pageable pageable);
}
