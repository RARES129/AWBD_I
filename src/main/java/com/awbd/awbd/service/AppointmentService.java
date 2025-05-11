package com.awbd.awbd.service;

import com.awbd.awbd.dto.AppointmentCreationDto;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.mapper.AppointmentMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import com.awbd.awbd.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final MechanicRepository mechanicRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final AppointmentMapper appointmentMapper;


    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toAppointmentDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        return appointmentMapper.toAppointmentDto(appointment);
    }

    public AppointmentDto createAppointment(AppointmentCreationDto appointmentCreationDto) {
        // Validate client exists
        Client client = clientRepository.findById(appointmentCreationDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + appointmentCreationDto.getClientId()));

        // Validate mechanic exists
        Mechanic mechanic = mechanicRepository.findById(appointmentCreationDto.getMechanicId())
                .orElseThrow(() -> new RuntimeException("Mechanic not found with id: " + appointmentCreationDto.getMechanicId()));

        // Validate vehicle exists and belongs to the client
        Vehicle vehicle = vehicleRepository.findById(appointmentCreationDto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + appointmentCreationDto.getVehicleId()));

        if (!vehicle.getOwner().getId().equals(client.getId())) {
            throw new RuntimeException("Vehicle with id: " + vehicle.getId() + " does not belong to client with id: " + client.getId());
        }

        // Validate service types exist and are assigned to the mechanic
        List<ServiceType> serviceTypes = new ArrayList<>();
        for (Long serviceTypeId : appointmentCreationDto.getServiceTypeIds()) {
            ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                    .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + serviceTypeId));

//            if (mechanic.getServiceTypes().stream().noneMatch(st -> st.getId().equals(serviceType.getId()))) {
//                throw new RuntimeException("ServiceType with id: " + serviceType.getId() + " is not assigned to mechanic with id: " + mechanic.getId());
//            }

            serviceTypes.add(serviceType);
        }

        // Create and save the appointment
        Appointment appointment = appointmentMapper.toAppointment(appointmentCreationDto);
        appointment.setClient(client);
        appointment.setMechanic(mechanic);
        appointment.setVehicle(vehicle);
        appointment.setServiceTypes(serviceTypes);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentDto(savedAppointment);
    }

    public AppointmentDto updateAppointment(Long id, AppointmentCreationDto appointmentCreationDto) {
        // Check if appointment exists
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // Validate client exists
        Client client = clientRepository.findById(appointmentCreationDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + appointmentCreationDto.getClientId()));

        // Validate mechanic exists
        Mechanic mechanic = mechanicRepository.findById(appointmentCreationDto.getMechanicId())
                .orElseThrow(() -> new RuntimeException("Mechanic not found with id: " + appointmentCreationDto.getMechanicId()));

        // Validate vehicle exists and belongs to the client
        Vehicle vehicle = vehicleRepository.findById(appointmentCreationDto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + appointmentCreationDto.getVehicleId()));

        if (!vehicle.getOwner().getId().equals(client.getId())) {
            throw new RuntimeException("Vehicle with id: " + vehicle.getId() + " does not belong to client with id: " + client.getId());
        }

        // Validate service types exist and are assigned to the mechanic
        List<ServiceType> serviceTypes = new ArrayList<>();
        for (Long serviceTypeId : appointmentCreationDto.getServiceTypeIds()) {
            ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                    .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + serviceTypeId));

//            if (mechanic.getServiceTypes().stream().noneMatch(st -> st.getId().equals(serviceType.getId()))) {
//                throw new RuntimeException("ServiceType with id: " + serviceType.getId() + " is not assigned to mechanic with id: " + mechanic.getId());
//            }

            serviceTypes.add(serviceType);
        }

        // Update the appointment
        existingAppointment.setDateTime(appointmentCreationDto.getDateTime());
        existingAppointment.setClient(client);
        existingAppointment.setMechanic(mechanic);
        existingAppointment.setVehicle(vehicle);
        existingAppointment.setServiceTypes(serviceTypes);

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toAppointmentDto(updatedAppointment);
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}
