package com.awbd.awbd.controller;

import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.exceptions.IllegalArgumentWithViewException;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handlerNotFoundException(Exception exception){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put("exception",exception);
        modelAndView.setViewName("notFoundException");
        return modelAndView;
    }

    @ExceptionHandler(EntityInUnfinishedAppointmentException.class)
    public String handleEntityInUse(EntityInUnfinishedAppointmentException ex,
                                    RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return switch (ex.getEntityName()) {
            case "Vehicle" -> "redirect:/vehicle";
            case "Service Type" -> "redirect:/service-type";
            default -> "redirect:/";
        };
    }

    @ExceptionHandler(IllegalArgumentWithViewException.class)
    public String handleIllegalArgumentWithView(IllegalArgumentWithViewException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute(ex.getAttributeName(), ex.getAttributeValue());
        return ex.getViewName();
    }
}