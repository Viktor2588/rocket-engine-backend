# Truth Ledger Contracts

The data contracts for this project are maintained in a separate repository.

## Location

```
../rocket-engine-contract/
```

## Contents

| Path | Description |
|------|-------------|
| `manifest.yaml` | Master config: entities, sources, SLAs |
| `queries/*.yaml` | Query contracts (lookup, temporal, explainability, conflicts, ranking, search) |
| `invariants/*.yaml` | Data invariants (referential, temporal, scoring) |
| `schema/truth-ledger-v1.sql` | PostgreSQL schema satisfying all contracts |

## Usage

1. **Schema Migration**: Copy `schema/truth-ledger-v1.sql` to `src/main/resources/db/migration/`
2. **API Implementation**: Implement query contracts as REST endpoints
3. **Validation**: Enforce invariants in JPA entities and application layer

## Keeping in Sync

When contracts change:
1. Pull latest from `rocket-engine-contract`
2. Update Flyway migrations if schema changed
3. Update API endpoints if query contracts changed
4. Update validation if invariants changed
