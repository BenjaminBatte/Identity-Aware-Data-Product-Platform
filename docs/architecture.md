---

## title: Architecture

---

[🏠 Home](index.md) | [📖 Overview](overview.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

# Architecture

This document summarizes the current system architecture for the **Identity‑Aware Data Product Platform** backend and the planned Angular admin frontend. It will evolve as we implement features.

---

## Class Diagram (initial domain)

![Domain Class Diagram](diagrams/domain.png)

**Notes**

* `UserOrgRole` enables multi‑tenancy and role‑based access in an org context.
* `DatasetProfileRef` bridges SQL (Postgres) to profile documents stored in Mongo.

---

## Project Tree Structure (current)

```text
platform-api/
├── src/main/java/com/benjaminbatte/platform
│   ├── common/                   # Shared utilities & cross-cutting concerns
│   │   └── exception/            # Global exception handling
│   ├── config/                   # Spring Boot configuration classes
│   ├── security/                 # Authentication & authorization (Keycloak, JWT)
│   ├── features/                 # Domain feature modules
│   │   ├── dataset/              # Dataset management
│   │   │   ├── domain/           # JPA entities
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── mapper/           # DTO ↔ Entity mapping
│   │   │   ├── repo/             # Data repositories
│   │   │   ├── service/          # Business logic
│   │   │   └── web/              # REST controllers
│   │   ├── org/                  # Organization management
│   │   │   ├── domain/
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   ├── repo/
│   │   │   ├── service/
│   │   │   └── web/
│   │   └── user/                 # User management
│   │       ├── domain/
│   │       ├── dto/
│   │       ├── mapper/
│   │       ├── repo/
│   │       ├── service/
│   │       └── web/
│   ├── jobs/                     # Scheduled/background jobs
│   ├── metadata/                 # Document & metadata storage
│   └── ml/                       # ML integration (forecasting, anomaly detection)
├── src/main/resources/
│   ├── application.yml           # Spring Boot configuration
│   ├── db/migration/             # Flyway database migrations
│   └── static/                   # (optional) static assets
└── src/test/java/                # Unit & integration tests
    └── com/benjaminbatte/platform

```

---

## Sequence Diagrams

### Create Dataset

![Create Dataset Sequence](diagrams/create_dataset.png)

### ML Forecast

![ML Forecast Sequence](diagrams/ml_forecast.png)

### Run Profiling Job

![Run Profiling Job Sequence](diagrams/run_profilling_job.png)

---

[🏠 Home](index.md)| [📖 Overview](overview.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)
