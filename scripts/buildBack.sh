#!/bin/bash

echo "Building backend..."
cd ..

echo "Running Maven clean install..."
mvn clean install -DskipTests

echo "Backend build completed!"

# Return to scripts directory
cd scripts || exit