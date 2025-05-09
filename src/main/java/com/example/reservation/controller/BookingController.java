package com.example.reservation.controller;

import com.example.reservation.model.Booking;
import com.example.reservation.model.Bus;
import com.example.reservation.model.User;
import com.example.reservation.repository.BookingRepository;
import com.example.reservation.service.BookingService;
import com.example.reservation.service.BusService;
import com.example.reservation.service.ETicketService;
import com.example.reservation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BusService busService;
    @Autowired
    private UserService userService;
    @Autowired
    private ETicketService eTicketService;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    @GetMapping("/my-bookings")
    public String myBookings(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Booking> bookings = bookingService.getUserBookings(user);
        //model.addAttribute("bookings", bookingService.getUserBookings(user));
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }
    @GetMapping("/bookings/{id}")
    public String getBookingDetails(@PathVariable Long id, Model model) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
        model.addAttribute("booking", booking);
        return "booking_detail"; // a new Thymeleaf template to show details
    }
    // Handle displaying the booking options page
    @GetMapping("/booking/options")
    public String bookingOptions(@RequestParam Long bookingId, Model model) {
        model.addAttribute("bookingId", bookingId);
        return "booking-options"; // your HTML view name
    }

    @PostMapping("/booking/confirm-booking")

    public String confirmBooking(@RequestParam(required = false) Long bookingId, Model model) {
        if (bookingId == null) {
            model.addAttribute("error", "Booking ID is missing.");
            return "error"; // or return to previous page
        }
        // your logic
        return "bookin-confirmation";
    }

    @PostMapping("/booking/confirm-booking-hold")

    public String holdBooking(@RequestParam Long bookingId, Model model) {
        // Logic to place hold (e.g., update booking status to "hold")
        model.addAttribute("bookingId", bookingId);
        return "bookin-confirmation"; // same or alternate page
    }
    @PostMapping("/booking/confirm-seat")
    public String confirmSeatSelection(@RequestParam("seat") String seat, Model model, Principal principal) {
        // Here, we would add logic to save the selected seat for the user
        // For now, let's just show the confirmation page with the selected seat
        model.addAttribute("seat", seat);
        return "seat-confirmation"; // You will create this confirmation page next
    }
    //added now...........................................
    @GetMapping("/bookings/edit/{id}")
    public String showEditBookingForm(@PathVariable Long id, Model model) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
        List<Bus> allBuses = busService.findAll(); // Fetch all buses
        model.addAttribute("booking", booking);
        model.addAttribute("allBuses", allBuses); // Add the list of buses to the model
        return "booking_edit_form";
    }

    /*@PostMapping("/bookings/edit/{id}")
    public String updateBooking(@PathVariable Long id, @ModelAttribute("booking") Booking updatedBooking, RedirectAttributes redirectAttributes) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
        updatedBooking.setId(id);
        bookingService.save(updatedBooking);
        redirectAttributes.addFlashAttribute("successMessage", "Booking updated successfully!");
        return "redirect:/my-bookings";
    }*/
    @PostMapping("/bookings/edit/{id}")
    public String updateBooking(@PathVariable Long id, @ModelAttribute("booking") Booking updatedBooking, RedirectAttributes redirectAttributes) {
        logger.info("Updating booking with ID: {}", id);

        // Log the details of the updated booking
        logger.info("Updated journey date: {}", updatedBooking.getJourneyDate());

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));

        // Make sure you're updating the existing booking with the new data
        existingBooking.setJourneyDate(updatedBooking.getJourneyDate());
        existingBooking.setSeatNumber(updatedBooking.getSeatNumber());

        // Save the updated booking
        bookingService.save(existingBooking);

        // Redirect to the 'my-bookings' page with a success message
        redirectAttributes.addFlashAttribute("successMessage", "Booking updated successfully!");

        return "redirect:/my-bookings";
    }

    @GetMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.deleteBooking(id);
        redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully!");
        return "redirect:/my-bookings";
    }
    @GetMapping("/booking/eticket/{bookingId}")
    public String showETicket(@PathVariable Long bookingId, Model model) {
        Map<String, Object> eTicketData = eTicketService.getETicketData(bookingId);
        if (eTicketData == null) {
            // Handle the case where the booking is not found
            model.addAttribute("errorMessage", "E-ticket not found for booking ID: " + bookingId);
            return "error"; // Or a specific error page
        }
        model.addAllAttributes(eTicketData);
        return "eticket"; // Render the eticket.html template
    }
}


