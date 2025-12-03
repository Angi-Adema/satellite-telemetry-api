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
  - Detect **anomalies**:
    - Low battery  
    - High temperature  
    - Critical state  

The goal is to demonstrate **Java, Spring Boot, REST APIs, JPA**, and mission-style telemetry monitoring.

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
A JPA entity representing a single telemetry snapshot:

- `batteryLevel`
- `temperature`
- `orientation` (embedded: roll, pitch, yaw)
- `status` (enum)
- `timestamp` (Instant, UTC)

---

### **TelemetryStatus**

Enum describing packet health:

- `NOMINAL`  
- `WARNING`  
- `CRITICAL`

---

### **Orientation**

Value object representing spacecraft attitude:

- `roll`
- `pitch`
- `yaw`

Stored in `TelemetryPacket` via `@Embeddable`.

---

### **TelemetryPacketRepository**

Spring Data JPA repository with:

- `findBySatelliteId(String satelliteId)`
- `findBySatelliteIdAndTimestampBetween(String id, Instant from, Instant to)`
- `findByTimestampAfter(Instant since)`

---

### **TelemetryService**

Core application logic handler:

- Telemetry ingestion  
- Satellite list generation  
- Latest status detection  
- Time-window history queries  
- Anomaly detection  

---

### **Anomaly**

DTO representing detected issues such as:

- `LOW_BATTERY`  
- `HIGH_TEMPERATURE`  
- `CRITICAL_STATE`  

---

## 🛠 Tech Stack

- **Java:** 21  
- **Framework:** Spring Boot 4.x  
- **Database:** H2 (in-memory)  
- **Persistence:** Spring Data JPA  
- **Build Tool:** Maven  
- **Additional:** Lombok  

---

## 📡 Domain Model Example

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
**Notes**
- `timestamp` is UTC (`Instant`)
- `status` is an enum: `NOMINAL`, `WARNING`, `CRITICAL`
- `orientation` is embedded as a component in the entity

---

# 🔗 REST Endpoints

**Base path:** `/api`

---

## **1. Health Check**

**GET** `/api/health`

**Response:**

```json
{ "status": "UP" }
```

---

## **2. Upload Telemetry**

**POST** `/api/telemetry/upload`

Upload a batch of telemetry packets.

**Request:**

```json
[
  {
    "satelliteId": "SAT-001",
    "timestamp": "2025-12-02T10:15:30Z",
    "batteryLevel": 82.5,
    "temperature": 15.2,
    "orientation": { "roll": 0.1, "pitch": -0.2, "yaw": 1.5 },
    "status": "NOMINAL"
  }
]
```

**Response:**

```json
{ "received": 1, "stored": 1 }
```

---

## **3. List Satellites (Latest Status)**

**GET** `/api/satellites`

Returns summary of each satellite’s latest contact and health:

```json
[
  {
    "satelliteId": "SAT-001",
    "lastContact": "2025-12-02T10:16:30Z",
    "lastStatus": "NOMINAL"
  },
  {
    "satelliteId": "SAT-002",
    "lastContact": "2025-12-02T10:21:00Z",
    "lastStatus": "WARNING"
  }
]
```

---

## **4. Latest Status for One Satellite**

**GET** `/api/satellites/{id}/status`

```json
{
  "id": 2,
  "satelliteId": "SAT-001",
  "timestamp": "2025-12-02T10:16:30Z",
  "batteryLevel": 82.0,
  "temperature": 15.0,
  "orientation": { "roll": 0.0, "pitch": -0.1, "yaw": 1.4 },
  "status": "NOMINAL"
}
```

Returns **404** if satellite has no telemetry.

---

## **5. Telemetry History (Time Range)**

**GET** `/api/satellites/{id}/telemetry?from=...&to=...`

```json
[
  {
    "id": 1,
    "satelliteId": "SAT-001",
    "timestamp": "2025-12-02T10:15:30Z",
    "batteryLevel": 82.5,
    "temperature": 15.2,
    "orientation": { "roll": 0.1, "pitch": -0.2, "yaw": 1.5 },
    "status": "NOMINAL"
  },
  {
    "id": 2,
    "satelliteId": "SAT-001",
    "timestamp": "2025-12-02T10:16:30Z",
    "batteryLevel": 82.0,
    "temperature": 15.0,
    "orientation": { "roll": 0.0, "pitch": -0.1, "yaw": 1.4 },
    "status": "NOMINAL"
  }
]
```

---

## **6. Anomaly Detection**

**GET** `/api/telemetry/anomalies?since=...`

Rules:

- `LOW_BATTERY` — battery < 30  
- `HIGH_TEMPERATURE` — temp > 40  
- `CRITICAL_STATE` — status == CRITICAL  

```json
[
  {
    "satelliteId": "SAT-001",
    "timestamp": "2025-12-02T10:20:00Z",
    "type": "LOW_BATTERY",
    "batteryLevel": 22.5,
    "temperature": null
  },
  {
    "satelliteId": "SAT-002",
    "timestamp": "2025-12-02T10:21:00Z",
    "type": "HIGH_TEMPERATURE",
    "batteryLevel": null,
    "temperature": 50.2
  },
  {
    "satelliteId": "SAT-003",
    "timestamp": "2025-12-02T10:22:00Z",
    "type": "CRITICAL_STATE",
    "batteryLevel": 40.0,
    "temperature": 20.0
  }
]
```

---

# 🧪 Example curl Commands

### Upload telemetry
```bash
curl -X POST http://localhost:8080/api/telemetry/upload \
  -H "Content-Type: application/json" \
  -d '[
    {
      "satelliteId":"SAT-001",
      "timestamp":"2025-12-02T10:15:30Z",
      "batteryLevel":82.5,
      "temperature":15.2,
      "orientation":{"roll":0.1,"pitch":-0.2,"yaw":1.5},
      "status":"NOMINAL"
    }
  ]'
```

### List satellites
```bash
curl http://localhost:8080/api/satellites
```

### Latest status
```bash
curl http://localhost:8080/api/satellites/SAT-001/status
```

### History
```bash
curl "http://localhost:8080/api/satellites/SAT-001/telemetry?from=2025-12-02T10:00:00Z&to=2025-12-02T11:00:00Z"
```

### Anomalies
```bash
curl "http://localhost:8080/api/telemetry/anomalies?since=2025-12-02T10:00:00Z"
```

---

# 🏃 Local Development

### Prerequisites
- Java **21+**  
- Maven or `./mvnw`  

### Run the application
```bash
./mvnw spring-boot:run
```

### Base URL
```
http://localhost:8080
```

### Health Check
```
http://localhost:8080/api/health
```

Expected:
```json
{ "status": "UP" }
```

---








