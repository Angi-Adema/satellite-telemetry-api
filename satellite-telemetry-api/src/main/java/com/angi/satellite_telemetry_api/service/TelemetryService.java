package com.angi.satellite_telemetry_api.service;

import com.angi.satellite_telemetry_api.model.Anomaly;
import com.angi.satellite_telemetry_api.model.TelemetryPacket;
import com.angi.satellite_telemetry_api.model.TelemetryStatus;
import com.angi.satellite_telemetry_api.repository.TelemetryPacketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelemetryService {
    
    private final TelemetryPacketRepository telemetryPacketRepository;

    // Ingest and store a batch of telemetry packets.
    //@param packets list of telemetry packets from the request
    //@return number of packets successfully stored

    public int ingestTelemetry(List<TelemetryPacket> packets) {
        List<TelemetryPacket> savedPackets = telemetryPacketRepository.saveAll(packets);
        return savedPackets.size();
    }

    //  /**
    //  * Get a summary of all satellites that have sent telemetry.
    //  *
    //  * Returns one entry per satellite with lastContact + lastStatus.
    //  */
    // public List<SatelliteSummary> listSatellites() {
    //     List<TelemetryPacket> allPackets = telemetryPacketRepository.findAll();

    //     // Group by satelliteId
    //     Map<String, List<TelemetryPacket>> bySatellite =
    //             allPackets.stream()
    //                       .collect(Collectors.groupingBy(TelemetryPacket::getSatelliteId));

    //     List<SatelliteSummary> summaries = new ArrayList<>();

    //     for (Map.Entry<String, List<TelemetryPacket>> entry : bySatellite.entrySet()) {
    //         String satelliteId = entry.getKey();
    //         List<TelemetryPacket> packets = entry.getValue();

    //         // Latest packet by timestamp
    //         TelemetryPacket latest = packets.stream()
    //                 .max(Comparator.comparing(TelemetryPacket::getTimestamp))
    //                 .orElse(null);

    //         if (latest != null) {
    //             summaries.add(new SatelliteSummary(
    //                     satelliteId,
    //                     latest.getTimestamp(),
    //                     latest.getStatus()
    //             ));
    //         }
    //     }

    //     // Optional: sort by satelliteId or lastContact descending
    //     summaries.sort(Comparator.comparing(SatelliteSummary::getSatelliteId));
    //     return summaries;
    // }

    // /**
    //  * Get the latest telemetry packet for a given satellite, if any.
    //  */
    // public Optional<TelemetryPacket> getLatestStatus(String satelliteId) {
    //     List<TelemetryPacket> packets = telemetryPacketRepository.findBySatelliteId(satelliteId);

    //     return packets.stream()
    //             .max(Comparator.comparing(TelemetryPacket::getTimestamp));
    // }

    // /**
    //  * Get telemetry history for a satellite between two timestamps.
    //  */
    // public List<TelemetryPacket> getTelemetryHistory(String satelliteId, Instant from, Instant to) {
    //     return telemetryPacketRepository.findBySatelliteIdAndTimestampBetween(
    //             satelliteId, from, to
    //     );
    // }

    // /**
    //  * Scan recent telemetry and detect anomalies, e.g.
    //  * - LOW_BATTERY if batteryLevel < 30
    //  * - HIGH_TEMPERATURE if temperature > 40
    //  * - CRITICAL_STATE if status == CRITICAL
    //  */
    // public List<Anomaly> getAnomalies(Instant since) {
    //     List<TelemetryPacket> recentPackets =
    //             telemetryPacketRepository.findByTimestampAfter(since);

    //     List<Anomaly> anomalies = new ArrayList<>();

    //     for (TelemetryPacket packet : recentPackets) {
    //         // Low battery
    //         if (packet.getBatteryLevel() < 30.0) {
    //             anomalies.add(new Anomaly(
    //                     packet.getSatelliteId(),
    //                     packet.getTimestamp(),
    //                     "LOW_BATTERY",
    //                     packet.getBatteryLevel(),
    //                     null
    //             ));
    //         }

    //         // High temperature
    //         if (packet.getTemperature() > 40.0) {
    //             anomalies.add(new Anomaly(
    //                     packet.getSatelliteId(),
    //                     packet.getTimestamp(),
    //                     "HIGH_TEMPERATURE",
    //                     null,
    //                     packet.getTemperature()
    //             ));
    //         }

    //         // Critical state
    //         if (packet.getStatus() == TelemetryStatus.CRITICAL) {
    //             anomalies.add(new Anomaly(
    //                     packet.getSatelliteId(),
    //                     packet.getTimestamp(),
    //                     "CRITICAL_STATE",
    //                     packet.getBatteryLevel(),
    //                     packet.getTemperature()
    //             ));
    //         }
    //     }

    //     return anomalies;
    // }

}
