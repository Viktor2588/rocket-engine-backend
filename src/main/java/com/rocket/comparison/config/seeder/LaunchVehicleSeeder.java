package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.LaunchVehicle;
import com.rocket.comparison.repository.LaunchVehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Seeds LaunchVehicle entities with initial data.
 * Contains 220+ launch vehicles from Truth Ledger.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LaunchVehicleSeeder implements EntitySeeder {

    private final LaunchVehicleRepository launchVehicleRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding launch vehicles...");
            seedLaunchVehicles();
            log.info("Seeded {} launch vehicles", count());
        }
    }

    @Override
    public String getEntityName() {
        return "launch vehicles";
    }

    @Override
    public long count() {
        return launchVehicleRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedLaunchVehicles() {
        // ==================== SpaceX Launch Vehicles ====================
        createVehicle("Falcon 1", "Falcon", null, "USA", "SpaceX", 21.3, 1.7, 38555.0, 2, 670, null, "Merlin 1A/1C", 1, "Kestrel", 1, "RP-1/LOX", 454L, "Retired", 2006, 2009, 5, 2, false, false, false, bd("7000000"), bd("10448"));
        createVehicle("Falcon 9", "Falcon", null, "USA", "SpaceX", 70.0, 3.7, 549054.0, 2, 22800, 8300, "Merlin 1D++", 9, "Merlin 1D Vac", 1, "RP-1/LOX", 7607L, "Active", 2010, null, 350, 348, true, true, true, bd("67000000"), bd("2939"));
        createVehicle("Falcon 9 v1.0", "Falcon", "v1.0", "USA", "SpaceX", 54.9, 3.7, 333400.0, 2, 10450, 4540, "Merlin 1C", 9, "Merlin 1C Vac", 1, "RP-1/LOX", 4940L, "Retired", 2010, 2013, 5, 5, false, false, false, bd("54000000"), bd("5167"));
        createVehicle("Falcon 9 v1.1", "Falcon", "v1.1", "USA", "SpaceX", 68.4, 3.7, 505846.0, 2, 13150, 4850, "Merlin 1D", 9, "Merlin 1D Vac", 1, "RP-1/LOX", 5885L, "Retired", 2013, 2016, 15, 14, false, false, false, bd("56500000"), bd("4297"));
        createVehicle("Falcon 9 Full Thrust", "Falcon", "FT", "USA", "SpaceX", 70.0, 3.7, 549054.0, 2, 22800, 8300, "Merlin 1D+", 9, "Merlin 1D+ Vac", 1, "RP-1/LOX", 7607L, "Retired", 2015, 2018, 35, 34, true, false, true, bd("62000000"), bd("2719"));
        createVehicle("Falcon 9 Block 5", "Falcon", "Block 5", "USA", "SpaceX", 70.0, 3.7, 549054.0, 2, 22800, 8300, "Merlin 1D++", 9, "Merlin 1D Vac++", 1, "RP-1/LOX", 7607L, "Active", 2018, null, 320, 318, true, true, true, bd("67000000"), bd("2939"));
        createVehicle("Falcon Heavy", "Falcon Heavy", null, "USA", "SpaceX", 70.0, 12.2, 1420788.0, 2, 63800, 26700, "Merlin 1D++", 27, "Merlin 1D Vac++", 1, "RP-1/LOX", 22819L, "Active", 2018, null, 12, 12, true, false, true, bd("150000000"), bd("2351"));
        createVehicle("Falcon Heavy Block 5", "Falcon Heavy", "Block 5", "USA", "SpaceX", 70.0, 12.2, 1420788.0, 2, 63800, 26700, "Merlin 1D++", 27, "Merlin 1D Vac++", 1, "RP-1/LOX", 22819L, "Active", 2018, null, 12, 12, true, false, true, bd("150000000"), bd("2351"));
        createVehicle("Super Heavy", "Starship", "Booster", "USA", "SpaceX", 71.0, 9.0, 3600000.0, 1, null, null, "Raptor 2", 33, null, null, "CH4/LOX", 74500L, "In Development", 2023, null, 7, 4, true, false, true, null, null);
        createVehicle("Super Heavy V3", "Starship", "Booster V3", "USA", "SpaceX", 71.0, 9.0, 3600000.0, 1, null, null, "Raptor 3", 33, null, null, "CH4/LOX", 80000L, "In Development", 2025, null, 0, 0, true, false, false, null, null);
        createVehicle("Starship", "Starship", null, "USA", "SpaceX", 121.0, 9.0, 5000000.0, 2, 150000, 100000, "Raptor 2", 33, "Raptor 2 Vac", 6, "CH4/LOX", 74500L, "In Development", 2023, null, 7, 4, true, true, true, bd("100000000"), bd("667"));
        createVehicle("Starship HLS", "Starship", "HLS", "USA", "SpaceX", 50.0, 9.0, null, 1, null, null, "Raptor 2 Vac", 6, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, true, false, null, null);
        createVehicle("Starship V2", "Starship", "V2", "USA", "SpaceX", 121.0, 9.0, 5000000.0, 2, 150000, 100000, "Raptor 2", 33, "Raptor 2 Vac", 6, "CH4/LOX", 74500L, "In Development", 2024, null, 3, 2, true, true, true, null, null);
        createVehicle("Starship V3", "Starship", "V3", "USA", "SpaceX", 121.0, 9.0, 5000000.0, 2, 200000, 100000, "Raptor 3", 33, "Raptor 3 Vac", 6, "CH4/LOX", 80000L, "In Development", 2025, null, 0, 0, true, true, false, null, null);
        createVehicle("Starship V4", "Starship", "V4", "USA", "SpaceX", 121.0, 9.0, 5500000.0, 2, 250000, 100000, "Raptor 4", 35, "Raptor 4 Vac", 6, "CH4/LOX", 90000L, "In Development", null, null, 0, 0, true, true, false, null, null);
        createVehicle("Starship Tanker", "Starship", "Tanker", "USA", "SpaceX", 50.0, 9.0, null, 1, null, null, "Raptor 2 Vac", 3, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Starship Depot", "Starship", "Depot", "USA", "SpaceX", 50.0, 9.0, null, 1, null, null, null, null, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Starship Cargo", "Starship", "Cargo", "USA", "SpaceX", 50.0, 9.0, null, 1, 100000, null, "Raptor 2 Vac", 6, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);

        // SpaceX Spacecraft
        createVehicle("Dragon", "Dragon", "1", "USA", "SpaceX", 6.1, 3.7, 4200.0, 1, null, null, "Draco", 18, null, null, "MMH/NTO", 4L, "Retired", 2010, 2020, 23, 22, true, false, false, null, null);
        createVehicle("Dragon 2", "Dragon", "2", "USA", "SpaceX", 8.1, 4.0, 12519.0, 1, null, null, "SuperDraco", 8, null, null, "MMH/NTO", 71L, "Active", 2019, null, 27, 27, true, true, true, null, null);
        createVehicle("Crew Dragon", "Dragon", "Crew", "USA", "SpaceX", 8.1, 4.0, 12519.0, 1, null, null, "SuperDraco", 8, null, null, "MMH/NTO", 71L, "Active", 2020, null, 15, 15, true, true, true, null, null);
        createVehicle("Cargo Dragon", "Dragon", "Cargo", "USA", "SpaceX", 8.1, 4.0, 12519.0, 1, null, null, "Draco", 16, null, null, "MMH/NTO", 4L, "Active", 2020, null, 12, 12, true, false, true, null, null);
        createVehicle("Dragon XL", "Dragon", "XL", "USA", "SpaceX", null, 5.0, null, 1, null, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, true, false, false, null, null);

        // ==================== Blue Origin ====================
        createVehicle("New Shepard", "New Shepard", null, "USA", "Blue Origin", 18.0, 3.7, 75000.0, 1, null, null, "BE-3", 1, null, null, "LH2/LOX", 490L, "Active", 2015, null, 25, 24, true, true, true, null, null);
        createVehicle("New Glenn", "New Glenn", null, "USA", "Blue Origin", 98.0, 7.0, 1400000.0, 2, 45000, 13000, "BE-4", 7, "BE-3U", 2, "CH4/LOX", 17100L, "Active", 2025, null, 1, 1, true, false, true, bd("68000000"), bd("1511"));

        // ==================== ULA ====================
        createVehicle("Atlas I", "Atlas", "I", "USA", "General Dynamics", 43.9, 3.05, 164300.0, 2, 2340, 1000, "MA-5A", 3, "RL-10A", 2, "RP-1/LOX+LH2", 1910L, "Retired", 1990, 1997, 11, 8, false, false, false, null, null);
        createVehicle("Atlas II", "Atlas", "II", "USA", "Lockheed Martin", 47.5, 3.05, 204300.0, 2, 6580, 2810, "MA-5A", 3, "RL-10A", 2, "RP-1/LOX+LH2", 2024L, "Retired", 1991, 2004, 63, 63, false, false, false, null, null);
        createVehicle("Atlas III", "Atlas", "III", "USA", "Lockheed Martin", 52.8, 3.05, 214380.0, 2, 8640, 4055, "RD-180", 1, "RL-10A", 1, "RP-1/LOX+LH2", 3827L, "Retired", 2000, 2005, 6, 6, false, false, false, null, null);
        createVehicle("Atlas V", "Atlas V", null, "USA", "ULA", 58.3, 3.81, 334500.0, 2, 18850, 8900, "RD-180", 1, "RL-10A/C", 1, "RP-1/LOX+LH2", 3827L, "Active", 2002, null, 100, 100, false, false, true, bd("110000000"), bd("5836"));
        createVehicle("Atlas V 401", "Atlas V", "401", "USA", "ULA", 58.3, 3.81, 334500.0, 2, 9797, 4950, "RD-180", 1, "RL-10A", 1, "RP-1/LOX+LH2", 3827L, "Active", 2002, null, 65, 65, false, false, true, bd("109000000"), bd("11125"));
        createVehicle("Atlas V 551", "Atlas V", "551", "USA", "ULA", 62.2, 3.81, 540000.0, 2, 18850, 8900, "RD-180", 1, "RL-10A", 1, "RP-1/LOX+LH2+SRBs", 4152L, "Active", 2006, null, 12, 12, false, false, true, bd("153000000"), bd("8117"));
        createVehicle("Atlas V N22", "Atlas V", "N22", "USA", "ULA", 58.3, 3.81, 388000.0, 2, 13600, null, "RD-180", 1, "RL-10C", 2, "RP-1/LOX+LH2+SRBs", 4152L, "Active", 2019, null, 4, 4, false, true, true, bd("140000000"), bd("10294"));
        createVehicle("Atlas-Agena", "Atlas", "Agena", "USA", "Convair/Lockheed", 32.0, 3.05, 120000.0, 2, 4500, null, "MA-5", 3, "Agena", 1, "RP-1/LOX+UDMH", 1700L, "Retired", 1960, 1978, 109, 91, false, false, false, null, null);
        createVehicle("Atlas-Centaur", "Atlas", "Centaur", "USA", "General Dynamics", 42.0, 3.05, 148000.0, 2, 4200, 1815, "MA-5", 3, "RL-10", 2, "RP-1/LOX+LH2", 1740L, "Retired", 1962, 1983, 63, 55, false, false, false, null, null);
        createVehicle("Delta II", "Delta", "II", "USA", "Boeing/ULA", 39.0, 2.44, 231870.0, 3, 6100, 2170, "RS-27A", 1, "AJ10-118K", 1, "RP-1/LOX+Solid", 2850L, "Retired", 1989, 2018, 155, 153, false, false, false, bd("65000000"), bd("10656"));
        createVehicle("Delta III", "Delta", "III", "USA", "Boeing", 35.0, 4.0, 301450.0, 2, 8290, 3810, "RS-27A", 1, "RL-10B", 1, "RP-1/LOX+LH2+Solid", 3060L, "Retired", 1998, 2000, 3, 1, false, false, false, null, null);
        createVehicle("Delta IV", "Delta IV", null, "USA", "ULA", 63.0, 5.1, 249500.0, 2, 9420, 4440, "RS-68", 1, "RL-10B-2", 1, "LH2/LOX", 2950L, "Retired", 2002, 2019, 29, 28, false, false, false, bd("164000000"), bd("17410"));
        createVehicle("Delta IV Heavy", "Delta IV", "Heavy", "USA", "ULA", 72.0, 5.1, 733000.0, 2, 28790, 14220, "RS-68A", 3, "RL-10B-2", 1, "LH2/LOX", 9420L, "Retired", 2004, 2024, 16, 15, false, true, false, bd("350000000"), bd("12157"));
        createVehicle("Vulcan Centaur", "Vulcan", "VC2S", "USA", "ULA", 61.6, 5.4, 546700.0, 2, 27200, 14400, "BE-4", 2, "RL-10C", 2, "CH4/LOX+LH2", 4900L, "Active", 2024, null, 3, 3, false, false, true, bd("110000000"), bd("4044"));
        createVehicle("Vulcan Centaur VC2", "Vulcan", "VC2", "USA", "ULA", 61.6, 5.4, 546700.0, 2, 18140, null, "BE-4", 2, "RL-10C", 2, "CH4/LOX+LH2", 4900L, "Active", 2024, null, 2, 2, false, false, true, bd("100000000"), bd("5513"));
        createVehicle("Centaur", "Centaur", null, "USA", "ULA", 12.7, 3.05, 23000.0, 1, null, null, "RL-10C", 2, null, null, "LH2/LOX", 220L, "Active", 1963, null, 250, 240, false, false, true, null, null);

        // ==================== Rocket Lab ====================
        createVehicle("Electron", "Electron", null, "NZL", "Rocket Lab", 18.0, 1.2, 12550.0, 2, 300, null, "Rutherford", 9, "Rutherford Vac", 1, "RP-1/LOX", 224L, "Active", 2017, null, 55, 50, true, false, true, bd("7500000"), bd("25000"));
        createVehicle("Electron Heavy", "Electron", "Heavy", "NZL", "Rocket Lab", 18.0, 2.4, 25000.0, 2, 600, null, "Rutherford", 18, "Rutherford Vac", 1, "RP-1/LOX", 448L, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Neutron", "Neutron", null, "NZL", "Rocket Lab", 40.0, 4.5, 480000.0, 2, 13000, 1500, "Archimedes", 9, "Archimedes Vac", 1, "CH4/LOX", 3600L, "In Development", 2025, null, 0, 0, true, false, false, bd("50000000"), bd("3846"));

        // ==================== Firefly ====================
        createVehicle("Firefly Alpha", "Alpha", null, "USA", "Firefly Aerospace", 29.0, 1.8, 54000.0, 2, 1170, 630, "Reaver 1", 4, "Lightning 1", 1, "RP-1/LOX", 736L, "Active", 2021, null, 9, 6, false, false, true, bd("15000000"), bd("12821"));

        // ==================== Relativity ====================
        createVehicle("Terran 1", "Terran", "1", "USA", "Relativity Space", 33.5, 2.3, 90700.0, 2, 1250, null, "Aeon 1", 9, "Aeon Vac", 1, "CH4/LOX", 900L, "In Development", 2023, null, 1, 0, false, false, false, bd("12000000"), bd("9600"));
        createVehicle("Terran R", "Terran", "R", "USA", "Relativity Space", 66.0, 5.0, 380000.0, 2, 20000, null, "Aeon R", 7, "Aeon R Vac", 1, "CH4/LOX", 6800L, "In Development", null, null, 0, 0, true, false, false, bd("50000000"), bd("2500"));

        // ==================== Astra ====================
        createVehicle("Astra Rocket 3", "Rocket", "3", "USA", "Astra", 11.6, 1.3, 12500.0, 2, 150, null, "Delphin", 5, "Aether", 1, "RP-1/LOX", 140L, "Retired", 2020, 2022, 7, 2, false, false, false, bd("2500000"), bd("16667"));
        createVehicle("Rocket 4", "Rocket", "4", "USA", "Astra", 14.0, 1.5, 18000.0, 2, 300, null, "Delphin", 5, "Aether", 1, "RP-1/LOX", 180L, "Cancelled", null, null, 0, 0, false, false, false, null, null);

        // ==================== NASA/US Government ====================
        createVehicle("SLS", "SLS", null, "USA", "Boeing/NASA", 98.0, 8.4, 2608000.0, 2, 95000, 27000, "RS-25", 4, "RL-10", 1, "LH2/LOX+SRBs", 39000L, "Active", 2022, null, 2, 2, false, true, true, bd("2000000000"), bd("21053"));
        createVehicle("SLS Block 1", "SLS", "Block 1", "USA", "Boeing/NASA", 98.0, 8.4, 2608000.0, 2, 95000, 27000, "RS-25", 4, "RL-10", 1, "LH2/LOX+SRBs", 39000L, "Active", 2022, null, 2, 2, false, true, true, bd("2000000000"), bd("21053"));
        createVehicle("SLS Block 1B", "SLS", "Block 1B", "USA", "Boeing/NASA", 111.0, 8.4, 2930000.0, 2, 105000, 42000, "RS-25", 4, "RL-10C", 4, "LH2/LOX+SRBs", 39000L, "In Development", null, null, 0, 0, false, true, false, bd("2500000000"), bd("23810"));
        createVehicle("SLS Block 2", "SLS", "Block 2", "USA", "Boeing/NASA", 111.0, 8.4, 3300000.0, 2, 130000, 46000, "RS-25", 4, "RL-10C", 4, "LH2/LOX+SRBs", 43000L, "In Development", null, null, 0, 0, false, true, false, bd("3000000000"), bd("23077"));
        createVehicle("Ares I", "Ares", "I", "USA", "ATK/NASA", 94.0, 5.5, 907000.0, 2, 25000, null, "SRB", 1, "J-2X", 1, "Solid+LH2/LOX", 15800L, "Cancelled", 2009, 2009, 1, 1, false, true, false, null, null);
        createVehicle("Ares V", "Ares", "V", "USA", "Boeing/NASA", 116.0, 10.1, 3700000.0, 3, 187700, 71000, "RS-68B", 6, "J-2X", 1, "LH2/LOX+SRBs", 42400L, "Cancelled", null, null, 0, 0, false, true, false, null, null);
        createVehicle("Space Shuttle", "Space Shuttle", null, "USA", "NASA/Rockwell", 56.1, 8.7, 2030000.0, 2, 24400, null, "RS-25", 3, null, null, "LH2/LOX+SRBs", 30160L, "Retired", 1981, 2011, 135, 133, true, true, false, bd("450000000"), bd("18443"));

        // ==================== Other US ====================
        createVehicle("Antares", "Antares", null, "USA", "Northrop Grumman", 40.5, 3.9, 282000.0, 2, 6600, null, "RD-181", 2, "Castor 30", 1, "RP-1/LOX+Solid", 3265L, "Active", 2013, null, 20, 18, false, false, true, bd("80000000"), bd("12121"));
        createVehicle("Antares 230", "Antares", "230", "USA", "Northrop Grumman", 42.5, 3.9, 298000.0, 2, 8000, null, "RD-181", 2, "Castor 30XL", 1, "RP-1/LOX+Solid", 3844L, "Retired", 2016, 2023, 8, 8, false, false, false, bd("85000000"), bd("10625"));
        createVehicle("Antares 330", "Antares", "330", "USA", "Northrop Grumman", 42.5, 3.9, 298000.0, 2, 8500, null, "Miranda", 7, "Castor 30XL", 1, "CH4/LOX+Solid", 4200L, "In Development", 2025, null, 0, 0, false, false, false, null, null);
        createVehicle("Athena I", "Athena", "I", "USA", "Lockheed Martin", 18.9, 2.36, 66300.0, 2, 820, null, "Castor 120", 1, "OAM", 1, "Solid", 1700L, "Retired", 1995, 2001, 4, 3, false, false, false, null, null);
        createVehicle("Athena II", "Athena", "II", "USA", "Lockheed Martin", 28.2, 2.36, 120700.0, 3, 2065, 590, "Castor 120", 2, "OAM", 1, "Solid", 1700L, "Retired", 1998, 1999, 3, 2, false, false, false, null, null);
        createVehicle("Minotaur", "Minotaur", null, "USA", "Orbital ATK", 19.2, 1.67, 36200.0, 4, 580, null, "M55A1", 1, "Orion 38", 1, "Solid", 762L, "Active", 2000, null, 12, 12, false, false, true, bd("40000000"), bd("68966"));
        createVehicle("Minotaur I", "Minotaur", "I", "USA", "Orbital ATK", 19.2, 1.67, 36200.0, 4, 580, null, "M55A1", 1, "Orion 38", 1, "Solid", 762L, "Active", 2000, null, 12, 12, false, false, true, bd("40000000"), bd("68966"));
        createVehicle("Minotaur IV", "Minotaur", "IV", "USA", "Orbital ATK", 23.9, 2.34, 86300.0, 4, 1735, null, "SR118", 1, "Orion 38", 1, "Solid", 1600L, "Active", 2010, null, 7, 7, false, false, true, bd("55000000"), bd("31700"));
        createVehicle("Minotaur V", "Minotaur", "V", "USA", "Orbital ATK", 24.6, 2.34, 89400.0, 5, null, 532, "SR118", 1, "Star 48V", 1, "Solid", 1600L, "Active", 2013, null, 1, 1, false, false, true, bd("55000000"), null);
        createVehicle("Minotaur-C", "Minotaur", "C", "USA", "Orbital ATK", 27.6, 2.34, 77800.0, 4, 1458, null, "Castor 120", 1, "Orion 38", 1, "Solid", 1700L, "Active", 2017, null, 2, 1, false, false, true, bd("40000000"), bd("27434"));
        createVehicle("Pegasus", "Pegasus", null, "USA", "Orbital ATK", 16.9, 1.27, 18500.0, 3, 375, null, "Orion 50S", 1, "Orion 38", 1, "Solid", 726L, "Active", 1990, null, 45, 40, false, false, true, bd("40000000"), bd("106667"));
        createVehicle("Pegasus XL", "Pegasus", "XL", "USA", "Orbital ATK", 17.6, 1.3, 23130.0, 3, 443, null, "Orion 50SXLG", 1, "Orion 50XL", 1, "Solid", 726L, "Active", 1994, null, 39, 34, false, false, true, bd("40000000"), bd("90293"));
        createVehicle("LauncherOne", "LauncherOne", null, "USA", "Virgin Orbit", 21.3, 1.6, 30000.0, 2, 500, null, "NewtonThree", 1, "NewtonFour", 1, "RP-1/LOX", 327L, "Retired", 2020, 2023, 6, 4, false, false, false, bd("12000000"), bd("24000"));
        createVehicle("Omega", "Omega", null, "USA", "Northrop Grumman", 40.0, 3.7, 600000.0, 2, 10000, null, "Castor 600", 1, "Castor 300", 1, "Solid", 5000L, "Cancelled", null, null, 0, 0, false, false, false, null, null);
        createVehicle("RS1", "RS1", null, "USA", "ABL Space", 27.0, 1.8, 26000.0, 2, 1000, null, "E2", 9, "E2 Vac", 1, "RP-1/LOX", 500L, "Cancelled", 2023, 2023, 1, 0, false, false, false, bd("12000000"), bd("12000"));
        createVehicle("Dauntless", "Dauntless", null, "USA", "Impulse Space", null, null, null, 1, null, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Daytona", "Daytona", null, "USA", "Phantom Space", 32.0, 1.8, 30000.0, 2, 1200, null, null, 6, null, 1, "RP-1/LOX", 600L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("MLV", "MLV", null, "USA", "ULA", null, null, null, 2, null, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Nova", "Nova", null, "USA", "Stoke Space", 30.0, 4.0, null, 2, 5000, null, null, 15, null, 3, "LH2/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Vector-R", "Vector", "R", "USA", "Vector", 13.0, 1.2, 6000.0, 2, 60, null, null, 3, null, 1, "Propylene/LOX", 50L, "Cancelled", null, null, 0, 0, false, false, false, null, null);
        createVehicle("K1", "K1", null, "USA", "Kistler/Rocketplane", 36.9, 6.7, 382500.0, 2, 4600, 2100, "NK-33", 3, "NK-43", 1, "RP-1/LOX", 5100L, "Cancelled", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Phantom 1", "Phantom", "1", "USA", "Phantom Space", 18.0, 1.4, 14000.0, 2, 450, null, null, 5, null, 1, "RP-1/LOX", 300L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Vaya Space Dauntless", "Dauntless", null, "USA", "Vaya Space", null, null, null, 2, null, null, null, null, null, null, "Hybrid", null, "In Development", null, null, 0, 0, false, false, false, null, null);

        // ==================== Historic US ====================
        createVehicle("Saturn V", "Saturn", "V", "USA", "Boeing/NASA", 110.6, 10.1, 2970000.0, 3, 140000, 48600, "F-1", 5, "J-2", 6, "RP-1/LOX+LH2", 35100L, "Retired", 1967, 1973, 13, 12, false, true, false, bd("185000000"), bd("1321"));
        createVehicle("Saturn IB", "Saturn", "IB", "USA", "Chrysler/NASA", 68.1, 6.6, 589770.0, 2, 21000, null, "H-1", 8, "J-2", 1, "RP-1/LOX+LH2", 7100L, "Retired", 1966, 1975, 9, 9, false, true, false, bd("55000000"), bd("2619"));
        createVehicle("Titan II", "Titan", "II", "USA", "Martin Marietta", 33.2, 3.05, 154000.0, 2, 3600, null, "LR-87", 2, "LR-91", 1, "N2O4/Aerozine50", 1900L, "Retired", 1964, 2003, 106, 101, false, true, false, null, null);
        createVehicle("Titan III", "Titan", "III", "USA", "Martin Marietta", 47.3, 3.05, 632000.0, 3, 14700, null, "LR-87", 2, "RL-10", 2, "N2O4/Aerozine50+Solid", 10700L, "Retired", 1964, 1992, 65, 58, false, false, false, null, null);
        createVehicle("Titan IV", "Titan", "IV", "USA", "Lockheed Martin", 62.2, 3.05, 943000.0, 3, 21680, 8620, "LR-87", 2, "RL-10", 2, "N2O4/Aerozine50+Solid", 15200L, "Retired", 1989, 2005, 39, 35, false, false, false, bd("432000000"), bd("19926"));
        createVehicle("Thor", "Thor", null, "USA", "Douglas", 19.8, 2.44, 49900.0, 1, null, null, "LR-79", 1, null, null, "RP-1/LOX", 667L, "Retired", 1957, 1980, 224, 190, false, false, false, null, null);
        createVehicle("Thor-Delta", "Delta", "Thor", "USA", "Douglas/NASA", 27.0, 2.44, 54000.0, 2, 100, null, "MB-3", 1, "AJ10-118", 1, "RP-1/LOX", 670L, "Retired", 1960, 1972, 75, 68, false, false, false, null, null);
        createVehicle("Juno I", "Juno", "I", "USA", "ABMA/Chrysler", 21.2, 1.78, 29060.0, 4, 11, null, "A-7", 1, "Recruit", 11, "Alcohol/LOX+Solid", 370L, "Retired", 1958, 1959, 6, 3, false, false, false, null, null);
        createVehicle("Juno II", "Juno", "II", "USA", "ABMA/Chrysler", 24.0, 2.67, 54430.0, 4, 41, null, "S-3D", 1, "Recruit", 11, "RP-1/LOX+Solid", 667L, "Retired", 1958, 1961, 10, 6, false, false, false, null, null);
        createVehicle("Scout", "Scout", null, "USA", "Vought/LTV", 23.0, 1.01, 21500.0, 4, 210, null, "Algol", 1, "Altair", 1, "Solid", 535L, "Retired", 1960, 1994, 118, 104, false, false, false, null, null);
        createVehicle("Redstone", "Redstone", null, "USA", "Chrysler", 21.1, 1.78, 27500.0, 1, null, null, "A-7", 1, null, null, "Alcohol/LOX", 370L, "Retired", 1953, 1961, 6, 5, false, true, false, null, null);
        createVehicle("Jupiter", "Jupiter", null, "USA", "Chrysler", 27.4, 2.67, 50000.0, 1, null, null, "S-3D", 1, null, null, "RP-1/LOX", 667L, "Retired", 1957, 1963, 46, 36, false, false, false, null, null);
        createVehicle("Vanguard", "Vanguard", null, "USA", "Martin", 21.9, 1.14, 10050.0, 3, 9, null, "GE X-405", 1, "AJ10-37", 1, "RP-1/LOX+Solid", 125L, "Retired", 1957, 1959, 11, 3, false, false, false, null, null);

        // ==================== Russian ====================
        createVehicle("Soyuz", "Soyuz", null, "RUS", "RKK Energia", 46.3, 2.95, 312000.0, 3, 7800, 3250, "RD-107A", 4, "RD-0110", 1, "RP-1/LOX", 4220L, "Active", 1966, null, 1900, 1850, false, true, true, bd("48500000"), bd("6218"));
        createVehicle("Soyuz-2.1a", "Soyuz", "2.1a", "RUS", "RKK Energia", 46.3, 2.95, 312000.0, 3, 7800, 3250, "RD-107A", 4, "RD-0110", 1, "RP-1/LOX", 4220L, "Active", 2004, null, 75, 73, false, true, true, bd("48500000"), bd("6218"));
        createVehicle("Soyuz-2.1b", "Soyuz", "2.1b", "RUS", "RKK Energia", 46.3, 2.95, 312000.0, 3, 8200, 3250, "RD-107A", 4, "RD-0124", 1, "RP-1/LOX", 4220L, "Active", 2006, null, 85, 82, false, true, true, bd("48500000"), bd("5915"));
        createVehicle("Soyuz-2.1v", "Soyuz", "2.1v", "RUS", "TsSKB-Progress", 44.0, 2.95, 158000.0, 2, 2850, null, "NK-33", 1, "RD-0124", 1, "RP-1/LOX", 1631L, "Active", 2013, null, 12, 11, false, false, true, bd("35000000"), bd("12281"));
        createVehicle("Soyuz 5", "Soyuz", "5", "RUS", "RKK Energia", 61.9, 4.1, 530000.0, 2, 17000, null, "RD-171MV", 1, "RD-0124M", 1, "RP-1/LOX", 7260L, "In Development", null, null, 0, 0, false, true, false, null, null);
        createVehicle("Soyuz-7", "Soyuz", "7", "RUS", "RKK Energia", null, null, null, 2, null, null, null, null, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Proton", "Proton", null, "RUS", "Khrunichev", 58.2, 7.4, 705000.0, 4, 23000, 6920, "RD-276", 6, "RD-0210", 4, "N2O4/UDMH", 10000L, "Active", 1965, null, 430, 390, false, false, true, bd("65000000"), bd("2826"));
        createVehicle("Proton-M", "Proton", "M", "RUS", "Khrunichev", 58.2, 7.4, 705000.0, 4, 23000, 6920, "RD-276", 6, "RD-0210", 4, "N2O4/UDMH", 10000L, "Active", 2001, null, 115, 105, false, false, true, bd("65000000"), bd("2826"));
        createVehicle("Angara", "Angara", null, "RUS", "Khrunichev", 64.0, 8.86, 773000.0, 3, 24500, 5400, "RD-191", 5, "RD-0124A", 1, "RP-1/LOX", 9800L, "Active", 2014, null, 8, 7, false, false, true, bd("100000000"), bd("4082"));
        createVehicle("Angara 1", "Angara", "1.2", "RUS", "Khrunichev", 41.5, 2.9, 171000.0, 2, 3500, null, "RD-191", 1, "RD-0124A", 1, "RP-1/LOX", 1920L, "Active", 2014, null, 4, 3, false, false, true, bd("40000000"), bd("11429"));
        createVehicle("Angara A5", "Angara", "A5", "RUS", "Khrunichev", 64.0, 8.86, 773000.0, 3, 24500, 5400, "RD-191", 5, "RD-0124A", 1, "RP-1/LOX", 9800L, "Active", 2014, null, 7, 6, false, false, true, bd("100000000"), bd("4082"));
        createVehicle("Angara A5M", "Angara", "A5M", "RUS", "Khrunichev", 64.0, 8.86, 773000.0, 3, 27000, 6000, "RD-191M", 5, "RD-0124MS", 1, "RP-1/LOX", 10200L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Angara A5P", "Angara", "A5P", "RUS", "Khrunichev", 64.0, 8.86, 773000.0, 3, 24500, null, "RD-191", 5, "RD-0124A", 1, "RP-1/LOX", 9800L, "In Development", null, null, 0, 0, false, true, false, null, null);
        createVehicle("Amur", "Amur", null, "RUS", "Roscosmos", 55.0, 4.1, 360000.0, 2, 10500, null, "RD-0169", 5, "RD-0169V", 1, "CH4/LOX", 5000L, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Zenit", "Zenit", null, "UKR", "Yuzhnoye", 57.0, 3.9, 459000.0, 2, 13740, 4500, "RD-171", 1, "RD-120", 1, "RP-1/LOX", 7260L, "Retired", 1985, 2017, 84, 77, false, false, false, bd("35000000"), bd("2548"));
        createVehicle("Rokot", "Rokot", null, "RUS", "Khrunichev", 29.0, 2.5, 107000.0, 3, 1950, null, "RD-0233", 3, "RD-0235", 1, "N2O4/UDMH", 2100L, "Retired", 1994, 2019, 33, 30, false, false, false, null, null);
        createVehicle("Rokot-M", "Rokot", "M", "RUS", "Khrunichev", 29.0, 2.5, 107000.0, 3, 2150, null, "RD-0233M", 3, "RD-0235M", 1, "N2O4/UDMH", 2300L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Dnepr", "Dnepr", null, "UKR", "Yuzhmash", 34.0, 3.0, 211000.0, 3, 4500, null, "RD-264", 4, "RD-869", 1, "N2O4/UDMH", 4000L, "Retired", 1999, 2015, 22, 21, false, false, false, bd("29000000"), bd("6444"));
        createVehicle("Tsyklon", "Tsyklon", null, "UKR", "Yuzhnoye", 39.3, 3.0, 182000.0, 3, 4100, null, "RD-251", 3, "RD-861", 1, "N2O4/UDMH", 2700L, "Retired", 1967, 2009, 122, 116, false, false, false, null, null);
        createVehicle("Kosmos", "Kosmos", "3M", "RUS", "Polyot", 32.4, 2.4, 109000.0, 2, 1500, null, "RD-216M", 2, "RD-219", 1, "N2O4/UDMH", 1570L, "Retired", 1967, 2010, 445, 426, false, false, false, null, null);
        createVehicle("Start", "Start", null, "RUS", "MIT", 22.7, 1.61, 47200.0, 4, 630, null, "15D119", 1, "15D116", 1, "Solid", 1000L, "Retired", 1993, 2006, 7, 6, false, false, false, null, null);
        createVehicle("Shtil", "Shtil", null, "RUS", "Makeyev", 14.8, 1.8, 40000.0, 2, 100, null, "3D40", 1, "3D34", 1, "N2O4/UDMH", 900L, "Retired", 1998, 2006, 2, 2, false, false, false, null, null);
        createVehicle("Energia", "Energia", null, "RUS", "NPO Energia", 59.0, 17.6, 2400000.0, 2, 105000, null, "RD-170", 4, "RD-0120", 4, "RP-1/LOX+LH2", 35600L, "Retired", 1987, 1988, 2, 2, false, true, false, bd("226000000"), bd("2152"));
        createVehicle("N1", "N1", null, "RUS", "OKB-1", 105.0, 17.0, 2735000.0, 5, 95000, null, "NK-15", 30, "NK-15V", 8, "RP-1/LOX", 45400L, "Retired", 1969, 1972, 4, 0, false, true, false, null, null);

        // ==================== European - Ariane ====================
        createVehicle("Ariane 1", "Ariane", "1", "ESA", "Aérospatiale", 47.4, 3.8, 210000.0, 3, 1850, 1000, "Viking 5", 4, "HM7", 1, "N2O4/UH25+LH2", 2480L, "Retired", 1979, 1986, 11, 9, false, false, false, null, null);
        createVehicle("Ariane 2", "Ariane", "2", "ESA", "Aérospatiale", 49.1, 3.8, 219000.0, 3, 2270, 1175, "Viking 5", 4, "HM7B", 1, "N2O4/UH25+LH2", 2580L, "Retired", 1986, 1989, 6, 5, false, false, false, null, null);
        createVehicle("Ariane 3", "Ariane", "3", "ESA", "Aérospatiale", 49.1, 3.8, 237000.0, 3, 2700, 1490, "Viking 5", 4, "HM7B", 1, "N2O4/UH25+LH2+Solid", 3200L, "Retired", 1984, 1989, 11, 10, false, false, false, null, null);
        createVehicle("Ariane 4", "Ariane", "4", "ESA", "ArianeGroup", 58.7, 3.8, 470000.0, 3, 10200, 4950, "Viking 5C", 4, "HM7B", 1, "N2O4/UH25+LH2", 5480L, "Retired", 1988, 2003, 116, 113, false, false, false, bd("85000000"), bd("8333"));
        createVehicle("Ariane 5", "Ariane", "5", "ESA", "ArianeGroup", 53.0, 5.4, 780000.0, 2, 21000, 10500, "Vulcain 2", 1, "HM7B", 1, "LH2/LOX+SRBs", 13800L, "Retired", 1996, 2023, 117, 112, false, false, false, bd("165000000"), bd("7857"));
        createVehicle("Ariane 5 ECA", "Ariane", "5 ECA", "ESA", "ArianeGroup", 53.0, 5.4, 780000.0, 2, 21000, 10500, "Vulcain 2", 1, "HM7B", 1, "LH2/LOX+SRBs", 13800L, "Retired", 2002, 2023, 87, 84, false, false, false, bd("165000000"), bd("7857"));
        createVehicle("Ariane 6", "Ariane", "6", "ESA", "ArianeGroup", 63.0, 5.4, 860000.0, 2, 21650, 11500, "Vulcain 2.1", 1, "Vinci", 1, "LH2/LOX+SRBs", 15400L, "Active", 2024, null, 2, 2, false, false, true, bd("115000000"), bd("5312"));
        createVehicle("Ariane 6 A62", "Ariane", "6 A62", "ESA", "ArianeGroup", 63.0, 5.4, 530000.0, 2, 10350, 5000, "Vulcain 2.1", 1, "Vinci", 1, "LH2/LOX+SRBs", 8000L, "Active", 2024, null, 2, 2, false, false, true, bd("75000000"), bd("7246"));
        createVehicle("Ariane 6 A64", "Ariane", "6 A64", "ESA", "ArianeGroup", 63.0, 5.4, 860000.0, 2, 21650, 11500, "Vulcain 2.1", 1, "Vinci", 1, "LH2/LOX+SRBs", 15400L, "Active", 2024, null, 0, 0, false, false, true, bd("115000000"), bd("5312"));

        // ==================== European - Vega ====================
        createVehicle("Vega", "Vega", null, "ESA", "Avio", 30.0, 3.0, 137000.0, 4, 1500, null, "P80", 1, "AVUM", 1, "Solid+UDMH/N2O4", 2261L, "Retired", 2012, 2024, 24, 22, false, false, false, bd("37000000"), bd("24667"));
        createVehicle("Vega C", "Vega", "C", "ESA", "Avio", 35.0, 3.4, 210000.0, 4, 2300, null, "P120C", 1, "AVUM+", 1, "Solid+UDMH/N2O4", 4500L, "Active", 2022, null, 3, 2, false, false, true, bd("45000000"), bd("19565"));
        createVehicle("Vega E", "Vega", "E", "ESA", "Avio", 35.0, 3.4, 200000.0, 3, 3000, null, "P120C+", 1, "M10", 1, "Solid+CH4/LOX", 4800L, "In Development", null, null, 0, 0, false, false, false, null, null);

        // ==================== European - Other ====================
        createVehicle("Diamant", "Diamant", null, "FRA", "SEREB", 18.9, 1.4, 24600.0, 3, 160, null, "Emeraude", 1, "P2.2", 1, "N2O4/UDMH+Solid", 270L, "Retired", 1965, 1975, 12, 9, false, false, false, null, null);
        createVehicle("Europa", "Europa", null, "ESA", "ELDO", 31.7, 3.05, 104670.0, 3, 1000, null, "RZ.2", 1, "Astris", 1, "RP-1/LOX+N2O4", 1540L, "Retired", 1968, 1971, 5, 0, false, false, false, null, null);
        createVehicle("Maia", "Maia", null, "ITA", "Avio", 15.0, 2.0, 17000.0, 2, 400, null, "M10", 1, null, null, "CH4/LOX", 98L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Spectrum", "Spectrum", null, "DEU", "Isar Aerospace", 27.0, 2.0, 50000.0, 2, 1000, 700, "Aquila", 9, "Aquila Vac", 1, "CH4/LOX", 1000L, "In Development", 2025, null, 0, 0, true, false, false, bd("12000000"), bd("12000"));
        createVehicle("RFA One", "RFA One", null, "DEU", "RFA", 30.0, 2.0, 60000.0, 3, 1300, 450, "Helix", 9, "Redshift", 1, "RP-1/LOX", 1000L, "In Development", 2024, null, 1, 0, false, false, false, bd("10000000"), bd("7692"));
        createVehicle("Skyrora XL", "Skyrora", "XL", "GBR", "Skyrora", 23.0, 1.5, 56000.0, 3, 315, null, "LEO", 3, "LEO Vac", 1, "RP-1/Peroxide", 200L, "In Development", null, null, 0, 0, false, false, false, bd("4500000"), bd("14286"));
        createVehicle("Prime", "Prime", null, "GBR", "Orbex", 19.0, 1.3, 18000.0, 2, 180, null, "Stage 2", 6, null, 1, "Bio-propane/LOX", 200L, "In Development", 2025, null, 0, 0, false, false, false, bd("6000000"), bd("33333"));
        createVehicle("OB-1 Mk1", "OB-1", null, "GBR", "Orbex", 19.0, 1.3, 18000.0, 2, 180, null, "Stage 2", 6, null, 1, "Bio-propane/LOX", 200L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Zephyr", "Zephyr", null, "FRA", "Latitude", 22.0, 2.0, 20000.0, 2, 200, null, null, 4, null, 1, "RP-1/LOX", 150L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Zephyr FR", "Zephyr", "FR", "FRA", "Latitude", 22.0, 2.0, 20000.0, 2, 200, null, null, 4, null, 1, "RP-1/LOX", 150L, "In Development", null, null, 0, 0, false, false, false, null, null);

        // ==================== Chinese - Long March ====================
        createVehicle("Long March 1", "Long March", "1", "CHN", "CALT", 29.5, 2.25, 81570.0, 3, 300, null, "YF-2A", 4, "FG-02", 1, "N2O4/UDMH+Solid", 1100L, "Retired", 1970, 1971, 2, 1, false, false, false, null, null);
        createVehicle("Long March 2", "Long March", "2", "CHN", "CALT", 32.6, 3.35, 190000.0, 2, 2500, null, "YF-20A", 4, "YF-22A", 1, "N2O4/UDMH", 2780L, "Retired", 1974, 1978, 4, 3, false, false, false, null, null);
        createVehicle("Long March 2C", "Long March", "2C", "CHN", "CALT", 42.0, 3.35, 233000.0, 2, 3850, 1250, "YF-21C", 4, "YF-24E", 1, "N2O4/UDMH", 2962L, "Active", 1982, null, 70, 68, false, false, true, bd("25000000"), bd("6494"));
        createVehicle("Long March 2D", "Long March", "2D", "CHN", "SAST", 41.1, 3.35, 232250.0, 2, 3500, 1300, "YF-21C", 4, "YF-24E", 1, "N2O4/UDMH", 2962L, "Active", 1992, null, 90, 89, false, false, true, bd("30000000"), bd("8571"));
        createVehicle("Long March 2F", "Long March", "2F", "CHN", "CALT", 62.0, 3.35, 480000.0, 2, 8400, null, "YF-20C", 4, "YF-24E", 1, "N2O4/UDMH+SRBs", 6040L, "Active", 1999, null, 20, 20, false, true, true, bd("70000000"), bd("8333"));
        createVehicle("Long March 3", "Long March", "3", "CHN", "CALT", 44.6, 3.35, 202000.0, 3, 5000, 1500, "YF-21B", 4, "YF-73", 2, "N2O4/UDMH+LH2", 2800L, "Retired", 1984, 2000, 13, 12, false, false, false, null, null);
        createVehicle("Long March 3A", "Long March", "3A", "CHN", "CALT", 52.5, 3.35, 240000.0, 3, 6000, 2600, "YF-21C", 4, "YF-75", 2, "N2O4/UDMH+LH2", 2962L, "Active", 1994, null, 30, 29, false, false, true, bd("55000000"), bd("9167"));
        createVehicle("Long March 3B", "Long March", "3B", "CHN", "CALT", 54.8, 3.35, 425800.0, 3, 11200, 5100, "YF-21C", 4, "YF-75", 2, "N2O4/UDMH+LH2", 5923L, "Active", 1996, null, 95, 93, false, false, true, bd("70000000"), bd("6250"));
        createVehicle("Long March 3B/E", "Long March", "3B/E", "CHN", "CALT", 56.3, 3.35, 458970.0, 3, 11500, 5500, "YF-21C", 4, "YF-75D", 2, "N2O4/UDMH+LH2", 5923L, "Active", 2007, null, 50, 49, false, false, true, bd("70000000"), bd("6087"));
        createVehicle("Long March 3C", "Long March", "3C", "CHN", "CALT", 54.8, 3.35, 345000.0, 3, 9100, 3800, "YF-21C", 4, "YF-75", 2, "N2O4/UDMH+LH2", 4500L, "Active", 2008, null, 25, 25, false, false, true, bd("55000000"), bd("6044"));
        createVehicle("Long March 4", "Long March", "4", "CHN", "SAST", 41.9, 3.35, 249200.0, 3, 4200, 1500, "YF-21B", 4, "YF-40", 1, "N2O4/UDMH", 2962L, "Retired", 1988, 1999, 2, 2, false, false, false, null, null);
        createVehicle("Long March 4B", "Long March", "4B", "CHN", "SAST", 45.8, 3.35, 249200.0, 3, 4200, 1500, "YF-21C", 4, "YF-40A", 1, "N2O4/UDMH", 2962L, "Active", 1999, null, 50, 49, false, false, true, bd("35000000"), bd("8333"));
        createVehicle("Long March 4C", "Long March", "4C", "CHN", "SAST", 45.8, 3.35, 250000.0, 3, 4200, 1500, "YF-21C", 4, "YF-40A", 1, "N2O4/UDMH", 2962L, "Active", 2006, null, 60, 58, false, false, true, bd("35000000"), bd("8333"));
        createVehicle("Long March 5", "Long March", "5", "CHN", "CALT", 57.0, 5.0, 867000.0, 2, 25000, 14000, "YF-77", 2, "YF-75D", 2, "LH2/LOX+RP-1", 10600L, "Active", 2016, null, 15, 14, false, false, true, bd("100000000"), bd("4000"));
        createVehicle("Long March 5B", "Long March", "5B", "CHN", "CALT", 53.7, 5.0, 837500.0, 1, 25000, null, "YF-77", 2, null, null, "LH2/LOX+RP-1", 10600L, "Active", 2020, null, 5, 5, false, false, true, bd("90000000"), bd("3600"));
        createVehicle("Long March 6", "Long March", "6", "CHN", "SAST", 29.2, 3.35, 103200.0, 3, 1080, 500, "YF-100", 1, "YF-115", 1, "RP-1/LOX", 1200L, "Active", 2015, null, 15, 14, false, false, true, bd("20000000"), bd("18519"));
        createVehicle("Long March 6A", "Long March", "6A", "CHN", "SAST", 50.0, 3.35, 530000.0, 2, 4000, null, "YF-100K", 1, "YF-115K", 1, "RP-1/LOX+Solid", 4800L, "Active", 2022, null, 5, 5, false, false, true, bd("40000000"), bd("10000"));
        createVehicle("Long March 6C", "Long March", "6C", "CHN", "SAST", 43.0, 3.35, 350000.0, 2, 2300, null, "YF-100K", 1, "YF-115K", 1, "RP-1/LOX", 1200L, "Active", 2024, null, 2, 2, false, false, true, bd("25000000"), bd("10870"));
        createVehicle("Long March 7", "Long March", "7", "CHN", "CALT", 53.1, 3.35, 597000.0, 2, 13500, 5500, "YF-100", 6, "YF-115", 2, "RP-1/LOX", 7200L, "Active", 2016, null, 12, 12, false, false, true, bd("55000000"), bd("4074"));
        createVehicle("Long March 7A", "Long March", "7A", "CHN", "CALT", 60.1, 3.35, 573000.0, 3, null, 7000, "YF-100", 6, "YF-75D", 2, "RP-1/LOX+LH2", 7200L, "Active", 2021, null, 5, 4, false, false, true, bd("70000000"), bd("10000"));
        createVehicle("Long March 8", "Long March", "8", "CHN", "CALT", 50.3, 3.35, 356000.0, 2, 8100, 2800, "YF-100", 2, "YF-75D", 1, "RP-1/LOX+LH2", 2400L, "Active", 2020, null, 6, 6, false, false, true, bd("40000000"), bd("4938"));
        createVehicle("Long March 8A", "Long March", "8A", "CHN", "CALT", 48.0, 3.35, 198000.0, 2, 5000, null, "YF-100K", 2, "YF-75D", 1, "RP-1/LOX+LH2", 2400L, "Active", 2024, null, 1, 1, false, false, true, bd("35000000"), bd("7000"));
        createVehicle("Long March 9", "Long March", "9", "CHN", "CALT", 110.0, 10.6, 4140000.0, 3, 150000, 50000, "YF-215", 16, "YF-79", 2, "RP-1/LOX+LH2", 60000L, "In Development", null, null, 0, 0, false, true, false, null, null);
        createVehicle("Long March 10", "Long March", "10", "CHN", "CALT", 92.0, 5.0, 2180000.0, 3, 70000, 27000, "YF-100K", 7, "YF-75E", 3, "RP-1/LOX+LH2", 26800L, "In Development", null, null, 0, 0, false, true, false, null, null);
        createVehicle("Long March 10A", "Long March", "10A", "CHN", "CALT", 67.0, 5.0, null, 2, null, null, "YF-100K", 7, "YF-75E", 2, "RP-1/LOX+LH2", null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Long March 11", "Long March", "11", "CHN", "CALT", 20.8, 2.0, 58000.0, 4, 700, 350, "Solid", 1, "Solid", 1, "Solid", 1200L, "Active", 2015, null, 20, 19, false, false, true, bd("5000000"), bd("7143"));
        createVehicle("Kuaizhou", "Kuaizhou", null, "CHN", "ExPace", 20.0, 1.4, 30000.0, 4, 300, null, "Solid", 1, "Solid", 1, "Solid", 590L, "Active", 2013, null, 30, 28, false, false, true, bd("4000000"), bd("13333"));
        createVehicle("Kuaizhou 1A", "Kuaizhou", "1A", "CHN", "ExPace", 20.0, 1.4, 30000.0, 4, 300, null, "Solid", 1, "Solid", 1, "Solid", 590L, "Active", 2017, null, 25, 23, false, false, true, bd("4000000"), bd("13333"));
        createVehicle("Kuaizhou 11", "Kuaizhou", "11", "CHN", "ExPace", 25.0, 2.2, 78000.0, 4, 1500, null, "Solid", 1, "Solid", 1, "Solid", 2000L, "Active", 2020, null, 4, 3, false, false, true, bd("15000000"), bd("10000"));

        // ==================== Chinese - Commercial ====================
        createVehicle("Jielong-1", "Jielong", "1", "CHN", "CASC", 19.5, 1.2, 23100.0, 4, 200, null, "Solid", 1, "Solid", 1, "Solid", 650L, "Active", 2019, null, 6, 6, false, false, true, bd("2500000"), bd("12500"));
        createVehicle("Jielong-3", "Jielong", "3", "CHN", "CASC", 31.0, 2.6, 140000.0, 4, 1500, null, "Solid", 1, "Solid", 1, "Solid", 2000L, "Active", 2022, null, 5, 5, false, false, true, bd("10000000"), bd("6667"));
        createVehicle("Jielong-4", "Jielong", "4", "CHN", "CASC", null, null, null, 4, null, null, "Solid", 1, "Solid", 1, "Solid", null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Ceres-1", "Ceres", "1", "CHN", "Galactic Energy", 19.0, 1.4, 33000.0, 4, 400, null, "Solid", 1, "Solid", 1, "Solid", 600L, "Active", 2020, null, 15, 14, false, false, true, bd("4500000"), bd("11250"));
        createVehicle("Ceres-1S", "Ceres", "1S", "CHN", "Galactic Energy", 20.0, 1.6, 42000.0, 4, 600, null, "Solid", 1, "Solid", 1, "Solid", 800L, "Active", 2024, null, 2, 2, false, false, true, bd("5000000"), bd("8333"));
        createVehicle("Pallas-1", "Pallas", "1", "CHN", "Galactic Energy", 35.0, 3.35, 260000.0, 2, 5000, null, "TQ-15A", 7, "TQ-12A", 1, "RP-1/LOX", 3500L, "In Development", 2025, null, 0, 0, true, false, false, null, null);
        createVehicle("Kinetica-1", "Kinetica", "1", "CHN", "CAS Space", 30.7, 2.65, 78000.0, 4, 1500, 500, "Solid", 1, "Solid", 1, "Solid", 2000L, "Active", 2022, null, 5, 4, false, false, true, bd("12000000"), bd("8000"));
        createVehicle("Hyperbola-1", "Hyperbola", "1", "CHN", "iSpace", 20.8, 1.4, 31000.0, 4, 300, null, "Solid", 1, "Solid", 1, "Solid", 600L, "Active", 2019, null, 6, 3, false, false, true, bd("4500000"), bd("15000"));
        createVehicle("Hyperbola-3", "Hyperbola", "3", "CHN", "iSpace", null, null, null, 2, null, null, null, null, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Zhuque-2", "Zhuque", "2", "CHN", "LandSpace", 49.5, 3.35, 216000.0, 2, 4000, 1500, "TQ-12", 4, "TQ-11", 1, "CH4/LOX", 2680L, "Active", 2023, null, 5, 3, true, false, true, bd("45000000"), bd("11250"));
        createVehicle("Zhuque-3", "Zhuque", "3", "CHN", "LandSpace", 76.6, 4.5, 660000.0, 2, 21000, null, "TQ-12A", 9, "TQ-15", 2, "CH4/LOX", 9000L, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Tianlong-2", "Tianlong", "2", "CHN", "Space Pioneer", 32.8, 3.35, 153000.0, 2, 2000, null, "TH-12", 5, "TH-11", 1, "RP-1/LOX", 1400L, "Active", 2023, null, 3, 2, true, false, true, bd("15000000"), bd("7500"));
        createVehicle("Tianlong-3", "Tianlong", "3", "CHN", "Space Pioneer", 71.0, 3.8, 590000.0, 2, 17000, null, "TH-12A", 9, "TH-15", 1, "RP-1/LOX", 6300L, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Gravity-1", "Gravity", "1", "CHN", "OrienSpace", 30.0, 3.8, 405000.0, 3, 6500, null, "Solid", 4, "YF-40", 1, "Solid+N2O4/UDMH", 6000L, "Active", 2024, null, 2, 2, false, false, true, bd("20000000"), bd("3077"));
        createVehicle("Gravity-2", "Gravity", "2", "CHN", "OrienSpace", null, null, null, 2, null, null, null, null, null, null, "CH4/LOX", null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Nebula-1", "Nebula", "1", "CHN", "Deep Blue Aerospace", 24.3, 2.25, 60000.0, 2, 1000, null, "Thunder-100", 3, "Thunder-20R", 1, "RP-1/LOX", 300L, "Active", 2024, null, 1, 1, true, false, true, bd("10000000"), bd("10000"));

        // ==================== Japanese ====================
        createVehicle("H-I", "H", "I", "JPN", "MHI", 40.3, 2.44, 139300.0, 3, 3200, 1100, "MB-3-3", 1, "LE-5", 1, "RP-1/LOX+LH2", 800L, "Retired", 1986, 1992, 9, 9, false, false, false, null, null);
        createVehicle("H-II", "H", "II", "JPN", "MHI", 50.0, 4.0, 264000.0, 2, 10060, 4000, "LE-7", 1, "LE-5A", 1, "LH2/LOX", 1700L, "Retired", 1994, 1999, 7, 5, false, false, false, null, null);
        createVehicle("H-IIA", "H-II", "A", "JPN", "MHI", 53.0, 4.0, 289000.0, 2, 15000, 6000, "LE-7A", 1, "LE-5B", 1, "LH2/LOX", 2200L, "Active", 2001, null, 50, 49, false, false, true, bd("90000000"), bd("6000"));
        createVehicle("H-IIB", "H-II", "B", "JPN", "MHI", 56.6, 5.2, 531000.0, 2, 19000, 8000, "LE-7A", 2, "LE-5B-2", 1, "LH2/LOX+SRBs", 5600L, "Retired", 2009, 2020, 9, 9, false, false, false, bd("110000000"), bd("5789"));
        createVehicle("H3", "H3", null, "JPN", "JAXA/MHI", 63.0, 5.2, 574000.0, 2, 6500, 4000, "LE-9", 3, "LE-5B-3", 1, "LH2/LOX+SRBs", 9800L, "Active", 2023, null, 4, 3, false, false, true, bd("50000000"), bd("7692"));
        createVehicle("H3-22S", "H3", "22S", "JPN", "JAXA/MHI", 57.0, 5.2, 422000.0, 2, 4000, null, "LE-9", 2, "LE-5B-3", 1, "LH2/LOX+SRBs", 6500L, "Active", 2024, null, 1, 1, false, false, true, bd("35000000"), bd("8750"));
        createVehicle("H3-22L", "H3", "22L", "JPN", "JAXA/MHI", 63.0, 5.2, 574000.0, 2, 6500, 4000, "LE-9", 2, "LE-5B-3", 1, "LH2/LOX+SRBs", 9800L, "Active", 2023, null, 3, 2, false, false, true, bd("50000000"), bd("7692"));
        createVehicle("H3-24L", "H3", "24L", "JPN", "JAXA/MHI", 63.0, 5.2, 654000.0, 2, 7900, 6500, "LE-9", 2, "LE-5B-3", 1, "LH2/LOX+SRBs", 11500L, "Active", 2024, null, 0, 0, false, false, true, bd("65000000"), bd("8228"));
        createVehicle("H3-30S", "H3", "30S", "JPN", "JAXA/MHI", 57.0, 5.2, 400000.0, 2, 3000, null, "LE-9", 3, "LE-5B-3", 1, "LH2/LOX", 9800L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Epsilon", "Epsilon", null, "JPN", "JAXA/IHI", 26.0, 2.6, 95600.0, 3, 1200, 450, "SRB-A3", 1, "M-35", 1, "Solid", 2270L, "Active", 2013, null, 6, 5, false, false, true, bd("38000000"), bd("31667"));
        createVehicle("Epsilon S", "Epsilon", "S", "JPN", "JAXA/IHI", 27.0, 2.6, 96000.0, 3, 1500, 600, "SRB-3", 1, "M-35", 1, "Solid", 2500L, "In Development", 2024, null, 1, 0, false, false, false, null, null);
        createVehicle("M-V", "Mu", "V", "JPN", "ISAS", 30.7, 2.5, 139000.0, 3, 1800, null, "M-14", 1, "M-34", 1, "Solid", 3960L, "Retired", 1997, 2006, 7, 6, false, false, false, null, null);
        createVehicle("Lambda", "Lambda", "4S", "JPN", "ISAS", 16.5, 0.735, 9400.0, 4, 26, null, "Solid", 1, "Solid", 1, "Solid", 300L, "Retired", 1966, 1970, 5, 1, false, false, false, null, null);
        createVehicle("N-I", "N", "I", "JPN", "MHI", 32.6, 2.44, 90400.0, 3, 1200, 400, "MB-3", 1, "LE-3", 1, "RP-1/LOX+N2O4/Aero50", 771L, "Retired", 1975, 1982, 7, 6, false, false, false, null, null);
        createVehicle("N-II", "N", "II", "JPN", "MHI", 35.4, 2.44, 135200.0, 3, 2000, 730, "MB-3-3", 1, "LE-5", 1, "RP-1/LOX+LH2", 800L, "Retired", 1981, 1987, 8, 8, false, false, false, null, null);
        createVehicle("SS-520", "SS-520", null, "JPN", "ISAS", 9.54, 0.52, 2600.0, 3, 4, null, "Solid", 1, "Solid", 1, "Solid", 180L, "Active", 2017, null, 2, 1, false, false, true, bd("4000000"), bd("1000000"));
        createVehicle("Kairos", "Kairos", null, "JPN", "Space One", 18.0, 1.4, 23000.0, 4, 250, null, "Solid", 1, "Solid", 1, "Solid", 400L, "Active", 2024, null, 2, 0, false, false, true, bd("6000000"), bd("24000"));
        createVehicle("Zero", "Zero", null, "JPN", "IHI Aerospace", 15.0, 1.0, 10000.0, 2, 150, null, "LNG", 1, null, null, "LNG/LOX", 100L, "In Development", null, null, 0, 0, false, false, false, null, null);

        // ==================== Indian ====================
        createVehicle("SLV", "SLV", "3", "IND", "ISRO", 22.0, 1.0, 17000.0, 4, 40, null, "Solid", 1, "Solid", 1, "Solid", 500L, "Retired", 1979, 1983, 4, 2, false, false, false, null, null);
        createVehicle("ASLV", "ASLV", null, "IND", "ISRO", 24.0, 1.0, 39000.0, 5, 150, null, "Solid", 5, "Solid", 1, "Solid", 900L, "Retired", 1987, 1994, 4, 1, false, false, false, null, null);
        createVehicle("PSLV", "PSLV", null, "IND", "ISRO", 44.0, 2.8, 320000.0, 4, 3800, 1425, "S139", 1, "L-40", 6, "Solid+UDMH", 4860L, "Active", 1993, null, 62, 59, false, false, true, bd("21000000"), bd("5526"));
        createVehicle("PSLV-CA", "PSLV", "CA", "IND", "ISRO", 44.0, 2.8, 230000.0, 4, 1100, null, "S139", 1, "L-40", 0, "Solid+UDMH", 2960L, "Active", 2007, null, 20, 19, false, false, true, bd("15000000"), bd("13636"));
        createVehicle("PSLV-DL", "PSLV", "DL", "IND", "ISRO", 44.0, 2.8, 260000.0, 4, 2100, null, "S139", 1, "L-40", 2, "Solid+UDMH", 3400L, "Active", 2019, null, 8, 8, false, false, true, bd("17000000"), bd("8095"));
        createVehicle("PSLV-QL", "PSLV", "QL", "IND", "ISRO", 44.0, 2.8, 295000.0, 4, 3000, null, "S139", 1, "L-40", 4, "Solid+UDMH", 4200L, "Active", 2021, null, 4, 4, false, false, true, bd("19000000"), bd("6333"));
        createVehicle("PSLV-XL", "PSLV", "XL", "IND", "ISRO", 44.0, 2.8, 320000.0, 4, 3800, 1425, "S139", 1, "L-40", 6, "Solid+UDMH", 4860L, "Active", 1993, null, 60, 57, false, false, true, bd("21000000"), bd("5526"));
        createVehicle("GSLV", "GSLV", null, "IND", "ISRO", 49.1, 2.8, 414750.0, 3, 5000, 2500, "Vikas", 4, "CE-7.5", 1, "UDMH/N2O4+LH2", 4740L, "Active", 2001, null, 15, 11, false, false, true, bd("36000000"), bd("7200"));
        createVehicle("GSLV Mk II", "GSLV", "Mk II", "IND", "ISRO", 49.1, 2.8, 414750.0, 3, 5000, 2500, "Vikas", 4, "CE-7.5", 1, "UDMH/N2O4+LH2", 4740L, "Active", 2010, null, 10, 8, false, false, true, bd("36000000"), bd("7200"));
        createVehicle("GSLV Mk III", "GSLV", "Mk III", "IND", "ISRO", 43.5, 4.0, 640000.0, 3, 10000, 4000, "Vikas", 2, "CE-20", 1, "UDMH/N2O4+LH2", 5150L, "Active", 2014, null, 7, 6, false, true, true, bd("50000000"), bd("5000"));
        createVehicle("LVM 3", "LVM", "3", "IND", "ISRO", 43.5, 4.0, 640000.0, 3, 10000, 4000, "Vikas", 2, "CE-20", 1, "UDMH/N2O4+LH2", 5150L, "Active", 2014, null, 10, 9, false, true, true, bd("50000000"), bd("5000"));
        createVehicle("SSLV", "SSLV", null, "IND", "ISRO", 34.0, 2.0, 120000.0, 3, 500, 300, "SS1", 1, "VTM", 1, "Solid", 2490L, "Active", 2022, null, 3, 2, false, false, true, bd("4500000"), bd("9000"));
        createVehicle("RLV-TD", "RLV", "TD", "IND", "ISRO", 6.5, 1.1, 1750.0, 1, null, null, "Solid", 1, null, null, "Solid", 700L, "In Development", 2016, null, 1, 1, true, false, false, null, null);
        createVehicle("Agnibaan", "Agnibaan", null, "IND", "Agnikul", 18.0, 1.3, 14000.0, 2, 100, null, "Agnilet", 7, "Agnilet Vac", 1, "RP-1/LOX", 200L, "In Development", 2024, null, 1, 1, false, false, true, bd("5000000"), bd("50000"));

        // ==================== South Korean ====================
        createVehicle("Naro", "Naro", "KSLV-1", "KOR", "KARI/Khrunichev", 33.0, 2.9, 140000.0, 2, 100, null, "RD-151", 1, "KSR-1", 1, "RP-1/LOX+Solid", 1700L, "Retired", 2009, 2013, 3, 1, false, false, false, bd("32000000"), bd("320000"));
        createVehicle("Nuri", "Nuri", "KSLV-II", "KOR", "KARI", 47.2, 3.5, 200000.0, 3, 2600, 1500, "KRE-075", 4, "KRE-007", 1, "RP-1/LOX", 3000L, "Active", 2021, null, 4, 3, false, false, true, bd("50000000"), bd("19231"));
        createVehicle("KSLV-III", "KSLV", "III", "KOR", "KARI", null, null, null, 2, null, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Blue Whale 1", "Blue Whale", "1", "KOR", "Perigee Aerospace", 21.0, 1.5, 15500.0, 2, 200, null, null, 3, null, 1, "RP-1/LOX", 150L, "In Development", null, null, 0, 0, true, false, false, null, null);
        createVehicle("Hanbit-Nano", "Hanbit", "Nano", "KOR", "Innospace", 16.0, 0.9, 7200.0, 2, 50, null, null, 1, null, 1, "Solid+Liquid", 100L, "Active", 2023, null, 1, 1, false, false, true, bd("2000000"), bd("40000"));

        // ==================== Israeli ====================
        createVehicle("Shavit", "Shavit", null, "ISR", "IAI", 21.0, 1.4, 30000.0, 4, 350, null, "Solid", 1, "Solid", 1, "Solid", 770L, "Active", 1988, null, 11, 9, false, false, true, bd("25000000"), bd("71429"));
        createVehicle("Shavit-2", "Shavit", "2", "ISR", "IAI", 26.0, 1.6, 45000.0, 4, 800, null, "Solid", 1, "Solid", 1, "Solid", 1200L, "Active", 2007, null, 3, 3, false, false, true, bd("30000000"), bd("37500"));

        // ==================== Iranian ====================
        createVehicle("Safir", "Safir", null, "IRN", "ISA", 22.0, 1.25, 26000.0, 2, 50, null, "Nodong", 1, "Solid/Liquid", 1, "UDMH/N2O4", 300L, "Active", 2008, null, 9, 5, false, false, true, null, null);
        createVehicle("Simorgh", "Simorgh", null, "IRN", "ISA", 27.0, 2.4, 87000.0, 2, 350, null, "Nodong", 4, "Safir Upper", 1, "UDMH/N2O4", 1240L, "Active", 2016, null, 6, 2, false, false, true, null, null);
        createVehicle("Qased", "Qased", null, "IRN", "IRGC", 18.0, 1.25, 15000.0, 3, 15, null, "Solid", 1, "Solid", 1, "Solid", 300L, "Active", 2020, null, 4, 3, false, false, true, null, null);
        createVehicle("Qaem 100", "Qaem", "100", "IRN", "IRGC", 20.0, 1.5, 26000.0, 3, 80, null, "Solid", 1, "Solid", 1, "Solid", 450L, "Active", 2024, null, 2, 2, false, false, true, null, null);

        // ==================== North Korean ====================
        createVehicle("Unha", "Unha", "3", "PRK", "NADA", 30.0, 2.4, 91000.0, 3, 100, null, "Nodong", 4, "Solid", 1, "UDMH/N2O4+Solid", 1200L, "Active", 2009, null, 5, 2, false, false, true, null, null);
        createVehicle("Chollima-1", "Chollima", "1", "PRK", "NADA", 25.0, 2.0, 50000.0, 3, 100, null, "Solid/Liquid", 1, "Solid", 1, "Hybrid", 600L, "Active", 2023, null, 3, 1, false, false, true, null, null);

        // ==================== Ukrainian ====================
        createVehicle("Cyclone-4M", "Cyclone", "4M", "UKR", "Yuzhnoye", 38.7, 3.0, 260000.0, 3, 5300, null, "RD-870", 1, "RD-861K", 1, "N2O4/UDMH", 2520L, "In Development", null, null, 0, 0, false, false, false, null, null);

        // ==================== Other Countries ====================
        createVehicle("VLS", "VLS", "1", "BRA", "AEB", 19.5, 1.0, 50000.0, 3, 380, null, "S43", 4, "S44", 1, "Solid", 1200L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Aurora", "Aurora", null, "CAN", "Maritime Launch", null, null, null, 3, null, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Aventura 1", "Aventura", "1", "ARG", "CONAE", 28.0, 2.5, null, 2, null, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Tronador II", "Tronador", "II", "ARG", "CONAE", 32.0, 2.5, 67000.0, 2, 250, null, "TEPSU-8500", 1, null, 1, "N2O4/UDMH", null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("SL1", "SL1", null, "DEU", "HyImpulse", 18.0, 0.9, 6000.0, 3, 50, null, "HyPLOX75", 1, "HyPLOX75", 1, "Paraffin/LOX", 90L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Siraya", "Siraya", null, "TWN", "TiSPACE", 10.0, 0.65, 1850.0, 2, 3, null, "Hybrid", 1, "Hybrid", 1, "Hybrid", 50L, "Active", 2023, null, 2, 1, false, false, true, null, null);
        createVehicle("Volans", "Volans", "V500", "IDN", "Equatorial Space", null, null, null, 2, 500, null, null, null, null, null, null, null, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("ŞIMŞEK-1", "ŞIMŞEK", "1", "TUR", "ROKETSAN", 13.0, 0.8, 5000.0, 2, 50, null, "Solid", 1, "Solid", 1, "Solid", 200L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Miura 1", "Miura", "1", "ESP", "PLD Space", 12.7, 0.7, 3200.0, 1, null, null, "TEPREL-B", 1, null, null, "RP-1/LOX", 30L, "Active", 2023, null, 1, 1, false, false, true, bd("1000000"), null);
        createVehicle("Miura 5", "Miura", "5", "ESP", "PLD Space", 34.0, 1.8, 30000.0, 2, 500, 300, "TEPREL-C", 5, "TEPREL-2", 1, "RP-1/LOX", 800L, "In Development", 2025, null, 0, 0, true, false, false, bd("12000000"), bd("24000"));
        createVehicle("Eris", "Eris", null, "AUS", "Gilmour Space", 25.0, 2.0, 35000.0, 3, 300, null, null, 5, null, 1, "Hybrid", 350L, "In Development", null, null, 0, 0, false, false, false, null, null);
        createVehicle("Stardust 1.0", "Stardust", "1.0", "USA", "bluShift", 6.1, 0.2, 130.0, 1, null, null, "Bio-fuel", 1, null, null, "Bio-fuel/LOX", 3L, "Active", 2021, null, 1, 1, false, false, true, bd("100000"), null);
    }

    // Helper methods for cleaner code
    private BigDecimal bd(String value) {
        return value != null ? new BigDecimal(value) : null;
    }

    private void createVehicle(String name, String family, String variant, String countryCode, String manufacturer,
            Double height, Double diameter, Double mass, Integer stages, Integer payloadLeo, Integer payloadGto,
            String firstEngines, Integer firstCount, String secondEngines, Integer secondCount,
            String propellant, Long thrust, String status, Integer firstFlight, Integer lastFlight,
            Integer totalLaunches, Integer successLaunches, Boolean reusable, Boolean humanRated, Boolean active,
            BigDecimal costPerLaunch, BigDecimal costPerKg) {

        Country country = getCountryMap().get(countryCode);
        LaunchVehicle lv = new LaunchVehicle();
        lv.setName(name);
        lv.setFamily(family);
        lv.setVariant(variant);
        lv.setCountry(country);
        lv.setManufacturer(manufacturer);
        lv.setHeightMeters(height);
        lv.setDiameterMeters(diameter);
        lv.setMassKg(mass);
        lv.setStages(stages);
        lv.setPayloadToLeoKg(payloadLeo);
        lv.setPayloadToGtoKg(payloadGto);
        lv.setFirstStageEngines(firstEngines);
        lv.setFirstStageEngineCount(firstCount);
        lv.setSecondStageEngines(secondEngines);
        lv.setSecondStageEngineCount(secondCount);
        lv.setPropellant(propellant);
        lv.setThrustAtLiftoffKn(thrust);
        lv.setStatus(status);
        lv.setFirstFlightYear(firstFlight);
        lv.setLastFlightYear(lastFlight);
        lv.setTotalLaunches(totalLaunches);
        lv.setSuccessfulLaunches(successLaunches);
        lv.setReusable(reusable);
        lv.setHumanRated(humanRated);
        lv.setActive(active);
        lv.setCostPerLaunchUsd(costPerLaunch);
        lv.setCostPerKgToLeoUsd(costPerKg);
        launchVehicleRepository.save(lv);
    }
}
