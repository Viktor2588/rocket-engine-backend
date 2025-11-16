package com.rocket.comparision.service;

import com.rocket.comparision.entity.Engine;
import com.rocket.comparision.repository.EngineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5433/rocket_engine_comparison",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class EngineServiceIntegrationTest {

    @Autowired
    private EngineService engineService;

    @Autowired
    private EngineRepository engineRepository;

    private Engine testEngine;
    private Engine spacexEngine;
    private Engine blueOriginEngine;

    @BeforeEach
    public void setUp() {
        engineRepository.deleteAll();

        // Create test engines
        testEngine = new Engine();
        testEngine.setName("Merlin 1D");
        testEngine.setManufacturer("SpaceX");
        testEngine.setThrust(934.0);
        testEngine.setIsp(282.0);
        testEngine.setPropellantType("RP-1/LOX");
        testEngine.setMass(470.0);
        testEngine.setDescription("The Merlin 1D is used on Falcon 9");

        spacexEngine = new Engine();
        spacexEngine.setName("Raptor 2");
        spacexEngine.setManufacturer("SpaceX");
        spacexEngine.setThrust(2050.0);
        spacexEngine.setIsp(380.0);
        spacexEngine.setPropellantType("Methane/LOX");
        spacexEngine.setMass(1400.0);
        spacexEngine.setDescription("Full-flow staged combustion engine");

        blueOriginEngine = new Engine();
        blueOriginEngine.setName("BE-4");
        blueOriginEngine.setManufacturer("Blue Origin");
        blueOriginEngine.setThrust(2450.0);
        blueOriginEngine.setIsp(380.0);
        blueOriginEngine.setPropellantType("NG/LOX");
        blueOriginEngine.setMass(1300.0);
        blueOriginEngine.setDescription("Used on Atlas V and Vulcan rockets");

        engineRepository.save(testEngine);
        engineRepository.save(spacexEngine);
        engineRepository.save(blueOriginEngine);
    }

    // ==================== Save Engine ====================
    @Test
    public void testSaveEngine_Success() {
        Engine newEngine = new Engine();
        newEngine.setName("RS-25");
        newEngine.setManufacturer("Aerojet Rocketdyne");
        newEngine.setThrust(1860.0);
        newEngine.setIsp(452.0);
        newEngine.setPropellantType("Hydrogen/LOX");
        newEngine.setMass(3600.0);
        newEngine.setDescription("Used on Space Launch System");

        Engine savedEngine = engineService.saveEngine(newEngine);

        assertNotNull(savedEngine.getId());
        assertEquals("RS-25", savedEngine.getName());
        assertEquals(4, engineRepository.count());
    }

    @Test
    public void testSaveEngine_PersistedCorrectly() {
        Engine newEngine = new Engine();
        newEngine.setName("Test Engine");
        newEngine.setManufacturer("Test Manufacturer");
        newEngine.setThrust(1000.0);
        newEngine.setIsp(300.0);
        newEngine.setPropellantType("LOX/H2");
        newEngine.setMass(500.0);
        newEngine.setDescription("Test description");

        Engine saved = engineService.saveEngine(newEngine);
        Optional<Engine> retrieved = engineService.getEngineById(saved.getId());

        assertTrue(retrieved.isPresent());
        assertEquals("Test Engine", retrieved.get().getName());
        assertEquals(1000.0, retrieved.get().getThrust());
    }

    // ==================== Get All Engines ====================
    @Test
    public void testGetAllEngines_Success() {
        List<Engine> engines = engineService.getAllEngines();

        assertEquals(3, engines.size());
        assertTrue(engines.stream().anyMatch(e -> "Merlin 1D".equals(e.getName())));
        assertTrue(engines.stream().anyMatch(e -> "Raptor 2".equals(e.getName())));
        assertTrue(engines.stream().anyMatch(e -> "BE-4".equals(e.getName())));
    }

    @Test
    public void testGetAllEngines_Empty() {
        engineRepository.deleteAll();
        List<Engine> engines = engineService.getAllEngines();

        assertEquals(0, engines.size());
    }

    // ==================== Get Engine by ID ====================
    @Test
    public void testGetEngineById_Success() {
        Optional<Engine> engine = engineService.getEngineById(testEngine.getId());

        assertTrue(engine.isPresent());
        assertEquals("Merlin 1D", engine.get().getName());
        assertEquals("SpaceX", engine.get().getManufacturer());
    }

    @Test
    public void testGetEngineById_NotFound() {
        Optional<Engine> engine = engineService.getEngineById(99999L);

        assertFalse(engine.isPresent());
    }

    // ==================== Update Engine ====================
    @Test
    public void testUpdateEngine_Success() {
        Engine updatedData = new Engine();
        updatedData.setName("Merlin 1D Updated");
        updatedData.setManufacturer("SpaceX");
        updatedData.setThrust(950.0);
        updatedData.setIsp(285.0);
        updatedData.setPropellantType("RP-1/LOX");
        updatedData.setMass(480.0);
        updatedData.setDescription("Updated description");

        Engine updated = engineService.updateEngine(testEngine.getId(), updatedData);

        assertEquals("Merlin 1D Updated", updated.getName());
        assertEquals(950.0, updated.getThrust());
        assertEquals(285.0, updated.getIsp());
    }

    @Test
    public void testUpdateEngine_NotFound() {
        Engine updatedData = new Engine();
        updatedData.setName("Test");
        updatedData.setManufacturer("Test");
        updatedData.setThrust(100.0);
        updatedData.setIsp(200.0);
        updatedData.setPropellantType("LOX/H2");
        updatedData.setMass(100.0);

        assertThrows(IllegalArgumentException.class, () ->
            engineService.updateEngine(99999L, updatedData)
        );
    }

    @Test
    public void testUpdateEngine_PartialUpdate() {
        Engine updatedData = new Engine();
        updatedData.setName("Merlin 1D - Modified");
        updatedData.setManufacturer("SpaceX");
        updatedData.setThrust(934.0);
        updatedData.setIsp(282.0);
        updatedData.setPropellantType("RP-1/LOX");
        updatedData.setMass(470.0);
        updatedData.setDescription("Modified description");

        Engine updated = engineService.updateEngine(testEngine.getId(), updatedData);

        assertEquals("Merlin 1D - Modified", updated.getName());
        assertEquals("Modified description", updated.getDescription());
        assertEquals(934.0, updated.getThrust()); // Unchanged
    }

    // ==================== Delete Engine ====================
    @Test
    public void testDeleteEngine_Success() {
        Long engineId = testEngine.getId();
        assertEquals(3, engineRepository.count());

        engineService.deleteEngine(engineId);

        assertEquals(2, engineRepository.count());
        assertFalse(engineRepository.existsById(engineId));
    }

    @Test
    public void testDeleteEngine_NonExistent() {
        // Should not throw, just no-op
        assertDoesNotThrow(() -> engineService.deleteEngine(99999L));
        assertEquals(3, engineRepository.count());
    }

    // ==================== Filter by Manufacturer ====================
    @Test
    public void testGetEnginesByManufacturer_Success() {
        List<Engine> spacexEngines = engineService.getEnginesByManufacturer("SpaceX");

        assertEquals(2, spacexEngines.size());
        assertTrue(spacexEngines.stream().allMatch(e -> "SpaceX".equals(e.getManufacturer())));
    }

    @Test
    public void testGetEnginesByManufacturer_SingleResult() {
        List<Engine> blueOriginEngines = engineService.getEnginesByManufacturer("Blue Origin");

        assertEquals(1, blueOriginEngines.size());
        assertEquals("BE-4", blueOriginEngines.get(0).getName());
    }

    @Test
    public void testGetEnginesByManufacturer_NoResults() {
        List<Engine> engines = engineService.getEnginesByManufacturer("NonExistent");

        assertEquals(0, engines.size());
    }

    @Test
    public void testGetEnginesByManufacturer_CaseSensitive() {
        List<Engine> engines = engineService.getEnginesByManufacturer("spacex");

        // This test depends on whether the service uses case-insensitive queries
        // Current implementation is case-sensitive
        assertEquals(0, engines.size());
    }

    // ==================== Filter by Propellant ====================
    @Test
    public void testGetEnginesByPropellant_Success() {
        List<Engine> rpLoxEngines = engineService.getEnginesByPropellant("RP-1/LOX");

        assertEquals(1, rpLoxEngines.size());
        assertEquals("Merlin 1D", rpLoxEngines.get(0).getName());
    }

    @Test
    public void testGetEnginesByPropellant_ExactMatch() {
        // Note: Service uses exact matching, not LIKE queries
        List<Engine> methLoxEngines = engineService.getEnginesByPropellant("Methane/LOX");

        assertEquals(1, methLoxEngines.size());
        assertEquals("Raptor 2", methLoxEngines.get(0).getName());
    }

    @Test
    public void testGetEnginesByPropellant_NoResults() {
        List<Engine> engines = engineService.getEnginesByPropellant("Kerosene/Air");

        assertEquals(0, engines.size());
    }

    // ==================== Filter by Minimum Thrust ====================
    @Test
    public void testGetEnginesByMinThrust_Success() {
        List<Engine> highThrustEngines = engineService.getEnginesByMinThrust(2000.0);

        assertEquals(2, highThrustEngines.size());
        assertTrue(highThrustEngines.stream().allMatch(e -> e.getThrust() >= 2000.0));
    }

    @Test
    public void testGetEnginesByMinThrust_AllEngines() {
        List<Engine> allEngines = engineService.getEnginesByMinThrust(0.0);

        assertEquals(3, allEngines.size());
    }

    @Test
    public void testGetEnginesByMinThrust_NoResults() {
        List<Engine> engines = engineService.getEnginesByMinThrust(5000.0);

        assertEquals(0, engines.size());
    }

    @Test
    public void testGetEnginesByMinThrust_BoundaryCondition() {
        // Note: findByThrustGreaterThan uses strict > not >=
        // So thrust > 934.0 returns only Raptor 2 and BE-4
        List<Engine> engines = engineService.getEnginesByMinThrust(934.0);

        assertEquals(2, engines.size());
        assertTrue(engines.stream().allMatch(e -> e.getThrust() > 934.0));
    }

    // ==================== Filter by Minimum ISP ====================
    @Test
    public void testGetEnginesByMinIsp_Success() {
        List<Engine> efficientEngines = engineService.getEnginesByMinIsp(350.0);

        assertEquals(2, efficientEngines.size());
        assertTrue(efficientEngines.stream().allMatch(e -> e.getIsp() >= 350.0));
    }

    @Test
    public void testGetEnginesByMinIsp_AllEngines() {
        List<Engine> allEngines = engineService.getEnginesByMinIsp(0.0);

        assertEquals(3, allEngines.size());
    }

    @Test
    public void testGetEnginesByMinIsp_NoResults() {
        List<Engine> engines = engineService.getEnginesByMinIsp(500.0);

        assertEquals(0, engines.size());
    }

    @Test
    public void testGetEnginesByMinIsp_BoundaryCondition() {
        // Note: findByIspGreaterThan uses strict > not >=
        // So isp > 380.0 returns no engines (Raptor 2 and BE-4 are exactly 380)
        List<Engine> engines = engineService.getEnginesByMinIsp(380.0);

        assertEquals(0, engines.size());
    }

    @Test
    public void testGetEnginesByMinIsp_JustBelow() {
        // isp > 379.0 includes Raptor 2 and BE-4
        List<Engine> engines = engineService.getEnginesByMinIsp(379.0);

        assertEquals(2, engines.size());
    }

    // ==================== Complex Filtering Scenarios ====================
    @Test
    public void testMultipleFiltersChained() {
        // Get SpaceX engines
        List<Engine> spacexEngines = engineService.getEnginesByManufacturer("SpaceX");

        // Filter by thrust
        List<Engine> spacexHighThrust = spacexEngines.stream()
            .filter(e -> e.getThrust() >= 2000.0)
            .toList();

        assertEquals(1, spacexHighThrust.size());
        assertEquals("Raptor 2", spacexHighThrust.get(0).getName());
    }

    @Test
    public void testDatabaseConsistency() {
        // Add an engine
        Engine newEngine = new Engine();
        newEngine.setName("Test Engine");
        newEngine.setManufacturer("Test Co");
        newEngine.setThrust(100.0);
        newEngine.setIsp(200.0);
        newEngine.setPropellantType("LOX/RP-1");
        newEngine.setMass(50.0);

        engineService.saveEngine(newEngine);

        // Retrieve via different methods
        List<Engine> allEngines = engineService.getAllEngines();
        Optional<Engine> byManufacturer = engineService.getEnginesByManufacturer("Test Co")
            .stream().findFirst();

        assertTrue(byManufacturer.isPresent());
        assertEquals(allEngines.size(), engineRepository.count());
    }
}
