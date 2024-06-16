#!/bin/bash

echo "Building shared libraries..."
(
  cd ./backend/shared
  ./mvnw clean install
) &&
(
  echo "Building microservices..."
) &&
(
  echo "Building the users microservice..."
  cd ./backend/users-ms
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the listings microservice..."
  cd ./backend/listings-ms
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the eureka microservice..."
  cd ./backend/eureka
  ./mvnw clean spring-boot:build-image
) &&
(
  echo "Building the gateway microservice..."
  cd ./backend/gateway
  ./mvnw clean spring-boot:build-image
) &&
 (
   echo "Building the webapp.."
   cd ./web-client
   ./build_image.sh
 )