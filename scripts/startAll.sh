#!/bin/bash

echo "Stopping all containers..."
./estopAll.sh

echo "Building frontend..."
./createNewFront.sh

echo "Building backend..."
./buildBack.sh

echo "Starting all services..."
cd ..
docker compose -f docker-compose.yml up -d

echo "All services started!"