#!/bin/bash

(
  ./build_microservices.sh
) &&
(
  echo "Starting the microservices..."
  docker-compose up -d
)