package com.rocket.comparison.service;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    // Basic CRUD operations
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }

    public Optional<Country> getCountryByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(isoCode.toUpperCase());
    }

    public Optional<Country> getCountryByName(String name) {
        return countryRepository.findByName(name);
    }

    public Country saveCountry(Country country) {
        // Ensure ISO code is uppercase
        if (country.getIsoCode() != null) {
            country.setIsoCode(country.getIsoCode().toUpperCase());
        }
        // Calculate launch success rate if not set
        if (country.getLaunchSuccessRate() == null && country.getTotalLaunches() != null && country.getTotalLaunches() > 0) {
            country.setLaunchSuccessRate(country.calculateLaunchSuccessRate());
        }
        return countryRepository.save(country);
    }

    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }

    public Country updateCountry(Long id, Country countryDetails) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with id: " + id));

        country.setName(countryDetails.getName());
        country.setIsoCode(countryDetails.getIsoCode() != null ? countryDetails.getIsoCode().toUpperCase() : null);
        country.setFlagUrl(countryDetails.getFlagUrl());
        country.setSpaceAgencyName(countryDetails.getSpaceAgencyName());
        country.setSpaceAgencyAcronym(countryDetails.getSpaceAgencyAcronym());
        country.setSpaceAgencyFounded(countryDetails.getSpaceAgencyFounded());
        country.setAnnualBudgetUsd(countryDetails.getAnnualBudgetUsd());
        country.setBudgetAsPercentOfGdp(countryDetails.getBudgetAsPercentOfGdp());
        country.setTotalLaunches(countryDetails.getTotalLaunches());
        country.setSuccessfulLaunches(countryDetails.getSuccessfulLaunches());
        country.setLaunchSuccessRate(countryDetails.getLaunchSuccessRate());
        country.setActiveAstronauts(countryDetails.getActiveAstronauts());
        country.setTotalSpaceAgencyEmployees(countryDetails.getTotalSpaceAgencyEmployees());
        country.setHumanSpaceflightCapable(countryDetails.getHumanSpaceflightCapable());
        country.setIndependentLaunchCapable(countryDetails.getIndependentLaunchCapable());
        country.setReusableRocketCapable(countryDetails.getReusableRocketCapable());
        country.setDeepSpaceCapable(countryDetails.getDeepSpaceCapable());
        country.setSpaceStationCapable(countryDetails.getSpaceStationCapable());
        country.setLunarLandingCapable(countryDetails.getLunarLandingCapable());
        country.setMarsLandingCapable(countryDetails.getMarsLandingCapable());
        country.setOverallCapabilityScore(countryDetails.getOverallCapabilityScore());
        country.setRegion(countryDetails.getRegion());
        country.setDescription(countryDetails.getDescription());

        // Recalculate success rate if launches updated
        if (country.getLaunchSuccessRate() == null && country.getTotalLaunches() != null && country.getTotalLaunches() > 0) {
            country.setLaunchSuccessRate(country.calculateLaunchSuccessRate());
        }

        return countryRepository.save(country);
    }

    // Region-based queries
    public List<Country> getCountriesByRegion(String region) {
        return countryRepository.findByRegion(region);
    }

    // Capability-based queries
    public List<Country> getCountriesWithHumanSpaceflight() {
        return countryRepository.findByHumanSpaceflightCapableTrue();
    }

    public List<Country> getCountriesWithLaunchCapability() {
        return countryRepository.findByIndependentLaunchCapableTrue();
    }

    public List<Country> getCountriesWithReusableRockets() {
        return countryRepository.findByReusableRocketCapableTrue();
    }

    public List<Country> getCountriesWithDeepSpaceCapability() {
        return countryRepository.findByDeepSpaceCapableTrue();
    }

    public List<Country> getCountriesWithSpaceStation() {
        return countryRepository.findBySpaceStationCapableTrue();
    }

    // Rankings
    public List<Country> getCountryRankings() {
        return countryRepository.findAllOrderByCapabilityScoreDesc();
    }

    public List<Country> getCountriesByMinScore(Double minScore) {
        return countryRepository.findByMinCapabilityScore(minScore);
    }

    public List<Country> getTopCountriesByLaunches() {
        return countryRepository.findTopByTotalLaunches();
    }

    public List<Country> getTopCountriesBySuccessRate() {
        return countryRepository.findTopBySuccessRate();
    }

    // Statistics
    public Long countWithLaunchCapability() {
        return countryRepository.countWithLaunchCapability();
    }

    public Long countWithHumanSpaceflight() {
        return countryRepository.countWithHumanSpaceflight();
    }

    // Check if country exists
    public boolean existsByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(isoCode.toUpperCase()).isPresent();
    }

    public boolean existsById(Long id) {
        return countryRepository.existsById(id);
    }
}
