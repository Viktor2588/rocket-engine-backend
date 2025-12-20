package com.rocket.comparison.service;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalStatisticsServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private EngineRepository engineRepository;
    @Mock
    private SatelliteRepository satelliteRepository;
    @Mock
    private LaunchSiteRepository launchSiteRepository;
    @Mock
    private SpaceMissionRepository spaceMissionRepository;
    @Mock
    private SpaceMilestoneRepository spaceMilestoneRepository;

    @InjectMocks
    private GlobalStatisticsService globalStatisticsService;

    private Country usa;

    @BeforeEach
    void setUp() {
        usa = new Country();
        usa.setId(1L);
        usa.setName("United States");
        usa.setIsoCode("USA");
        usa.setOverallCapabilityScore(95.0);
    }

    @Test
    void getGlobalOverview_shouldReturnAllCounts() {
        when(countryRepository.count()).thenReturn(12L);
        when(engineRepository.count()).thenReturn(50L);
        when(satelliteRepository.count()).thenReturn(100L);
        when(launchSiteRepository.count()).thenReturn(20L);
        when(spaceMissionRepository.count()).thenReturn(200L);
        when(spaceMilestoneRepository.count()).thenReturn(30L);
        when(satelliteRepository.countActiveSatellites()).thenReturn(80L);
        when(launchSiteRepository.countActiveLaunchSites()).thenReturn(15L);
        when(launchSiteRepository.countHumanRatedSites()).thenReturn(5L);
        when(launchSiteRepository.countInterplanetaryCapableSites()).thenReturn(3L);
        when(spaceMissionRepository.countByLaunchYear(org.mockito.ArgumentMatchers.anyInt())).thenReturn(25L);

        Map<String, Object> result = globalStatisticsService.getGlobalOverview();

        assertThat(result).containsEntry("totalCountries", 12L);
        assertThat(result).containsEntry("totalEngines", 50L);
        assertThat(result).containsEntry("totalSatellites", 100L);
        assertThat(result).containsEntry("totalLaunchSites", 20L);
        assertThat(result).containsEntry("totalMissions", 200L);
        assertThat(result).containsEntry("totalMilestones", 30L);
        assertThat(result).containsEntry("activeSatellites", 80L);
        assertThat(result).containsEntry("activeLaunchSites", 15L);
        assertThat(result).containsKey("generatedAt");
    }

    @Test
    void getEntityCounts_shouldReturnDetailedCounts() {
        when(countryRepository.count()).thenReturn(12L);
        when(countryRepository.countWithLaunchCapability()).thenReturn(8L);
        when(countryRepository.countWithHumanSpaceflight()).thenReturn(3L);
        when(engineRepository.count()).thenReturn(50L);
        when(satelliteRepository.count()).thenReturn(100L);
        when(satelliteRepository.countActiveSatellites()).thenReturn(80L);
        when(launchSiteRepository.count()).thenReturn(20L);
        when(launchSiteRepository.countActiveLaunchSites()).thenReturn(15L);
        when(launchSiteRepository.countHumanRatedSites()).thenReturn(5L);
        when(spaceMissionRepository.count()).thenReturn(200L);
        when(spaceMissionRepository.countCrewedMissions()).thenReturn(50L);
        when(spaceMilestoneRepository.count()).thenReturn(30L);
        when(spaceMilestoneRepository.countGlobalFirsts()).thenReturn(10L);

        Map<String, Object> result = globalStatisticsService.getEntityCounts();

        assertThat(result).containsKeys("countries", "engines", "satellites", "launchSites", "missions", "milestones");

        @SuppressWarnings("unchecked")
        Map<String, Object> countries = (Map<String, Object>) result.get("countries");
        assertThat(countries).containsEntry("total", 12L);
        assertThat(countries).containsEntry("withLaunchCapability", 8L);
        assertThat(countries).containsEntry("withHumanSpaceflight", 3L);
    }

    @Test
    void getCountryStatistics_shouldReturnStatisticsForAllCountries() {
        when(countryRepository.findAll()).thenReturn(List.of(usa));
        when(engineRepository.countByCountryId(1L)).thenReturn(10L);
        when(satelliteRepository.countByCountry(1L)).thenReturn(50L);
        when(launchSiteRepository.countByCountry(1L)).thenReturn(5L);
        when(spaceMissionRepository.countByCountry(1L)).thenReturn(100L);
        when(spaceMilestoneRepository.countByCountry(1L)).thenReturn(20L);

        List<Map<String, Object>> result = globalStatisticsService.getCountryStatistics();

        assertThat(result).hasSize(1);
        Map<String, Object> usaStats = result.get(0);
        assertThat(usaStats).containsEntry("name", "United States");
        assertThat(usaStats).containsEntry("isoCode", "USA");
        assertThat(usaStats).containsEntry("engineCount", 10L);
        assertThat(usaStats).containsEntry("satelliteCount", 50L);
        assertThat(usaStats).containsEntry("missionCount", 100L);
        assertThat(usaStats).containsEntry("capabilityScore", 95.0);
    }

    @Test
    void getTopCountries_shouldReturnLimitedResults() {
        when(countryRepository.findAll()).thenReturn(List.of(usa));
        when(engineRepository.countByCountryId(1L)).thenReturn(10L);
        when(satelliteRepository.countByCountry(1L)).thenReturn(50L);
        when(launchSiteRepository.countByCountry(1L)).thenReturn(5L);
        when(spaceMissionRepository.countByCountry(1L)).thenReturn(100L);
        when(spaceMilestoneRepository.countByCountry(1L)).thenReturn(20L);

        Map<String, Object> result = globalStatisticsService.getTopCountries(5);

        assertThat(result).containsKeys("byCapabilityScore", "byMissionCount", "bySatelliteCount");
    }

    @Test
    void getEngineTechnologyStats_shouldReturnTechnologyBreakdown() {
        List<Object[]> propellantData = new java.util.ArrayList<>();
        propellantData.add(new Object[]{"LOX/Methane", 10L});
        propellantData.add(new Object[]{"LOX/RP-1", 15L});

        List<Object[]> cycleData = new java.util.ArrayList<>();
        cycleData.add(new Object[]{"Full-flow staged combustion", 5L});

        List<Object[]> statusData = new java.util.ArrayList<>();
        statusData.add(new Object[]{"Active", 20L});

        when(engineRepository.countByPropellant()).thenReturn(propellantData);
        when(engineRepository.countByPowerCycle()).thenReturn(cycleData);
        when(engineRepository.countByStatus()).thenReturn(statusData);
        when(engineRepository.findMaxThrust()).thenReturn(2200000.0);
        when(engineRepository.findMaxIsp()).thenReturn(363.0);
        when(engineRepository.findAvgThrust()).thenReturn(1000000.0);

        Map<String, Object> result = globalStatisticsService.getEngineTechnologyStats();

        assertThat(result).containsKeys("byPropellantType", "byCycleType", "byStatus", "highestThrustN", "highestIspS");
        assertThat(result.get("highestThrustN")).isEqualTo(2200000.0);
        assertThat(result.get("highestIspS")).isEqualTo(363.0);
    }

    @Test
    void getLaunchInfrastructureStats_shouldReturnInfraStats() {
        when(launchSiteRepository.count()).thenReturn(20L);
        when(launchSiteRepository.countActiveLaunchSites()).thenReturn(15L);
        when(launchSiteRepository.countHumanRatedSites()).thenReturn(5L);
        when(launchSiteRepository.countInterplanetaryCapableSites()).thenReturn(3L);
        when(launchSiteRepository.countGeoCapableSites()).thenReturn(10L);
        when(launchSiteRepository.countPolarCapableSites()).thenReturn(8L);
        when(launchSiteRepository.countSitesWithLandingFacilities()).thenReturn(4L);
        when(launchSiteRepository.countSitesByCountry()).thenReturn(List.of());
        when(launchSiteRepository.findAllRegions()).thenReturn(List.of("North America", "Europe"));

        Map<String, Object> result = globalStatisticsService.getLaunchInfrastructureStats();

        assertThat(result).containsEntry("totalLaunchSites", 20L);
        assertThat(result).containsEntry("activeSites", 15L);
        assertThat(result).containsEntry("humanRatedSites", 5L);
        assertThat(result).containsEntry("interplanetaryCapable", 3L);
    }

    @Test
    void getMissionStats_shouldReturnMissionStatistics() {
        when(spaceMissionRepository.count()).thenReturn(200L);
        when(spaceMissionRepository.countCrewedMissions()).thenReturn(50L);
        when(spaceMissionRepository.countMissionsByType()).thenReturn(List.of());
        when(spaceMissionRepository.countMissionsByYear()).thenReturn(List.of());

        Map<String, Object> result = globalStatisticsService.getMissionStats();

        assertThat(result).containsEntry("totalMissions", 200L);
        assertThat(result).containsEntry("crewedMissions", 50L);
        assertThat(result).containsEntry("uncrewedMissions", 150L);
    }
}
