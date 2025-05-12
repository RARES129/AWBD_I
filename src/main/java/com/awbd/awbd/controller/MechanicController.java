package com.awbd.awbd.controller;

import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.service.MechanicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mechanic")
@RequiredArgsConstructor
public class MechanicController {

    private final MechanicService mechanicService;

//    @RequestMapping("/form")
//    public String mechanicForm(Model model) {
//        MechanicDto mechanic = new MechanicDto();
//        model.addAttribute("mechanic",  mechanic);
//        return "mechanicForm";
//    }

    @RequestMapping("")
    public String mechanicList(Model model) {
        List<MechanicDto> mechanics = mechanicService.findAll();
        model.addAttribute("mechanics",mechanics);
        return "mechanicList";
    }

//    @RequestMapping("/edit/{id}")
//    public String edit(@PathVariable String id, Model model) {
//        model.addAttribute("mechanic", mechanicService.findById(Long.valueOf(id)));
//        System.out.println(mechanicService.findById(Long.valueOf(id)));
//        return "mechanicForm";
//    }
//
//    @RequestMapping("/delete/{id}")
//    public String deleteById(@PathVariable String id){
//        mechanicService.deleteById(Long.valueOf(id));
//        return "redirect:/mechanic";
//    }
}
