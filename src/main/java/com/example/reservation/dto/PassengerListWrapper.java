package com.example.reservation.dto;



import com.example.reservation.model.Passenger;
import java.util.ArrayList;
import java.util.List;

public class PassengerListWrapper {
    private List<Passenger> passengers = new ArrayList<>();

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}

