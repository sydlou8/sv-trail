# THE SILICON VALLEY TRAIL
## Table of Contents
1. Introduction
2. Download Instructions
3. Considerations
4. Milestones
5. 

## Introduction

## Download Instructions
### System Requirements
## Considerations
### Core Requirements
The game itself felt like a simple game with mangeageable logic. You select different choices which fire different events. You lose when you reach 
### Design Patterns
**Strategy Pattern** — `ActionStrategy` interface with `TravelStrategy`, `RestStrategy`, `HackathonStrategy`, etc. Resolved via `ActionStrategyFactory`.

**Factory Pattern** — `EventFactory` generates randomized events, weighted by location, weather, and GitHub data.

**Builder Pattern** — `GameSession.builder()`, `EventResult.builder()` (Lombok-assisted).

**State Machine** — `GameState` enum: `ACTIVE`, `WON`, `LOST`. `GameSession` transitions driven by `TurnService`.

**Observer Pattern** — `GameEventPublisher` notifies `StatChangeObserver` and `GameStateObserver` when resources change (lays groundwork for WebSocket push later).

**Facade Pattern** — `GameService` exposes the simplified game API over `TurnService`, `EventService`, `ResourceService`.

### Technologies
>|Technology|Consideration|
>|-|-|
>|Spring Boot|A powerful core backend framework to structure my application with MVC, REST APIs, Dependency injection, etc|
>|Docker| I decided to use docker to containerize my project. I have had issues previously where downloading the code then running it, does not run the game properly on different computers. Using docker will allow us to have an application with the correct environment and versions across different computers.|
>|Redis|This is important for caching data so that we don't have to keep polling external APIs. NOTE: We ended up not using this due to time issues.|
>|JWT| This is important for Authentication and Authorization. It provides stateless authentication, an  added layer of security that is carried out by Spring Security to enforce authorization. Also allows us to track users in our game.|
>|PostgreSQL|It is a relational database using SQL, making it reliable for persistent game and user data|
>|Flyway|This allows us to easily manage our versioned database schema migrations. If we needed to add more columns to a table, we can do so without deleting all our data and having to restore it. We just keep track of our tables and additions with V1, V2,..., Vn. Kind of like Git commits.|

### Notable Libraries
|Library|Description
|-|-|
|Lombok|Uses compile-time annotations to reduce boilerplate code, making code look cleaner|

## Milestones
#### Day 1: Foundation
1. Docker Set Up
2. PostgreSQL Set Up
3. Redis Set Up
4. Configuration
5. Base Flyway Migrations
6. JWT Auth Set Up

#### Day 2: Game Data
1. Create models: entities, enums, etc
2. Seed migrations

#### Day 3: Turn Engine
This day is where we start coding some common design patterns like Strategy, Factory, etc
#### Day 4: Events and API Integration
#### Day 5: Polishing and Testing p1
#### Day 6: Extra features
#### Day 7: Polishing and Testing p2
