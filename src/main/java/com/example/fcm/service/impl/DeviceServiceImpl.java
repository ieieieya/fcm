package com.example.fcm.service.impl;

import com.example.fcm.model.Device;
import com.example.fcm.repository.DeviceRepository;
import com.example.fcm.service.DeviceService;
import com.example.fcm.service.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    @Transactional
    public DeviceDTO save(DeviceDTO deviceDTO) {
        Device device = deviceRepository.save(toDeviceEntity(deviceDTO));
        return toDeviceDto(device);
    }

    private DeviceDTO toDeviceDto(Device device) {
        return DeviceDTO.builder()
                .platform(device.getPlatform())
                .enable(device.getEnable())
                .build();
    }

    private Device toDeviceEntity(DeviceDTO deviceDTO) {
        return Device.builder()
                .externalId(deviceDTO.getExternalId())
                .token(deviceDTO.getToken())
                .platform(deviceDTO.getPlatform())
                .enable(true)
                .build();
    }

    @Override
    @Transactional
    public DeviceDTO update(DeviceDTO deviceDTO) {
        Device device = findByExternalID(deviceDTO.getExternalId());
        device.setToken(deviceDTO.getToken());
        device.setEnable(deviceDTO.getEnable());
        return toDeviceDto(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public void delete(DeviceDTO deviceDTO) {
        deviceRepository.deleteByExternalId(deviceDTO.getExternalId());
    }

    @Override
    public Device findByExternalID(Long externalId) {
        return deviceRepository.findByExternalId(externalId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Device not found by id: '%s'", externalId)));
    }
}