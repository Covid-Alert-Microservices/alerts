# Microservice template [![Build](https://github.com/Covid-Alert-Microservices/Microservice-template/actions/workflows/build.yaml/badge.svg)](https://github.com/Covid-Alert-Microservices/Microservice-template/actions/workflows/build.yaml)

This repository aims to provide a starter template for our microservices.
It provides the base configuration to integrate with other microservices as Keycloak.

## Currently shipped in the template

- Configuration of Spring Security to only accept authenticated request on `/api/**`
- Authentication of requests with Keycloak
- Listening and publishing of Kafka topics
- CI to build the application

## Environment variables

The following environment variables can be configured:
- `KEYCLOAK_URL`: the URL to the Keycloak instance (default: `http://localhost:5000`)
- `KAFKA_URL`: the URL to the Kafka node (default: `http://localhost:29092`)
- `POSTGRES_HOST`: the host for the PostgreSQL database (default: `localhost:5432/postgres`)
- `POSTGRES_USER`: the user for the PostgreSQL database (default: `postgres`)
- `POSTGRES_PASSWORD`: the password for the PostgreSQL database (default: `postgres`)

## Removing unused parts

### Keycloak

- Delete classes
  - `MyController`
  - `KeycloakSecurityConfig`
- Delete lines
  - Keycloak related lines in `application.properties`
  - Keycloak related lines in `build.gradle`

### Kafka

- Delete classes
  - `KafkaConfig`
  - `PingPongListener`
- Delete lines
  - Kafka related lines in `application.properties` in both `main` and `test` source-sets
  - Kafka related lines in `build.gradle`

### PostgreSQL

- Delete classes
  - `Movie`
  - `MoviesRepositories`
- Delete lines
  - Postgres related in `application.properties` in both `main` and `test` source-sets
  - Postgres related in `build.gradle`

### Redis

- Delete classes
  - `RedisController`
- Delete lines
  - Redis related in `build.gradle`