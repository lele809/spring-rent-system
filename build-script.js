#!/usr/bin/env node

const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

console.log('Starting build process...');

try {
  // Check if we're in the root directory or need to navigate to house-fronted
  const frontendDir = path.join(__dirname, 'house-fronted');
  
  if (fs.existsSync(frontendDir) && fs.existsSync(path.join(frontendDir, 'package.json'))) {
    console.log('Building frontend in house-fronted directory...');
    process.chdir(frontendDir);
    
    // Run Vite build (includes TypeScript compilation)
    console.log('Running Vite build...');
    execSync('npx vite build', { stdio: 'inherit' });
    
    // Copy build output to root dist directory for deployment
    const distSource = path.join(process.cwd(), 'dist');
    const distTarget = path.join(__dirname, 'dist');
    
    if (fs.existsSync(distSource)) {
      console.log('Copying build output to root dist directory...');
      // Remove existing dist directory if it exists
      if (fs.existsSync(distTarget)) {
        fs.rmSync(distTarget, { recursive: true, force: true });
      }
      // Copy directory recursively
      fs.cpSync(distSource, distTarget, { recursive: true });
    }
    console.log('Frontend build completed successfully!');
  } else {
    console.log('No house-fronted directory found, skipping frontend build.');
  }
  
  console.log('Build completed successfully!');
} catch (error) {
  console.error('Build failed:', error.message);
  process.exit(1);
}
