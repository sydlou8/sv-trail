# Presentation Notes
## Understanding the Problem
1. User must travel to the last location to _**WIN**_ the game.
2. Game must keep track of a given amount of stats.
3. User will _**LOSE**_ the game if `cash` or `morale` reaches `0`
4. Game will have a choice of actions to choose from each turn.
5. When user chooses the option to travel, an Event will trigger (Weather or seeded event)
6. Must include at least 1 API
7. Game must include secrets that are not stored in the game.
8. Game must have a few meaningful tests.

## Tech Stack
1. Backend --> Spring Boot
    - explain spring boot and why i used it
2. Database -->  PostgreSQL
3. No Frontend due to time constraints --> can still play game on Swagger and Postman
4. Docker 

## The Classes
1. Explain the different models/repositories for the game.
    - Show the SQL chart
    - Explain JPA
2. Flyway and PostgreSQL for migrations and persistence
3. Explain DTOs -> requests and responses
4. Explain JWT
    - Login
    - Header
    - Filter + Endpoints
    - SecurityConfig
6. Explain use of Strategy Pattern (Bonus: Builder pattern)
    - Can add new strategies easily
7. Explain use of Factory Pattern 
9. API usage
    - .env and application.yaml
10. Demo
11. Things that gave me trouble:
    - typos
    - import errors
    - pending events
12. What I wish I added: