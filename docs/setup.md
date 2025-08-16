---

## title: Setup

---

# Setup

This page explains how to run the **Identity‑Aware Data Product Platform** on your laptop (Docker Compose for infra, Spring Boot backend, Angular 18 admin UI) and how to prepare it for production builds.

---

## ⚡ Express Setup (Preconfigured)

If you just want a one‑command bootstrap for local dev, use the quick script (hypothetical):

```bash
git clone <your-repo-url> platform && cd platform
./setup.sh --quickstart
```

This script will:

* Bring up Docker Compose infra
* Apply default Keycloak realm + users
* Start backend (port 8081)
* Start frontend (port 4200)

Use this path if you want the **happy path** without customizing configs.

---

## Prerequisites

* **Docker** & **Docker Compose** (v2+)
* **Java 21** (Temurin/OpenJDK)
* **Maven 3.9+** (or `./mvnw` wrapper)
* **Node.js 20+** and **npm 10+** (Angular 18 compatible)
* **Git**

> Optional: **mkcert** for local HTTPS, **kubectl** & **Helm** for K8s.

---

## Setup Time Estimates

| Phase            | Estimated Time |
| ---------------- | -------------- |
| Docker Infra     | 5–10 mins      |
| Keycloak Config  | 8–15 mins      |
| Backend Startup  | 2–4 mins       |
| Frontend Startup | 2–3 mins       |

---

## Version Matrix (Verified Combinations)

| Spring Boot | Angular | Keycloak |
| ----------- | ------- | -------- |
| 3.2.0       | 18.1.0  | 24.0     |

> Other versions may work but are not fully tested.

---

## Repository layout (expected)

```
platform/
├─ backend/platform-api/          # Spring Boot service (REST, Jobs, ML stubs)
│  ├─ src/main/java/...
│  ├─ src/main/resources/
│  └─ pom.xml
├─ frontend/admin-ui/             # Angular 18 admin app
│  ├─ src/
│  └─ package.json
├─ ops/
│  ├─ docker-compose.yml          # Postgres, Mongo, Redis, Kafka, Keycloak
│  ├─ keycloak/
│  │  └─ realm-export.json        # Prebuilt realm (optional)
│  └─ k8s/                         # Helm/K8s manifests (prod)
└─ README.md
```

---

## TL;DR Quickstart (local)

```bash
# 0) Clone
git clone <your-repo-url> platform && cd platform

# 1) Infra
cd ops
# (first time) create .env with passwords
cp .env.example .env
# bring up databases + Kafka + Keycloak
docker compose up -d

# 2) Backend (new terminal)
cd ../backend/platform-api
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
./mvnw -DskipTests spring-boot:run -Dspring-boot.run.profiles=local
# API at http://localhost:8081 (example port)

# 3) Frontend (new terminal)
cd frontend/admin-ui
npm ci
npm start
# UI at http://localhost:4200
```

> After the stacks are up, open **Keycloak** at [http://localhost:8080](http://localhost:8080) (admin/admin by default in this setup) and import/create the **platform** realm as described below. Then log into the Angular app with a user from that realm.

---

## 1) Docker Compose infrastructure

(...existing content remains unchanged...)

---

## 8) Troubleshooting

Add some pre-flight checks before you begin:

```bash
# Verify docker & compose available
docker --version
docker compose version

# Check ports are free (Linux/macOS)
lsof -i :8080 -i :8081 -i :4200 || true
```

**Ports in use**

* Change local ports in Compose or app configs (8081 for API, 4200 for UI, 8080 for Keycloak).

**JWT 401 / issuer mismatch**

* Verify `issuer-uri` in Spring matches the realm’s issuer exactly.

(...rest unchanged...)

---

## 10) Next steps

* Add **Springdoc OpenAPI** for interactive API docs.
* Wire **Quartz** jobs and Kafka consumers for dataset profiling automation.
* Integrate **ONNX Runtime/Tribuo** endpoints for anomaly detection / forecasting.
* Add **role‑based UI guards** in Angular routes (admin/analyst/viewer).
* Provide **Helm charts** in `ops/k8s/` for prod rollout.

---

## Happy Path vs Customization Path

* **Happy Path:** Use Express Setup or TL;DR Quickstart with defaults, ideal for evaluation and demos.
* **Customization Path:** Manually configure Keycloak, tweak DB credentials, override ports, or adapt Helm charts for production clusters.
