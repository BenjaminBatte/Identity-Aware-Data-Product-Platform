---

## title: Overview

---

[🏠 Home](index.md) | [📖 Overview](overview.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

# Identity‑Aware Data Product Platform — Overview

A Spring Boot + Angular application with secure IAM, SQL/NoSQL data storage, ML analytics, and SaaS‑ready architecture.

---

## 1. Project Overview

This project is a multi‑service platform that allows authenticated administrators to manage datasets, automate processing jobs, and run AI‑powered analytics (forecasting, anomaly detection) in a secure, scalable environment.

It demonstrates:

* Enterprise SaaS patterns (stateless microservices, horizontal scaling)
* Spring Boot backend with REST APIs, SQL/NoSQL persistence, and IAM integration (Keycloak)
* Angular 18 frontend with OIDC authentication for administrators
* Machine Learning module integrated into the backend for insights
* Real‑time & scheduled data processing
* Deployment‑ready Docker/Kubernetes configuration

---

## 2. Features

**Core:**

1. **Identity & Access Management (IAM)**

    * OAuth2/OIDC authentication via Keycloak
    * Role‑based access control (admin, analyst, viewer)
    * Secure API endpoints with JWT tokens
2. **Dataset Management**

    * CRUD operations for datasets
    * Version history tracking
    * Metadata storage (profiles, tags, schema)
3. **Data Profiling & Automation**

    * Scheduled jobs (Spring Batch / Quartz)
    * Event‑driven processing (Kafka)
4. **Machine Learning Analytics**

    * Time series anomaly detection
    * Forecasting using ONNX Runtime / Tribuo
    * APIs for raw series submission & results
5. **Observability & Monitoring**

    * Metrics (Micrometer + Prometheus)
    * Tracing (OpenTelemetry)
    * Centralized logs

**Frontend (Angular 18):**

* Responsive admin dashboard (Angular Material)
* Protected routes with OIDC login
* Pages: Datasets, Jobs, ML Insights, User Management

---

## 3. Technology Stack

**Backend:** Spring Boot 3.x, JPA, MongoDB, Redis, Kafka, ONNX/Tribuo, Micrometer, Quartz, Flyway

**Frontend:** Angular 18, Angular Material, Chart.js, OAuth2 OIDC

**Infrastructure:** PostgreSQL, MongoDB, Redis, Keycloak, Docker/K8s

---

## 4. System Architecture

High‑level flow:

1. User logs in via Angular → Keycloak → JWT
2. Angular calls Spring Boot API Gateway with JWT
3. Gateway routes to Dataset, Metadata, Job, or ML service
4. Services use Kafka for async workflows
5. Results stored in Postgres/Mongo
6. Angular shows data & charts

---

## 5. Learning Objectives

* REST API design, security, scaling
* IAM expertise: OIDC, RBAC, token validation
* SQL + NoSQL integration
* ML deployment in backend
* SaaS architecture: multi‑tenancy, HPA
* Angular Material admin dashboards
* DevOps: Docker, K8s, monitoring

---

[🏠 Home](index.md) | [📖 Overview](overview.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)
