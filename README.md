# THE SILICON VALLEY TRAIL
## Table of Contents
1. [Download Instructions](#download-instructions)
2. [Considerations](#considerations)
3. [Milestones](#milestones)
4. [Design Trade-Offs](#design-trade-offs)

## Download Instructions
### Prerequisites

Install the following before cloning the project:

| Tool | Version | Purpose | Download |
|------|---------|---------|----------|
| **Docker Desktop** | Latest | Runs PostgreSQL in a container (port 5433 must be free) | https://www.docker.com/products/docker-desktop |
| **Postman** | Latest | Sending API requests (no frontend) | https://www.postman.com/downloads |
| **Java 25 JDK** | 25+ | Required only if running the app locally (outside Docker) | https://openjdk.org/projects/jdk/25/ or https://www.oracle.com/java/technologies/downloads/ |
| **Maven** | 3.9+ | Required only if running locally (or use `./mvnw` wrapper included) | https://maven.apache.org |

> If you only run the app via Docker, you do **not** need Java or Maven installed.

#### 1. Clone the repository
```bash
git clone https://github.com/sydlou8/sv-trail.git
cd sv-trail
```

#### 2. Create your .env file

Copy the template below into a file named .env in the project root:

```properties
# PostgreSQL
POSTGRES_HOST=localhost
POSTGRES_PORT=5433
POSTGRES_DB=svtrail
POSTGRES_USER=your_db_user
POSTGRES_PASSWORD=your_db_password

# JWT — use any long random string (32+ characters recommended)
JWT_SECRET=replace_this_with_a_long_random_secret_string
```

> **Important:** Never commit your .env file. It is already listed in .gitignore.

To generate a secure `JWT_SECRET` you can run:
```bash
openssl rand -hex 32
```

#### 3. Start the infrastructure (PostgreSQL)

```bash
docker compose up postgres -d
```
This starts:
- PostgreSQL on `localhost:5433` with the database `svtrail`

#### 4. Run the application

**Option A — locally with Maven (recommended for development):**
```bash
./mvnw spring-boot:run
```

**Option B — fully in Docker:**
```bash
docker compose up --build
```

> **Note:** When running fully in Docker, `POSTGRES_HOST` in your `.env` should be `postgres` (the container service name), not `localhost`. The `docker-compose.yml` sets this automatically for the app service.

The API will be available at: `http://localhost:8080`

---

### Playing the Game

Once the server is running, open Postman (or navigate to the Swagger UI).

#### Swagger UI (recommended)
http://localhost:8080/swagger-ui.html

All endpoints are documented there with descriptions, example request bodies, and available options for every field.

> **First time?** Click the **Authorize** button at the top of the page, paste your JWT token (without the `Bearer ` prefix), and click Authorize. All game endpoints require this.

#### Quick Start in Postman

**1. Register an account**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "yourname",
  "email": "you@example.com",
  "password": "yourpassword"
}
```

**2. Log in and copy your token**
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "yourname",
  "password": "yourpassword"
}
```
Copy the `token` value from the response.

**3. Set your Authorization header**

In Postman, on every subsequent request set:
```
Authorization: Bearer <paste token here>
```

**4. Create a game session**

Choose your role — it determines your starting stats:

| Role | Cash | Morale | Hype | Coffee | Freelance Pay |
|------|------|--------|------|--------|---------------|
| `BEGINNER_DEV` | $1,000 | 80 | 10 | 30 | 100 |
| `INTERMEDIATE_DEV` | $3,000 | 50 | 20 | 20 | 150 |
| `ADVANCED_DEV` | $5,000 | 30 | 30 | 10 | 200 |

```
POST http://localhost:8080/api/game
Content-Type: application/json

{ "role": "BEGINNER_DEV" }
```
Copy the `gameSessionId` from the response.

**5. Take turns**

Available actions each turn:

| Action | Effect |
|--------|--------|
| `TRAVEL` | Move to the next city (-$300). Triggers a random event. |
| `REST` | Recover morale (-5 hype) |
| `SUPPLY` | Buy 3 coffees (-$50) |
| `FREELANCE` | Earn cash (based on role + morale) |
| `HACKATHON` | 15% win: +$1000, +20 morale, +10 hype. Otherwise: -5 morale, -5 hype. Costs 1 coffee. |
| `DEBUG` | Fix 5 bugs. Costs 1 coffee. |

```
POST http://localhost:8080/api/game/{gameSessionId}/turn
Content-Type: application/json

{ "actionType": "TRAVEL" }
```

**6. Resolve pending events**

If `TRAVEL` triggers a story event, the response will include a `pendingEventId` and a list of `eventChoices`. You **must** resolve it before taking another turn:

```
POST http://localhost:8080/api/game/{gameSessionId}/event-choice
Content-Type: application/json

{ "choiceId": "<id from eventChoices list>" }
```

**7. Win or lose**

- You **win** by traveling through all cities and reaching the final city.
- You **lose** if your `cash` or `morale` drops to 0.

Check your current state anytime:
```
GET http://localhost:8080/api/game/{gameSessionId}/state
```

---
## Considerations 
### Design Patterns
**Strategy Pattern** — `ActionStrategy` interface with `TravelStrategy`, `RestStrategy`, `HackathonStrategy`, etc. Resolved via `ActionStrategyFactory`.

**Factory Pattern** — `EventFactory` generates randomized events, weighted by location and weather. 

**Builder Pattern** — `GameSession.builder()`, `EventResult.builder()` (Lombok-assisted).

**State Machine** — `GameState` enum: `ACTIVE`, `WON`, `LOST`. `GameSession` transitions driven by `TurnService`.

**Facade Pattern** — `GameService` exposes the simplified game API over `TurnService`, `EventService`, `ResourceService`.

### Technologies
| Technology | Consideration |
|------------|---------------|
| Spring Boot | A powerful core backend framework to structure my application with MVC, REST APIs, Dependency injection, etc |
| Docker | I decided to use docker to containerize my project. I have had issues previously where downloading the code then running it, does not run the game properly on different computers. Using docker will allow us to have an application with the correct environment and versions across different computers. |
| JWT | This is important for Authentication and Authorization. It provides stateless authentication, an added layer of security that is carried out by Spring Security to enforce authorization. Also allows us to track users in our game. |
| PostgreSQL | It is a relational database using SQL, making it reliable for persistent game and user data |
| Flyway | This allows us to easily manage our versioned database schema migrations. If we needed to add more columns to a table, we can do so without deleting all our data and having to restore it. We just keep track of our tables and additions with V1, V2,..., Vn. Kind of like Git commits. |

### Notable Libraries
| Library | Description |
|---------|-------------|
| **Lombok** | Uses compile-time annotations to reduce boilerplate code, making code look cleaner |
| **jjwt** (0.12.6) | Generates and validates JWT tokens for stateless authentication |
| **springdoc-openapi** (2.8.0) | Auto-generates Swagger UI from annotations, available at `/swagger-ui.html` |
| **Testcontainers** | Spins up a real PostgreSQL container during integration tests for accurate test environments |

## Milestones
#### Day 1: Foundation
1. Docker Set Up
2. PostgreSQL Set Up
4. Configuration
5. Base Flyway Migrations
6. JWT Auth Set Up

#### Day 2: Game Data
1. Create models: entities, enums, etc
2. Seed migrations

#### Day 3: Turn Service
> This day is where we start coding some common design patterns like Strategy, Factory, etc

#### Day 4-5: Events and API Integration
1. Learn the Open-Mateo API for weather based events (no API key necessary) 
2. Integrate Seeded Events

#### Day 6-7: Polishing and Testing
1. Fix any bugs
2. Build JUnit Tests
3. work on README

## Design Trade-Offs
Initially, I had a lot of features planned. Some of these included:
1. Scoreboard
2. GitHub API for trending repos
3. A React (Vite) frontend

Time was just not on my side. I needed to get what I had fully functioning without any issues.

I tried my best to code the project with different design patterns to follow SOLID principles. However, again because of time, I missed the opportunity to showcase the Observer Pattern. I also had to consider whether or not if this would be overkill for my project 

### Issues
The biggest issues I encountered were syntax issues and import issues. There was an issue where I had to import two files with the same name, but since Java doesn't natively support import aliasing, I had one of two options:
```java
public class ExampleAlias extends ExampleClass {

}
```
and importing that class instead... or the second option would have been explicitly call the class like `java.util.List`

I just ended up calling them explicitly.

Another issue I had was when I was running Spring Boot. A lot of times my game would not even run because there would be a tiny typos that would have to search for in the project.

During testing, I discovered that `pendingEvent` and its related fields weren't populating correctly, which meant traveling between cities worked without events firing. I also discovered there was no check preventing a new turn while a pending event existed — both were fixed.

## Troubleshooting

### Flyway checksum mismatch
If you see `Migration checksum mismatch for migration version X`, run:
```bash
./mvnw flyway:repair \
  -Dflyway.url=jdbc:postgresql://localhost:5433/svtrail \
  -Dflyway.user=<POSTGRES_USER> \
  -Dflyway.password=<POSTGRES_PASSWORD>
```
Then restart the app normally.
