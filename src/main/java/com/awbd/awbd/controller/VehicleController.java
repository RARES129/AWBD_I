package com.awbd.awbd.controller;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

        try {
            vehicleService.save(vehicle);
        } catch (DataIntegrityViolationException e) {
            bindingResult.addError(new FieldError("vehicle", "plateNumber", "Plate number must be unique"));
            return "vehicleForm";
        }

        return "redirect:/vehicle" ;
    }

    @RequestMapping("")
    public String vehicleList(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "id") String sortBy) {
        Page<VehicleDto> vehiclePage = vehicleService.findClientVehiclesPaginated(PageRequest.of(page, size, Sort.by(sortBy)));
        model.addAttribute("vehiclePage", vehiclePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        return "vehicleList";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        vehicleService.ensureNotInUse(Long.valueOf(id));
        VehicleDto vehicle = vehicleService.findById(Long.valueOf(id));
        model.addAttribute("vehicle", vehicle);
        return "vehicleForm";
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable String id) {
        vehicleService.deleteById(Long.valueOf(id));
        return "redirect:/vehicle";
    }
}
