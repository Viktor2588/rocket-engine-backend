# API Contracts

The API contracts defining entities, endpoints, DTOs, and validation rules are maintained in a separate repository.

## Location

```
contracts/                    # Git submodule
```

**Remote**: https://github.com/Viktor2588/rocket-engine-contract.git

## Contract Structure

```
contracts/
├── manifest.yaml           # API configuration and metadata
├── entities/               # Entity definitions (JPA models)
│   ├── engine.yaml
│   ├── country.yaml
│   ├── launch_vehicle.yaml
│   ├── satellite.yaml
│   ├── launch_site.yaml
│   ├── space_mission.yaml
│   ├── space_milestone.yaml
│   └── capability_score.yaml
├── endpoints/              # REST API endpoint definitions
│   ├── engines.yaml
│   ├── countries.yaml
│   ├── launch-vehicles.yaml
│   ├── satellites.yaml
│   ├── launch-sites.yaml
│   ├── missions.yaml
│   └── milestones.yaml
└── enums/
    └── common.yaml         # All enum type definitions
```

## What Contracts Define

| Contract Type | Defines |
|---------------|---------|
| **Entities** | Fields, types, validation rules, relationships, indexes |
| **Endpoints** | HTTP methods, paths, parameters, request/response schemas |
| **DTOs** | Summary objects, comparison results, statistics |
| **Enums** | Status types, categories, orbit types, mission types |

## Implementation Mapping

| Contract | Backend Implementation |
|----------|----------------------|
| `entities/*.yaml` | `src/main/java/.../entity/*.java` |
| `endpoints/*.yaml` | `src/main/java/.../controller/*.java` |
| `enums/common.yaml` | `src/main/java/.../entity/*.java` (enum classes) |

## Keeping in Sync

When contracts change:
1. Update submodule: `git submodule update --remote contracts`
2. Update JPA entities if entity contracts changed
3. Update controllers if endpoint contracts changed
4. Update DTOs if response schemas changed
5. Run tests to verify compliance
