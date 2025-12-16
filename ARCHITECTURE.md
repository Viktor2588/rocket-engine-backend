# Rocket Engine Backend - Architecture Summary

> Comprehensive architectural analysis of the Space Exploration Comparison Platform

## Executive Summary

| Attribute | Value |
|-----------|-------|
| **Framework** | Spring Boot 3.5.7 |
| **Language** | Java 17 |
| **Database** | PostgreSQL |
| **Build System** | Gradle 8.14.3 |
| **Architecture** | Layered MVC |
| **Total LOC** | ~13,270 lines |
| **Package** | `com.rocket.comparison` |

---

## Project Structure

```
rocket-engine-backend/
├── src/main/java/com/rocket/comparison/
│   ├── ComparisonApplication.java          # Entry point
│   ├── config/                              # Configuration layer
│   │   ├── DataSeeder.java                  # Database initialization (798 LOC)
│   │   ├── CorsConfig.java                  # CORS settings
│   │   └── RestClientConfig.java            # HTTP client config
│   ├── controller/                          # REST API layer (14 controllers)
│   ├── entity/                              # JPA domain model (8 entities + 8 enums)
│   ├── exception/                           # Error handling
│   ├── integration/spacedevs/               # External API integration
│   │   ├── SpaceDevsApiClient.java          # REST client
│   │   ├── SpaceDevsSyncService.java        # Sync service
│   │   └── dto/                             # API DTOs
│   ├── repository/                          # Data access (8 repositories)
│   └── service/                             # Business logic (12 services)
├── src/main/resources/
│   └── application.properties               # App configuration
├── scripts/                                 # Data seeding scripts
├── build.gradle                             # Build configuration
└── docker-compose.yml                       # Container setup
```

---

## Domain Model

### Core Entities

```
┌─────────────────────────────────────────────────────────────────────┐
│                              COUNTRY                                 │
│  (id, name, isoCode, region, spaceAgencyName, capabilities...)      │
└─────────────────────────────────────────────────────────────────────┘
          │
          ├──────────────────┬──────────────────┬──────────────────┐
          ▼                  ▼                  ▼                  ▼
    ┌──────────┐      ┌─────────────┐    ┌────────────┐    ┌──────────────┐
    │  ENGINE  │      │LAUNCH_VEHICLE│   │SPACE_MISSION│   │  SATELLITE   │
    │(propellant│      │(payloadToLeo,│   │(missionType,│   │(satelliteType│
    │ isp, thrust)│    │ manufacturer)│   │ launchDate) │   │ orbitType)   │
    └──────────┘      └─────────────┘    └─────┬───────┘    └──────────────┘
                                               │
          ├──────────────────┬─────────────────┼──────────────────┐
          ▼                  ▼                 ▼                  ▼
    ┌──────────────┐  ┌───────────────┐  ┌──────────────┐  ┌──────────────┐
    │ LAUNCH_SITE  │  │SPACE_MILESTONE│  │    (links)   │  │CAPABILITY_   │
    │(lat, lng,    │  │(milestoneType,│  │              │  │   SCORE      │
    │ capabilities)│  │ dateAchieved) │  │              │  │(category,    │
    └──────────────┘  └───────────────┘  └──────────────┘  │ score)       │
                                                           └──────────────┘
```

### Entity Details

| Entity | Purpose | Key Fields | Location |
|--------|---------|------------|----------|
| **Country** | Space-faring nations | id, name, isoCode, capabilities, budget | `entity/Country.java` |
| **Engine** | Rocket propulsion systems | id, name, propellant, isp, thrust, mass | `entity/Engine.java` |
| **LaunchVehicle** | Rockets/launch systems | id, name, payloadToLeo, successRate, reusable | `entity/LaunchVehicle.java` |
| **SpaceMission** | Launch events | id, name, missionType, status, launchDate, crewed | `entity/SpaceMission.java` |
| **Satellite** | Orbital assets | id, name, satelliteType, orbitType, altitude | `entity/Satellite.java` |
| **LaunchSite** | Spaceports | id, name, latitude, longitude, capabilities | `entity/LaunchSite.java` |
| **SpaceMilestone** | Historic achievements | id, title, milestoneType, dateAchieved | `entity/SpaceMilestone.java` |
| **CapabilityScore** | Space capability metrics | id, category, score, ranking | `entity/CapabilityScore.java` |

### Enumerations

| Enum | Values | Purpose |
|------|--------|---------|
| **MissionType** | 43 types | Classify missions (Crewed, Lunar, Mars, Planetary...) |
| **CapabilityCategory** | 7 categories | Weighted scoring (Launch: 0.20, Human: 0.15...) |
| **MilestoneType** | ~10 types | Historic firsts (First satellite, First human...) |
| **SatelliteType** | ~10 types | Satellite classification |
| **OrbitType** | ~12 types | LEO, GEO, SSO, L2, Lunar... |
| **MissionStatus** | 4 states | Planned, Active, Completed, Failed |
| **SatelliteStatus** | 5 states | Operational, Retired, Planned... |
| **LaunchSiteStatus** | 3 states | Operational, Retired, In Development |

