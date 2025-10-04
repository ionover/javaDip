#!/bin/bash

echo "Stopping all containers..."
./estopAll.sh

echo "Building frontend..."
./createNewFront.sh

echo "Building backend..."
./buildBack.sh

echo "Starting all services with rebuild..."
cd ..
docker compose -f docker-compose.yml up -d --build --force-recreate

echo "All services started!"

# Return to scripts directory
cd scripts || exit
