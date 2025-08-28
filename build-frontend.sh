#!/bin/bash

# Frontend build script for Render deployment
set -e

echo "=== Frontend Build Script Started ==="
echo "Current working directory: $(pwd)"

# List root directory contents
echo "Root directory contents:"
ls -la

# Check if house-fronted directory exists
if [ ! -d "house-fronted" ]; then
  echo "ERROR: house-fronted directory not found!"
  exit 1
fi

# Navigate to frontend directory
echo "Changing to house-fronted directory..."
cd house-fronted

echo "Frontend directory contents:"
ls -la

# Check for required files
if [ ! -f "package.json" ]; then
  echo "ERROR: package.json not found in house-fronted directory!"
  exit 1
fi

if [ ! -f "index.html" ]; then
  echo "ERROR: index.html not found in house-fronted directory!"
  exit 1
fi

if [ ! -f "vite.config.ts" ]; then
  echo "ERROR: vite.config.ts not found in house-fronted directory!"
  exit 1
fi

echo "All required files found. Starting build process..."

# Install dependencies
echo "Installing dependencies..."
npm ci

# Run build
echo "Running build command..."
npm run build

# Check build output
if [ -d "dist" ]; then
  echo "Build successful! Contents of dist directory:"
  ls -la dist/
else
  echo "ERROR: Build failed - dist directory not created"
  exit 1
fi

echo "=== Frontend Build Script Completed Successfully ===" 