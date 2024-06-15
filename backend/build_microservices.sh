#!/bin/bash

echo "Building shared libraries..."
(
  cd ./shared
  ./mvnw clean install
) &&
(
  echo "Building microservices..."
) &&
(
  echo "Building the users microservice..."
  cd ./users-ms
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the listings microservice..."
  cd ./listings-ms
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the eureka microservice..."
  cd ./eureka
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the gateway microservice..."
  cd ./gateway
  ./mvnw clean spring-boot:build-image
)