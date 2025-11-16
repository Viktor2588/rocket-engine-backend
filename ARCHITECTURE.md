# 🚀 Rocket Engine Comparison API — Architecture

A Spring Boot REST API for managing and comparing rocket engine specifications.

---

## 📋 Overview

**Purpose**: Provide RESTful endpoints to store, retrieve, filter, and compare rocket engine data.

**Tech Stack**:
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **ORM**: Spring Data JPA / Hibernate
- **Utilities**: Lombok, CrossOrigin for CORS

---

## 🏗️ Project Structure

```
rocket-comparision-server/
├── src/main/java/com/rocket/comparision/
│   ├── entity/           # JPA entities
│   │   └── Engine.java
│   ├── repository/       # Data access layer
│   │   └── EngineRepository.java
│   ├── service/          # Business logic layer
│   │   └── EngineService.java
│   ├── controller/       # REST API layer
│   │   └── EngineController.java
│   └── ComparisionApplication.java
├── src/main/resources/
│   └── application.properties   # Config
├── build.gradle
└── gradle/
```

---

## 🔄 Data Flow

```
HTTP Request
    ↓
EngineController (REST endpoints)
    ↓
EngineService (Business logic)
    ↓
EngineRepository (JPA queries)
    ↓
PostgreSQL Database
    ↓
HTTP Response (JSON)
```

---

## 📦 Layer Descriptions

### **Entity Layer** (`entity/`)
- **Engine.java**: JPA entity mapping to `engines` table
  - Fields: id, name, manufacturer, thrust, isp, propellantType, mass, description
  - Annotations: `@Entity`, `@Table`, Lombok `@Data`

### **Repository Layer** (`repository/`)
- **EngineRepository**: Extends `JpaRepository<Engine, Long>`
  - Built-in CRUD: `findAll()`, `findById()`, `save()`, `deleteById()`
  - Custom queries: `findByManufacturer()`, `findByPropellantType()`, `findByThrustGreaterThan()`, `findByIspGreaterThan()`

### **Service Layer** (`service/`)
- **EngineService**: Business logic and data processing
  - CRUD operations wrapper
  - Filter operations: by manufacturer, propellant, minimum thrust/isp
  - Update logic with validation
  - Uses `@RequiredArgsConstructor` for dependency injection

### **Controller Layer** (`controller/`)
- **EngineController**: REST API endpoints
  - Base path: `/api/engines`
  - CORS enabled for frontend integration
  - Uses `@RestController`, `@RequestMapping`, `@CrossOrigin`

---

## 🔌 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/engines` | GET | Get all engines |
| `/api/engines/{id}` | GET | Get engine by ID |
| `/api/engines` | POST | Create new engine |
| `/api/engines/{id}` | PUT | Update engine |
| `/api/engines/{id}` | DELETE | Delete engine |
| `/api/engines/manufacturer/{manufacturer}` | GET | Filter by manufacturer |
| `/api/engines/propellant/{propellantType}` | GET | Filter by propellant |
| `/api/engines/thrust-min/{thrust}` | GET | Filter by min thrust |
| `/api/engines/isp-min/{isp}` | GET | Filter by min ISP |
| `/api/engines/compare?engine1Id=X&engine2Id=Y` | GET | Compare two engines |

---

## 🗄️ Database Schema

**Table**: `engines`

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO INCREMENT |
| name | VARCHAR | NOT NULL |
| manufacturer | VARCHAR | NOT NULL |
| thrust | DOUBLE | NOT NULL |
| isp | DOUBLE | NOT NULL |
| propellant_type | VARCHAR | NOT NULL |
| mass | DOUBLE | NOT NULL |
| description | TEXT | NULLABLE |

---

## ⚙️ Configuration

**File**: `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rocket_comparison
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update    # Auto-create/update schema
server.port=8080
```

---

## 🚀 Running the Application

```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# Server starts on http://localhost:8080
```

---

## 📊 Example Request/Response

**Request**:
```bash
POST /api/engines
{
  "name": "Merlin 1D",
  "manufacturer": "SpaceX",
  "thrust": 845,
  "isp": 311,
  "propellantType": "RP-1/LOX",
  "mass": 470
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "name": "Merlin 1D",
  "manufacturer": "SpaceX",
  "thrust": 845,
  "isp": 311,
  "propellantType": "RP-1/LOX",
  "mass": 470,
  "description": null
}
```

---

## 🔐 Security (Future)
- Spring Security (optional)
- JWT authentication for POST/PUT/DELETE
- Role-based access control

---

## 📈 Next Phase
- React frontend integration
- Docker containerization
- CI/CD pipeline (GitHub Actions)
- Unit & integration tests
