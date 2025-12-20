package com.rocket.comparison.service;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.LaunchVehicle;
import com.rocket.comparison.repository.LaunchVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LaunchVehicleServiceTest {

    @Mock
    private LaunchVehicleRepository launchVehicleRepository;

    @InjectMocks
    private LaunchVehicleService launchVehicleService;

    private LaunchVehicle falcon9;
    private LaunchVehicle starship;
    private Country usa;

    @BeforeEach
    void setUp() {
        usa = new Country();
        usa.setId(1L);
        usa.setName("United States");
        usa.setIsoCode("USA");

        falcon9 = new LaunchVehicle();
        falcon9.setId(1L);
        falcon9.setName("Falcon 9");
        falcon9.setFamily("Falcon");
        falcon9.setActive(true);
        falcon9.setReusable(true);
        falcon9.setHumanRated(true);
        falcon9.setCountry(usa);
        falcon9.setPayloadToLeoKg(22800);

        starship = new LaunchVehicle();
        starship.setId(2L);
        starship.setName("Starship");
        starship.setFamily("Starship");
        starship.setActive(false);
        starship.setReusable(true);
        starship.setHumanRated(false);
        starship.setCountry(usa);
        starship.setPayloadToLeoKg(150000);
    }

    @Test
    void findAll_shouldReturnAllVehicles() {
        when(launchVehicleRepository.findAll()).thenReturn(Arrays.asList(falcon9, starship));

        List<LaunchVehicle> result = launchVehicleService.findAll();

        assertThat(result).hasSize(2);
        verify(launchVehicleRepository).findAll();
    }

    @Test
    void findById_whenExists_shouldReturnVehicle() {
        when(launchVehicleRepository.findById(1L)).thenReturn(Optional.of(falcon9));

        Optional<LaunchVehicle> result = launchVehicleService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Falcon 9");
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        when(launchVehicleRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<LaunchVehicle> result = launchVehicleService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByName_shouldReturnVehicle() {
        when(launchVehicleRepository.findByName("Falcon 9")).thenReturn(Optional.of(falcon9));

        Optional<LaunchVehicle> result = launchVehicleService.findByName("Falcon 9");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByCountry_shouldReturnFilteredList() {
        when(launchVehicleRepository.findByCountryId(1L)).thenReturn(Arrays.asList(falcon9, starship));

        List<LaunchVehicle> result = launchVehicleService.findByCountry(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void getLaunchVehiclesByCountryCode_shouldConvertToUppercase() {
        when(launchVehicleRepository.findByCountryIsoCode("USA")).thenReturn(Arrays.asList(falcon9, starship));

        List<LaunchVehicle> result = launchVehicleService.getLaunchVehiclesByCountryCode("usa");

        assertThat(result).hasSize(2);
        verify(launchVehicleRepository).findByCountryIsoCode("USA");
    }

    @Test
    void findActive_shouldReturnActiveVehicles() {
        when(launchVehicleRepository.findByActiveTrue()).thenReturn(List.of(falcon9));

        List<LaunchVehicle> result = launchVehicleService.findActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActive()).isTrue();
    }

    @Test
    void findReusable_shouldReturnReusableVehicles() {
        when(launchVehicleRepository.findByReusableTrue()).thenReturn(Arrays.asList(falcon9, starship));

        List<LaunchVehicle> result = launchVehicleService.findReusable();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(LaunchVehicle::getReusable);
    }

    @Test
    void findHumanRated_shouldReturnHumanRatedVehicles() {
        when(launchVehicleRepository.findByHumanRatedTrue()).thenReturn(List.of(falcon9));

        List<LaunchVehicle> result = launchVehicleService.findHumanRated();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHumanRated()).isTrue();
    }

    @Test
    void findByFamily_shouldReturnFilteredList() {
        when(launchVehicleRepository.findByFamily("Falcon")).thenReturn(List.of(falcon9));

        List<LaunchVehicle> result = launchVehicleService.findByFamily("Falcon");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFamily()).isEqualTo("Falcon");
    }

    @Test
    void findByPayloadCapacity_shouldReturnOrderedList() {
        when(launchVehicleRepository.findByPayloadCapacity()).thenReturn(Arrays.asList(starship, falcon9));

        List<LaunchVehicle> result = launchVehicleService.findByPayloadCapacity();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Starship");
    }

    @Test
    void save_shouldSaveAndReturnVehicle() {
        when(launchVehicleRepository.save(any(LaunchVehicle.class))).thenReturn(falcon9);

        LaunchVehicle result = launchVehicleService.save(falcon9);

        assertThat(result.getName()).isEqualTo("Falcon 9");
        verify(launchVehicleRepository).save(falcon9);
    }

    @Test
    void deleteById_shouldCallRepository() {
        doNothing().when(launchVehicleRepository).deleteById(1L);

        launchVehicleService.deleteById(1L);

        verify(launchVehicleRepository).deleteById(1L);
    }

    @Test
    void countActive_shouldReturnCount() {
        when(launchVehicleRepository.countActiveVehicles()).thenReturn(10L);

        Long result = launchVehicleService.countActive();

        assertThat(result).isEqualTo(10L);
    }

    @Test
    void countReusable_shouldReturnCount() {
        when(launchVehicleRepository.countReusableVehicles()).thenReturn(5L);

        Long result = launchVehicleService.countReusable();

        assertThat(result).isEqualTo(5L);
    }

    @Test
    void count_shouldReturnTotalCount() {
        when(launchVehicleRepository.count()).thenReturn(20L);

        long result = launchVehicleService.count();

        assertThat(result).isEqualTo(20L);
    }
}
