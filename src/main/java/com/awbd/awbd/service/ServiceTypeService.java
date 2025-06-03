package com.awbd.awbd.service;

import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import com.awbd.awbd.mapper.ServiceTypeMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ServiceTypeDto save(ServiceTypeDto serviceTypeDto) {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        ServiceType serviceType = serviceTypeMapper.toServiceType(serviceTypeDto, mechanic);
        return serviceTypeMapper.toServiceTypeDto(serviceTypeRepository.save(serviceType));
    }

    public List<ServiceTypeDto> findMechanicServiceTypes() {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        return serviceTypeRepository.findByMechanicId(mechanic.getId())
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .toList();
    }

    public Page<ServiceTypeDto> findMechanicServiceTypesPaginated(Pageable pageable) {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        return serviceTypeRepository.findByMechanicId(mechanic.getId(), pageable)
                .map(serviceTypeMapper::toServiceTypeDto);
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
            throw new EntityInUnfinishedAppointmentException("Service Type");
        }

        for (Appointment appointment : appointments) {
            appointment.getServiceTypes().removeIf(serviceType -> serviceType.getId().equals(id));
        }

        appointmentRepository.saveAll(appointments);
        serviceTypeRepository.deleteById(id);
    }

    public ServiceTypeDto findById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Type not found with id: " + id));
        return serviceTypeMapper.toServiceTypeDto(serviceType);
    }
}