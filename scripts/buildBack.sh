#!/bin/bash

echo "Building backend..."
cd ..

echo "Running Maven clean install..."
mvn clean install -Dtest="**/unit/**/*Test,**/unit/**/*Tests" -DfailIfNoTests=false

echo "Backend build completed!"

# Return to scripts directory
cd scripts || exit
