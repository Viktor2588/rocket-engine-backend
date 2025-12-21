package com.rocket.comparison.service;

import com.rocket.comparison.entity.LaunchVehicle;
import com.rocket.comparison.repository.LaunchVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaunchVehicleService {

    private final LaunchVehicleRepository launchVehicleRepository;

    public List<LaunchVehicle> findAll() {
        return launchVehicleRepository.findAllWithCountry();
    }

    public Optional<LaunchVehicle> findById(Long id) {
        return launchVehicleRepository.findByIdWithCountry(id);
    }

    public Optional<LaunchVehicle> findByName(String name) {
        return launchVehicleRepository.findByName(name);
    }

    public List<LaunchVehicle> findByCountry(Long countryId) {
        return launchVehicleRepository.findByCountryId(countryId);
    }

    public List<LaunchVehicle> getLaunchVehiclesByCountryCode(String isoCode) {
        return launchVehicleRepository.findByCountryIsoCode(isoCode.toUpperCase());
    }

    public List<LaunchVehicle> findActive() {
        return launchVehicleRepository.findByActiveTrue();
    }

    public List<LaunchVehicle> findReusable() {
        return launchVehicleRepository.findByReusableTrue();
    }

    public List<LaunchVehicle> findHumanRated() {
        return launchVehicleRepository.findByHumanRatedTrue();
    }

    public List<LaunchVehicle> findByFamily(String family) {
        return launchVehicleRepository.findByFamily(family);
    }

    public List<LaunchVehicle> findByPayloadCapacity() {
        return launchVehicleRepository.findByPayloadCapacity();
    }

    public LaunchVehicle save(LaunchVehicle vehicle) {
        return launchVehicleRepository.save(vehicle);
    }

    public void deleteById(Long id) {
        launchVehicleRepository.deleteById(id);
    }

    public Long countActive() {
        return launchVehicleRepository.countActiveVehicles();
    }

    public Long countReusable() {
        return launchVehicleRepository.countReusableVehicles();
    }

    public long count() {
        return launchVehicleRepository.count();
    }
}
