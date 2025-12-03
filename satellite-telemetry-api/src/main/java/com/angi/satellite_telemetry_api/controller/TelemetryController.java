package com.angi.satellite_telemetry_api.controller;

import com.angi.satellite_telemetry_api.model.TelemetryPacket;
import com.angi.satellite_telemetry_api.service.SatelliteSummary;
import com.angi.satellite_telemetry_api.service.TelemetryService;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class TelemetryController {
    
    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    // Health check to be sure the app is running.
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

  
    // /**
    //  * Upload a batch of telemetry packets.
    //  *
    //  * Example request JSON:
    //  * [
    //  *   {
    //  *     "timestamp": "2025-12-02T10:15:30Z",
    //  *     "satelliteId": "SAT-001",
    //  *     "batteryLevel": 82.5,
    //  *     "temperature": 15.2,
    //  *     "orientation": { "roll": 0.1, "pitch": -0.2, "yaw": 1.5 },
    //  *     "status": "NOMINAL"
    //  *   }
    //  * ]
    //  */


    @PostMapping("/telemetry/upload")
    public ResponseEntity<Map<String, Integer>> uploadTelemetry(
            @RequestBody List<TelemetryPacket> packets
    ) {
        int stored = telemetryService.ingestTelemetry(packets);
        return ResponseEntity.ok(
                Map.of("received", packets.size(), "stored", stored)
        );
    }

    // List all satellites that have telemetry in the system with their latest contact time and status.
    
    @GetMapping("/satellites")
    public List<SatelliteSummary> listSatellites() {
        return telemetryService.listSatellites();
    }

    // Get the latest telemetry packet for one satellite.

    @GetMapping("/satellites/{id}/status")
    public ResponseEntity<TelemetryPacket> getLatestStatus(
            @PathVariable("id") String satelliteId
    ) {
        Optional<TelemetryPacket> latest = telemetryService.getLatestStatus(satelliteId);
        return latest.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // /**
    //  * Get telemetry history for a satellite between two timestamps.
    //  *
    //  * Example:
    //  * GET /api/satellites/SAT-001/telemetry?from=2025-12-02T10:00:00Z&to=2025-12-02T11:00:00Z
    //  */

    @GetMapping("/satellites/{id}/telemetry")
    public List<TelemetryPacket> getTelemetryHistory(
            @PathVariable("id") String satelliteId,
            @RequestParam("from") Instant from,
            @RequestParam("to") Instant to
    ) {
        return telemetryService.getTelemetryHistory(satelliteId, from, to);
    }

    // /**
    //  * Get anomalies detected in telemetry since a given timestamp.
    //  *
    //  * Example:
    //  * GET /api/telemetry/anomalies?since=2025-12-02T09:00:00Z
    //  */
    // @GetMapping("/telemetry/anomalies")
    // public List<Anomaly> getAnomalies(
    //         @RequestParam("since") Instant since
    // ) {
    //     return telemetryService.getAnomalies(since);
    // }
    
}
