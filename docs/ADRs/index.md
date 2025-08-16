---
## title: ADRs
---

[🏠 Home](../index.md) | [📐 Architecture](../architecture.md) | [📊 API Specs](../api-specs.md) | [🔒 Security](../security.md) | [⚙ Setup](../setup.md) | [🤖 ML Module](../ml-module.md) | [🖼 Diagrams](../diagrams.md) | [📝 Changelog](../CHANGELOG.md)

# Architecture Decision Records (ADRs)

ADRs document significant architectural decisions: **context**, **decision**, and **consequences**.
This page is the landing page for ADRs only (the site home is at `../index.md`).

---

## Index

- [ADR-001: Use Keycloak for IAM](ADR-001-use-keycloak.md)
- [ADR-002: Polyglot Persistence (Postgres + MongoDB)](ADR-002-polyglot-persistence.md)
- [ADR-003: Adopt Spring Boot + Angular Stack](ADR-003-spring-boot-angular.md)
- [ADR-004: Event-Driven Jobs with Kafka](ADR-004-kafka-jobs.md)
- [ADR-005: ML Integration with ONNX/Tribuo](ADR-005-ml-integration.md)

> Tip: Keep each ADR short (1–2 pages). When decisions change, add a new ADR and link it as “Supersedes” / “Superseded by”.

---

## Template

```md
# ADR-NNN: Title

## Status
Proposed | Accepted | Deprecated | Superseded by ADR-XXX

## Context
Why we needed to make this decision (constraints, goals, tradeoffs).

## Decision
What we chose and key rationale.

## Consequences
Positive/negative outcomes, operational impact, migrations.
