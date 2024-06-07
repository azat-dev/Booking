#!/bin/bash

echo "Building the microservices..."

echo "Building the users microservice..."
cd ./users-ms
./gradlew bootBuildImage

#echo "Building the listings microservice..."
#cd ./listings-ms
#./gradlew bootBuildImage
#
#echo "Building the eureka microservice..."
#cd ./eureka
#./mvnw spring-boot:build-image