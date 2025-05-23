package com.example.reservation.repository;



import com.example.reservation.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findBySourceAndDestination(String source, String destination);

}

