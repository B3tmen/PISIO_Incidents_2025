package org.unibl.etf.analyticsservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.analyticsservice.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/by-type")
    public ResponseEntity<?> byType() {
        return ResponseEntity.ok(service.getIncidentCountByType());
    }

    @GetMapping("/by-day")
    public ResponseEntity<?> byDay() {
        return ResponseEntity.ok(service.getIncidentCountByDay());
    }

    @GetMapping("/by-location")
    public ResponseEntity<?> byLocation() {
        return ResponseEntity.ok(service.getIncidentCountByLocation());
    }
}
