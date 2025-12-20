package com.rocket.comparison.service;

import com.rocket.comparison.dto.ExportDto;
import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private EngineRepository engineRepository;
    @Mock
    private LaunchVehicleRepository launchVehicleRepository;
    @Mock
    private SpaceMissionRepository spaceMissionRepository;
    @Mock
    private SpaceMilestoneRepository spaceMilestoneRepository;
    @Mock
    private LaunchSiteRepository launchSiteRepository;
    @Mock
    private SatelliteRepository satelliteRepository;
    @Mock
    private CapabilityScoreRepository capabilityScoreRepository;

    @InjectMocks
    private ExportService exportService;

    private Country usa;
    private Engine raptor;

    @BeforeEach
    void setUp() {
        usa = new Country();
        usa.setId(1L);
        usa.setName("United States");
        usa.setIsoCode("USA");

        raptor = new Engine();
        raptor.setId(1L);
        raptor.setName("Raptor");
    }

    @Test
    void exportAllData_shouldReturnAllEntities() {
        when(countryRepository.findAll()).thenReturn(List.of(usa));
        when(engineRepository.findAll()).thenReturn(List.of(raptor));
        when(launchVehicleRepository.findAll()).thenReturn(Collections.emptyList());
        when(spaceMissionRepository.findAll()).thenReturn(Collections.emptyList());
        when(spaceMilestoneRepository.findAll()).thenReturn(Collections.emptyList());
        when(launchSiteRepository.findAll()).thenReturn(Collections.emptyList());
        when(satelliteRepository.findAll()).thenReturn(Collections.emptyList());
        when(capabilityScoreRepository.findAll()).thenReturn(Collections.emptyList());

        ExportDto result = exportService.exportAllData();

        assertThat(result).isNotNull();
        assertThat(result.getCountries()).hasSize(1);
        assertThat(result.getEngines()).hasSize(1);
        assertThat(result.getMetadata()).isNotNull();
        assertThat(result.getMetadata().getVersion()).isEqualTo("1.0");
        assertThat(result.getMetadata().getCounts().getCountries()).isEqualTo(1);
        assertThat(result.getMetadata().getCounts().getEngines()).isEqualTo(1);
        assertThat(result.getMetadata().getCounts().getTotal()).isEqualTo(2);
    }

    @Test
    void exportAllData_shouldCalculateTotalCorrectly() {
        Country russia = new Country();
        russia.setId(2L);
        russia.setName("Russia");

        when(countryRepository.findAll()).thenReturn(Arrays.asList(usa, russia));
        when(engineRepository.findAll()).thenReturn(List.of(raptor));
        when(launchVehicleRepository.findAll()).thenReturn(Collections.emptyList());
        when(spaceMissionRepository.findAll()).thenReturn(Collections.emptyList());
        when(spaceMilestoneRepository.findAll()).thenReturn(Collections.emptyList());
        when(launchSiteRepository.findAll()).thenReturn(Collections.emptyList());
        when(satelliteRepository.findAll()).thenReturn(Collections.emptyList());
        when(capabilityScoreRepository.findAll()).thenReturn(Collections.emptyList());

        ExportDto result = exportService.exportAllData();

        assertThat(result.getMetadata().getCounts().getCountries()).isEqualTo(2);
        assertThat(result.getMetadata().getCounts().getEngines()).isEqualTo(1);
        assertThat(result.getMetadata().getCounts().getTotal()).isEqualTo(3);
    }

    @Test
    void exportSelectedData_shouldOnlyIncludeSelectedEntities() {
        when(countryRepository.findAll()).thenReturn(List.of(usa));
        when(engineRepository.findAll()).thenReturn(List.of(raptor));

        ExportDto result = exportService.exportSelectedData(
                true,   // countries
                true,   // engines
                false,  // launchVehicles
                false,  // spaceMissions
                false,  // spaceMilestones
                false,  // launchSites
                false,  // satellites
                false   // capabilityScores
        );

        assertThat(result.getCountries()).hasSize(1);
        assertThat(result.getEngines()).hasSize(1);
        assertThat(result.getLaunchVehicles()).isEmpty();
        assertThat(result.getSpaceMissions()).isEmpty();
        assertThat(result.getMetadata().getCounts().getTotal()).isEqualTo(2);
    }

    @Test
    void exportSelectedData_withNothingSelected_shouldReturnEmptyLists() {
        ExportDto result = exportService.exportSelectedData(
                false, false, false, false, false, false, false, false
        );

        assertThat(result.getCountries()).isEmpty();
        assertThat(result.getEngines()).isEmpty();
        assertThat(result.getLaunchVehicles()).isEmpty();
        assertThat(result.getMetadata().getCounts().getTotal()).isEqualTo(0);
    }

    @Test
    void exportAllData_shouldIncludeExportTimestamp() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());
        when(engineRepository.findAll()).thenReturn(Collections.emptyList());
        when(launchVehicleRepository.findAll()).thenReturn(Collections.emptyList());
        when(spaceMissionRepository.findAll()).thenReturn(Collections.emptyList());
        when(spaceMilestoneRepository.findAll()).thenReturn(Collections.emptyList());
        when(launchSiteRepository.findAll()).thenReturn(Collections.emptyList());
        when(satelliteRepository.findAll()).thenReturn(Collections.emptyList());
        when(capabilityScoreRepository.findAll()).thenReturn(Collections.emptyList());

        ExportDto result = exportService.exportAllData();

        assertThat(result.getMetadata().getExportedAt()).isNotNull();
    }
}
