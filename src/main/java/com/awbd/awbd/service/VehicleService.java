package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.mapper.VehicleMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final ClientRepository clientRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void save(VehicleDto vehicleDto) {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto, client);
        vehicleRepository.save(vehicle);
    }

    public VehicleDto findById(Long id) {
        Vehicle vehicle = vehicleRepository.findVehicleById(id);
        return vehicleMapper.toVehicleDto(vehicle);
    }

    @Transactional
    public void deleteById(Long id) {
        List<Appointment> appointments = appointmentRepository.findByVehicle_Id(id);
        if (appointments.stream().anyMatch(appointment -> appointment.getReceipt() == null)){
            throw new RuntimeException("Vehicle is used in an unfinished appointment.");
        }

        for (Appointment appointment : appointments) {
            appointment.setVehicle(null);
        }

        appointmentRepository.saveAll(appointments);
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

    public void ensureNotInUse(Long id) {
        if (appointmentRepository.existsByVehicle_Id(id)) {
            throw new RuntimeException("Vehicle is used in an unfinished appointment.");
        }
    }
}
