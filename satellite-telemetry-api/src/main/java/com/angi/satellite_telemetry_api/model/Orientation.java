// Sub-class representing the orientation (attitude) of the satellite.
package com.angi.satellite_telemetry_api.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orientation {

    private double roll;
    private double pitch;
    private double yaw;
}

// Attitude is the orientation of the satellite in 3D space, typically represented by roll, pitch, and yaw angles.
