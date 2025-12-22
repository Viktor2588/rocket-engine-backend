# Rocket Engine Backend

Spring Boot REST API for space exploration data comparison.

## Project Overview

- **Framework**: Spring Boot 3.x with Java 21
- **Database**: PostgreSQL (Neon serverless)
- **Deployment**: Render (Docker)
- **Frontend**: React app at `../rocket-engine-frontend`

## API Contracts

**IMPORTANT**: This project must fulfill the contracts defined in:

```
contracts/                    # Git submodule
```

**Remote**: https://github.com/Viktor2588/rocket-engine-contract.git

### Contract Repository Structure

| Path | Purpose |
|------|---------|
| `entities/*.yaml` | JPA entity field definitions, validation, relationships |
| `endpoints/*.yaml` | REST endpoint operations, parameters, responses |
| `enums/common.yaml` | All enum types used across the API |
| `manifest.yaml` | API configuration and module listing |

### Entities to Implement

- Engine, Country, LaunchVehicle
- Satellite, LaunchSite
- SpaceMission, SpaceMilestone
- CapabilityScore

### API Modules

- `/api/engines` - Rocket engine CRUD + comparison
- `/api/countries` - Countries with space capabilities
- `/api/launch-vehicles` - Rockets and launchers
- `/api/satellites` - Satellite database
- `/api/launch-sites` - Spaceports
- `/api/missions` - Space missions
- `/api/milestones` - Historical achievements
- `/api/analytics`, `/api/comparison`, `/api/rankings`, `/api/statistics`

## Key Files

| File | Purpose |
|------|---------|
| `src/main/java/.../entity/` | JPA entities |
| `src/main/java/.../controller/` | REST controllers |
| `src/main/java/.../service/` | Business logic |
| `src/main/java/.../repository/` | Data access |
| `src/main/resources/application.properties` | Config |
| `docker-entrypoint.sh` | Converts DATABASE_URL to JDBC |

## Development Notes

- Use `FetchType.LAZY` for relationships
- EntityGraph methods for eager loading when needed
- `open-in-view=true` in production (temporary fix)
- Flyway disabled in production (Hibernate creates schema)

## Running Locally

```bash
./gradlew bootRun
```

API available at: http://localhost:8080

## Production

- URL: https://rocket-engine-backend.onrender.com
- Database: Neon PostgreSQL
