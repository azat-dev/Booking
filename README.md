<div align="center">

  <h1 style="border-bottom: none">
      <b>Booking</b>
  </h1>

  <p>
    This is a demo project that demonstrates my coding style and skills.  😎<br/>
    The project contains airbnb like application that allows users to book accommodations
  </p>
</div>
<div align="center">

![swift](https://img.shields.io/badge/JAVA-ff3d29)
![SOLID](https://img.shields.io/badge/CQRS-0021Aa)
![SOLID](https://img.shields.io/badge/MICROSERVICES-f0207a)
![SOLID](https://img.shields.io/badge/DOMAIN_DRIVEN_DESIGN-f0207a)
![clean architecture](https://img.shields.io/badge/SPRING-00AA0e)
![SwiftLint](https://img.shields.io/badge/KAFKA-9400FF)

</div>

## Features

Main features of the project:

- Command Query Responsibility Segregation (CQRS)
- Domain Driven Design (DDD)
- E2E tests
- Unit tests
- Spring Boot

## Microservices

## Requirements

Make sure you have the following components installed before running the application:

- JDK 21 - project built with Oracle [OpenJDK 21](https://jdk.java.net/21/)
- Maven 3.9.2 - project includes Maven Wrapper (mvnw)
- Docker - to run the application in a Docker container.

## Running the application on local environment

```bash
# Clone this repository
$ git clone https://github.com/azat-dev/Booking.git

# Go into the repository
$ cd Booking

# Variant 1
# Run the app
$ ./build_microservices.sh

# Reads environment variables from .env file
$ docker-compose up -d

# OR

# Variant 2
$ ./run.sh
```

## API Documentation

The `backend/specs` files define the API specification using OpenAPI. It describes the available endpoints, routes, and
data models used.