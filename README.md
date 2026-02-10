# Helmes Sector Selection

A full-stack web application for selecting business sectors. Users enter their name, choose sectors from a hierarchical tree, agree to terms, and save. Data persists per session and the form is refilled on revisit.

## Tech Stack

- **Frontend:** React 19, TypeScript 5.9, Tailwind CSS 4, Vite 7
- **Backend:** Java 21, Spring Boot 3.4, Maven
- **Database:** PostgreSQL 17
- **Testing:** Vitest + React Testing Library (frontend), JUnit 5 + Mockito (backend)
- **Linting:** ESLint strict + Prettier (frontend), Checkstyle (backend)
- **Infra:** Docker, Docker Compose, Nginx, Flyway

## Prerequisites

- Docker

For local development without Docker:

- Java 21
- Node.js 22+ with pnpm
- PostgreSQL 14+

## Quick Start (Docker)

```bash
docker compose up --build
```

To stop and remove all containers and volumes:

```bash
docker compose down -v
```

- **Frontend:** http://localhost:3000
- **Backend:** http://localhost:8080
- **API docs:** http://localhost:8080/api/v1/swagger-ui

## Local Development

### Database

Set up a local PostgreSQL database (Flyway-based, recommended):

```sql
CREATE USER helmes WITH PASSWORD 'helmes';
CREATE DATABASE helmes OWNER helmes;
```

Alternative: restore the provided dump (`dump.sql`) into a fresh local database:

```bash
psql -U helmes -d helmes -f dump.sql
```

If you restore from `dump.sql`, run backend with Flyway disabled (the dump already contains the schema and seed data):

```bash
cd backend
SPRING_FLYWAY_ENABLED=false ./mvnw spring-boot:run
```

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Runs on http://localhost:8080. Flyway applies migrations automatically.

### Frontend

```bash
cd frontend
pnpm install
pnpm dev
```

Runs on http://localhost:5173 with API proxy to the backend.

## API Documentation

All endpoints are under `/api/v1/`. Full interactive API documentation is available via Swagger UI at:

http://localhost:8080/api/v1/swagger-ui

## Testing

### Backend

```bash
cd backend
./mvnw test
```

20 unit tests covering services and controllers.

### Frontend

```bash
cd frontend
pnpm test
```

16 unit tests covering components and the main App.

## Session

User data is tied to the browser session ID. To start fresh, clear the `SESSION` cookie in your browser's developer tools (Application -> Cookies) or open an incognito window.
This creates a new session identity; it does not delete previously saved rows from the database.
Deleting persisted selection records is outside the scope of this assignment.
