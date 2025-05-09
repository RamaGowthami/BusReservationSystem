/*/
/*
*/
/*

*/
package com.example.reservation.controller;

import com.example.reservation.model.Booking;
import com.example.reservation.model.Bus;
import com.example.reservation.model.Passenger;
import com.example.reservation.model.User;
import com.example.reservation.service.BookingService;
import com.example.reservation.service.BusService;
import com.example.reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.slf4j.Logger; // Import the Logger interface
import org.slf4j.LoggerFactory;
import com.example.reservation.exception.RouteNotFoundException;
import com.example.reservation.dto.PassengerListWrapper;

@Controller
@RequestMapping("/bus")
public class BusController {

    @Autowired
    private BusService busService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(BusController.class); // Initialize the logger

    @GetMapping("/search")
    public String showSearchForm() {
        return "bus_search_form";
    }

    @GetMapping("/search/results")
    public String searchBuses(@RequestParam String source,
                              @RequestParam String destination,
                              @RequestParam int availableSeats,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate journeyDate,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            List<Bus> searchResults = busService.findAvailableBuses(source, destination, availableSeats, journeyDate);
            if (searchResults == null || searchResults.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No buses found matching your criteria.");
                return "redirect:/bus/search"; // Redirect back to the search form
            }
            model.addAttribute("buses", searchResults);
            model.addAttribute("searchCriteria", Map.of(
                    "source", source,
                    "destination", destination,
                    "availableSeats", availableSeats,
                    "journeyDate", journeyDate
            ));
            return "bus_list";
        } catch (IllegalArgumentException e) {
            // Handle specific exceptions from the service layer
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bus/search";
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/bus/search";
        }
    }

    @GetMapping("/list")
    public String listBuses(Model model) {
        try {
            model.addAttribute("buses", busService.findAll());
            return "bus_list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            return "error"; //create a generic error page.
        }
    }

    @GetMapping("/book/{id}")
    public String showBookingForm(@PathVariable Long id, Model model) {
        Bus bus = busService.findById(id);
        if (bus == null) {
            return "error";
        }
        List<String> bookedSeats = bookingService.getBookedSeats(bus, LocalDate.now());
        model.addAttribute("bus", bus);
        model.addAttribute("bookedSeats", bookedSeats);
        return "book_bus";
    }

    @PostMapping("/book-seats")
    public RedirectView bookSeats(@RequestParam Long busId,
                                  @RequestParam String selectedSeats,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate journeyDate,
                                  Principal principal) {

        User user = userService.findByUsername(principal.getName());
        Bus bus = busService.findById(busId);
        List<String> seatsList = Arrays.asList(selectedSeats.split(","));

        if (!bookingService.areSeatsAvailable(bus, journeyDate, seatsList)) {
            return new RedirectView("/bus/book/" + busId + "?error=seats_unavailable");
        }

        Booking booking = new Booking();
        booking.setBus(bus);
        booking.setUser(user);
        booking.setSeatsBooked(seatsList.size());
        booking.setSeatNumber(selectedSeats);
        booking.setJourneyDate(journeyDate);

        Booking savedBooking = bookingService.bookBus(booking);

        RedirectView redirectView = new RedirectView("/bus/register-passengers/" + savedBooking.getId());
        // No need to add model attribute for RedirectView when constructing the URL directly

        return redirectView;
    }

    @PostMapping("/book")
    public String bookBus(@RequestParam Long busId,
                          @RequestParam int seats,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate journeyDate,
                          Principal principal,
                          Model model) {

        User user = userService.findByUsername(principal.getName());
        Bus bus = busService.findById(busId);

        if (!bookingService.checkSeatAvailability(bus, journeyDate, seats)) {
            model.addAttribute("error", "Not enough seats available.");
            model.addAttribute("busId", busId);
            return "book_bus";
        }

        Booking booking = new Booking();
        booking.setBus(bus);
        booking.setUser(user);
        booking.setSeatsBooked(seats);
        booking.setJourneyDate(journeyDate);

        Booking savedBooking = bookingService.bookBus(booking);
        return "redirect:/bus/register-passengers/" + savedBooking.getId();
    }

    /*@GetMapping("/register-passengers/{bookingId}")
    public String showPassengerRegistrationForm(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            return "error";
        }
        model.addAttribute("booking", booking);
        int numberOfPassengers = booking.getSeatsBooked();
        model.addAttribute("numberOfPassengers", numberOfPassengers);

        // Create a list to hold the passenger details for the form
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfPassengers; i++) {
            passengers.add(new Passenger()); // Add empty Passenger objects
        }
        model.addAttribute("passengers", passengers); // Add the passengers list to the model

        return "passenger_registration_form";
    }*/
    @GetMapping("/register-passengers/{bookingId}")
    public String showPassengerRegistrationForm(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            return "error";
        }
        model.addAttribute("booking", booking);
        int numberOfPassengers = booking.getSeatsBooked();
        model.addAttribute("numberOfPassengers", numberOfPassengers);

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfPassengers; i++) {
            passengers.add(new Passenger());
        }

        PassengerListWrapper passengerListWrapper = new PassengerListWrapper();
        passengerListWrapper.setPassengers(passengers);

        model.addAttribute("passengerListWrapper", passengerListWrapper); // Add the wrapper to the model
        return "passenger_registration_form";
    }



    /*@PostMapping("/register-passengers/{bookingId}")
    public String registerPassengers(@PathVariable Long bookingId,
                                     @ModelAttribute("passengerListWrapper") PassengerListWrapper passengerListWrapper) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            return "error";
        }

        List<Passenger> passengers = passengerListWrapper.getPassengers();
        if (passengers != null) {
            for (Passenger passenger : passengers) {
                passenger.setBooking(booking);
                logger.info("Saving passenger: Name={}, Age={}, Gender={}, Mobile={}, Booking ID={}",
                        passenger.getName(), passenger.getAge(), passenger.getGender(), passenger.getMobileNumber(), bookingId);
            }
            bookingService.savePassengers(booking, passengers);
        }

       // return "redirect:/booking/eticket/" + bookingId;
        //bookingService.savePassengers(booking, passengers);

        return "redirect:/booking/options?bookingId=" + bookingId;
        // Redirect to the booking options page instead of the e-ticket

    }*/
    @PostMapping("/register-passengers/{bookingId}")
    public String registerPassengers(@PathVariable Long bookingId,
                                     @ModelAttribute("passengerListWrapper") PassengerListWrapper passengerListWrapper,
                                     Model model) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found for ID: " + bookingId);
            return "error";
        }

        List<Passenger> passengers = passengerListWrapper.getPassengers();
        if (passengers == null || passengers.isEmpty()) {
            model.addAttribute("error", "No passenger data received.");
            return "error";
        }

        for (Passenger passenger : passengers) {
            passenger.setBooking(booking);
            logger.info("Saving passenger: Name={}, Age={}, Gender={}, Mobile={}, Booking ID={}",
                    passenger.getName(), passenger.getAge(), passenger.getGender(),
                    passenger.getMobileNumber(), bookingId);
        }

        bookingService.savePassengers(booking, passengers);

        return "redirect:/booking/options?bookingId=" + bookingId;
    }




    @GetMapping("/bus/select-seat")
    public String selectSeat(@RequestParam Long busId,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate journeyDate,
                             Model model) {

        Bus bus = busService.findById(busId);
        List<String> bookedSeats = bookingService.getBookedSeats(bus, journeyDate);

        model.addAttribute("bus", bus);
        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("journeyDate", journeyDate);
        return "book_bus";
    }

    @PostMapping("/bus/book-seat")
    public String bookSeat(@RequestParam Long busId,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate journeyDate,
                           @RequestParam String selectedSeat,
                           Principal principal,
                           Model model) {

        Bus bus = busService.findById(busId);
        User user = userService.findByUsername(principal.getName());

        if (!bookingService.isSeatAvailable(bus, journeyDate, selectedSeat)) {
            model.addAttribute("error", "Seat already booked.");
            return "book_bus";
        }

        Booking booking = new Booking();
        booking.setBus(bus);
        booking.setUser(user);
        booking.setJourneyDate(journeyDate);
        booking.setSeatNumber(selectedSeat);
        booking.setSeatsBooked(1);

        bookingService.bookBus(booking);

        return "redirect:/booking/options?bookingId=" + booking.getId();
    }

    @GetMapping("/my-bookings")
    public String myBookings(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Booking> bookings = bookingService.findBookingsByUser(user);
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }

    @GetMapping("/seats")
    @ResponseBody
    public List<String> getBookedSeats(@RequestParam Long busId,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate journeyDate) {
        return bookingService.getBookedSeats(busId, journeyDate);
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Bus bus = busService.findById(id);
        if (bus == null) {
            return "error"; // Or handle not found scenario
        }
        model.addAttribute("bus", bus);
        return "bus_edit_form"; // Create this Thymeleaf template
    }

    @PostMapping("/edit/{id}")
    public String updateBus(@PathVariable Long id, @ModelAttribute("bus") Bus updatedBus, RedirectAttributes redirectAttributes) {
        Bus existingBus = busService.findById(id);
        if (existingBus == null) {
            return "error"; // Or handle not found
        }
        updatedBus.setId(id); // Ensure the ID is set for updating
        busService.save(updatedBus);
        redirectAttributes.addFlashAttribute("successMessage", "Bus details updated successfully!");
        return "redirect:/bus/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        busService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Bus deleted successfully!");
        return "redirect:/bus/list";
    }
}