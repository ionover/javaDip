#!/bin/bash

echo "Stopping all containers..."
./estopAll.sh

echo "Building frontend..."
./createNewFront.sh

echo "Building backend..."
./buildBack.sh

echo "Generating init-db.sql from template..."
cd ..
# Загружаем переменные из .env файла
export $(grep -v '^#' .env | xargs)
# Генерируем init-db.sql из шаблона с подстановкой переменных
envsubst < init-db.sql.template > init-db.sql

echo "Starting all services with rebuild..."
docker compose -f docker-compose.yml up -d --build --force-recreate

echo "All services started!"

# Return to scripts directory
cd scripts || exit
