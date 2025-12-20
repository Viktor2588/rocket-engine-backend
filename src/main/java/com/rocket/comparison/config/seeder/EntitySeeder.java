package com.rocket.comparison.config.seeder;

/**
 * Interface for entity seeders.
 * Each seeder is responsible for seeding a specific entity type.
 */
public interface EntitySeeder {

    /**
     * Seeds entities if the repository is empty.
     */
    void seedIfEmpty();

    /**
     * Returns the entity type name for logging.
     */
    String getEntityName();

    /**
     * Returns the current count of entities.
     */
    long count();
}
