package com.rocket.comparison.controller;

import com.rocket.comparison.entity.LaunchVehicle;
import com.rocket.comparison.service.LaunchVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/launch-vehicles")
@RequiredArgsConstructor
public class LaunchVehicleController {

    private final LaunchVehicleService launchVehicleService;

    @GetMapping
    public List<LaunchVehicle> getAll() {
        return launchVehicleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaunchVehicle> getById(@PathVariable Long id) {
        return launchVehicleService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-country/{countryId}")
    public List<LaunchVehicle> getByCountry(@PathVariable Long countryId) {
        return launchVehicleService.findByCountry(countryId);
    }

    @GetMapping("/active")
    public List<LaunchVehicle> getActive() {
        return launchVehicleService.findActive();
    }

    @GetMapping("/reusable")
    public List<LaunchVehicle> getReusable() {
        return launchVehicleService.findReusable();
    }

    @GetMapping("/human-rated")
    public List<LaunchVehicle> getHumanRated() {
        return launchVehicleService.findHumanRated();
    }

    @GetMapping("/family/{family}")
    public List<LaunchVehicle> getByFamily(@PathVariable String family) {
        return launchVehicleService.findByFamily(family);
    }

    @GetMapping("/by-payload")
    public List<LaunchVehicle> getByPayloadCapacity() {
        return launchVehicleService.findByPayloadCapacity();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
            "total", launchVehicleService.count(),
            "active", launchVehicleService.countActive(),
            "reusable", launchVehicleService.countReusable()
        ));
    }

    @PostMapping
    public LaunchVehicle create(@RequestBody LaunchVehicle vehicle) {
        return launchVehicleService.save(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaunchVehicle> update(@PathVariable Long id, @RequestBody LaunchVehicle vehicle) {
        return launchVehicleService.findById(id)
            .map(existing -> {
                vehicle.setId(id);
                return ResponseEntity.ok(launchVehicleService.save(vehicle));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (launchVehicleService.findById(id).isPresent()) {
            launchVehicleService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
