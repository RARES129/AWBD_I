package com.awbd.awbd.service;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Receipt;
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

    public List<ReceiptDto> getReceiptsByClientId(Long clientId) {
        return receiptRepository.findByClientId(clientId).stream()
                .map(receiptMapper::toReceiptDto)
                .collect(Collectors.toList());
    }

    public List<ReceiptDto> getReceiptsByMechanicId(Long mechanicId) {
        return receiptRepository.findByMechanicId(mechanicId).stream()
                .map(receiptMapper::toReceiptDto)
                .collect(Collectors.toList());
    }


    // Method to get a receipt by appointment ID with client authorization check
    public ReceiptDto getReceiptByAppointmentIdForClient(Long clientId, Long appointmentId) {
        List<Receipt> receipts = receiptRepository.findByAppointmentId(appointmentId);
        if (receipts.isEmpty()) {
            throw new RuntimeException("Receipt not found for appointment with id: " + appointmentId);
        }

        Receipt receipt = receipts.get(0);
        if (!receipt.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Client with id: " + clientId + " does not have access to receipt for appointment with id: " + appointmentId);
        }

        return receiptMapper.toReceiptDto(receipt);
    }

    // Method to get a receipt by appointment ID with mechanic authorization check
    public ReceiptDto getReceiptByAppointmentIdForMechanic(Long mechanicId, Long appointmentId) {
        List<Receipt> receipts = receiptRepository.findByAppointmentId(appointmentId);
        if (receipts.isEmpty()) {
            throw new RuntimeException("Receipt not found for appointment with id: " + appointmentId);
        }

        Receipt receipt = receipts.get(0);
        if (!receipt.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic with id: " + mechanicId + " does not have access to receipt for appointment with id: " + appointmentId);
        }

        return receiptMapper.toReceiptDto(receipt);
    }

    // Method to get a receipt by ID with client authorization check
    public ReceiptDto getReceiptByIdForClient(Long clientId, Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));

        if (!receipt.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Client with id: " + clientId + " does not have access to receipt with id: " + receiptId);
        }

        return receiptMapper.toReceiptDto(receipt);
    }

    // Method to get a receipt by ID with mechanic authorization check
    public ReceiptDto getReceiptByIdForMechanic(Long mechanicId, Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));

        if (!receipt.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic with id: " + mechanicId + " does not have access to receipt with id: " + receiptId);
        }

        return receiptMapper.toReceiptDto(receipt);
    }


    // Method to create a receipt for a mechanic with auto-calculated values
    public ReceiptDto createReceiptForMechanicByAppointmentId(Long mechanicId, Long appointmentId) {
        // Validate appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Check if mechanic has access to the appointment
        if (!appointment.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic with id: " + mechanicId + " does not have access to appointment with id: " + appointment.getId());
        }

        // Create a new receipt with auto-calculated values
        Receipt receipt = new Receipt();
        receipt.setIssueDate(LocalDate.now()); // Set current date

        // Calculate total amount from service types
        Double totalAmount = appointment.getServiceTypes().stream()
                .mapToDouble(serviceType -> serviceType.getPrice())
                .sum();
        receipt.setTotalAmount(totalAmount);

        receipt.setClient(appointment.getClient());
        receipt.setMechanic(appointment.getMechanic());
        receipt.setAppointment(appointment);

        Receipt savedReceipt = receiptRepository.save(receipt);
        return receiptMapper.toReceiptDto(savedReceipt);
    }

    public ReceiptDto updateReceiptForMechanicByAppointmentId(Long mechanicId, Long receiptId, Long appointmentId) {
        // Find the existing receipt
        Receipt existingReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));

        // Ensure the mechanic owns the receipt
        if (!existingReceipt.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic with id: " + mechanicId + " does not have access to receipt with id: " + receiptId);
        }

        // Load the appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Ensure the mechanic owns the appointment
        if (!appointment.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic with id: " + mechanicId + " does not have access to appointment with id: " + appointmentId);
        }

        // ✅ Ensure the receipt is assigned to the given appointment
        if (!existingReceipt.getAppointment().getId().equals(appointmentId)) {
            throw new RuntimeException("Receipt with id: " + receiptId + " is not associated with appointment id: " + appointmentId);
        }

        // Auto-calculate and update fields
        existingReceipt.setIssueDate(LocalDate.now());

        Double totalAmount = appointment.getServiceTypes().stream()
                .mapToDouble(serviceType -> serviceType.getPrice())
                .sum();
        existingReceipt.setTotalAmount(totalAmount);

        // Set linked data again just in case
        existingReceipt.setClient(appointment.getClient());
        existingReceipt.setMechanic(appointment.getMechanic());
        existingReceipt.setAppointment(appointment);

        Receipt updatedReceipt = receiptRepository.save(existingReceipt);
        return receiptMapper.toReceiptDto(updatedReceipt);
    }


    // Method to delete a receipt for a mechanic
    public void deleteReceiptForMechanic(Long mechanicId, Long receiptId) {
        // Check if receipt exists
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));

        // Check if mechanic has access to the receipt
        if (!receipt.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic with id: " + mechanicId + " does not have access to receipt with id: " + receiptId);
        }

        receiptRepository.deleteById(receiptId);
    }
}
