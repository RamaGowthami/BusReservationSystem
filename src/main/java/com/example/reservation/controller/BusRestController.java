package com.example.reservation.controller;

import com.example.reservation.model.Bus;
import com.example.reservation.service.BusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@Tag(name = "Bus Operations", description = "Operations related to bus data")
public class BusRestController {

    @Autowired
    private BusService busService;

    @GetMapping
    @Operation(summary = "Get all buses", description = "Retrieve all buses in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all buses"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<Bus> getAllBuses() {
        return busService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific bus by ID", description = "Retrieve bus details based on the bus ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved bus"),
            @ApiResponse(responseCode = "404", description = "Bus not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Bus getBusById(
            @Parameter(description = "ID of the bus to retrieve", required = true)
            @PathVariable Long id) {
        return busService.findById(id);
    }
}
