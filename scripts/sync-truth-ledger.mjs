#!/usr/bin/env node

/**
 * Truth Ledger Sync Script for Rocket Engine Backend
 *
 * This script syncs entities from Truth Ledger to the backend.
 * Truth Ledger is the source of truth for verified entity data.
 *
 * Usage:
 *   npm run sync              # Sync all from Truth Ledger (production)
 *   npm run sync -- --engines # Sync engines only
 *   npm run sync -- --vehicles # Sync launch vehicles only
 *   npm run sync -- --local   # Use localhost
 */

const PROD_URL = 'https://rocket-engine-backend.onrender.com';
const LOCAL_URL = 'http://localhost:8080';

async function syncFromTruthLedger() {
  const args = process.argv.slice(2);
  const useLocal = args.includes('--local');
  const enginesOnly = args.includes('--engines');
  const vehiclesOnly = args.includes('--vehicles');

  const baseUrl = useLocal ? LOCAL_URL : PROD_URL;

  console.log('🔄 Rocket Engine Backend - Truth Ledger Sync Tool');
  console.log('━'.repeat(50));
  console.log(`📡 Target: ${baseUrl}`);
  console.log(`📚 Source: Truth Ledger (verified entity data)`);
  console.log('');

  try {
    let endpoint;
    let description;

    if (enginesOnly) {
      endpoint = '/api/sync/truth-ledger/engines';
      description = 'Syncing engines from Truth Ledger...';
    } else if (vehiclesOnly) {
      endpoint = '/api/sync/truth-ledger/launch-vehicles';
      description = 'Syncing launch vehicles from Truth Ledger...';
    } else {
      endpoint = '/api/sync/truth-ledger/all';
      description = 'Syncing all entities from Truth Ledger...';
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

    console.log('✅ Sync completed successfully!');
    console.log('');
    console.log('📊 Results:');
    console.log(JSON.stringify(result, null, 2));
    console.log('');

    // Display summary based on result structure
    if (result.engines) {
      const e = result.engines;
      console.log(`   🔧 Engines: ${e.created || 0} created, ${e.updated || 0} updated (total: ${e.total || 0})`);
      if (e.errors > 0) {
        console.log(`      ⚠️  ${e.errors} errors`);
      }
    }
    if (result.launchVehicles) {
      const v = result.launchVehicles;
      console.log(`   🚀 Vehicles: ${v.created || 0} created, ${v.updated || 0} updated (total: ${v.total || 0})`);
      if (v.errors > 0) {
        console.log(`      ⚠️  ${v.errors} errors`);
      }
    }
    if (result.created !== undefined) {
      // Single entity type sync
      console.log(`   📦 Created: ${result.created}, Updated: ${result.updated}, Total: ${result.total}`);
      if (result.errors > 0) {
        console.log(`   ⚠️  Errors: ${result.errors}`);
      }
    }

    console.log('');
    console.log('💡 Note: Truth Ledger is the source of truth for verified entity data.');
    console.log('   Entities are created/updated based on verified facts from multiple sources.');

  } catch (error) {
    console.error('❌ Sync failed:', error.message);
    console.log('');
    console.log('💡 Tips:');
    console.log('   - Make sure the backend is running');
    console.log('   - Use --local flag for localhost:8080');
    console.log('   - Check if Truth Ledger is running and accessible');
    console.log('   - Check if the server is awake (Render cold start)');
    process.exit(1);
  }
}

syncFromTruthLedger();
