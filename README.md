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

