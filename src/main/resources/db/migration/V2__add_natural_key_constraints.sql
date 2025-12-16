-- V2__add_natural_key_constraints.sql
-- BE-071: Add unique constraints for natural keys

-- Countries: iso_code should be unique
ALTER TABLE countries ADD CONSTRAINT uk_country_iso_code UNIQUE (iso_code);

-- Engines: name + country_id should be unique (same engine name could exist in different countries)
-- Note: Using partial index to handle null country_id
CREATE UNIQUE INDEX uk_engine_name_country ON engines (name, country_id) WHERE country_id IS NOT NULL;
CREATE UNIQUE INDEX uk_engine_name_origin ON engines (name, origin) WHERE country_id IS NULL AND origin IS NOT NULL;

-- Satellites: norad_id and cospar_id should be unique
ALTER TABLE satellites ADD CONSTRAINT uk_satellite_norad_id UNIQUE (norad_id);
ALTER TABLE satellites ADD CONSTRAINT uk_satellite_cospar_id UNIQUE (cospar_id);

-- Launch sites: short_name should be unique
ALTER TABLE launch_sites ADD CONSTRAINT uk_launch_site_short_name UNIQUE (short_name);

-- Space missions: name + country should be unique (in case of duplicate mission names across countries)
CREATE UNIQUE INDEX uk_mission_name_country ON space_missions (name, country_id) WHERE country_id IS NOT NULL;

-- Space milestones: title + country + date should be unique
CREATE UNIQUE INDEX uk_milestone_title_country_date ON space_milestones (title, country_id, date_achieved) WHERE country_id IS NOT NULL;

-- Launch vehicles: name + country should be unique
CREATE UNIQUE INDEX uk_launch_vehicle_name_country ON launch_vehicles (name, country_id) WHERE country_id IS NOT NULL;

-- Capability scores: country_id + category should be unique (one score per category per country)
ALTER TABLE capability_scores ADD CONSTRAINT uk_capability_score_country_category UNIQUE (country_id, category);
