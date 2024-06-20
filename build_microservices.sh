#!/bin/bash

echo "Building shared libraries..."
(
  cd ./backend/microservices/shared
  ./mvnw clean install
) &&
(
  echo "Building microservices..."
) &&
(
  echo "Building the users microservice..."
  cd ./backend/microservices/users-ms
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the listings microservice..."
  cd ./backend/microservices/listings-ms
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the eureka microservice..."
  cd ./backend/microservices/eureka
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the gateway microservice..."
  cd ./backend/microservices/gateway
  ./mvnw clean spring-boot:build-image
) &&
 (
   echo "Building the webapp.."
   cd ./web-client
   ./build_image.sh
 )