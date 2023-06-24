package org.example.external.rest.controllers;

import org.example.domain.application.FlightService;
import org.example.domain.model.Booking;
import org.example.domain.model.Flight;
import org.example.domain.model.Route;
import org.example.domain.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class FlightController {
    
    final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/flights")
    public List<Flight> getFlights() {
        return this.service.searchAll();
    }

    @PostMapping("/api/v1/flights")
    public Boolean postFlights() {
        this.service.createSampleFlights();
        return true;
    }
}
