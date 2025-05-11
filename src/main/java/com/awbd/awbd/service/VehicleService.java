package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.VehicleCreationDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.mapper.VehicleMapper;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.UserRepository;
import com.awbd.awbd.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final ClientRepository clientRepository;

    public List<VehicleDto> findAll() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toVehicleDto)
                .toList();
    }

    @Transactional
    public void save(VehicleDto vehicleDto) {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        Vehicle vehicle;

        if (vehicleDto.getId() != null) {
            vehicle = vehicleRepository.findById(vehicleDto.getId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found."));
            vehicleMapper.updateVehicleFromDto(vehicleDto, vehicle);
        } else {
            vehicle = vehicleMapper.toVehicle(vehicleDto, client);
        }
        vehicleRepository.save(vehicle);
    }

    public VehicleDto findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));
        return vehicleMapper.toVehicleDto(vehicle);
    }

    @Transactional
    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);
    }

    public List<VehicleDto> findClientVehicles() {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        return vehicleRepository.findByOwnerId(client.getId())
                .stream()
                .map(vehicleMapper::toVehicleDto)
                .toList();
    }
}
