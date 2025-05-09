package com.example.reservation.repository;

import com.example.reservation.model.Booking;
import com.example.reservation.model.Bus;
import com.example.reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);

    List<Booking> findByBusAndJourneyDate(Bus bus, LocalDate journeyDate);

    boolean existsByBusIdAndSeatNumberAndJourneyDate(Long busId, String seatNumber, LocalDate journeyDate);

    boolean existsByBusAndJourneyDateAndSeatNumber(Bus bus, LocalDate journeyDate, String seat);

    @Query("SELECT b.seatNumber FROM Booking b WHERE b.bus.id = :busId AND b.journeyDate = :date")
    List<String> findSeatNumbersByBusIdAndJourneyDate(@Param("busId") Long busId, @Param("date") LocalDate date);

    @Query("SELECT b.seatNumber FROM Booking b WHERE b.bus = :bus AND b.journeyDate = :journeyDate")
    List<String> findBookedSeats(@Param("bus") Bus bus, @Param("journeyDate") LocalDate journeyDate);
}
