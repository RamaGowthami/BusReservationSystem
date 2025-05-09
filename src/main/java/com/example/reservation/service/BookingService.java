/*package com.example.reservation.service;

import com.example.reservation.model.Booking;
import com.example.reservation.model.Bus;
import com.example.reservation.model.Passenger;
import com.example.reservation.model.User;
import com.example.reservation.repository.BookingRepository;
import com.example.reservation.repository.BusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepository;

    public Booking bookBus(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUser(user);
    }

    public boolean checkSeatAvailability(Bus bus, LocalDate journeyDate, int requestedSeats) {
        int bookedSeats = bookingRepository
                .findByBusAndJourneyDate(bus, journeyDate)
                .stream()
                .mapToInt(Booking::getSeatsBooked)
                .sum();
        return (bookedSeats + requestedSeats) <= bus.getTotalSeats();
    }

    public List<String> getBookedSeats(Long busId, LocalDate date) {
        return bookingRepository.findSeatNumbersByBusIdAndJourneyDate(busId, date);
    }

    public List<String> getBookedSeats(Bus bus, LocalDate date) {
        return bookingRepository.findBookedSeats(bus, date);
    }

    public boolean isSeatAvailable(Bus bus, LocalDate journeyDate, String seat) {
        return !bookingRepository.existsByBusAndJourneyDateAndSeatNumber(bus, journeyDate, seat);
    }

    public boolean isSeatAvailable(Long busId, String seat, LocalDate date) {
        return !bookingRepository.existsByBusIdAndSeatNumberAndJourneyDate(busId, seat, date);
    }

    public void bookSeat(User user, Long busId, String seat, LocalDate date) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBus(busRepository.findById(busId).orElseThrow());
        booking.setSeatNumber(seat);
        booking.setJourneyDate(date);
        booking.setSeatsBooked(1);
        bookingRepository.save(booking);
    }
    public List<Booking> findBookingsByUser(User user) {
        // Assuming you have a repository to fetch bookings by user
        return bookingRepository.findByUser(user);
    }
    //Added now.........................................................
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }


    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Transactional
    public void savePassengers(Booking booking, List<Passenger> passengers) {
        booking.setPassengers(passengers);
        bookingRepository.save(booking); // Saving the booking will cascade and save passengers
    }


}*/
package com.example.reservation.service;

import com.example.reservation.model.Booking;
import com.example.reservation.model.Bus;
import com.example.reservation.model.Passenger;
import com.example.reservation.model.User;
import com.example.reservation.repository.BookingRepository;
import com.example.reservation.repository.BusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepository;

    public Booking bookBus(Booking booking) {
        return bookingRepository.save(booking);
    }

    /*public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUser(user);
    }*/
    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUser(user);
    }

    public boolean checkSeatAvailability(Bus bus, LocalDate journeyDate, int requestedSeats) {
        int bookedSeats = bookingRepository
                .findByBusAndJourneyDate(bus, journeyDate)
                .stream()
                .mapToInt(Booking::getSeatsBooked)
                .sum();
        return (bookedSeats + requestedSeats) <= bus.getTotalSeats();
    }

    public List<String> getBookedSeats(Long busId, LocalDate date) {
        return bookingRepository.findSeatNumbersByBusIdAndJourneyDate(busId, date);
    }

    public List<String> getBookedSeats(Bus bus, LocalDate date) {
        return bookingRepository.findBookedSeats(bus, date);
    }

    public boolean isSeatAvailable(Bus bus, LocalDate journeyDate, String seat) {
        return !bookingRepository.existsByBusAndJourneyDateAndSeatNumber(bus, journeyDate, seat);
    }

    public boolean isSeatAvailable(Long busId, String seat, LocalDate date) {
        return !bookingRepository.existsByBusIdAndSeatNumberAndJourneyDate(busId, seat, date);
    }

    public void bookSeat(User user, Long busId, String seat, LocalDate date) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBus(busRepository.findById(busId).orElseThrow());
        booking.setSeatNumber(seat);
        booking.setJourneyDate(date);
        booking.setSeatsBooked(1);
        bookingRepository.save(booking);
    }
    public List<Booking> findBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);  // return the saved booking
    }


    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Transactional
    public void savePassengers(Booking booking, List<Passenger> newPassengers) {
        booking = bookingRepository.findById(booking.getId()).orElseThrow(() -> new RuntimeException("Booking not found")); // Re-fetch to ensure it's in the current session
        booking.getPassengers().clear(); // Remove existing passengers
        for (Passenger passenger : newPassengers) {
            passenger.setBooking(booking);
            booking.getPassengers().add(passenger);
        }
        bookingRepository.save(booking); // Save the updated booking
    }

    public boolean areSeatsAvailable(Bus bus, LocalDate journeyDate, List<String> seats) {
        for (String seat : seats) {
            if (bookingRepository.existsByBusAndJourneyDateAndSeatNumber(bus, journeyDate, seat)) {
                return false; // If any of the selected seats are already booked, return false
            }
        }
        return true; // All selected seats are available
    }
    //added for RestController




    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }



}
