// This file serves as a DTO (data transfer object) that represents a detected problem in the telemetry stream.
package com.angi.satellite_telemetry_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anomaly {
    
    private String satelliteId;

    private Instant timestamp;

    // Type of anomaly (LOW_BATTERY, HIGH_TEMPERATURE, CRITICAL_STATE, etc.)
    private String type;

    // Battery level at the time of the anomaly (if applicable)
    private Double batteryLevel;

    // Temperature at the time of the anomaly (if applicable)
    private Double temperature;

}
