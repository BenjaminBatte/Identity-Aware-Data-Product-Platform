---

## title: Identity-Aware Data Product Platform Documentation

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

# 🔐 Identity-Aware Data Product Platform

*Secure, multi-tenant platform for data product management*

---

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-green)
![Angular](https://img.shields.io/badge/Angular-18-red)
![MongoDB](https://img.shields.io/badge/MongoDB-7%2B-green)

**Quick Start:** [Setup Guide →](setup.md)

---

## 📚 Documentation

### Core Documentation

* [System Architecture](architecture.md) - Diagrams and project structure
* [API Specifications](api-specs.md) - REST endpoints and OpenAPI docs
* [Security Model](security.md) - IAM flow and roles

### Implementation Guides

* [Setup Instructions](setup.md) - Local dev & deployment
* [ML Module](ml-module.md) - Machine learning integration

### Reference

* [Changelog](CHANGELOG.md) - Version history
* [ADRs](ADRs/index.md) - Architectural Decision Records

---

## 📊 System Diagrams

| Diagram                                                | Preview                                                  |
| ------------------------------------------------------ | -------------------------------------------------------- |
| [Domain Class Diagram](diagrams/domain.png)            | <img src="diagrams/domain.png" width="200"/>             |
| [Create Dataset Sequence](diagrams/create_dataset.png) | <img src="diagrams/create_dataset.png" width="200"/>     |
| [ML Forecast Sequence](diagrams/ml_forecast.png)       | <img src="diagrams/ml_forecast.png" width="200"/>        |
| [Run Profiling Job](diagrams/run_profilling_job.png)   | <img src="diagrams/run_profilling_job.png" width="200"/> |

---

## 📝 ADRs (Architecture Decision Records)

Architecture Decision Records (ADRs) capture the context, decision, and consequences of significant choices made during system design.

### Current ADRs

* [ADR-001: Use Keycloak for IAM](ADRs/ADR-001-use-keycloak.md)
* [ADR-002: Polyglot Persistence (Postgres + MongoDB)](ADRs/ADR-002-polyglot-persistence.md)
* [ADR-003: Adopt Spring Boot + Angular Stack](ADRs/ADR-003-spring-boot-angular.md)
* [ADR-004: Event-Driven Jobs with Kafka](ADRs/ADR-004-kafka-jobs.md)
* [ADR-005: ML Integration with ONNX/Tribuo](ADRs/ADR-005-ml-integration.md)

### Template

Each ADR follows this format:

```md
# ADR-NNN: Title

## Context
Why do we need to make this decision?

## Decision
What option did we choose?

## Consequences
What are the implications (positive and negative)?
```

---

[**Get Started** → Setup Guide](setup.md)

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

<sub>© 2025 Identity-Aware Data Product Platform • [GitHub Repo](https://github.com/BenjaminBatte/platform-api)</sub>
