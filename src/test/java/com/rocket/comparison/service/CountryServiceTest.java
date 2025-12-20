package com.rocket.comparison.service;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.repository.CountryRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country usa;
    private Country russia;

    @BeforeEach
    void setUp() {
        usa = new Country();
        usa.setId(1L);
        usa.setName("United States");
        usa.setIsoCode("USA");
        usa.setRegion("North America");
        usa.setHumanSpaceflightCapable(true);
        usa.setIndependentLaunchCapable(true);
        usa.setTotalLaunches(1800);
        usa.setSuccessfulLaunches(1700);

        russia = new Country();
        russia.setId(2L);
        russia.setName("Russia");
        russia.setIsoCode("RUS");
        russia.setRegion("Europe");
        russia.setHumanSpaceflightCapable(true);
        russia.setIndependentLaunchCapable(true);
        russia.setTotalLaunches(3200);
        russia.setSuccessfulLaunches(3000);
    }

    @Test
    void getAllCountries_shouldReturnAllCountries() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(usa, russia));

        List<Country> result = countryService.getAllCountries();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(usa, russia);
        verify(countryRepository).findAll();
    }

    @Test
    void getAllCountriesPageable_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Country> page = new PageImpl<>(Arrays.asList(usa, russia), pageable, 2);
        when(countryRepository.findAll(pageable)).thenReturn(page);

        Page<Country> result = countryService.getAllCountries(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(countryRepository).findAll(pageable);
    }

    @Test
    void getCountryById_whenExists_shouldReturnCountry() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(usa));

        Optional<Country> result = countryService.getCountryById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("United States");
        verify(countryRepository).findById(1L);
    }

    @Test
    void getCountryById_whenNotExists_shouldReturnEmpty() {
        when(countryRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Country> result = countryService.getCountryById(999L);

        assertThat(result).isEmpty();
        verify(countryRepository).findById(999L);
    }

    @Test
    void getCountryByIsoCode_shouldConvertToUppercase() {
        when(countryRepository.findByIsoCode("USA")).thenReturn(Optional.of(usa));

        Optional<Country> result = countryService.getCountryByIsoCode("usa");

        assertThat(result).isPresent();
        verify(countryRepository).findByIsoCode("USA");
    }

    @Test
    void getCountryByName_shouldReturnCountry() {
        when(countryRepository.findByName("United States")).thenReturn(Optional.of(usa));

        Optional<Country> result = countryService.getCountryByName("United States");

        assertThat(result).isPresent();
        assertThat(result.get().getIsoCode()).isEqualTo("USA");
    }

    @Test
    void saveCountry_shouldConvertIsoCodeToUppercase() {
        Country newCountry = new Country();
        newCountry.setName("France");
        newCountry.setIsoCode("fra");

        when(countryRepository.save(any(Country.class))).thenAnswer(i -> i.getArgument(0));

        Country result = countryService.saveCountry(newCountry);

        assertThat(result.getIsoCode()).isEqualTo("FRA");
        verify(countryRepository).save(newCountry);
    }

    @Test
    void deleteCountry_shouldCallRepository() {
        doNothing().when(countryRepository).deleteById(1L);

        countryService.deleteCountry(1L);

        verify(countryRepository).deleteById(1L);
    }

    @Test
    void updateCountry_whenExists_shouldUpdateAndReturn() {
        Country updateDetails = new Country();
        updateDetails.setName("United States of America");
        updateDetails.setIsoCode("usa");
        updateDetails.setRegion("North America");

        when(countryRepository.findById(1L)).thenReturn(Optional.of(usa));
        when(countryRepository.save(any(Country.class))).thenAnswer(i -> i.getArgument(0));

        Country result = countryService.updateCountry(1L, updateDetails);

        assertThat(result.getName()).isEqualTo("United States of America");
        assertThat(result.getIsoCode()).isEqualTo("USA");
        verify(countryRepository).findById(1L);
        verify(countryRepository).save(any(Country.class));
    }

    @Test
    void updateCountry_whenNotExists_shouldThrowException() {
        Country updateDetails = new Country();
        when(countryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.updateCountry(999L, updateDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Country not found");
    }

    @Test
    void getCountriesByRegion_shouldReturnFilteredList() {
        when(countryRepository.findByRegion("Europe")).thenReturn(List.of(russia));

        List<Country> result = countryService.getCountriesByRegion("Europe");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Russia");
    }

    @Test
    void getCountriesWithHumanSpaceflight_shouldReturnCapableCountries() {
        when(countryRepository.findByHumanSpaceflightCapableTrue()).thenReturn(Arrays.asList(usa, russia));

        List<Country> result = countryService.getCountriesWithHumanSpaceflight();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(Country::getHumanSpaceflightCapable);
    }

    @Test
    void getCountriesWithLaunchCapability_shouldReturnCapableCountries() {
        when(countryRepository.findByIndependentLaunchCapableTrue()).thenReturn(Arrays.asList(usa, russia));

        List<Country> result = countryService.getCountriesWithLaunchCapability();

        assertThat(result).hasSize(2);
    }

    @Test
    void countAll_shouldReturnCount() {
        when(countryRepository.count()).thenReturn(12L);

        long result = countryService.countAll();

        assertThat(result).isEqualTo(12L);
    }

    @Test
    void countWithLaunchCapability_shouldReturnCount() {
        when(countryRepository.countWithLaunchCapability()).thenReturn(5L);

        Long result = countryService.countWithLaunchCapability();

        assertThat(result).isEqualTo(5L);
    }

    @Test
    void existsByIsoCode_shouldConvertToUppercaseAndCheck() {
        when(countryRepository.findByIsoCode("USA")).thenReturn(Optional.of(usa));

        boolean result = countryService.existsByIsoCode("usa");

        assertThat(result).isTrue();
        verify(countryRepository).findByIsoCode("USA");
    }

    @Test
    void existsById_shouldReturnTrue_whenExists() {
        when(countryRepository.existsById(1L)).thenReturn(true);

        boolean result = countryService.existsById(1L);

        assertThat(result).isTrue();
    }

    @Test
    void existsById_shouldReturnFalse_whenNotExists() {
        when(countryRepository.existsById(999L)).thenReturn(false);

        boolean result = countryService.existsById(999L);

        assertThat(result).isFalse();
    }
}
