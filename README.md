# Movie Recommender

Graph-powered movie recommendation platform built with Angular, Spring Boot microservices, and Neo4j.

The application lets users register/login, browse movies, rate titles, maintain a watchlist, and get collaborative/content-based recommendations.

## Tech Stack

- Frontend: Angular 18
- API Gateway: Spring Boot + Spring Cloud Gateway MVC + JWT auth
- Microservices: Spring Boot (User, Movie, Rating, Recommendation)
- Database: Neo4j (remote Aura or self-hosted)
- Containerization: Docker + Docker Compose
- Build Tools: Maven (backend), npm (frontend), Make (helper commands)

## Architecture Overview

1. Frontend calls API Gateway at `https://localhost:8443/api`.
2. API Gateway validates JWT (except login/register) and routes requests by path.
3. Domain services execute business logic and query/update Neo4j.
4. Frontend renders results and shows loading states for potentially slower graph queries.

High-level routing through the gateway:

- `/api/users/**` -> `user-service` (port 8081)
- `/api/movies/**` -> `movie-service` (port 8082)
- `/api/ratings/**` -> `rating-service` (port 8083)
- `/api/recommendations/**` -> `recommendation-service` (port 8084)

## Repository Structure

```text
movie-recommender/
|- backend/
|  |- api-gateway/
|  |- user-service/
|  |- movie-service/
|  |- rating-service/
|  |- recommendation-service/
|- frontend/
|- docker-compose.yml
|- Makefile
|- jar-files.sh
|- .env
```

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 18+ and npm
- Docker + Docker Compose (if using containerized backend)
- Neo4j database (Aura or local)

## Environment Variables

Project services read environment variables from root `.env`.

Required keys:

- `JWT_SECRET`: Base64-encoded JWT signing key used by gateway and user-service
- `KEYSTORE_PASSWORD`: Password for the https certificate
- `DB_URL`: Neo4j connection URL (for example `neo4j+s://...`)
- `DB_USERNAME`: Neo4j username
- `DB_PASSWORD`: Neo4j password

Example template:

```env
JWT_SECRET=your_base64_jwt_secret
KEYSTORE_PASSWORD=password
DB_URL=neo4j+s://your-instance.databases.neo4j.io
DB_USERNAME=neo4j
DB_PASSWORD=your_password
```

Important security note:

- Do not commit real credentials to Git.
- If credentials were committed in the past, rotate secrets immediately.

## Running the Project

### Option A: Dockerized backend + local frontend (recommended)

1. Build backend jars:

```bash
make jar
```

2. Build and start backend containers:

```bash
make build
make up
```

3. Start frontend:

```bash
cd frontend
npm install
npm start
```

4. Open:

- Frontend: `http://localhost:4200`
- API Gateway: `http://localhost:8080`

### Option B: Run everything locally without Docker

In separate terminals, run each backend service:

```bash
cd backend/user-service && ./mvnw spring-boot:run
cd backend/movie-service && ./mvnw spring-boot:run
cd backend/rating-service && ./mvnw spring-boot:run
cd backend/recommendation-service && ./mvnw spring-boot:run
cd backend/api-gateway && ./mvnw spring-boot:run
```

Then run frontend:

```bash
cd frontend
npm install
npm start
```

## Makefile Commands

From repository root:

```bash
make jar      # build all backend jars via jar-files.sh
make build    # docker compose build
make up       # docker compose up -d
make down     # docker compose down
make logs     # docker compose logs -f
make status   # docker compose ps
make images   # docker compose images
make restart  # restart containers
make remove   # remove images/volumes/orphans (destructive)
```

## Backend Services and Ports

| Service | Port | Responsibility |
|---|---:|---|
| api-gateway | 8080 | JWT auth, CORS, request routing |
| user-service | 8081 | Register/login, profile, watchlist |
| movie-service | 8082 | Movies catalog and movie details |
| rating-service | 8083 | Create/remove ratings |
| recommendation-service | 8084 | Collaborative/content/similar recommendations |

## API Quick Reference

Base URL (via gateway): `http://localhost:8080/api`

Public endpoints:

- `POST /users/auth/register`
- `POST /users/auth/login`

Protected endpoints (JWT required):

- `GET /users/profile/{id}`
- `POST /users/watchlist`
- `DELETE /users/watchlist`
- `GET /movies`
- `GET /movies/{id}`
- `POST /movies`
- `GET /movies/persons/{id}`
- `POST /ratings`
- `DELETE /ratings`
- `GET /recommendations/collaborative/{userId}`
- `GET /recommendations/content/{userId}`
- `GET /recommendations/similar/{movieId}`

Authorization header format:

```http
Authorization: Bearer <jwt_token>
```

## Frontend Pages

- `/login` login page
- `/register` registration page
- `/` home movie listing and filters
- `/movies/:id` movie details + rating + watchlist actions
- `/ratings` rating management
- `/recommendations` personalized recommendations
- `/watchlist` saved movies

Error routes used by interceptor:

- `/400`, `/401`, `/403`, `/404`, `/408`, `/409`, `/429`, `/500`, `/503`

## Neo4j Latency Notes

This project now includes UI handling for delayed graph queries:

- Global request loading indicator in app shell
- Skeleton loaders on heavy pages
- Empty states and actionable messages
- Status messages for rating/watchlist actions

If recommendation queries are slow, first validate:

1. Neo4j instance health and network latency
2. Query performance and indexes
3. Container memory/CPU limits

## Build and Test

Backend (per service):

```bash
./mvnw clean package
```

Frontend:

```bash
cd frontend
npm run build
npm test
```

## Troubleshooting

1. `401 Unauthorized` on protected endpoints:
Set token after login and include `Authorization: Bearer <token>`.

2. CORS issues from frontend:
Ensure frontend runs at `http://localhost:4200` and gateway is reachable at `http://localhost:8080`.

3. Backend services cannot connect to Neo4j:
Verify `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in `.env`.

4. Gateway starts but routes fail:
Check all backend services are healthy and listening on expected ports.

5. Docker starts but API not reachable:
Run `make logs` and inspect gateway/service startup errors.

## License

MIT License. See `LICENSE`.