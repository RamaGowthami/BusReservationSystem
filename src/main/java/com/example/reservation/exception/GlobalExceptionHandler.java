package com.example.reservation.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSearchCriteriaException.class)
    public String handleInvalidSearchCriteriaException(InvalidSearchCriteriaException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/bus/search";
    }

    @ExceptionHandler(BusNotFoundException.class)
    public String handleBusNotFoundException(BusNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(SeatNotAvailableException.class)
    public String handleSeatNotAvailableException(SeatNotAvailableException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }


    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        return "error";
    }
    @ExceptionHandler(RouteNotFoundException.class)
    public String handleRouteNotFoundException(RouteNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/bus/search"; // Redirect to the search form
    }

}
