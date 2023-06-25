package org.example.external.rest.controllers;

import org.example.domain.application.FlightService;
import org.example.domain.model.Flight;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightController {

    final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/flights")
    public List<Flight> getFlights(
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return this.service.searchAll(limit, offset);
    }

    @PostMapping("/api/v1/flights")
    public Boolean postFlights() {
        this.service.createSampleFlights();
        return true;
    }
}
