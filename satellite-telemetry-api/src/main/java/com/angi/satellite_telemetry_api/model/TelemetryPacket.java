package com.angi.satellite_telemetry_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryPacket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String satelliteId;

    private Instant timestamp;

    private double batteryLevel;

    private double temperature;

    // Tells JPA to include in the same row the roll, pitch and yaw fields instead of creating a new table.
    @Embedded
    private Orientation orientation;

    // Store the status as a string (NOMINAL, WARNING, CRITICAL) in the database.
    @Enumerated(EnumType.STRING)
    private TelemetryStatus status;
}
