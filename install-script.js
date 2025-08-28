#!/usr/bin/env node

const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

console.log('Starting installation process...');

try {
  // Check if we're in the root directory or need to navigate to house-fronted
  const frontendDir = path.join(__dirname, 'house-fronted');
  
  if (fs.existsSync(frontendDir) && fs.existsSync(path.join(frontendDir, 'package.json'))) {
    console.log('Installing frontend dependencies in house-fronted directory...');
    process.chdir(frontendDir);
    execSync('npm ci', { stdio: 'inherit' });
  } else if (fs.existsSync('package.json')) {
    console.log('Installing dependencies in current directory...');
    execSync('npm ci', { stdio: 'inherit' });
  } else {
    throw new Error('No package.json found in current directory or house-fronted subdirectory');
  }
  
  console.log('Installation completed successfully!');
} catch (error) {
  console.error('Installation failed:', error.message);
  process.exit(1);
}