# Alerts Microservice
[![Build](https://github.com/Covid-Alert-Microservices/alerts/actions/workflows/build.yaml/badge.svg)](https://github.com/Covid-Alert-Microservices/alerts/actions/workflows/build.yaml) [![Release](https://img.shields.io/github/v/release/Covid-Alert-Microservices/alerts)](https://github.com/Covid-Alert-Microservices/alerts/tags) [![ReleaseDate](https://img.shields.io/github/release-date/Covid-Alert-Microservices/alerts)](https://github.com/Covid-Alert-Microservices/alerts/tags)

## Description
This repository manages alerts for Covid-Alert.

## Environment variables

The following environment variables can be configured:
- `KEYCLOAK_URL`: the URL to the Keycloak instance (default: `http://localhost:5000`)
- `KAFKA_URL`: the URL to the Kafka node (default: `http://localhost:29092`)
- `POSTGRES_HOST`: the host for the PostgreSQL database (default: `localhost:5432/postgres`)
- `POSTGRES_USER`: the user for the PostgreSQL database (default: `postgres`)
- `POSTGRES_PASSWORD`: the password for the PostgreSQL database (default: `postgres`)