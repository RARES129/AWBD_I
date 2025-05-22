package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.mapper.ServiceTypeMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;
    private final MechanicRepository mechanicRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void save(ServiceTypeDto serviceTypeDto) {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        ServiceType serviceType = serviceTypeMapper.toServiceType(serviceTypeDto, mechanic);
        serviceTypeRepository.save(serviceType);
    }

    public List<ServiceTypeDto> findMechanicServiceTypes() {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        return serviceTypeRepository.findByMechanicId(mechanic.getId())
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .toList();
    }

    public List<ServiceTypeDto> findMechanicServiceTypes(Long mechanicId) {
        return serviceTypeRepository.findByMechanicId(mechanicId)
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        List<Appointment> appointments = appointmentRepository.findByServiceTypes_Id(id);
        if (appointments.stream().anyMatch(appointment -> appointment.getReceipt() == null)){
            throw new RuntimeException("Service Type is used in an unfinished appointment.");
        }

        for (Appointment appointment : appointments) {
            appointment.getServiceTypes().removeIf(serviceType -> serviceType.getId().equals(id));
        }

        appointmentRepository.saveAll(appointments);
        serviceTypeRepository.deleteById(id);
    }

    public ServiceTypeDto findById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service Type not found."));
        return serviceTypeMapper.toServiceTypeDto(serviceType);
    }
}