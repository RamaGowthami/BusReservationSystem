package com.example.reservation.service;

import com.example.reservation.exception.BusNotFoundException;
import com.example.reservation.exception.InvalidSearchCriteriaException;
import com.example.reservation.exception.RouteNotFoundException; // Import the new exception
import com.example.reservation.exception.InvalidSearchCriteriaException;
import com.example.reservation.exception.RouteNotFoundException;
import com.example.reservation.model.Bus;
import com.example.reservation.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;
    @Autowired
    private BookingService bookingService;

    public List<Bus> findAll() {
        return busRepository.findAll();  // Fetch all buses from the repository
    }

    public Bus findById(Long id) {
        return busRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bus not found"));
    }

    public Bus save(Bus bus) {
        return busRepository.save(bus);  // Save the bus and return it
    }

    public List<Bus> search(String source, String destination) {
        return busRepository.findBySourceAndDestination(source, destination);
    }

    public void delete(Long id) {
        busRepository.deleteById(id);
    }

    public List<Bus> findAvailableBuses(String source, String destination, int availableSeats, LocalDate journeyDate) {
        // 1. Input Validation
        if (source == null || source.trim().isEmpty() || destination == null || destination.trim().isEmpty()) {
            throw new InvalidSearchCriteriaException("Source and destination must be provided.");
        }
        if (availableSeats <= 0) {
            throw new InvalidSearchCriteriaException("Available seats must be greater than zero.");
        }

        // 2. Query the Database
        List<Bus> allBuses = busRepository.findBySourceAndDestination(source, destination);

        // 3. Check for Empty Result and Throw Exception
        if (allBuses.isEmpty()) {
            throw new RouteNotFoundException(
                    "No buses found for the specified route: " + source + " to " + destination);
                    //source,  // Pass the source
                    //destination);  // Pass the destination
                    //availableSeats, // Pass availableSeats
                    //journeyDate); // Pass journeyDate
        }

        // 4. Filter by Available Seats
        List<Bus> availableBuses = new ArrayList<>();
        for (Bus bus : allBuses) {
            int bookedSeats = bookingService.getBookedSeats(bus, journeyDate).size();
            if ((bus.getTotalSeats() - bookedSeats) >= availableSeats) {
                availableBuses.add(bus);
            }
        }
        return availableBuses;
    }
    //for restful Apis
}

