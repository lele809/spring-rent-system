#!/bin/bash

# Build script to ensure correct directory execution
set -e

echo "Current directory: $(pwd)"
echo "Listing current directory contents:"
ls -la

# Ensure we are in the correct directory
if [ ! -f "package.json" ]; then
  echo "package.json not found, trying to navigate to house-fronted directory"
  if [ -d "house-fronted" ]; then
    cd house-fronted
    echo "Changed to house-fronted directory: $(pwd)"
  else
    echo "Error: Cannot find house-fronted directory or package.json"
    exit 1
  fi
fi

# Check if required files exist
if [ ! -f "index.html" ]; then
  echo "Error: index.html not found in $(pwd)"
  ls -la
  exit 1
fi

if [ ! -f "vite.config.ts" ]; then
  echo "Error: vite.config.ts not found in $(pwd)"
  ls -la
  exit 1
fi

echo "Found required files, starting build..."
echo "Running npm ci..."
npm ci

echo "Running npm run build..."
npm run build

echo "Build completed successfully!"
echo "Build output:"
ls -la dist/ 