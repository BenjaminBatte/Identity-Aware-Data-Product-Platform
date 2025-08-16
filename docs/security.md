---

## title: Security

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)

# Security

This page outlines security controls and operating practices for the **Identity‑Aware Data Product Platform**. The platform follows a defense‑in‑depth model across identity, transport, data, application, and operations layers.

---

## 1) Identity & Access Management (IAM)

**Provider:** Keycloak (OIDC/OAuth2)

* **Realms & Clients:** `platform` realm; public client for Angular, resource server validation for Spring Boot.
* **RBAC:** Realm roles `admin`, `analyst`, `viewer` mapped to API authorities.
* **Token rules:** Access tokens ≤ 5–15 min; refresh tokens ≤ 30–60 min; rotate refresh tokens.
* **Secure defaults:** PKCE for SPA; `confidential` clients use client secrets only server‑side.

---

## 2) API Security (Spring Security)

* **JWT Resource Server:** Validate tokens from Keycloak issuer.
* **Authorization:** Method‑level `@PreAuthorize` checks by role; endpoint whitelist for health/metrics only.
* **CORS:** Restricted to trusted origins in non‑prod; configurable via env.
* **Rate limiting:** Recommend gateway/WAF (e.g., NGINX/Envoy) token‑bucket per IP + per user.
* **Headers:** Strict security headers set at the edge (NGINX/Ingress):

    * `Content-Security-Policy` (CSP) with explicit script/style sources
    * `Strict-Transport-Security` (HSTS) in prod
    * `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, `Referrer-Policy: no-referrer`

---

## 3) Data Protection

* **Transport:** TLS 1.2+ everywhere (public and internal). Use mkcert for local, proper certs in prod.
* **At Rest:**

    * Postgres/Mongo volumes encrypted at disk (cloud‑native or LUKS); secrets never stored in code.
    * Field‑level encryption for sensitive values as needed.
* **Backups:** Automated daily backups with tested restore; immutable retention for ≥ 7–30 days.

---

## 4) Secrets Management

* **Local dev:** `.env` for Compose (never commit).
* **Prod:** Use vault/secret stores (Kubernetes Secrets + sealed‑secrets, HashiCorp Vault, or cloud secret managers).
* **Rotation:** Keys/secrets rotated at least quarterly or on incident.

---

## 5) Observability & Audit

* **Metrics:** Micrometer/Prometheus (no PII in metric labels).
* **Tracing:** OpenTelemetry with sampling (avoid user data in spans).
* **Logs:** Structured JSON, no secrets/PII; redact tokens; centralize to Loki/ELK.
* **Audit trails:** Record security‑relevant events (login, role change, dataset access, job execution) with actor, timestamp, outcome.

---

## 6) OWASP & Input Hardening

* Validate and sanitize all inputs (Bean Validation + server‑side constraints).
* Limit payload sizes and enforce timeouts.
* Return generic errors; do not leak internals.
* Use parameterized queries (via JPA) to prevent injection.

---

## 7) Multi‑Tenancy & Authorization

* **Option A (simple):** Org field on entities + row‑level checks in service layer.
* **Option B (Postgres RLS):** Enforce tenant isolation at DB with session context.
* Include tenant in JWT claims and validate on each access path.

---

## 8) Network & Deployment

* **Segmentation:** Private subnets for data stores; public access only via ingress.
* **Kubernetes:**

    * NetworkPolicies to restrict pod‑to‑pod traffic.
    * PodSecurity/PSA: enforce restricted baseline.
    * Read‑only root filesystems where possible; drop Linux capabilities.
* **Images:** Use minimal distroless base images; pin digests; sign images (cosign) and verify in admission.

---

## 9) Supply Chain

* **Dependency management:** Use Renovate/Dependabot; lockfiles and SBOM (CycloneDX) from Maven & npm.
* **Scanning:** SCA (e.g., OWASP Dependency‑Check, Trivy) and container image scanning in CI.
* **Build provenance:** Reproducible builds; keep build logs and artifact checksums.

---

## 10) Incident Response

* **Runbooks:** Define on‑call escalation, triage steps, communication templates.
* **Forensics:** Centralized logs retained ≥ 30–90 days; time‑synced servers; snapshot procedures.
* **Post‑incident:** Blameless postmortems; action items tracked to closure.

---

## 11) Compliance Readiness (non‑cert)

* **Data residency:** Tag datasets with jurisdiction; restrict cross‑region if needed.
* **PII handling:** Data classification and masking; DSR workflows (export/delete) where required.
* **Access controls:** Least‑privilege for service accounts and operators.

---

## 12) Configuration by Environment

| Setting         | Dev (local)             | Staging/Prod                     |
| --------------- | ----------------------- | -------------------------------- |
| TLS             | Optional (mkcert)       | Required (ACME or managed certs) |
| Token lifetimes | Longer, relaxed         | Shorter, strict + rotation       |
| CORS            | `http://localhost:4200` | Only approved domains            |
| DB credentials  | `.env`                  | Secret manager / K8s Secrets     |
| Logging level   | `INFO`/`DEBUG`          | `INFO`/`WARN`                    |
| Metrics/tracing | Optional                | Mandatory                        |

---

## 13) Security Checklists

**Before commit:**

* [ ] Secrets removed from code & history
* [ ] Lint/tests pass; dependencies updated

**Before deploy:**

* [ ] Ingress TLS configured; HSTS enabled
* [ ] CORS reviewed; CSP applied
* [ ] Secrets provisioned via secret store
* [ ] DB migrations reviewed; backups enabled

**Routine:**

* [ ] Review admin users & roles monthly
* [ ] Rotate keys and tokens
* [ ] Patch base images & dependencies

---

[🏠 Home](index.md) | [⚙ Setup](setup.md) | [📐 Architecture](architecture.md) | [📜 ADRs](ADRs/index.md) | [🔒 Security](security.md) | [📊 API Specs](api-specs.md) | [🤖 ML Module](ml-module.md) | [🖼 Diagrams](diagrams.md) | [📝 Changelog](CHANGELOG.md)
