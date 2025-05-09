package com.example.reservation.controller;

import com.example.reservation.model.Booking;
import com.example.reservation.service.BookingService;
import com.example.reservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking Management", description = "Operations related to booking management")
public class BookingRestController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/my-bookings")
    @Operation(summary = "Get all bookings for a specific user", description = "Provide a username to fetch all bookings for the user")
    public List<Booking> myBookings(
            @Parameter(description = "Username of the user whose bookings are to be fetched", required = true)
            @RequestParam String username) {
        return bookingService.getUserBookings(userService.findByUsername(username));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking details by ID", description = "Provide a booking ID to fetch the booking details")
    public Booking getBookingDetails(
            @Parameter(description = "ID of the booking", required = true)
            @PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new booking", description = "Create a new booking with the provided details")
    public Booking createBooking(
            @Parameter(description = "Booking details to be created", required = true)
            @RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing booking", description = "Provide the booking ID and updated booking details")
    public Booking updateBooking(
            @Parameter(description = "ID of the booking to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated booking details", required = true)
            @RequestBody Booking booking) {
        booking.setId(id);
        return bookingService.save(booking);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a booking by ID", description = "Provide the booking ID to delete the booking")
    public void deleteBooking(
            @Parameter(description = "ID of the booking to be deleted", required = true)
            @PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
