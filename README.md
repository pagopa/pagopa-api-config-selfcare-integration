# pagoPa Api Config - SelfCare integration

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pagopa_pagopa-api-config-selfcare-integration&metric=alert_status)](https://sonarcloud.io/dashboard?id=pagopa_pagopa-api-config-selfcare-integration)

Spring Application that exposes a subset of the APIs to manage configuration for EC/PSP on the _Nodo dei Pagamenti_,
needed by PagoPA SelfCare application.

- [pagoPa Api Config - SelfCare integration](#pagopa-api-config---selfcare-integration)
    * [Api Documentation 📖](#api-documentation---)
    * [Technology Stack](#technology-stack)
    * [Start Project Locally 🚀](#start-project-locally---)
        + [Prerequisites](#prerequisites)
        + [Run docker container](#run-docker-container)
    * [Develop Locally 💻](#develop-locally---)
        + [Prerequisites](#prerequisites-1)
        + [Run the project](#run-the-project)
        + [Spring Profiles](#spring-profiles)
        + [Testing 🧪](#testing---)
            - [Unit testing](#unit-testing)
            - [Integration testing](#integration-testing)
            - [Performance testing](#performance-testing)
    * [Contributors 👥](#contributors---)
        + [Mainteiners](#mainteiners)

---

## Api Documentation 📖

See
the [OpenApi 3 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-api-config-selfcare-integration/main/openapi/openapi.json)

---

## Technology Stack

- Java 11
- Spring Boot
- Spring Web
- Hibernate
- JPA

---

## Start Project Locally 🚀

### Prerequisites

- docker

### Run docker container

from `./docker` directory

`sh ./run_docker.sh dev`

ℹ️ Note: for PagoPa ACR is required the login `az acr login -n <acr-name>`

---

## Develop Locally 💻

### Prerequisites

- git
- maven
- jdk-11

### Run the project

Start the springboot application with this command for local test:

`mvn spring-boot:run -Dspring-boot.run.profiles=local`

or, for H2 tests:

`mvn spring-boot:run -Dspring-boot.run.profiles=h2`

Using the spring profile `local`, the Spring application connects to the H2 in-memory DB.  
For access to H2 console, use this url: http://localhost:8080/h2-console/

### Spring Profiles

- **local**: to develop locally, pointing to DEV database.
- **h2**: to develop locally, pointing to local H2 database.
- _default (no profile set)_: The application gets the properties from the environment (for Azure).

### Testing 🧪

#### Unit testing

To run the **Junit** tests:

`mvn clean verify`

#### Integration testing

From `./integration-test/src`

1. `yarn install`
2. `yarn test`

#### Performance testing

install [k6](https://k6.io/) and then from `./performance-test/src`

1. `k6 run --env VARS=local.environment.json --env TEST_TYPE=./test-types/load.json main_scenario.js`

---

## Contributors 👥

Made with ❤️ by PagoPa S.p.A.

### Mainteiners

See `CODEOWNERS` file
