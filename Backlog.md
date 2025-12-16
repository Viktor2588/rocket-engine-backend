Backend backlog (with explicit Frontend impacts)

I’m going to structure this as Epics → Tickets with IDs you can copy into Jira/Linear. Each backend ticket includes a FE impact flag so you can mirror them into frontend work.

Priority scale: P0 (must do now) → P3 (later)
Effort: S/M/L (relative)
Epic BE-A — API safety rails (prevent table scans + memory spikes)
ID	Priority	Effort	Backend ticket	FE impact
BE-001	P0	S	Enforce global pagination limits (default + max page size) across all list endpoints	Yes (list pages must honor paging)
BE-002	P0	S	Deprecate ?unpaged=true: log warning + return capped results (or require admin)	Yes (remove reliance on unpaged)
BE-003	P0	S	Deprecate /all endpoints (same treatment as above)	Yes (update calls)
BE-004	P0	S	Add “export” alternative if needed: GET /api/{resource}/export (admin-only, streaming)	Yes (only if FE uses export)

Acceptance criteria (for BE-001..004)

    No public endpoint can return an unbounded dataset.
    All list endpoints behave consistently with page, size, sort.
    If legacy unpaged calls happen, they’re observable (logs/metrics) and don’t blow up memory.

Frontend tasks triggered: FE-001..FE-004 (see FE section).
Epic BE-B — Fix “count by loading rows” and other obvious inefficiencies
ID	Priority	Effort	Backend ticket	FE impact
BE-010	P0	S	Replace .getAllCountries().size() in /api/countries/statistics with repository COUNT(*)	No (response unchanged)
BE-011	P1	M	Audit entire codebase for findAll().size() / in-memory filtering/sorting in controllers/services; replace with DB queries	No/Maybe (only if response changes)

Acceptance criteria

    Statistics endpoints use aggregate queries (COUNT, SUM, AVG) not entity loads.
    Top risky endpoints show reduced query counts and stable latency.

Epic BE-C — Standard error responses + validation (robustness + FE predictability)
ID	Priority	Effort	Backend ticket	FE impact
BE-020	P0	M	Standardize error format using Spring ProblemDetail for 400/404/409/500	Yes (FE error parsing)
BE-021	P0	S	Add request correlation ID to responses + logs (e.g. X-Request-Id)	Optional (FE can display/capture)
BE-022	P1	M	Enforce request validation with @Valid + request DTOs (start with create/update endpoints)	Yes (FE must send valid shapes; handle 400 field errors)

Acceptance criteria

    All errors return consistent JSON with stable fields (status/title/detail/instance + optional errorCode + fieldErrors).
    Validation failures return per-field error details.

Epic BE-D — Introduce real DTO layer (stop returning JPA entities)

This is the biggest maintainability change and will impact FE the most. Do it incrementally per resource.
ID	Priority	Effort	Backend ticket	FE impact
BE-030	P0	M	Create api/dto + api/mapper packages; pick mapping approach (MapStruct or manual)	Yes (new response types begin appearing)
BE-031	P1	M	Migrate Countries endpoints to DTOs (list + details + rankings + comparisons)	Yes
BE-032	P1	M	Migrate Engines endpoints to DTOs (list + filters + compare)	Yes
BE-033	P2	M	Migrate LaunchVehicles endpoints to DTOs	Yes
BE-034	P2	M	Migrate Missions endpoints to DTOs	Yes
BE-035	P2	M	Migrate Satellites endpoints to DTOs	Yes
BE-036	P2	M	Migrate LaunchSites endpoints to DTOs	Yes
BE-037	P2	M	Migrate Milestones endpoints to DTOs	Yes
BE-038	P2	M	Migrate CapabilityScores endpoints to DTOs	Yes
BE-039	P1	S	Move existing inner controller record DTOs into shared DTO package (e.g. CountrySummary, ComparisonResult)	Yes (import/type updates)

Compatibility strategy ticket (strongly recommended)
ID	Priority	Effort	Backend ticket	FE impact
BE-040	P0	M	Add a rollout plan for breaking changes: either /api/v2 or “dual response” period behind a flag/header	Yes (FE coordinates migration)

Acceptance criteria

    Controllers do not return entities (for migrated resources).
    List endpoints return “summary DTOs” (small payload).
    Details endpoints return explicit “details DTOs” (no accidental relationship expansion).

Epic BE-E — High-risk endpoint rewrites (details, rankings, analytics)

These are your “likely slow” endpoints; fix them after you have DTOs/projections available.
ID	Priority	Effort	Backend ticket	FE impact
BE-050	P1	L	Rewrite GET /api/countries/{id}/details as a single endpoint-specific query plan using DTO projections (no sequential entity fetch chains)	Yes (response shape likely changes slightly)
BE-051	P1	M	Ensure rankings endpoints sort/filter in DB only (no findAll() + in-memory sorting)	Possibly (if paging/sorting params added)
BE-052	P1	L	Optimize GET /api/analytics/summary: define response DTO + implement repository-level aggregates (JPQL/native SQL as needed)	Possibly (response shape becomes stable/explicit)
BE-053	P1	L	Optimize GET /api/analytics/launches-per-year/by-country: bucket by year in SQL + index support	Possibly
BE-054	P2	M	Optimize GET /api/analytics/records: compute MAX/records in SQL, not by scanning entities	Possibly
BE-055	P2	M	Add caching for analytics endpoints (Caffeine TTL)	No (transparent)

Acceptance criteria

    Query counts are predictable (avoid N+1).
    Analytics endpoints use DB aggregations.
    Heavy endpoints are paginated or capped; response sizes are controlled.

Epic BE-F — External sync “nice-to-have”, but operationally clean
ID	Priority	Effort	Backend ticket	FE impact
BE-060	P1	M	Make external sync fully optional: app.sync.enabled=false by default; never blocks startup	No
BE-061	P1	M	Add sync status persistence (sync_runs table) + GET /api/data-sync/status	Yes (if you want UI visibility)
BE-062	P2	M	Add resilience around TheSpaceDevs calls (timeouts + retry + circuit breaker)	No
BE-063	P2	S	Protect /api/data-sync/* endpoints (admin-only)	Yes (auth + admin UI)

Acceptance criteria

    You can tell last successful sync time + error reason via API/DB.
    Sync failures never silently “look like empty real data”.

Epic BE-G — Database discipline (migrations + constraints)
ID	Priority	Effort	Backend ticket	FE impact
BE-070	P0	M	Introduce Flyway + baseline migration; set ddl-auto=validate	No
BE-071	P1	M	Add DB constraints for natural keys + invariants (e.g. unique isoCode)	Possibly (409 conflicts become more common → FE handles)
BE-072	P2	M	Audit existing 50+ indexes using Postgres stats; remove unused, add composite indexes aligned to real queries	No
Epic BE-H — Observability + CI quality gates
ID	Priority	Effort	Backend ticket	FE impact
BE-080	P1	S	Add Actuator health/metrics endpoints (secure as needed)	No
BE-081	P1	M	Add integration tests using Testcontainers Postgres for key repositories/endpoints	No
BE-082	P2	S	Add SQL slow-query logging in non-prod + baseline perf test script	No
Epic BE-I — Structure refactor (maintainability)
ID	Priority	Effort	Backend ticket	FE impact
BE-090	P3	L	Gradually refactor from package-by-layer to package-by-feature (country/, engine/, analytics/, integration/)	No
BE-091	P3	S	Add ADRs for major decisions (DTO policy, migrations, sync policy, caching)	No
