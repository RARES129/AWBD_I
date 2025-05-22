package com.awbd.awbd.controller;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @RequestMapping("/form")
    public String vehicleForm(Model model) {
        VehicleDto vehicle = new VehicleDto();
        model.addAttribute("vehicle",  vehicle);
        return "vehicleForm";
    }

    @PostMapping("")
    public String saveOrUpdate(@Valid @ModelAttribute("vehicle") VehicleDto vehicle,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "vehicleForm";
        }

        vehicleService.save(vehicle);
        return "redirect:/vehicle" ;
    }

    @RequestMapping("")
    public String vehicleList(Model model) {
        List<VehicleDto> vehicles = vehicleService.findClientVehicles();
        model.addAttribute("vehicles", vehicles);
        return "vehicleList";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.ensureNotInUse(Long.valueOf(id));
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/vehicle";
        }
        VehicleDto vehicle = vehicleService.findById(Long.valueOf(id));
        model.addAttribute("vehicle", vehicle);
        return "vehicleForm";
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.deleteById(Long.valueOf(id));
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vehicle";
    }
}
