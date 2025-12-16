package com.rocket.comparison.service;

import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EngineService {

    private final EngineRepository engineRepository;

    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }

    public Page<Engine> getAllEngines(Pageable pageable) {
        return engineRepository.findAll(pageable);
    }

    public Optional<Engine> getEngineById(Long id) {
        return engineRepository.findById(id);
    }

    @Transactional
    public Engine saveEngine(Engine engine) {
        return engineRepository.save(engine);
    }

    @Transactional
    public void deleteEngine(Long id) {
        engineRepository.deleteById(id);
    }

    public List<Engine> getEnginesByDesigner(String designer) {
        return engineRepository.findByDesigner(designer);
    }

    public List<Engine> getEnginesByPropellant(String propellant) {
        return engineRepository.findByPropellant(propellant);
    }

    public List<Engine> getEnginesByMinThrust(Long thrust) {
        return engineRepository.findByThrustNGreaterThan(thrust);
    }

    public List<Engine> getEnginesByMinIsp(Double isp) {
        return engineRepository.findByIsp_sGreaterThan(isp);
    }

    public List<Engine> getEnginesByCountryId(Long countryId) {
        return engineRepository.findByCountryId(countryId);
    }

    public List<Engine> getEnginesByCountryCode(String isoCode) {
        String upperCode = isoCode.toUpperCase();
        // First try by country relationship
        List<Engine> engines = engineRepository.findByCountryIsoCode(upperCode);
        // If no results, fall back to origin field (legacy data)
        if (engines.isEmpty()) {
            engines = engineRepository.findByOrigin(upperCode);
        }
        return engines;
    }

    public List<Engine> getEnginesByOrigin(String origin) {
        return engineRepository.findByOrigin(origin);
    }

    public Long countEnginesByCountryId(Long countryId) {
        return engineRepository.countByCountryId(countryId);
    }

    @Transactional
    public Engine updateEngine(Long id, Engine engineDetails) {
        Engine engine = engineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Engine not found with id: " + id));

        engine.setName(engineDetails.getName());
        engine.setOrigin(engineDetails.getOrigin());
        engine.setDesigner(engineDetails.getDesigner());
        engine.setVehicle(engineDetails.getVehicle());
        engine.setStatus(engineDetails.getStatus());
        engine.setUse(engineDetails.getUse());
        engine.setPropellant(engineDetails.getPropellant());
        engine.setPowerCycle(engineDetails.getPowerCycle());
        engine.setIsp_s(engineDetails.getIsp_s());
        engine.setThrustN(engineDetails.getThrustN());
        engine.setChamberPressureBar(engineDetails.getChamberPressureBar());
        engine.setMassKg(engineDetails.getMassKg());
        engine.setThrustToWeightRatio(engineDetails.getThrustToWeightRatio());
        engine.setOfRatio(engineDetails.getOfRatio());
        engine.setDescription(engineDetails.getDescription());
        engine.setCountry(engineDetails.getCountry());

        return engineRepository.save(engine);
    }
}
