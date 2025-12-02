package com.angi.satellite_telemetry_api.service;

import com.angi.satellite_telemetry_api.model.TelemetryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Simple summary view of a satellite's latest known state.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteSummary {

    private String satelliteId;
    private Instant lastContact;
    private TelemetryStatus lastStatus;
}
