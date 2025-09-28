#!/bin/bash

echo "Building frontend..."
cd ../frontend || exit

echo "Installing dependencies..."
npm i

echo "Cleaning previous build..."
rm -rf dist

echo "Building frontend..."
npm run build

echo "Frontend build completed!"

# Return to scripts directory
cd ../scripts || exit