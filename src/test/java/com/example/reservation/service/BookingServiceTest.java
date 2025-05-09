package com.example.reservation.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.reservation.model.Booking;
import com.example.reservation.model.Bus;
import com.example.reservation.repository.BookingRepository;
import com.example.reservation.repository.BusRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void testCheckSeatAvailability() {
        LocalDate journeyDate = LocalDate.of(2025, 5, 9);
// Create a mock bus
        Bus bus = mock(Bus.class);
        when(bus.getTotalSeats()).thenReturn(40);
        // Create the first booking with 10 booked seats
        Booking booking1 = new Booking();
        booking1.setSeatsBooked(10);
        booking1.setBus(bus);
        booking1.setJourneyDate(journeyDate);

        // Mock the repository to return a list with this booking
        when(bookingRepository.findByBusAndJourneyDate(bus, journeyDate))
                .thenReturn(List.of(booking1)); // Return the list with one booking

        // Now check availability with a requested 30 seats
        boolean result = bookingService.checkSeatAvailability(bus, journeyDate, 30);

        // Assert that there are enough seats
        assertTrue(result, "There should be enough seats available.");
    }

}
