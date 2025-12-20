package com.rocket.comparison.service;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EngineServiceTest {

    @Mock
    private EngineRepository engineRepository;

    @InjectMocks
    private EngineService engineService;

    private Engine raptor;
    private Engine merlin;
    private Country usa;

    @BeforeEach
    void setUp() {
        usa = new Country();
        usa.setId(1L);
        usa.setName("United States");
        usa.setIsoCode("USA");

        raptor = new Engine();
        raptor.setId(1L);
        raptor.setName("Raptor");
        raptor.setDesigner("SpaceX");
        raptor.setOrigin("USA");
        raptor.setPropellant("LOX/Methane");
        raptor.setThrustN(2200000L);
        raptor.setIsp_s(363.0);
        raptor.setCountry(usa);

        merlin = new Engine();
        merlin.setId(2L);
        merlin.setName("Merlin 1D");
        merlin.setDesigner("SpaceX");
        merlin.setOrigin("USA");
        merlin.setPropellant("LOX/RP-1");
        merlin.setThrustN(981000L);
        merlin.setIsp_s(311.0);
        merlin.setCountry(usa);
    }

    @Test
    void getAllEngines_shouldReturnAllEngines() {
        when(engineRepository.findAll()).thenReturn(Arrays.asList(raptor, merlin));

        List<Engine> result = engineService.getAllEngines();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(raptor, merlin);
        verify(engineRepository).findAll();
    }

    @Test
    void getAllEnginesPageable_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Engine> page = new PageImpl<>(Arrays.asList(raptor, merlin), pageable, 2);
        when(engineRepository.findAll(pageable)).thenReturn(page);

        Page<Engine> result = engineService.getAllEngines(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(engineRepository).findAll(pageable);
    }

    @Test
    void getEngineById_whenExists_shouldReturnEngine() {
        when(engineRepository.findById(1L)).thenReturn(Optional.of(raptor));

        Optional<Engine> result = engineService.getEngineById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Raptor");
        verify(engineRepository).findById(1L);
    }

    @Test
    void getEngineById_whenNotExists_shouldReturnEmpty() {
        when(engineRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Engine> result = engineService.getEngineById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void saveEngine_shouldSaveAndReturn() {
        when(engineRepository.save(any(Engine.class))).thenReturn(raptor);

        Engine result = engineService.saveEngine(raptor);

        assertThat(result.getName()).isEqualTo("Raptor");
        verify(engineRepository).save(raptor);
    }

    @Test
    void deleteEngine_shouldCallRepository() {
        doNothing().when(engineRepository).deleteById(1L);

        engineService.deleteEngine(1L);

        verify(engineRepository).deleteById(1L);
    }

    @Test
    void getEnginesByDesigner_shouldReturnFilteredList() {
        when(engineRepository.findByDesigner("SpaceX")).thenReturn(Arrays.asList(raptor, merlin));

        List<Engine> result = engineService.getEnginesByDesigner("SpaceX");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(e -> "SpaceX".equals(e.getDesigner()));
    }

    @Test
    void getEnginesByPropellant_shouldReturnFilteredList() {
        when(engineRepository.findByPropellant("LOX/Methane")).thenReturn(List.of(raptor));

        List<Engine> result = engineService.getEnginesByPropellant("LOX/Methane");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Raptor");
    }

    @Test
    void getEnginesByMinThrust_shouldReturnFilteredList() {
        when(engineRepository.findByThrustNGreaterThan(1000000L)).thenReturn(List.of(raptor));

        List<Engine> result = engineService.getEnginesByMinThrust(1000000L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getThrustN()).isGreaterThan(1000000L);
    }

    @Test
    void getEnginesByMinIsp_shouldReturnFilteredList() {
        when(engineRepository.findByIsp_sGreaterThan(350.0)).thenReturn(List.of(raptor));

        List<Engine> result = engineService.getEnginesByMinIsp(350.0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsp_s()).isGreaterThan(350.0);
    }

    @Test
    void getEnginesByCountryId_shouldReturnFilteredList() {
        when(engineRepository.findByCountryId(1L)).thenReturn(Arrays.asList(raptor, merlin));

        List<Engine> result = engineService.getEnginesByCountryId(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void getEnginesByCountryCode_shouldConvertToUppercaseAndSearchByRelationship() {
        when(engineRepository.findByCountryIsoCode("USA")).thenReturn(Arrays.asList(raptor, merlin));

        List<Engine> result = engineService.getEnginesByCountryCode("usa");

        assertThat(result).hasSize(2);
        verify(engineRepository).findByCountryIsoCode("USA");
    }

    @Test
    void getEnginesByCountryCode_shouldFallbackToOriginIfRelationshipEmpty() {
        when(engineRepository.findByCountryIsoCode("USA")).thenReturn(Collections.emptyList());
        when(engineRepository.findByOrigin("USA")).thenReturn(Arrays.asList(raptor, merlin));

        List<Engine> result = engineService.getEnginesByCountryCode("usa");

        assertThat(result).hasSize(2);
        verify(engineRepository).findByCountryIsoCode("USA");
        verify(engineRepository).findByOrigin("USA");
    }

    @Test
    void getEnginesByOrigin_shouldReturnFilteredList() {
        when(engineRepository.findByOrigin("USA")).thenReturn(Arrays.asList(raptor, merlin));

        List<Engine> result = engineService.getEnginesByOrigin("USA");

        assertThat(result).hasSize(2);
    }

    @Test
    void countEnginesByCountryId_shouldReturnCount() {
        when(engineRepository.countByCountryId(1L)).thenReturn(5L);

        Long result = engineService.countEnginesByCountryId(1L);

        assertThat(result).isEqualTo(5L);
    }

    @Test
    void updateEngine_whenExists_shouldUpdateAndReturn() {
        Engine updateDetails = new Engine();
        updateDetails.setName("Raptor 2");
        updateDetails.setDesigner("SpaceX");
        updateDetails.setThrustN(2300000L);

        when(engineRepository.findById(1L)).thenReturn(Optional.of(raptor));
        when(engineRepository.save(any(Engine.class))).thenAnswer(i -> i.getArgument(0));

        Engine result = engineService.updateEngine(1L, updateDetails);

        assertThat(result.getName()).isEqualTo("Raptor 2");
        assertThat(result.getThrustN()).isEqualTo(2300000L);
        verify(engineRepository).findById(1L);
        verify(engineRepository).save(any(Engine.class));
    }

    @Test
    void updateEngine_whenNotExists_shouldThrowException() {
        Engine updateDetails = new Engine();
        when(engineRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> engineService.updateEngine(999L, updateDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Engine not found");
    }
}
