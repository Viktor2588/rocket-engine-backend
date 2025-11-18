-- Seed 34 rocket engines into the database
-- Using plain SQL that Spring Boot can execute

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Merlin 1D', 'USA', 'SpaceX', 'Falcon 9', 'Active', '1st stage', 'RP-1 / LOX', 'Pump-fed', 282.0, 845000, 210.0, 411.0, NULL, 2.43, 'Kerolox engine for Falcon 9 first stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Merlin Vacuum', 'USA', 'SpaceX', 'Falcon 9', 'Active', '2nd stage', 'RP-1 / LOX', 'Pump-fed', 348.0, 411000, 210.0, 385.0, NULL, 2.43, 'Vacuum-optimized kerolox engine for Falcon 9 second stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Raptor 3', 'USA', 'SpaceX', 'Starship', 'Development', '1st stage', 'LCH4 / LOX', 'Full-flow staged combustion', 350.0, 2200000, 300.0, 2000.0, NULL, 3.6, 'Methane/LOX engine for Super Heavy and Starship.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('F-1', 'USA', 'Rocketdyne', 'Saturn V', 'Retired', '1st stage', 'RP-1 / LOX', 'Gas generator', 304.0, 7740000, 210.0, 8400.0, NULL, 2.27, 'Kerolox first-stage engine for Saturn V, most powerful single nozzle engine.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RS-25', 'USA', 'Rocketdyne', 'Space Shuttle / SLS', 'Active', '1st stage', 'LH2 / LOX', 'Staged combustion', 453.0, 2280000, 206.0, 3177.0, NULL, 6.04, 'Hydrolox engine used on Space Shuttle and SLS core stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('BE-4', 'USA', 'Blue Origin', 'Vulcan', 'Active', '1st stage', 'LCH4 / LOX', 'Gas generator', 339.0, 2400000, 190.0, 1710.0, NULL, 3.6, 'Methane/LOX engine for Vulcan and New Glenn.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RS-68A', 'USA', 'Aerojet Rocketdyne', 'Delta IV', 'Active', '1st stage', 'LH2 / LOX', 'Gas generator', 414.0, 3560000, 204.0, 4465.0, NULL, 5.5, 'Hydrolox booster engine for Delta IV.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RD-170/171M', 'Russia', 'NPO Energomash', 'Energia / Zenit', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 337.0, 7904000, 240.0, 9750.0, NULL, 2.27, 'Four-chamber kerolox booster engine for Energia and Zenit.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RD-180', 'Russia', 'NPO Energomash', 'Atlas V', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 338.0, 3820000, 240.0, 5390.0, NULL, 2.27, 'Two-chamber kerolox booster engine for Atlas V.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RD-191', 'Russia', 'NPO Energomash', 'Angara', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 338.0, 1900000, 240.0, 2700.0, NULL, 2.27, 'Single-chamber kerolox engine for Angara.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RD-0120', 'Russia', 'NPO Energomash', 'Energia', 'Retired', '2nd stage', 'LH2 / LOX', 'Gas generator', 413.0, 1890000, 250.0, 3240.0, NULL, 5.65, 'Hydrolox engine for Energia second stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('NK-33', 'Russia', 'Kuznetsov Design Bureau', 'Soyuz-2', 'Active', '2nd stage', 'RP-1 / LOX', 'Gas generator', 331.0, 1638000, 240.0, 1650.0, NULL, 2.27, 'Kerolox engine for Soyuz second stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('RD-108A', 'Russia', 'NPO Energomash', 'Soyuz', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 320.0, 1020000, 210.0, 1250.0, NULL, 2.27, 'Four-chamber kerolox engine for Soyuz first stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('HM7B', 'France', 'ArianeGroup', 'Ariane 5', 'Active', '2nd stage', 'LH2 / LOX', 'Gas generator', 431.0, 645000, 80.0, 277.0, NULL, 5.9, 'Hydrolox engine for Ariane 5 second stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Vulcain 2', 'France', 'ArianeGroup', 'Ariane 5', 'Active', '1st stage', 'LH2 / LOX', 'Gas generator', 431.0, 1345000, 205.0, 2163.0, NULL, 5.9, 'Hydrolox engine for Ariane 5 first stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('AR2', 'Europe', 'ArianeGroup', 'Ariane 6', 'Development', '1st stage', 'LH2 / LOX', 'Gas generator', 447.0, 1333000, NULL, NULL, NULL, NULL, 'Hydrolox engine for Ariane 6.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('SSME', 'USA', 'Rocketdyne', 'Space Shuttle', 'Retired', '1st stage', 'LH2 / LOX', 'Staged combustion', 453.0, 2280000, 206.0, 3177.0, NULL, 6.04, 'Same as RS-25, used on Space Shuttle.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('LE-7A', 'Japan', 'JAXA', 'H-IIA', 'Active', '1st stage', 'LH2 / LOX', 'Staged combustion', 428.0, 1098000, 196.0, 1650.0, NULL, NULL, 'Hydrolox engine for H-IIA first stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('LE-5B', 'Japan', 'JAXA', 'H-IIA', 'Active', '2nd stage', 'LH2 / LOX', 'Gas generator', 447.0, 137000, 125.0, 201.0, NULL, NULL, 'Hydrolox engine for H-IIA second stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('CE-20', 'South Korea', 'KARI', 'NURI', 'Active', '3rd stage', 'LH2 / LOX', 'Gas generator', 450.0, 750000, 150.0, 1350.0, NULL, NULL, 'Hydrolox engine for NURI third stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('CE-75', 'South Korea', 'KARI', 'NURI', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 302.0, 755000, 210.0, 1350.0, NULL, NULL, 'Kerolox engine for NURI first stage.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('SRB-A3', 'USA', 'Northrop Grumman', 'Delta IV Heavy', 'Active', 'Booster', 'Solid', 'Solid rocket', 283.0, 17200000, 60.0, NULL, NULL, NULL, 'Solid rocket booster for Delta IV Heavy.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('P80', 'Europe', 'Avio', 'Vega', 'Active', '1st stage', 'Solid', 'Solid rocket', 279.0, 2668000, 67.0, NULL, NULL, NULL, 'Solid rocket booster for Vega.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Soltan', 'Iran', 'Shahid Hemmat', 'Safir', 'Active', '1st stage', 'Liquid', 'Pump-fed', 266.0, 1960000, NULL, NULL, NULL, NULL, 'Liquid engine for Safir.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('SCE-200', 'India', 'ISRO', 'LVM3', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 335.0, 2030000, NULL, NULL, NULL, NULL, 'Kerolox engine for LVM3.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Vikas', 'India', 'ISRO', 'GSLV', 'Active', '2nd stage', 'RP-1 / LOX', 'Gas generator', 293.0, 803000, 210.0, 1010.0, NULL, NULL, 'Kerolox engine for GSLV.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('YF-100', 'China', 'CNSA', 'Long March 3A', 'Active', '1st stage', 'RP-1 / LOX', 'Pump-fed', 300.0, 1961000, 250.0, NULL, NULL, NULL, 'Kerolox engine for Long March 3A.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('YF-75', 'China', 'CNSA', 'Long March 3A', 'Active', '3rd stage', 'LH2 / LOX', 'Gas generator', 426.0, 171000, 200.0, NULL, NULL, NULL, 'Hydrolox engine for Long March 3A.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('YF-77', 'China', 'CNSA', 'Long March 5', 'Active', '2nd stage', 'LH2 / LOX', 'Full-flow staged combustion', 451.0, 520000, 270.0, NULL, NULL, NULL, 'Hydrolox engine for Long March 5.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('YF-115', 'China', 'CNSA', 'Long March 5', 'Active', '1st stage', 'RP-1 / LOX', 'Gas generator', 310.0, 4250000, 260.0, NULL, NULL, NULL, 'Kerolox engine for Long March 5.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('YF-280', 'China', 'CNSA', 'Long March 6', 'Development', '2nd stage', 'LH2 / LOX', 'Gas generator', 442.0, 200000, NULL, NULL, NULL, NULL, 'Hydrolox engine for Long March 6.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Huolian-1', 'China', 'Expace', 'Kuaizhou', 'Active', '1st stage', 'Solid', 'Solid rocket', 267.0, 5200000, NULL, NULL, NULL, NULL, 'Solid rocket booster for Kuaizhou.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Aquila', 'Germany', 'Isar Aerospace', 'Spectrum', 'Development', '1st stage', 'Propane / LOX', 'Gas generator', 318.0, 75000, NULL, NULL, NULL, NULL, 'Propane/LOX engine for Isar Aerospace Spectrum.');

INSERT INTO engines (name, origin, designer, vehicle, status, use, propellant, power_cycle, isp_s, thrust_n, chamber_pressure_bar, mass_kg, thrust_to_weight_ratio, of_ratio, description)
VALUES ('Archimedes', 'USA', 'Rocket Lab', 'Neutron', 'Development', '2nd stage', 'RP-1 / LOX', 'Gas generator', 290.0, 45000, NULL, 35.0, NULL, NULL, 'The Archimedes is an upper stage engine being developed by Rocket Lab for the Neutron rocket.');
