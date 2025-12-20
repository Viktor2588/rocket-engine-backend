package com.rocket.comparison.service;

import com.rocket.comparison.dto.ExportDto;
import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for exporting all data from the system (BE-004).
 * Gathers data from all repositories and packages it for download.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ExportService {

    private static final String EXPORT_VERSION = "1.0";

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final LaunchVehicleRepository launchVehicleRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final SpaceMilestoneRepository spaceMilestoneRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final SatelliteRepository satelliteRepository;
    private final CapabilityScoreRepository capabilityScoreRepository;

    /**
     * Export all data from the system.
     *
     * @return ExportDto containing all entities and metadata
     */
    public ExportDto exportAllData() {
        log.info("Starting full data export...");
        LocalDateTime exportTime = LocalDateTime.now();

        // Fetch all data
        List<Country> countries = countryRepository.findAll();
        List<Engine> engines = engineRepository.findAll();
        List<LaunchVehicle> launchVehicles = launchVehicleRepository.findAll();
        List<SpaceMission> spaceMissions = spaceMissionRepository.findAll();
        List<SpaceMilestone> spaceMilestones = spaceMilestoneRepository.findAll();
        List<LaunchSite> launchSites = launchSiteRepository.findAll();
        List<Satellite> satellites = satelliteRepository.findAll();
        List<CapabilityScore> capabilityScores = capabilityScoreRepository.findAll();

        // Build counts
        ExportDto.ExportCounts counts = ExportDto.ExportCounts.builder()
                .countries(countries.size())
                .engines(engines.size())
                .launchVehicles(launchVehicles.size())
                .spaceMissions(spaceMissions.size())
                .spaceMilestones(spaceMilestones.size())
                .launchSites(launchSites.size())
                .satellites(satellites.size())
                .capabilityScores(capabilityScores.size())
                .total(countries.size() + engines.size() + launchVehicles.size() +
                       spaceMissions.size() + spaceMilestones.size() + launchSites.size() +
                       satellites.size() + capabilityScores.size())
                .build();

        // Build metadata
        ExportDto.ExportMetadata metadata = ExportDto.ExportMetadata.builder()
                .exportedAt(exportTime)
                .version(EXPORT_VERSION)
                .counts(counts)
                .build();

        log.info("Data export completed. Total records: {}", counts.getTotal());

        return ExportDto.builder()
                .metadata(metadata)
                .countries(countries)
                .engines(engines)
                .launchVehicles(launchVehicles)
                .spaceMissions(spaceMissions)
                .spaceMilestones(spaceMilestones)
                .launchSites(launchSites)
                .satellites(satellites)
                .capabilityScores(capabilityScores)
                .build();
    }

    /**
     * Export only specific entity types.
     *
     * @param includeCountries include countries in export
     * @param includeEngines include engines in export
     * @param includeLaunchVehicles include launch vehicles in export
     * @param includeSpaceMissions include space missions in export
     * @param includeSpaceMilestones include space milestones in export
     * @param includeLaunchSites include launch sites in export
     * @param includeSatellites include satellites in export
     * @param includeCapabilityScores include capability scores in export
     * @return ExportDto with selected entities
     */
    public ExportDto exportSelectedData(
            boolean includeCountries,
            boolean includeEngines,
            boolean includeLaunchVehicles,
            boolean includeSpaceMissions,
            boolean includeSpaceMilestones,
            boolean includeLaunchSites,
            boolean includeSatellites,
            boolean includeCapabilityScores) {

        log.info("Starting selective data export...");
        LocalDateTime exportTime = LocalDateTime.now();

        List<Country> countries = includeCountries ? countryRepository.findAll() : List.of();
        List<Engine> engines = includeEngines ? engineRepository.findAll() : List.of();
        List<LaunchVehicle> launchVehicles = includeLaunchVehicles ? launchVehicleRepository.findAll() : List.of();
        List<SpaceMission> spaceMissions = includeSpaceMissions ? spaceMissionRepository.findAll() : List.of();
        List<SpaceMilestone> spaceMilestones = includeSpaceMilestones ? spaceMilestoneRepository.findAll() : List.of();
        List<LaunchSite> launchSites = includeLaunchSites ? launchSiteRepository.findAll() : List.of();
        List<Satellite> satellites = includeSatellites ? satelliteRepository.findAll() : List.of();
        List<CapabilityScore> capabilityScores = includeCapabilityScores ? capabilityScoreRepository.findAll() : List.of();

        ExportDto.ExportCounts counts = ExportDto.ExportCounts.builder()
                .countries(countries.size())
                .engines(engines.size())
                .launchVehicles(launchVehicles.size())
                .spaceMissions(spaceMissions.size())
                .spaceMilestones(spaceMilestones.size())
                .launchSites(launchSites.size())
                .satellites(satellites.size())
                .capabilityScores(capabilityScores.size())
                .total(countries.size() + engines.size() + launchVehicles.size() +
                       spaceMissions.size() + spaceMilestones.size() + launchSites.size() +
                       satellites.size() + capabilityScores.size())
                .build();

        ExportDto.ExportMetadata metadata = ExportDto.ExportMetadata.builder()
                .exportedAt(exportTime)
                .version(EXPORT_VERSION)
                .counts(counts)
                .build();

        log.info("Selective data export completed. Total records: {}", counts.getTotal());

        return ExportDto.builder()
                .metadata(metadata)
                .countries(countries)
                .engines(engines)
                .launchVehicles(launchVehicles)
                .spaceMissions(spaceMissions)
                .spaceMilestones(spaceMilestones)
                .launchSites(launchSites)
                .satellites(satellites)
                .capabilityScores(capabilityScores)
                .build();
    }
}
