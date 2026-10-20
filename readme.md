# 🏥 Pulse AI — Backend

Backend for an enterprise hospital management system. 🚀

- **Stack:** Spring Boot 4 · Java 17 · Gradle · PostgreSQL · Flyway · JWT auth
- **Modules:** Auth (+ MagicLink), Patient, Doctor, Department, Appointment, Prescription, MedicalRecord, MedicalHistory, Medicine, Notification, Search, SHealth

## 📁 Package layout

Feature-first, one folder per domain under `src/main/java/app/Api/<Module>/`:

```
Api/<Module>/
  <Module>Controller.java   REST endpoints
  <Module>Service.java      business logic
  <Module>Repository.java   Spring Data JPA
  <Module>Entity.java       JPA entity (extends app.Base.JpaBaseEntity)
  Dtos/                     request/response DTOs (Patient, Auth so far)
  <Module>Mapper.java       MapStruct mapping (Patient, Auth so far)
```

Cross-cutting code lives in `app/Configs/` (Security, Exceptions, Cache, Threads) and `app/Shared/Enum/`.

## 🔐 Security

- Stateless JWT (`Configs/Security/Jwt/JwtFilter`), one shared secret.
- `Configs/Security/PublicRoutesConfig.routes` is the allowlist — **anything not listed requires a bearer token.**
- Role-based access via `@PreAuthorize("hasRole('...')")`, backed by `UserEntity.role` → `ROLE_<value>`.
- ⚠️ **Known gap:** nothing currently assigns `DOCTOR`/`ADMIN`/`PATIENT` roles yet — registration always defaults to `USER`.

## 🗄️ Data layer

- Flyway migrations under `src/main/resources/db/migrations/` (plural path — Flyway is the source of truth for schema).
- Soft-delete flags (`active`) exist on a few entities but aren't enforced yet — deletes are currently hard deletes.

## 🌱 Data seeder

`app/Seeder/` fills every entity with fake data on boot (`app.seed.enabled=true`). Seeds realistic appointments, medical records tied to completed visits, and roles (ADMIN/STAFF/DOCTOR/PATIENT) — then triggers a full search reindex.

## 🔍 Search sync — Kafka → Elasticsearch

Postgres is the source of truth; **Elasticsearch** is a derived, searchable mirror kept in sync through an application-level event pipeline (not WAL-based CDC):

1. A service makes a write and calls `CdcEmitter.emit(...)`.
2. After the transaction commits, a lightweight event (`entityType` + `entityId` + `eventType` — never the payload) is published to **Kafka**, keyed `ENTITY_TYPE:id` so updates to the same row stay ordered.
3. `CdcEventConsumer` picks it up and asks `SearchIndexer` to re-read the row from Postgres and upsert/delete it in Elasticsearch.
4. Indexes are versioned behind an alias, so a full `reindexAll()` swaps atomically with zero downtime.

Kafka topics and the cluster can be inspected via **Kafka UI** at `http://localhost:8080`.

## 📊 Observability — Prometheus, Grafana, ELK

- **Prometheus** (`http://localhost:9090`) scrapes app metrics (Spring Actuator/Micrometer).
- **Grafana** (`http://localhost:3001`) visualizes them — datasource is auto-provisioned, no manual setup.
- **Filebeat → Elasticsearch → Kibana** (`http://localhost:5601`) ships and indexes application logs for search/debugging.

## 🧰 Running locally

```bash
docker compose up -d     # Postgres, Kafka(+UI), Elasticsearch, Redis, Prometheus, Grafana, Filebeat, Kibana
./gradlew bootRun
```

| Service | URL |
|---|---|
| App | `http://localhost:8080`* |
| Kafka UI | `http://localhost:8080` |
| Elasticsearch | `http://localhost:9200` |
| Prometheus | `http://localhost:9090` |
| Grafana | `http://localhost:3001` |
| Kibana | `http://localhost:5601` |

*\*Check `application.properties` — Kafka UI also binds 8080, so confirm the app's actual port before running both.*

## 🧪 Redis — wired, not yet used

`Configs/Cache/RedisConfig` is currently an empty stub. **Planned uses once implemented:**

- 🔁 Caching read-heavy, low-churn lookups (Doctor/Department listings)
- 🚪 JWT blacklist for logout/token revocation (there's currently no logout endpoint)

## 🔜 Upcoming / known follow-ups

- 🔑 **Move the hardcoded JWT secret** (`JwtService.SECRET_KEY`) to config/env and rotate it before any real deployment.
- 👤 **Role provisioning flow** — decide self-serve patient signup vs. admin-provisioned doctor/staff accounts; nothing assigns non-`USER` roles yet.
- 🧹 **Soft delete** — enforce the existing `active` flags on Patient/Doctor/MedicalHistory instead of hard-deleting clinical data.
- 📦 **DTO migration** — Appointment, Department, Doctor, MedicalHistory, MedicalRecord, Medicine, and Prescription still expose raw JPA entities in their controllers; give each the DTO + Mapper treatment `Patient`/`Auth` already have.
- 📄 **Pagination** — most list endpoints still lack it; `Patient.getAll` (`Pageable`/`page`/`size`) is the pattern to copy.
- 🔔 **Notification as an async consumer** — Notification is still written synchronously; the intended flow is domain events (appointment created/cancelled, prescription issued, patient registered) published to Kafka and consumed by Notification, keeping modules decoupled.
- 🔭 **`/search?q=&type=`** — the actual query-side search endpoint isn't built yet, and the legacy `SearchEntity` table should be deleted rather than extended.
- ⚡ **Redis caching + JWT blacklist** (see above).
- 🧾 **Google auth** — `AuthController.googleAuthCodeAuthentication` is currently a stub.
- 💊 **Medicine inventory** — no stock/batch/expiry fields yet; needed if a pharmacy/dispense flow is added.
