/*package com.example.reservation.service;

// In src/main/java/com/example/reservation/service/ETicketService.java


import com.example.reservation.model.Booking;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ETicketService {

    private final BookingService bookingService;

    public ETicketService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public Map<String, Object> getETicketData(Long bookingId) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            // Handle booking not found exception appropriately
            return null; // Or throw an exception
        }

        Map<String, Object> model = new HashMap<>();
        model.put("booking", booking);
        model.put("passengers", booking.getPassengers());
        model.put("bus", booking.getBus());
        model.put("user", booking.getUser());

        return model;
    }

    // Method to generate PDF e-ticket (using iText or PDFBox) would go here
    // public byte[] generatePdfETicket(Long bookingId) { ... }
}*/
package com.example.reservation.service;
import com.example.reservation.model.Booking;
import com.example.reservation.model.Passenger;
import com.example.reservation.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ETicketService {

    private static final Logger logger = LoggerFactory.getLogger(ETicketService.class);
    private final BookingService bookingService;

    public ETicketService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public Map<String, Object> getETicketData(Long bookingId) {
        Booking booking = bookingService.findBookingById(bookingId);
        if (booking == null) {
            return null;
        }

        List<Passenger> passengers = booking.getPassengers();
        logger.info("Number of passengers for booking ID {}: {}", bookingId, (passengers != null ? passengers.size() : 0));

        Map<String, Object> model = new HashMap<>();
        model.put("booking", booking);
        model.put("passengers", passengers);
        model.put("bus", booking.getBus());
        model.put("user", booking.getUser());

        return model;
    }
}