---

## Data Flow

```
HTTP Request
    ↓
Controller (REST endpoints, validation)
    ↓
Service (Business logic, transactions)
    ↓
Repository (JPA queries, data access)
    ↓
PostgreSQL Database
    ↓
HTTP Response (JSON)
```

---

## API Layer

### Controllers Overview

| Controller | Base Route | Endpoints | Purpose |
|------------|-----------|-----------|---------|
| **EngineController** | `/api/engines` | 14 | CRUD + filtering |
| **CountryController** | `/api/countries` | 22 | CRUD + rankings + comparisons |
| **LaunchVehicleController** | `/api/launch-vehicles` | 12+ | CRUD + filtering |
| **SpaceMissionController** | `/api/missions` | 12+ | CRUD + filtering |
| **SatelliteController** | `/api/satellites` | 12+ | CRUD + orbit queries |
| **LaunchSiteController** | `/api/launch-sites` | 12+ | CRUD + location queries |
| **SpaceMilestoneController** | `/api/milestones` | 10+ | CRUD + timeline |
| **CapabilityScoreController** | `/api/capability-scores` | 8+ | Scoring + rankings |
| **AnalyticsController** | `/api/analytics` | 8 | Trends + records |
| **ComparisonController** | `/api/comparison` | 4+ | Multi-entity comparison |
| **RankingsController** | `/api/rankings` | 6+ | Global rankings |
| **VisualizationController** | `/api/visualization` | 6+ | Chart data |
| **GlobalStatisticsController** | `/api/global-statistics` | 4+ | Global metrics |
| **DataSyncController** | `/api/data-sync` | 3+ | External API sync |

### Key API Patterns

```
# Standard CRUD
GET    /api/{resource}              # List all (paginated)
GET    /api/{resource}?unpaged=true # List all (unpaged)
GET    /api/{resource}/{id}         # Get by ID
POST   /api/{resource}              # Create
PUT    /api/{resource}/{id}         # Update
DELETE /api/{resource}/{id}         # Delete

# Filtering
GET    /api/engines/designer/{designer}
GET    /api/engines/propellant/{propellant}
GET    /api/engines/by-country/{countryId}
GET    /api/countries/capability/human-spaceflight

# Comparison
GET    /api/engines/compare?engine1Id=X&engine2Id=Y
GET    /api/countries/compare/{id1}/vs/{id2}

# Analytics
GET    /api/analytics/launches-per-year
GET    /api/analytics/technology-trends
GET    /api/analytics/records
```

### Response Formats

```json
// Standard Entity Response
{
  "id": 1,
  "name": "Raptor 2",
  "designer": "SpaceX",
  "propellant": "CH4/LOX",
  "thrustN": 2300000,
  "ispS": 363
}

// Paginated Response
{
  "content": [...],
  "pageable": {"size": 20, "number": 0},
  "totalElements": 150,
  "totalPages": 8,
  "last": false
}

// Comparison Response
{
  "country1": {...},
  "country2": {...},
  "overallLeader": "United States",
  "scoreDifference": 13.5,
  "capabilities": {
    "country1Advantages": ["Human Spaceflight", "Reusable Rockets"],
    "country2Advantages": [...]
  }
}
```

---

## Service Layer

### Business Services

| Service | Responsibility | Key Operations |
|---------|---------------|----------------|
| **EngineService** | Engine operations | CRUD, filtering by designer/propellant/country |
| **CountryService** | Country management | Rankings, capability filtering, comparisons |
| **LaunchVehicleService** | Vehicle operations | Payload filtering, status queries |
| **SpaceMissionService** | Mission tracking | Type/status filtering, date ranges |
| **SatelliteService** | Satellite operations | Orbit/type filtering, constellation queries |
| **LaunchSiteService** | Site management | Location queries, capability filtering |
| **SpaceMilestoneService** | Milestone tracking | Timeline queries, type filtering |
| **CapabilityScoreService** | Scoring system | Category scores, overall calculations |
| **AnalyticsService** | Data analysis | Launch trends, technology trends, records |
| **ComparisonService** | Entity comparison | Multi-entity analysis |
| **GlobalStatisticsService** | Global metrics | Totals, aggregations |
| **VisualizationService** | Chart data | Timeline data, radar charts |

### Transaction Management

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // Default read-only
public class EngineService {

    @Transactional  // Write operations
    public Engine updateEngine(Long id, Engine engine) { ... }
}
```

---

## Repository Layer

### Data Access Patterns

```java
// CountryRepository.java - Example patterns

// Derived queries (method name -> SQL)
Optional<Country> findByIsoCode(String isoCode);
List<Country> findByHumanSpaceflightCapableTrue();

