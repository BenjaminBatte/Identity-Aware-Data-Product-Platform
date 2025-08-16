---

## title: API Specs

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

# API Specifications

This page documents the **REST API endpoints** of the Identity‑Aware Data Product Platform.

---

## Authentication

Base URL (local dev): `http://localhost:8081`

### `GET /me`

Returns the current user profile and roles. Requires a valid JWT issued by Keycloak.

Response example:

```json
{
  "username": "alice",
  "roles": ["admin"]
}
```

---

## Datasets

### `POST /datasets`

Create a new dataset.

Request body:

```json
{
  "name": "sales-data",
  "description": "Quarterly sales dataset",
  "tags": ["finance", "q1"]
}
```

### `GET /datasets`

List datasets (supports pagination and filtering).

Response example:

```json
[
  {
    "id": "123",
    "name": "sales-data",
    "createdAt": "2025-08-15T12:00:00Z"
  }
]
```

### `GET /datasets/{id}`

Retrieve dataset details including versions.

### `POST /datasets/{id}/versions`

Add a new version to an existing dataset.

---

## Jobs

### `POST /jobs/profile/{datasetId}`

Start a profiling job for the given dataset.

### `GET /jobs`

List all jobs with status.

Response example:

```json
[
  {
    "id": "job-456",
    "datasetId": "123",
    "status": "RUNNING",
    "startedAt": "2025-08-15T12:10:00Z"
  }
]
```

---

## Machine Learning

### `POST /ml/anomaly`

Detect anomalies in a time series.

Request body:

```json
{
  "series": [
    {"timestamp": "2025-08-01T00:00:00Z", "value": 100},
    {"timestamp": "2025-08-02T00:00:00Z", "value": 250}
  ]
}
```

Response example:

```json
{
  "anomalies": [
    {"timestamp": "2025-08-02T00:00:00Z", "value": 250}
  ]
}
```

### `POST /ml/forecast`

Forecast future values based on historical data.

Request body:

```json
{
  "series": [
    {"timestamp": "2025-08-01T00:00:00Z", "value": 100},
    {"timestamp": "2025-08-02T00:00:00Z", "value": 120}
  ],
  "horizon": 7
}
```

Response example:

```json
{
  "forecast": [
    {"timestamp": "2025-08-03T00:00:00Z", "value": 130},
    {"timestamp": "2025-08-04T00:00:00Z", "value": 140}
  ]
}
```

---

## Conventions

* All APIs require **JWT Bearer token** in `Authorization` header.
* Responses follow standard JSON structures with `id`, timestamps in ISO‑8601 format.
* Errors use Spring Boot’s `ProblemDetail` format with `status`, `title`, and `detail`.

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)
