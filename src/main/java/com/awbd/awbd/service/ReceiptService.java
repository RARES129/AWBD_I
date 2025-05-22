package com.awbd.awbd.service;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Receipt;
import com.awbd.awbd.entity.ServiceCopy;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.mapper.ReceiptMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final AppointmentRepository appointmentRepository;
    private final ReceiptMapper receiptMapper;

    public void generateReceiptForAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);

        Receipt receipt = new Receipt();
        receipt.setIssueDate(LocalDate.now());

        Double totalAmount = appointment.getServiceTypes().stream()
                .mapToDouble(ServiceType::getPrice)
                .sum();
        receipt.setTotalAmount(totalAmount);

        receipt.setClient(appointment.getClient());
        receipt.setMechanic(appointment.getMechanic());
        receipt.setAppointment(appointment);
        receipt.setVehicle(
                appointment.getVehicle().getBrand() + " " +
                appointment.getVehicle().getModel() + " " +
                appointment.getVehicle().getPlateNumber());

        List<ServiceCopy> serviceCopies = appointment.getServiceTypes().stream()
                .map(st -> new ServiceCopy(st.getName(), st.getPrice()))
                .toList();
        receipt.setServices(serviceCopies);

        receipt.setAppointment(appointment);
        appointment.setReceipt(receipt);

        appointmentRepository.save(appointment);
    }

    public ReceiptDto getReceiptByAppointmentId(Long appointmentId) {
        Receipt receipt = receiptRepository.findByAppointmentId(appointmentId);
        return receiptMapper.toReceiptDto(receipt);
    }
}
