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

    Optional<Device> findByDeviceName(String deviceName);
    Optional<Device> findByCount(Integer count);
    Optional<Device> findByDeviceId(Long deviceId);

    // Уже есть (без пагинации):
    List<Device> findByTypeId_Id(Long typeId);
    List<Device> findByBrandId_Id(Long brandId);
    List<Device> findByBrandId_IdAndTypeId_Id(Long brandId, Long typeId);

    // Уже есть (с пагинацией):
    Page<Device> findByBrandId_IdAndTypeId_Id(Long brandId, Long typeId, Pageable pageable);

    // ДОБАВЬ ЭТИ ДВА (с пагинацией для одного фильтра):
    Page<Device> findByBrandId_Id(Long brandId, Pageable pageable);
    Page<Device> findByTypeId_Id(Long typeId, Pageable pageable);
}
