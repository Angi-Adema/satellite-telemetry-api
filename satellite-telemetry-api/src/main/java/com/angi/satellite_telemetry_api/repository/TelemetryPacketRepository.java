package com.angi.satellite_telemetry_api.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.angi.satellite_telemetry_api.model.TelemetryPacket;

@Repository
public interface TelemetryPacketRepository extends JpaRepository<TelemetryPacket, Long> {
    // Find all the packets for a given satellite ID
    List<TelemetryPacket> findBySatelliteId(String satelliteId);

    // Find packets for a satellite between two timestamps
    List<TelemetryPacket> findBySatelliteIdAndTimestampBetween(
        String satelliteId, 
        Instant start,
        Instant end
    );

    // Find packets with a specific time for anomaly detection
    List<TelemetryPacket> findByTimestampAfter(Instant since);
}
