package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.mapper.AppointmentMapper;
import com.awbd.awbd.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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

    @Transactional
    public void save(AppointmentDto appointmentDto) {
        String username = SecurityUtil.getSessionUsername();
        Client client = clientRepository.findByUsername(username);

        Appointment appointment = appointmentMapper.toAppointment(appointmentDto, client, mechanicRepository, vehicleRepository, serviceTypeRepository);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findAppointments() {
        String username = SecurityUtil.getSessionUsername();
        User user = userRepository.findByUsername(username);

        List<Appointment> appointments;

        if (user instanceof Client) {
            appointments = appointmentRepository.findByClientIdOrderByDateTimeAsc(user.getId());
        } else if (user instanceof Mechanic) {
            appointments = appointmentRepository.findByMechanicIdOrderByDateTimeAsc(user.getId());
        } else return Collections.emptyList();

        return appointments;
    }
}
