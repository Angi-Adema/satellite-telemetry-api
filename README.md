# Satellite Telemetry API

A Spring Boot–based **satellite telemetry ground system** that simulates how mission operators ingest, store, query, and analyze spacecraft health data.

Built as a portfolio project to demonstrate backend and data modeling skills for **space and defense systems** (e.g., Lockheed Martin Space).

---

## 🛰 Project Overview

This service models a simplified **satellite ground segment**:

- Accepts **telemetry packets** from multiple satellites
- Stores them in a **relational database** (H2 + JPA)
- Exposes REST endpoints to:
  - Upload telemetry in batches
  - List all satellites and their latest status
  - Query telemetry history in a time window
  - Detect **anomalies** in recent telemetry:
    - Low battery
    - High temperature
    - Critical status

The goal is to show familiarity with **Java, Spring Boot, REST APIs, JPA, and mission-style status monitoring**.

---

## 🧱 Architecture

```text
[ Client / Postman / curl ]
            |
            v
      [ REST Controller ]
            |
            v
      [ TelemetryService ]
            |
            v
[ TelemetryPacketRepository (JPA) ]
            |
            v
       [ H2 Database ]
```
---

## 📦 Key Components

### **TelemetryPacket**
A JPA entity representing a single telemetry snapshot including:

- `batteryLevel`
- `temperature`
- `orientation` (roll, pitch, yaw)
- `status` (enum)
- `timestamp` (Instant, UTC)

---

### **TelemetryStatus**
Enum describing the health state of a telemetry packet:

- `NOMINAL`
- `WARNING`
- `CRITICAL`

---

### **Orientation**
An embedded value object modeling spacecraft attitude:

- `roll`
- `pitch`
- `yaw`

Stored inside the `TelemetryPacket` entity using `@Embeddable`.

---

### **TelemetryPacketRepository**
A Spring Data JPA repository handling queries such as:

- `findBySatelliteId(String satelliteId)`
- `findBySatelliteIdAndTimestampBetween(String id, Instant from, Instant to)`
- `findByTimestampAfter(Instant since)`

---

### **TelemetryService**
Contains all core application logic:

- Telemetry ingestion  
- Satellite listing  
- Latest status lookup  
- Time-range history queries  
- Anomaly detection logic  

---

### **Anomaly**
A DTO representing a detected issue in the telemetry stream.

Examples:

- `LOW_BATTERY`
- `HIGH_TEMPERATURE`
- `CRITICAL_STATE`

---

## 🛠 Tech Stack

- **Language:** Java 21  
- **Framework:** Spring Boot 4.x  
- **Database:** H2 (in-memory)  
- **Persistence:** Spring Data JPA  
- **Build Tool:** Maven  
- **Additional:** Lombok for reducing boilerplate  

---

## 📡 Domain Model (`TelemetryPacket`)

Each telemetry packet represents one snapshot of satellite health.

```json
{
  "satelliteId": "SAT-001",
  "timestamp": "2025-12-02T10:16:30Z",
  "batteryLevel": 82.0,
  "temperature": 15.0,
  "orientation": {
    "roll": 0.0,
    "pitch": -0.1,
    "yaw": 1.4
  },
  "status": "NOMINAL"
}
```







