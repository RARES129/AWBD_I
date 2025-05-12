package com.awbd.awbd.controller;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.AppointmentCreationDto;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.service.AppointmentService;
import com.awbd.awbd.service.MechanicService;
import com.awbd.awbd.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final VehicleService vehicleService;

    @GetMapping("/mechanic/{mechanicId}")
    public String appointmentForm(@PathVariable Long mechanicId, Model model){
        System.out.println("aici");
        AppointmentDto appointment = new AppointmentDto();
        appointment.setMechanicId(mechanicId);
        model.addAttribute("appointment",  appointment);
        List<VehicleDto> vehicles = vehicleService.findClientVehicles();
        model.addAttribute("vehicles", vehicles);
        return "appointmentForm";
    }

    @PostMapping("")
    public String saveOrUpdate(@ModelAttribute AppointmentDto appointment) {
        System.out.println("vehicle id: " + appointment.getVehicleId());
        appointmentService.save(appointment);
        return "redirect:/appointment" ;
    }

    @RequestMapping("")
    public String appointmentList(Model model) {
        List<Appointment> appointments = appointmentService.findAppointments();
        model.addAttribute("appointments", appointments);
        return "appointmentList";
    }


//    /**
//     * Gets all appointments
//     *
//     * @return a list of all appointments as DTOs
//     */
//    @GetMapping
//    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
//        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
//        return new ResponseEntity<>(appointments, HttpStatus.OK);
//    }
//
//    /**
//     * Gets an appointment by ID
//     *
//     * @param id the ID of the appointment to get
//     * @return the appointment as a DTO if found, or 404 if not found
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
//        try {
//            AppointmentDto appointment = appointmentService.getAppointmentById(id);
//            return new ResponseEntity<>(appointment, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    /**
//     * Creates a new appointment
//     *
//     * @param appointmentCreationDto the appointment data
//     * @return the created appointment as a DTO
//     */
//    @PostMapping
//    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentCreationDto appointmentCreationDto) {
//        try {
//            AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentCreationDto);
//            return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    /**
//     * Updates an existing appointment
//     *
//     * @param id the ID of the appointment to update
//     * @param appointmentCreationDto the updated appointment data
//     * @return the updated appointment as a DTO if found, or 404 if not found
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<AppointmentDto> updateAppointment(
//            @PathVariable Long id,
//            @RequestBody AppointmentCreationDto appointmentCreationDto) {
//        try {
//            AppointmentDto updatedAppointment = appointmentService.updateAppointment(id, appointmentCreationDto);
//            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            if (e.getMessage().startsWith("Appointment not found")) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//        }
//    }
//
//    /**
//     * Deletes an appointment
//     *
//     * @param id the ID of the appointment to delete
//     * @return no content if successful, not found if the appointment doesn't exist
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
//        try {
//            appointmentService.deleteAppointment(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}