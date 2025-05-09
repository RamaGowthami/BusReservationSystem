package com.example.reservation.repository;
import com.example.reservation.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PassengerRepository extends JpaRepository<Passenger, Long>
{
}


