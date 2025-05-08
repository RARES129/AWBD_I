package com.awbd.awbd.controller;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    /**
     * Gets all receipts (admin only)
     * @param clientId the ID of the client
     * @return a list of all receipts as DTOs
     */
    @GetMapping("/receipts/client/{clientId}")
    public ResponseEntity<List<ReceiptDto>> getReceiptsByClientId(@PathVariable Long clientId) {
        List<ReceiptDto> receipts = receiptService.getReceiptsByClientId(clientId);
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }

    /**
     * Gets all receipts (admin only)
     * @param mechanicId the ID of the client
     * @return a list of all receipts as DTOs
     */
    @GetMapping("/receipts/mechanic/{mechanicId}")
    public ResponseEntity<List<ReceiptDto>> getReceiptsByMechanicId(@PathVariable Long mechanicId) {
        List<ReceiptDto> receipts = receiptService.getReceiptsByMechanicId(mechanicId);
        return new ResponseEntity<>(receipts, HttpStatus.OK);
    }

    /**
     * Gets a receipt by client ID and receipt ID
     * Client can only access their own receipts
     *
     * @param clientId the ID of the client
     * @param receiptId the ID of the receipt
     * @return the receipt as a DTO if found and authorized, or 403 if not authorized, or 404 if not found
     */
    @GetMapping("/receipt/{clientId}/{receiptId}")
    public ResponseEntity<ReceiptDto> getReceiptByIdForClient(
            @PathVariable Long clientId,
            @PathVariable Long receiptId) {
        try {
            ReceiptDto receipt = receiptService.getReceiptByIdForClient(clientId, receiptId);
            return new ResponseEntity<>(receipt, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Gets a receipt by client ID and appointment ID
     * Client can only access receipts from their own appointments
     *
     * @param clientId the ID of the client
     * @param appointmentId the ID of the appointment
     * @return the receipt as a DTO if found and authorized, or 403 if not authorized, or 404 if not found
     */
    @GetMapping("/receipt/{clientId}/appointment/{appointmentId}")
    public ResponseEntity<ReceiptDto> getReceiptByAppointmentIdForClient(
            @PathVariable Long clientId,
            @PathVariable Long appointmentId) {
        try {
            ReceiptDto receipt = receiptService.getReceiptByAppointmentIdForClient(clientId, appointmentId);
            return new ResponseEntity<>(receipt, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Gets a receipt by mechanic ID and receipt ID
     * Mechanic can only access receipts from their own appointments
     *
     * @param mechanicId the ID of the mechanic
     * @param receiptId the ID of the receipt
     * @return the receipt as a DTO if found and authorized, or 403 if not authorized, or 404 if not found
     */
    @GetMapping("/receipt/mechanic/{mechanicId}/{receiptId}")
    public ResponseEntity<ReceiptDto> getReceiptByIdForMechanic(
            @PathVariable Long mechanicId,
            @PathVariable Long receiptId) {
        try {
            ReceiptDto receipt = receiptService.getReceiptByIdForMechanic(mechanicId, receiptId);
            return new ResponseEntity<>(receipt, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Gets a receipt by mechanic ID and appointment ID
     * Mechanic can only access receipts from their own appointments
     *
     * @param mechanicId the ID of the mechanic
     * @param appointmentId the ID of the appointment
     * @return the receipt as a DTO if found and authorized, or 403 if not authorized, or 404 if not found
     */
    @GetMapping("/receipt/mechanic/{mechanicId}/appointment/{appointmentId}")
    public ResponseEntity<ReceiptDto> getReceiptByAppointmentIdForMechanic(
            @PathVariable Long mechanicId,
            @PathVariable Long appointmentId) {
        try {
            ReceiptDto receipt = receiptService.getReceiptByAppointmentIdForMechanic(mechanicId, appointmentId);
            return new ResponseEntity<>(receipt, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }



    /**
     * Creates a new receipt for a mechanic with auto-calculated values
     * Mechanic can only create receipts for their own appointments
     * The receipt will have the current date and total amount calculated from the appointment's service types
     *
     * @param mechanicId the ID of the mechanic
     * @param appointmentId the ID of the appointment
     * @return the created receipt as a DTO
     */
    @PostMapping("/receipt/mechanic/{mechanicId}/appointment/{appointmentId}")
    public ResponseEntity<ReceiptDto> createReceiptForMechanicByAppointmentId(
            @PathVariable Long mechanicId,
            @PathVariable Long appointmentId) {
        try {
            ReceiptDto createdReceipt = receiptService.createReceiptForMechanicByAppointmentId(mechanicId, appointmentId);
            return new ResponseEntity<>(createdReceipt, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Updates an existing receipt for a mechanic using auto-calculated values
     * Mechanic can only update receipts for their own appointments
     *
     * @param mechanicId the ID of the mechanic
     * @param receiptId the ID of the receipt
     * @param appointmentId the ID of the appointment
     * @return the updated receipt as a DTO
     */
    @PutMapping("/receipt/mechanic/{mechanicId}/appointment/{appointmentId}/receipt/{receiptId}")
    public ResponseEntity<ReceiptDto> updateReceiptForMechanicByAppointmentId(
            @PathVariable Long mechanicId,
            @PathVariable Long receiptId,
            @PathVariable Long appointmentId) {
        try {
            ReceiptDto updatedReceipt = receiptService.updateReceiptForMechanicByAppointmentId(mechanicId, receiptId, appointmentId);
            return new ResponseEntity<>(updatedReceipt, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else if (e.getMessage().startsWith("Receipt not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }


    /**
     * Deletes a receipt for a mechanic
     * Mechanic can only delete receipts for their own appointments
     *
     * @param mechanicId the ID of the mechanic
     * @param receiptId the ID of the receipt to delete
     * @return no content if successful and authorized, forbidden if not authorized, not found if the receipt doesn't exist
     */
    @DeleteMapping("/receipt/mechanic/{mechanicId}/{receiptId}")
    public ResponseEntity<Void> deleteReceiptForMechanic(
            @PathVariable Long mechanicId,
            @PathVariable Long receiptId) {
        try {
            receiptService.deleteReceiptForMechanic(mechanicId, receiptId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("does not have access")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
