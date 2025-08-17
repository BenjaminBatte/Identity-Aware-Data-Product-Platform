-- ===========================
-- V1__init.sql (PostgreSQL)
-- Schema for: orgs, users, user_org_roles, datasets, dataset_versions,
--             dataset_profile_ref, dataset_tags
-- ===========================

-- NOTE: We rely on the app (@UuidGenerator) to generate UUIDs.
-- No DB-side UUID default to avoid requiring extensions on all environments.

-- ORGS
CREATE TABLE IF NOT EXISTS orgs (
  id          UUID PRIMARY KEY,
  name        TEXT        NOT NULL,
  slug        VARCHAR(120) NOT NULL UNIQUE,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS ix_org_slug ON orgs(slug);

-- USERS
CREATE TABLE IF NOT EXISTS users (
  id           UUID PRIMARY KEY,
  email        VARCHAR(255) NOT NULL UNIQUE,
  display_name TEXT         NOT NULL,
  created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  active       BOOLEAN      NOT NULL DEFAULT TRUE
);
CREATE INDEX IF NOT EXISTS ix_user_email ON users(email);

-- USER ↔ ORG ROLE (membership)
CREATE TABLE IF NOT EXISTS user_org_roles (
  id        UUID PRIMARY KEY,
  user_id   UUID        NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
  org_id    UUID        NOT NULL REFERENCES orgs(id)  ON DELETE RESTRICT,
  role      VARCHAR(16) NOT NULL,  -- ADMIN | ANALYST | VIEWER
  granted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_user_org UNIQUE (user_id, org_id)
);
CREATE INDEX IF NOT EXISTS ix_uor_user ON user_org_roles(user_id);
CREATE INDEX IF NOT EXISTS ix_uor_org  ON user_org_roles(org_id);

-- DATASETS
CREATE TABLE IF NOT EXISTS datasets (
  id          UUID PRIMARY KEY,
  org_id      UUID  NOT NULL REFERENCES orgs(id)   ON DELETE RESTRICT,
  owner_id    UUID  NOT NULL REFERENCES users(id)  ON DELETE RESTRICT,
  name        TEXT  NOT NULL,
  description TEXT,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_org_name UNIQUE (org_id, name)
);
CREATE INDEX IF NOT EXISTS ix_dataset_org   ON datasets(org_id);
CREATE INDEX IF NOT EXISTS ix_dataset_owner ON datasets(owner_id);

-- DATASET TAGS (ElementCollection)
CREATE TABLE IF NOT EXISTS dataset_tags (
  dataset_id UUID NOT NULL REFERENCES datasets(id) ON DELETE CASCADE,
  tag        TEXT NOT NULL,
  PRIMARY KEY (dataset_id, tag)
);

-- DATASET VERSIONS
CREATE TABLE IF NOT EXISTS dataset_versions (
  id           UUID PRIMARY KEY,
  dataset_id   UUID NOT NULL REFERENCES datasets(id) ON DELETE CASCADE,
  version      INT  NOT NULL,
  storage_uri  VARCHAR(1000) NOT NULL,
  schema_json  TEXT,
  row_count    BIGINT,
  checksum     VARCHAR(128),
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uk_dataset_version UNIQUE (dataset_id, version)
);
CREATE INDEX IF NOT EXISTS ix_dsv_dataset ON dataset_versions(dataset_id);

-- PROFILE REF (→ Mongo doc id), 1:1 with dataset_version
CREATE TABLE IF NOT EXISTS dataset_profile_ref (
  id                  UUID PRIMARY KEY,
  dataset_version_id  UUID NOT NULL UNIQUE REFERENCES dataset_versions(id) ON DELETE CASCADE,
  profile_doc_id      VARCHAR(128) NOT NULL,
  generated_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_profile_version ON dataset_profile_ref(dataset_version_id);

