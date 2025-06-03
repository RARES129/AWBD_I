package com.awbd.awbd.service;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import com.awbd.awbd.mapper.VehicleMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public VehicleDto save(VehicleDto vehicleDto) {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto, client);
        return vehicleMapper.toVehicleDto(vehicleRepository.save(vehicle));
    }

    public VehicleDto findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return vehicleMapper.toVehicleDto(vehicle);
    }

    @Transactional
    public void deleteById(Long id) {
        List<Appointment> appointments = appointmentRepository.findByVehicle_Id(id);
        if (appointments.stream().anyMatch(appointment -> appointment.getReceipt() == null)){
            throw new EntityInUnfinishedAppointmentException("Vehicle");
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

    public Page<VehicleDto> findClientVehiclesPaginated(Pageable pageable) {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        return vehicleRepository.findByOwnerId(client.getId(), pageable)
                .map(vehicleMapper::toVehicleDto);
    }

    public void ensureNotInUse(Long id) {
        if (appointmentRepository.existsByVehicle_Id(id)) {
            throw new EntityInUnfinishedAppointmentException("Vehicle");
        }
    }
}
