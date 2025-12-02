package com.angi.satellite_telemetry_api.controller;

import com.angi.satellite_telemetry_api.model.Anomaly;
import com.angi.satellite_telemetry_api.model.TelemetryPacket;
import com.angi.satellite_telemetry_api.service.SatelliteSummary;
import com.angi.satellite_telemetry_api.service.TelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TelemetryController {
    
    private final TelemetryService telemetryService;

    // Health check to be sure the app is running.
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
    
}
