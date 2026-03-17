# Football API (backend)

Spring Boot REST API for the Football Tournament website – manages teams, players, matches, statistics and admin operations.

After reading the exam task I immediately started thinking in terms of SQL tables and queries (INNER JOINs and sub‑queries). Because of that, the core part of the solution for finding pairs of players who have played together is implemented primarily with SQL rather than only with in‑memory Java logic.

## Main features

- **Public read APIs** – browse matches, teams, players, groups, standings and player statistics.
- **Role-based security with JWT** – email/password login, roles `ROLE_USER`, `ROLE_EDITOR`, `ROLE_ADMIN`.
- **Google login** – OAuth2 client configured for Google; successful login returns a JWT used by the frontend.
- **Admin & editor APIs** – CRUD endpoints for teams, players and matches, CSV import endpoints (admin only).
- **User management** – list users, change roles, ban/unban users (admin only).
- **Password reset** – email-based “forgot password” flow with expiring reset tokens.

## Docker & deployment

- Dockerfile builds a runnable JAR image on top of Eclipse Temurin JRE 17 (exposes port **8091**).
- `deploy/docker-compose.yml` defines a `football-api` service used on the Hetzner Linux server.
- Environment is configured via `/root/secrets/football-api.env` (mail credentials, Google OAuth secret) and `FRONTEND_URL` in `docker-compose.yml`.
- GitHub Actions workflow `.github/workflows/deploy.yml` builds and pushes the Docker image to Docker Hub and deploys to Hetzner via SSH on every push to `main`.

## Related frontend

The React frontend for this API lives here:  
`https://github.com/Almishev/sirma-football-ui`

Deployed application (frontend + backend):  
`http://95.216.141.216.nip.io/`

## Notes

- Database: PostgreSQL (Neon in production).
- Authentication: stateless JWT, `Authorization: Bearer <token>`.
- CSV import order: **teams → players → matches → records**.
## Original Bulgarian README (translated)

The original Bulgarian README repeated the same information in Bulgarian. In short, it described:

- public read APIs for matches, teams, players, groups, standings and statistics;
- role‑based security with JWT and roles `ROLE_USER`, `ROLE_EDITOR`, `ROLE_ADMIN`;
- Google login implemented with Spring Security OAuth2, returning a JWT consumed by the frontend;
- admin and editor CRUD APIs for teams, players and matches, plus CSV import endpoints (admin only);
- user management APIs for listing users, changing roles and banning accounts;
- a “forgot password” flow using email and expiring reset tokens.

It also pointed to the React frontend repository  
`https://github.com/Almishev/sirma-football-ui`  
and the deployed application at  
`http://95.216.141.216.nip.io/`,  
noting that PostgreSQL (Neon) is used in production, JWT is stateless, and CSV files should be imported in the order **teams → players → matches → records**.

