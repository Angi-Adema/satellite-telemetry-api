# 🛰️ Satellite Telemetry API

A mission-inspired backend system that simulates a simplified **satellite ground segment**, built with **Java 21**, **Spring Boot**, and **JPA**.  
Designed as a portfolio project demonstrating readiness for **space, defense, and mission assurance software engineering roles**.

---

## 🛰️ About This Project

Satellite Telemetry API models how a real ground system receives, stores, and analyzes spacecraft telemetry.  
It demonstrates my ability to design **high-reliability backend services** with clean architecture, data modeling, and mission-style workflows.

This project highlights my strengths in:

- Backend engineering with modern **Java & Spring Boot**
- Data modeling using **JPA + relational databases**
- Designing structured, maintainable **REST APIs**
- Thinking like a mission systems engineer (health checks, anomaly detection, status tracking)
- Understanding space operations concepts: telemetry, attitude, battery thermals, spacecraft state-of-health
- Building systems aligned with **mission assurance**, **operational safety**, and **ground software engineering**

This repository serves as a portfolio demonstration of my readiness to contribute to:

💠 **Lockheed Martin Space**  
💠 **Raytheon**  
💠 **Sierra Space**  
💠 **Ball Aerospace**  
💠 **Other space & defense mission software teams**

---

## 🧭 Project Overview

This service models a simplified **satellite telemetry ground system**:

- Ingests **telemetry packets** from multiple spacecraft
- Stores them using **Spring Data JPA** and an **H2 in-memory database**
- Exposes REST endpoints to:
  - Upload telemetry batches  
  - List all satellites and their latest status  
  - Query historical telemetry by time window  
  - Detect anomalies (battery, temperature, critical state)

This project demonstrates comfort with **real-time data workflows**, **mission operations concepts**, and **backend engineering for high-integrity systems**.

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

## 🛰 Lockheed / Space Systems Pitch

This project mirrors several core concepts used in real mission operations:

- Consuming spacecraft telemetry
- Deriving real-time health status
- Monitoring battery and thermal constraints
- Detecting off-nominal behavior
- Supporting operator dashboards and mission consoles

It demonstrates:

- Clean Java/Spring backend design
- Solid JPA-based data modeling
- Understanding of space operations workflows
- Ability to build maintainable, mission-relevant backend systems

This architecture aligns well with engineering work performed on:

- Ground systems  
- Autonomy frameworks  
- Mission software  
- Flight/ground integration  
- Spacecraft health and status pipelines  


## 🔭 Future Enhancements

- **Swagger / OpenAPI documentation**
- **API key authentication**
- **More anomaly types:**
  - Orientation drift (roll/pitch/yaw threshold)
  - Stale telemetry (no recent contact)
  - Rapid battery drain detection
- **JUnit / Mockito test suite**
- **React dashboard for visualization**







