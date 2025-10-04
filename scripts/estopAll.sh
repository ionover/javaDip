#!/bin/bash

cd ..

echo "Stopping all containers..."
docker compose -f docker-compose.yml down -v

echo "All containers stopped!"

# Return to scripts directory
cd scripts || exit
