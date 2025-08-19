-- ===========================
-- V1__init.sql (PostgreSQL)
-- Schema aligned with Java entities
-- ===========================

-- ORGANIZATION (matches Org entity)
CREATE TABLE IF NOT EXISTS organization (
                                            id          UUID PRIMARY KEY,
                                            name        VARCHAR(200) NOT NULL,
    slug        VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
    );
CREATE INDEX IF NOT EXISTS ix_org_slug ON organization(slug);

-- USERS (matches User entity)
CREATE TABLE IF NOT EXISTS users (
                                     id           UUID PRIMARY KEY,
                                     email        VARCHAR(320) NOT NULL UNIQUE,
    external_id  VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(200),
    status       BOOLEAN NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ NOT NULL
    );
CREATE INDEX IF NOT EXISTS ux_users_email ON users(email);
CREATE INDEX IF NOT EXISTS ux_users_external_id ON users(external_id);

-- USER_ORG_ROLES (matches UserOrgRole entity)
CREATE TABLE IF NOT EXISTS user_org_roles (
                                              id         UUID PRIMARY KEY,
                                              user_id    UUID NOT NULL REFERENCES users(id),
    org_id     UUID NOT NULL REFERENCES organization(id),
    role       VARCHAR(16) NOT NULL,
    granted_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_user_org UNIQUE (user_id, org_id)
    );
CREATE INDEX IF NOT EXISTS ix_uor_user ON user_org_roles(user_id);
CREATE INDEX IF NOT EXISTS ix_uor_org ON user_org_roles(org_id);

-- DATASETS (matches Dataset entity)
CREATE TABLE IF NOT EXISTS datasets (
                                        id          UUID PRIMARY KEY,
                                        org_id      UUID NOT NULL REFERENCES organization(id),
    owner_id    UUID NOT NULL REFERENCES users(id),
    name        TEXT NOT NULL,
    description VARCHAR(2000),
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ,
    CONSTRAINT uk_org_name UNIQUE (org_id, name)
    );
CREATE INDEX IF NOT EXISTS ix_dataset_org ON datasets(org_id);
CREATE INDEX IF NOT EXISTS ix_dataset_owner ON datasets(owner_id);

-- DATASET_TAGS (matches Dataset.tags collection)
CREATE TABLE IF NOT EXISTS dataset_tags (
                                            dataset_id UUID NOT NULL REFERENCES datasets(id) ON DELETE CASCADE,
    tag        TEXT NOT NULL,
    PRIMARY KEY (dataset_id, tag)
    );

-- DATASET_VERSIONS (matches DatasetVersion entity)
CREATE TABLE IF NOT EXISTS dataset_versions (
                                                id           UUID PRIMARY KEY,
                                                dataset_id   UUID NOT NULL REFERENCES datasets(id) ON DELETE CASCADE,
    version      INT NOT NULL,
    storage_uri  VARCHAR(1000) NOT NULL,
    schema_json  TEXT,
    row_count    BIGINT,
    checksum     VARCHAR(128),
    created_at   TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_dataset_version UNIQUE (dataset_id, version)
    );
CREATE INDEX IF NOT EXISTS ix_dsv_dataset ON dataset_versions(dataset_id);

-- DATASET_PROFILE_REF (matches DatasetProfileRef entity)
CREATE TABLE IF NOT EXISTS dataset_profile_ref (
                                                   id                  UUID PRIMARY KEY,
                                                   dataset_version_id  UUID NOT NULL UNIQUE REFERENCES dataset_versions(id) ON DELETE CASCADE,
    profile_doc_id      VARCHAR(128) NOT NULL,
    generated_at        TIMESTAMPTZ NOT NULL
    );
CREATE UNIQUE INDEX IF NOT EXISTS ix_profile_version ON dataset_profile_ref(dataset_version_id);