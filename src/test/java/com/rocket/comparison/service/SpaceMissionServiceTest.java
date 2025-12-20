package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.SpaceMissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceMissionServiceTest {

    @Mock
    private SpaceMissionRepository missionRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private SpaceMissionService spaceMissionService;

    private SpaceMission apollo11;
    private SpaceMission artemis1;
    private Country usa;

    @BeforeEach
    void setUp() {
        usa = new Country();
        usa.setId(1L);
        usa.setName("United States");
        usa.setIsoCode("USA");

        apollo11 = new SpaceMission();
        apollo11.setId(1L);
        apollo11.setName("Apollo 11");
        apollo11.setCountry(usa);
        apollo11.setLaunchDate(LocalDate.of(1969, 7, 16));
        apollo11.setStatus(MissionStatus.COMPLETED);
        apollo11.setMissionType(MissionType.LUNAR_CREWED_LANDING);
        apollo11.setDestination(Destination.LUNAR_SURFACE);
        apollo11.setCrewed(true);
        apollo11.setCrewSize(3);
        apollo11.setIsHistoricFirst(true);

        artemis1 = new SpaceMission();
        artemis1.setId(2L);
        artemis1.setName("Artemis I");
        artemis1.setCountry(usa);
        artemis1.setLaunchDate(LocalDate.of(2022, 11, 16));
        artemis1.setStatus(MissionStatus.COMPLETED);
        artemis1.setMissionType(MissionType.LUNAR_ORBITER);
        artemis1.setDestination(Destination.LUNAR_ORBIT);
        artemis1.setCrewed(false);
    }

    @Test
    void getAllMissions_shouldReturnAllMissions() {
        when(missionRepository.findAll()).thenReturn(Arrays.asList(apollo11, artemis1));

        List<SpaceMission> result = spaceMissionService.getAllMissions();

        assertThat(result).hasSize(2);
        verify(missionRepository).findAll();
    }

    @Test
    void getMissionById_whenExists_shouldReturnMission() {
        when(missionRepository.findById(1L)).thenReturn(Optional.of(apollo11));

        Optional<SpaceMission> result = spaceMissionService.getMissionById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Apollo 11");
    }

    @Test
    void getMissionById_whenNotExists_shouldReturnEmpty() {
        when(missionRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<SpaceMission> result = spaceMissionService.getMissionById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void saveMission_shouldSaveAndReturn() {
        when(missionRepository.save(any(SpaceMission.class))).thenReturn(apollo11);

        SpaceMission result = spaceMissionService.saveMission(apollo11);

        assertThat(result.getName()).isEqualTo("Apollo 11");
        verify(missionRepository).save(apollo11);
    }

    @Test
    void deleteMission_shouldCallRepository() {
        doNothing().when(missionRepository).deleteById(1L);

        spaceMissionService.deleteMission(1L);

        verify(missionRepository).deleteById(1L);
    }

    @Test
    void updateMission_whenExists_shouldUpdateAndReturn() {
        SpaceMission updateDetails = new SpaceMission();
        updateDetails.setName("Apollo 11 - Moon Landing");
        updateDetails.setDescription("First crewed Moon landing");

        when(missionRepository.findById(1L)).thenReturn(Optional.of(apollo11));
        when(missionRepository.save(any(SpaceMission.class))).thenAnswer(i -> i.getArgument(0));

        SpaceMission result = spaceMissionService.updateMission(1L, updateDetails);

        assertThat(result.getName()).isEqualTo("Apollo 11 - Moon Landing");
        assertThat(result.getDescription()).isEqualTo("First crewed Moon landing");
        verify(missionRepository).save(any(SpaceMission.class));
    }

    @Test
    void updateMission_whenNotExists_shouldThrowException() {
        SpaceMission updateDetails = new SpaceMission();
        when(missionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> spaceMissionService.updateMission(999L, updateDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Mission not found");
    }

    @Test
    void getMissionsByCountry_shouldReturnFilteredList() {
        when(missionRepository.findByCountryIdOrderByLaunchDateDesc(1L))
                .thenReturn(Arrays.asList(artemis1, apollo11));

        List<SpaceMission> result = spaceMissionService.getMissionsByCountry(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Artemis I");
    }
}
