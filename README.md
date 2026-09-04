# PeakForm Coaching Portal — SIT218 Task 7.3HD

A single Spring Boot app containing **both** a deliberately vulnerable
implementation and a hardened implementation of five web application
vulnerabilities, so they can be demonstrated side by side against the same
running app and the same database.

## Running the app

Requires JDK 17 and Maven (or use `./mvnw` if you generate the wrapper).

```
cd peakform
mvn spring-boot:run
```

The app starts on **http://localhost:8080** using an embedded, file-based
H2 database (created automatically under `./data/`), so no separate DB
server setup is required for grading. Demo users and demo members are
seeded automatically on first run — see `DataSeeder.java`.

**Demo logins** (seeded, ROLE / username / password):
- ROLE_ADMIN  — `admin1` / `Admin@12345`
- ROLE_COACH  — `coach1` / `Coach@12345`
- ROLE_CLIENT — `client1` / `Client@12345`

### Running against MySQL instead (matches the unit's Kali VM)
Edit `src/main/resources/application.properties` per the commented block
at the bottom of that file, then run `src/main/resources/db/mysql-procedures.sql`
once against the `peakform` schema to create the stored procedure used by
the SQLi-prevention demo.

## URL map

| Vulnerability | Vulnerable demo | Fixed demo |
|---|---|---|
| 1. Stored XSS | `/vuln/notes` | `/secure/notes` |
| 2. SQL Injection | `/vuln/search?term=` | `/secure/search?term=` |
| 3. Unrestricted file upload | `/vuln/upload` | `/secure/upload` |
| 4. CSRF | `/vuln/profile/1` | `/secure/profile/1` (Task 3, custom token) and `/secure-auth/client` (Task 4, Spring Security default CSRF, login required) |
| 5. Data aggregation / inference | `/vuln/directory` | `/secure/directory` (requires COACH/ADMIN login) |

Exception handling demo: `/secure/member/1` (happy path) vs
`/secure/member/999` (controller-level `@ExceptionHandler`); any
unexpected error anywhere in the app falls through to the global
`@ControllerAdvice` handler. All errors are logged to `logs/peakform.log`.

## Attack walkthroughs (for the report / video)

**1. Stored XSS** — go to `/vuln/notes`, submit a note with content
`<script>alert(document.cookie)</script>`. The alert fires for anyone who
subsequently loads the page. Compare with `/secure/notes`: the same
payload is either rejected by validation or rendered as inert text.

**2. SQL Injection** — go to `/vuln/search`, search for `' OR '1'='1`
(returns every row regardless of name) or
`x' UNION SELECT id,username,password,role,1,1 FROM app_user -- ` (dumps
password hashes into the results table). The page also prints the exact
SQL string executed, so the injection is visible directly. Compare with
`/secure/search`, where the same input is treated as literal search text.

**3. Unrestricted upload** — go to `/vuln/upload`, upload a file named
`evil.html` containing `<script>alert('xss via upload')</script>`, then
open `/uploads/evil.html` directly — it executes in-browser. Compare with
`/secure/upload`, where the same file is rejected (wrong magic bytes for
any allowed extension) and, if it somehow passed, would be stored outside
the web-servable folder under a random filename anyway.

**4. CSRF** — open `docs/attack-poc/csrf-poc.html` (edit the `action` URL
first if not running on localhost:8080) from a different origin/tab while
a PeakForm session is active. It silently POSTs to
`/vuln/profile/update` and changes the member's email with no user
interaction. Replaying the identical PoC against `/secure/profile/update`
fails (custom CSRF token + Referer check both fail). Replaying it against
`/secure-auth/client/settings` fails with HTTP 403 (Spring Security's
built-in CSRF filter rejects it before any controller code runs).

**5. Data aggregation / inference** — go to
`/vuln/directory?suburb=Torquay&minAge=70&maxAge=85&keyword=cardiac` with
no login at all. This single query, built entirely from "harmless"
individual filters, uniquely identifies a specific member, their exact
medical condition, and the exact time window they're home alone. Compare
with `/secure/directory`: login is required (COACH/ADMIN), medical notes
are never returned in list results, age is generalised to a 10-year
bracket, the time slot is generalised to a day only, queries are
rate-limited, and every query is logged with the requesting user's identity.

## SAST / DAST

See the report (Task 2) for the manual static code review mapped to
CWE IDs and file/line locations, and the ZAP dynamic-scan walkthrough
against this app's running `/vuln/**` and `/secure/**` endpoints. Run
SonarQube/Semgrep and ZAP yourself against this codebase on your VM to
capture the actual tool-output screenshots the task requires — this
sandbox environment cannot run GUI tools.

## Project layout

```
com.peakform
 ├── model/            JPA entities (User, Role, Member, ProgressNote, UploadedFile)
 ├── repository/        Spring Data JPA repositories
 ├── vulnerable/        The 5 deliberately vulnerable controllers
 ├── secure/            The 5 fixed controllers + supporting services (Task 3)
 ├── security/           Spring Security config, auth, role-gated demo (Task 4)
 ├── exception/         Global + custom exceptions (Task 3)
 └── config/            Home/login controller, demo data seeder
```
