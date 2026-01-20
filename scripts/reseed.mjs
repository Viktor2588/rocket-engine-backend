#!/usr/bin/env node

/**
 * Reseed Script for Rocket Engine Backend
 *
 * Usage:
 *   npm run reseed              # Reseed all (production)
 *   npm run reseed -- --engines # Reseed engines only
 *   npm run reseed -- --local   # Use localhost
 */

const PROD_URL = 'https://rocket-engine-backend.onrender.com';
const LOCAL_URL = 'http://localhost:8080';

async function reseed() {
  const args = process.argv.slice(2);
  const useLocal = args.includes('--local');
  const enginesOnly = args.includes('--engines');
  const vehiclesOnly = args.includes('--vehicles');

  const baseUrl = useLocal ? LOCAL_URL : PROD_URL;

  console.log('🚀 Rocket Engine Backend - Reseed Tool');
  console.log('━'.repeat(50));
  console.log(`📡 Target: ${baseUrl}`);
  console.log('');

  try {
    let endpoint;
    let description;

    if (enginesOnly) {
      endpoint = '/api/sync/reseed/engines';
      description = 'Reseeding engines...';
    } else if (vehiclesOnly) {
      endpoint = '/api/sync/reseed/launch-vehicles';
      description = 'Reseeding launch vehicles...';
    } else {
      endpoint = '/api/sync/reseed/all';
      description = 'Reseeding all data...';
    }

    console.log(`⏳ ${description}`);
    console.log('');

    const response = await fetch(`${baseUrl}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }

    const result = await response.json();

    console.log('✅ Reseed completed successfully!');
    console.log('');
    console.log('📊 Results:');
    console.log(JSON.stringify(result, null, 2));
    console.log('');

    if (result.engines) {
      console.log(`   🔧 Engines: ${result.engines.deleted} deleted → ${result.engines.seeded} seeded`);
    }
    if (result.launchVehicles) {
      console.log(`   🚀 Vehicles: ${result.launchVehicles.deleted} deleted → ${result.launchVehicles.seeded} seeded`);
    }
    if (result.seeded) {
      console.log(`   📦 Total seeded: ${result.seeded}`);
    }

  } catch (error) {
    console.error('❌ Reseed failed:', error.message);
    console.log('');
    console.log('💡 Tips:');
    console.log('   - Make sure the backend is running');
    console.log('   - Use --local flag for localhost:8080');
    console.log('   - Check if the server is awake (Render cold start)');
    process.exit(1);
  }
}

reseed();
