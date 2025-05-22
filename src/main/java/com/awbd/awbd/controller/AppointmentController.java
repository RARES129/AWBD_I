package com.awbd.awbd.controller;

import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.service.AppointmentService;
import com.awbd.awbd.service.ReceiptService;
import com.awbd.awbd.service.ServiceTypeService;
import com.awbd.awbd.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final VehicleService vehicleService;
    private final ServiceTypeService serviceTypeService;
    private final ReceiptService receiptService;

    @GetMapping("/mechanic/{mechanicId}")
    public String appointmentForm(@PathVariable Long mechanicId, Model model){
        AppointmentDto appointment = new AppointmentDto();
        appointment.setMechanicId(mechanicId);
        model.addAttribute("appointment",  appointment);

        List<VehicleDto> vehicles = vehicleService.findClientVehicles();
        model.addAttribute("vehicles", vehicles);

        List<ServiceTypeDto> serviceTypes = serviceTypeService.findMechanicServiceTypes(mechanicId);
        model.addAttribute("serviceTypes", serviceTypes);

        return "appointmentForm";
    }

    @PostMapping("")
    public String saveOrUpdate(@Valid @ModelAttribute("appointment") AppointmentDto appointment,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()){
            List<VehicleDto> vehicles = vehicleService.findClientVehicles();
            model.addAttribute("vehicles", vehicles);

            List<ServiceTypeDto> serviceTypes = serviceTypeService.findMechanicServiceTypes(appointment.getMechanicId());
            model.addAttribute("serviceTypes", serviceTypes);

            return "appointmentForm";
        }

        appointmentService.save(appointment);
        return "redirect:/appointment" ;
    }

    @RequestMapping("")
    public String appointmentList(Model model) {
        List<Appointment> appointments = appointmentService.findAppointments();
        model.addAttribute("appointments", appointments);
        return "appointmentList";
    }

    @PostMapping("/{appointmentId}/receipt")
    public String generateReceipt(@PathVariable Long appointmentId) {
        receiptService.generateReceiptForAppointment(appointmentId);
        return "redirect:/appointment";
    }

    @GetMapping("/receipt/{appointmentId}")
    public String viewReceipt(@PathVariable Long appointmentId, Model model) {
        ReceiptDto receipt = receiptService.getReceiptByAppointmentId(appointmentId);
        model.addAttribute("receipt", receipt);
        return "receiptView";
    }
}