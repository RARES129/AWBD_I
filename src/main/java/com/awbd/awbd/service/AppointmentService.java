package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.AppointmentCreationDto;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.*;
import com.awbd.awbd.mapper.AppointmentMapper;
import com.awbd.awbd.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final MechanicRepository mechanicRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
    private final ServiceTypeRepository serviceTypeRepository;

//    public List<AppointmentDto> getAllAppointments() {
//        return appointmentRepository.findAll().stream()
//                .map(appointmentMapper::toAppointmentDto)
//                .collect(Collectors.toList());
//    }
//
//    public AppointmentDto getAppointmentById(Long id) {
//        Appointment appointment = appointmentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
//        return appointmentMapper.toAppointmentDto(appointment);
//    }
//
//    public AppointmentDto createAppointment(AppointmentCreationDto appointmentCreationDto) {
//        // Validate client exists
//        Client client = clientRepository.findById(appointmentCreationDto.getClientId())
//                .orElseThrow(() -> new RuntimeException("Client not found with id: " + appointmentCreationDto.getClientId()));
//
//        // Validate mechanic exists
//        Mechanic mechanic = mechanicRepository.findById(appointmentCreationDto.getMechanicId())
//                .orElseThrow(() -> new RuntimeException("Mechanic not found with id: " + appointmentCreationDto.getMechanicId()));
//
//        // Validate vehicle exists and belongs to the client
//        Vehicle vehicle = vehicleRepository.findById(appointmentCreationDto.getVehicleId())
//                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + appointmentCreationDto.getVehicleId()));
//
//        if (!vehicle.getOwner().getId().equals(client.getId())) {
//            throw new RuntimeException("Vehicle with id: " + vehicle.getId() + " does not belong to client with id: " + client.getId());
//        }
//
//        // Validate service types exist and are assigned to the mechanic
//        List<ServiceType> serviceTypes = new ArrayList<>();
//        for (Long serviceTypeId : appointmentCreationDto.getServiceTypeIds()) {
//            ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
//                    .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + serviceTypeId));
//
////            if (mechanic.getServiceTypes().stream().noneMatch(st -> st.getId().equals(serviceType.getId()))) {
////                throw new RuntimeException("ServiceType with id: " + serviceType.getId() + " is not assigned to mechanic with id: " + mechanic.getId());
////            }
//
//            serviceTypes.add(serviceType);
//        }
//
//        // Create and save the appointment
//        Appointment appointment = appointmentMapper.toAppointment(appointmentCreationDto);
//        appointment.setClient(client);
//        appointment.setMechanic(mechanic);
//        appointment.setVehicle(vehicle);
//        appointment.setServiceTypes(serviceTypes);
//
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//        return appointmentMapper.toAppointmentDto(savedAppointment);
//    }
//
//    public AppointmentDto updateAppointment(Long id, AppointmentCreationDto appointmentCreationDto) {
//        // Check if appointment exists
//        Appointment existingAppointment = appointmentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
//
//        // Validate client exists
//        Client client = clientRepository.findById(appointmentCreationDto.getClientId())
//                .orElseThrow(() -> new RuntimeException("Client not found with id: " + appointmentCreationDto.getClientId()));
//
//        // Validate mechanic exists
//        Mechanic mechanic = mechanicRepository.findById(appointmentCreationDto.getMechanicId())
//                .orElseThrow(() -> new RuntimeException("Mechanic not found with id: " + appointmentCreationDto.getMechanicId()));
//
//        // Validate vehicle exists and belongs to the client
//        Vehicle vehicle = vehicleRepository.findById(appointmentCreationDto.getVehicleId())
//                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + appointmentCreationDto.getVehicleId()));
//
//        if (!vehicle.getOwner().getId().equals(client.getId())) {
//            throw new RuntimeException("Vehicle with id: " + vehicle.getId() + " does not belong to client with id: " + client.getId());
//        }
//
//        // Validate service types exist and are assigned to the mechanic
//        List<ServiceType> serviceTypes = new ArrayList<>();
//        for (Long serviceTypeId : appointmentCreationDto.getServiceTypeIds()) {
//            ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
//                    .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + serviceTypeId));
//
////            if (mechanic.getServiceTypes().stream().noneMatch(st -> st.getId().equals(serviceType.getId()))) {
////                throw new RuntimeException("ServiceType with id: " + serviceType.getId() + " is not assigned to mechanic with id: " + mechanic.getId());
////            }
//
//            serviceTypes.add(serviceType);
//        }
//
//        // Update the appointment
//        existingAppointment.setDateTime(appointmentCreationDto.getDateTime());
//        existingAppointment.setClient(client);
//        existingAppointment.setMechanic(mechanic);
//        existingAppointment.setVehicle(vehicle);
//        existingAppointment.setServiceTypes(serviceTypes);
//
//        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
//        return appointmentMapper.toAppointmentDto(updatedAppointment);
//    }
//
//    public void deleteAppointment(Long id) {
//        if (!appointmentRepository.existsById(id)) {
//            throw new RuntimeException("Appointment not found with id: " + id);
//        }
//        appointmentRepository.deleteById(id);
//    }

    public List<Appointment> findClientAppointments() {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        return appointmentRepository.findByClientId(client.getId())
                .stream()
                .toList();
    }

    @Transactional
    public void save(AppointmentDto appointmentDto) {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        Appointment appointment;

        System.out.println(appointmentDto.toString());

        appointment = appointmentMapper.toAppointment(appointmentDto, client, mechanicRepository, vehicleRepository, serviceTypeRepository);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findAppointments() {
        List<Appointment> appointments;
        String username = SecurityUtil.getSessionUsername();
        User user = userRepository.findByUsername(username);
        if (user instanceof Client) {
            appointments = appointmentRepository.findByClientId(((Client) user).getId());
        } else if (user instanceof Mechanic) {
            appointments = appointmentRepository.findByMechanicId(((Mechanic) user).getId());
        } else return Collections.emptyList();;
        return appointments;
    }

//    public List<Appointment> findMechanicAppointments() {
//        String username = SecurityUtil.getSessionUsername();
//        Mechanic mechanic = mechanicRepository.findByUsername(username);
//
//        return appointmentRepository.findByClientId(mechanic.getId())
//                .stream()
//                .toList();
//    }
}
