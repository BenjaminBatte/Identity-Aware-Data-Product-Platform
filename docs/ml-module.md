---

## title: ML Module

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

# Machine Learning Module

This page describes the **Machine Learning (ML) capabilities** integrated into the Identity‑Aware Data Product Platform. It covers supported tasks, runtime architecture, data flow, and extension points.

---

## 1) Overview

The ML module enables:

* **Time Series Anomaly Detection** — flag unusual values in a dataset.
* **Forecasting** — predict future values from historical data.

Implemented in Java and integrated with Spring Boot microservices. Models can be run using:

* **ONNX Runtime** (for portability of pre‑trained models)
* **Tribuo** (Java ML library for training & inference)

---

## 2) Architecture

* **ML Service (Spring Boot):** exposes REST APIs (`/ml/anomaly`, `/ml/forecast`).
* **Model Loader:** loads ONNX/Tribuo models from MongoDB GridFS or local volume.
* **Inference Engine:** executes prediction/anomaly detection requests.
* **Kafka Events:** jobs may trigger async ML tasks; results stored in Mongo.
* **UI Integration:** Angular admin dashboard visualizes anomalies/forecasts with charts.

---

## 3) API Contracts

### `POST /ml/anomaly`

* **Purpose:** Detect anomalies in a univariate time series.
* **Input:** JSON array of `{timestamp, value}` objects.
* **Output:** List of anomaly points with optional scores.

**Request**

```http
POST /ml/anomaly
Content-Type: application/json
```

```json
{
  "series": [
    {"timestamp": "2025-08-01T00:00:00Z", "value": 100},
    {"timestamp": "2025-08-02T00:00:00Z", "value": 250}
  ]
}
```

**Response**

```json
{
  "anomalies": [
    {"timestamp": "2025-08-02T00:00:00Z", "value": 250, "score": 3.1}
  ]
}
```

### `POST /ml/forecast`

* **Purpose:** Forecast future values for a univariate time series.
* **Input:** JSON array of `{timestamp, value}` and an integer horizon (days/steps).
* **Output:** Forecasted points for the given horizon.

**Request**

```http
POST /ml/forecast
Content-Type: application/json
```

```json
{
  "series": [
    {"timestamp": "2025-08-01T00:00:00Z", "value": 100},
    {"timestamp": "2025-08-02T00:00:00Z", "value": 120}
  ],
  "horizon": 3
}
```

**Response**

```json
{
  "forecast": [
    {"timestamp": "2025-08-03T00:00:00Z", "value": 130},
    {"timestamp": "2025-08-04T00:00:00Z", "value": 135},
    {"timestamp": "2025-08-05T00:00:00Z", "value": 138}
  ]
}
```

---

## 4) Data Flow

1. User uploads dataset or selects existing one.
2. Profiling jobs run (scheduled or triggered) and may push metrics to Kafka.
3. ML Service consumes series data.
4. Inference executed (ONNX/Tribuo).
5. Results stored in MongoDB.
6. Angular frontend renders visualizations (ng2‑charts).

---

## 5) Model Management

* **Versioning:** Each model stored with version metadata in Mongo.
* **Deployment:** New models deployed by uploading ONNX files or training via Tribuo.
* **Hot reload:** ML Service polls model store or listens for Kafka events to reload models without downtime.

---

## 6) Example Usage (Copy‑Paste Ready)

**Anomaly Detection**

```bash
curl -sS http://localhost:8081/ml/anomaly \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{
    "series": [
      {"timestamp": "2025-08-01T00:00:00Z", "value": 100},
      {"timestamp": "2025-08-02T00:00:00Z", "value": 250}
    ]
  }'
```

**Forecast**

```bash
curl -sS http://localhost:8081/ml/forecast \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{
    "series": [
      {"timestamp": "2025-08-01T00:00:00Z", "value": 100},
      {"timestamp": "2025-08-02T00:00:00Z", "value": 120}
    ],
    "horizon": 3
  }'
```

---

## 7) Extensibility

* **New algorithms:** Additional anomaly detection or forecasting algorithms can be added by extending the service layer.
* **Cross‑language models:** Use ONNX to import models trained in Python (PyTorch, scikit‑learn, TensorFlow).
* **Pipeline integration:** Future plan to add retraining pipelines via a Python microservice.

---

## 8) Monitoring & Metrics

* **Latency & Throughput:** measured per request, exported via Micrometer.
* **Model accuracy metrics:** tracked offline and stored with model metadata.
* **Failure cases:** inference errors logged with correlation IDs.

---

## 9) Roadmap

* Add support for streaming inference via Kafka topics.
* Integrate AutoML for easier model selection.
* Support feature extraction pipelines for structured datasets.
* Add retraining workflows (Python sidecar service).

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md ) | [📝 Changelog](CHANGELOG.md)
