/*package com.example.reservation.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String source;
    private String destination;
    private int totalSeats;
    private LocalTime time_of_bus;



    private double price;

    // Getters and Setters
// Constructors
    public Bus() {
    }

    public Bus(String name, String source, String destination, int totalSeats) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
*/
package com.example.reservation.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String source;
    private String destination;
    private int totalSeats;
    private LocalTime time_of_bus;
    private double price;

    // Default constructor
    public Bus() {
    }

    // All-args constructor
    public Bus(String name, String source, String destination, int totalSeats, LocalTime time_of_bus, double price) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.time_of_bus = time_of_bus;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public LocalTime getTime_of_bus() {
        return time_of_bus;
    }

    public void setTime_of_bus(LocalTime time_of_bus) {
        this.time_of_bus = time_of_bus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

