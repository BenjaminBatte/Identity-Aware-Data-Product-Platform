---
## title: Architecture
---
# Architecture

This document summarizes the current system architecture for the **Identity‑Aware Data Product Platform** backend and the planned Angular admin frontend. It will evolve as we implement features.

---
## Class Diagram (initial domain)

![Domain Class Diagram](https://raw.githubusercontent.com/BenjaminBatte/platform-api/main/docs/diagrams/domain.png)

**Notes**

* `UserOrgRole` enables multi‑tenancy and role‑based access in an org context.
* `DatasetProfileRef` bridges SQL (Postgres) to profile documents stored in Mongo.

---

## Project Tree Structure (current)

```text
platform-api/
├─ pom.xml
├─ mvnw / mvnw.cmd
├─ HELP.md
├─ docs/
│  ├─ architecture.md               # this file
│  ├─ api-specs.md                  # (planned) REST endpoints / OpenAPI notes
│  ├─ security.md                   # (planned) IAM flow and roles
│  ├─ setup.md                      # (planned) local dev & deploy
│  └─ diagrams/                     # PNG/SVG exports and .mmd sources
│
└─ src/
   ├─ main/
   │  ├─ resources/
   │  │  ├─ application.yml         # datasource, JPA, (optional) mongo disable
   │  │  └─ db/migration/           # Flyway migrations (e.g., V1__init.sql)
   │  └─ java/com/benjaminbatte/platform/
   │     ├─ PlatformApiApplication.java
   │     ├─ config/                 # CORS, Jackson, Problem+JSON, etc.
   │     ├─ security/               # (planned) JWT resource server, method security
   │     ├─ domain/                 # JPA entities (Dataset, DatasetVersion, ...)
   │     ├─ dto/                    # request/response records
   │     ├─ repository/             # Spring Data JPA repositories
   │     ├─ service/                # transactional business services
   │     ├─ web/                    # REST controllers
   │     ├─ mapper/                 # (optional) MapStruct mappers
   │     ├─ jobs/                   # Quartz jobs & schedulers
   │     ├─ metadata/               # Mongo docs/repos (profiles, checks)
   │     └─ ml/                     # ONNX/Tribuo adapters & facades
   │
   └─ test/
      └─ java/com/benjaminbatte/platform/
         └─ PlatformApiApplicationTests.java
```

---

## Sequence Diagrams

### Create Dataset

![Create Dataset Sequence](https://raw.githubusercontent.com/BenjaminBatte/platform-api/main/docs/diagrams/create_dataset.png)

### ML Forecast

![ML Forecast Sequence](https://raw.githubusercontent.com/BenjaminBatte/platform-api/main/docs/diagrams/ml_forecast.png)

### Run Profiling Job

![Run Profiling Job Sequence](https://raw.githubusercontent.com/BenjaminBatte/platform-api/main/docs/diagrams/run_profilling_job.png)

---