// Custom JPQL
@Query("SELECT c FROM Country c WHERE c.overallCapabilityScore IS NOT NULL
        ORDER BY c.overallCapabilityScore DESC")
List<Country> findAllOrderByCapabilityScoreDesc();

// Aggregation
@Query("SELECT COUNT(c) FROM Country c WHERE c.independentLaunchCapable = true")
Long countWithLaunchCapability();

// Parameterized
@Query("SELECT c FROM Country c WHERE c.overallCapabilityScore <= :threshold")
List<Country> findPotentialEmergingNations(@Param("threshold") Double threshold);
```

### Index Strategy

All entities have strategic indexes on:
- Foreign keys (`country_id`)
- Frequently filtered columns (`status`, `region`, `type`)
- Sort columns (`overallCapabilityScore`, `launchDate`)
- Unique constraints where applicable

---

## External Integration

### TheSpaceDevs API

```
Base URL: https://ll.thespacedevs.com/2.2.0

Endpoints Used:
├── /agencies/          -> Country data
├── /launches/          -> SpaceMission data
├── /config/launcher/   -> LaunchVehicle data
├── /pads/              -> LaunchSite data
└── /launches/upcoming/ -> Future missions
```

### Integration Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│ SpaceDevsApi    │────>│ SpaceDevsSyncSvc │────>│ Repositories    │
│ Client          │     │                  │     │                 │
│ (REST calls)    │     │ (DTO -> Entity)  │     │ (Persistence)   │
└─────────────────┘     └──────────────────┘     └─────────────────┘
        │
        ▼
┌─────────────────┐
│ DTOs:           │
│ - LaunchDto     │
│ - AgencyDto     │
│ - LauncherCfgDto│
│ - PadDto        │
└─────────────────┘
```

### Error Handling

- REST client catches `RestClientException`
- Returns empty collections on failure
- Logs errors with context
- Non-critical failures don't crash startup

---

## Configuration

### Application Properties

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5433/rocket_engine_comparison
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.batch_size=10

# CORS
cors.allowed.origins=http://localhost:3000,http://localhost:5173
```

### Data Seeding (DataSeeder.java)

On startup, seeds if tables empty:
- **24 countries** with capability scores
- **12 engines** (Raptor, Merlin, RS-25, RD-180...)
- **14 launch vehicles** (Falcon 9, Starship, SLS...)
- **8 milestones** (First satellite, Moon landing...)
- **8 missions** (Apollo 11, Artemis I, JWST...)
- **8 satellites** (ISS, Hubble, Starlink...)
- **11 launch sites** (Kennedy, Baikonur...)

Then syncs external API data.

---

## Performance Optimizations

### Database

| Optimization | Implementation |
|--------------|----------------|
| Connection Pooling | HikariCP: 20 max, 5 min idle |
| Batch Operations | batch_size=10, ordered inserts |
| Strategic Indexes | 50+ indexes on key columns |
| Lazy Loading | FetchType.LAZY on relationships |

### API

| Optimization | Implementation |
|--------------|----------------|
| Pagination | Default 20, max 100 per page |
| JSON Optimization | @JsonIgnore to prevent cycles |
| Separate Unpaged | `?unpaged=true` for full datasets |

---

## Architecture Patterns

### Layered Architecture

```
┌─────────────────────────────────────────┐
│           Controllers (REST)             │  <- HTTP handling, validation
├─────────────────────────────────────────┤
│           Services (Business)            │  <- Business logic, transactions
├─────────────────────────────────────────┤
│         Repositories (Data)              │  <- Data access, queries
├─────────────────────────────────────────┤
│         Entities (Domain)                │  <- JPA entities, relationships
├─────────────────────────────────────────┤
│           PostgreSQL                     │  <- Persistence
└─────────────────────────────────────────┘
```

### Key Design Decisions

1. **Entity Relationships**: One-to-Many via foreign keys with proper cascading
2. **Transaction Boundaries**: Services manage transaction scopes
3. **API Design**: Resource-centric REST with query parameters for filtering
4. **Error Handling**: GlobalExceptionHandler + Optional responses
5. **External Integration**: Facade pattern with graceful degradation

---

## Dependencies

```gradle
dependencies {
    // Core
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Database
    implementation 'org.postgresql:postgresql:42.7.8'

    // Utilities
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

## File Statistics

| Category | Files | Lines |
|----------|-------|-------|
| Entities + Enums | 16 | ~2,000 |
| Controllers | 14 | ~2,500 |
| Services | 12 | ~1,500 |
| Repositories | 8 | ~500 |
| Configuration | 3 | ~850 |
| Integration | 7 | ~700 |
| **Total** | **~80** | **~13,270** |

---

## Quick Reference

### Start Application
```bash
./gradlew bootRun
```

### API Base URL
```
http://localhost:8080/api
```

### Database
```
PostgreSQL @ localhost:5433/rocket_engine_comparison
```

### Key Endpoints
```
GET /api/countries              # All countries
GET /api/engines                # All engines
GET /api/launch-vehicles        # All vehicles
GET /api/analytics/summary      # Full analytics
GET /api/rankings               # Global rankings
```

---

*Generated: 2025-12-16*
