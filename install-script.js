#!/usr/bin/env node

const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

console.log('Starting installation process...');

try {
  // Only install frontend dependencies to avoid infinite recursion
  const frontendDir = path.join(__dirname, 'house-fronted');
  
  if (fs.existsSync(frontendDir) && fs.existsSync(path.join(frontendDir, 'package.json'))) {
    console.log('Installing frontend dependencies in house-fronted directory...');
    process.chdir(frontendDir);
    execSync('npm ci', { stdio: 'inherit' });
    console.log('Frontend installation completed successfully!');
  } else {
    console.log('No house-fronted directory found, skipping frontend installation.');
  }
} catch (error) {
  console.error('Installation failed:', error.message);
  process.exit(1);
}
