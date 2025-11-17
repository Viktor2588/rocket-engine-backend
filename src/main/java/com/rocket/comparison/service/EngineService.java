package com.rocket.comparison.service;

import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EngineService {

    private final EngineRepository engineRepository;

    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }

    public Optional<Engine> getEngineById(Long id) {
        return engineRepository.findById(id);
    }

    public Engine saveEngine(Engine engine) {
        return engineRepository.save(engine);
    }

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

        return engineRepository.save(engine);
    }
}
