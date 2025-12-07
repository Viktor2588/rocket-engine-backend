package com.rocket.comparison.repository;

import com.rocket.comparison.entity.LaunchVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaunchVehicleRepository extends JpaRepository<LaunchVehicle, Long> {

    List<LaunchVehicle> findByCountryId(Long countryId);

    List<LaunchVehicle> findByActiveTrue();

    List<LaunchVehicle> findByReusableTrue();

    List<LaunchVehicle> findByHumanRatedTrue();

    List<LaunchVehicle> findByFamily(String family);

    Optional<LaunchVehicle> findByName(String name);

    Optional<LaunchVehicle> findByFullName(String fullName);

    @Query("SELECT lv FROM LaunchVehicle lv WHERE lv.status = 'Active' ORDER BY lv.totalLaunches DESC")
    List<LaunchVehicle> findActiveVehiclesByLaunches();

    @Query("SELECT lv FROM LaunchVehicle lv WHERE lv.payloadToLeoKg IS NOT NULL ORDER BY lv.payloadToLeoKg DESC")
    List<LaunchVehicle> findByPayloadCapacity();

    @Query("SELECT COUNT(lv) FROM LaunchVehicle lv WHERE lv.active = true")
    Long countActiveVehicles();

    @Query("SELECT COUNT(lv) FROM LaunchVehicle lv WHERE lv.reusable = true")
    Long countReusableVehicles();
}
