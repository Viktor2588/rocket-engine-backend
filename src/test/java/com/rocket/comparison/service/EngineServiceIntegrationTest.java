package com.rocket.comparison.service;

import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
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
        testEngine.setDesigner("SpaceX");
        testEngine.setOrigin("USA");
        testEngine.setThrustN(934000L);
        testEngine.setIsp_s(282.0);
        testEngine.setPropellant("RP-1/LOX");
        testEngine.setMassKg(470.0);
        testEngine.setDescription("The Merlin 1D is used on Falcon 9");

        spacexEngine = new Engine();
        spacexEngine.setName("Raptor 2");
        spacexEngine.setDesigner("SpaceX");
        spacexEngine.setOrigin("USA");
        spacexEngine.setThrustN(2050000L);
        spacexEngine.setIsp_s(380.0);
        spacexEngine.setPropellant("Methane/LOX");
        spacexEngine.setMassKg(1400.0);
        spacexEngine.setDescription("Full-flow staged combustion engine");

        blueOriginEngine = new Engine();
        blueOriginEngine.setName("BE-4");
        blueOriginEngine.setDesigner("Blue Origin");
        blueOriginEngine.setOrigin("USA");
        blueOriginEngine.setThrustN(2450000L);
        blueOriginEngine.setIsp_s(380.0);
        blueOriginEngine.setPropellant("NG/LOX");
        blueOriginEngine.setMassKg(1300.0);
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
        newEngine.setDesigner("Aerojet Rocketdyne");
        newEngine.setOrigin("USA");
        newEngine.setThrustN(1860000L);
        newEngine.setIsp_s(452.0);
        newEngine.setPropellant("Hydrogen/LOX");
        newEngine.setMassKg(3600.0);
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
        newEngine.setDesigner("Test Manufacturer");
        newEngine.setOrigin("USA");
        newEngine.setThrustN(1000000L);
        newEngine.setIsp_s(300.0);
        newEngine.setPropellant("LOX/H2");
        newEngine.setMassKg(500.0);
        newEngine.setDescription("Test description");

        Engine saved = engineService.saveEngine(newEngine);
        Optional<Engine> retrieved = engineService.getEngineById(saved.getId());

        assertTrue(retrieved.isPresent());
        assertEquals("Test Engine", retrieved.get().getName());
        assertEquals(1000000L, retrieved.get().getThrustN());
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
        assertEquals("SpaceX", engine.get().getDesigner());
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
        updatedData.setDesigner("SpaceX");
        updatedData.setOrigin("USA");
        updatedData.setThrustN(950000L);
        updatedData.setIsp_s(285.0);
        updatedData.setPropellant("RP-1/LOX");
        updatedData.setMassKg(480.0);
        updatedData.setDescription("Updated description");

        Engine updated = engineService.updateEngine(testEngine.getId(), updatedData);

        assertEquals("Merlin 1D Updated", updated.getName());
        assertEquals(950000L, updated.getThrustN());
        assertEquals(285.0, updated.getIsp_s());
    }

    @Test
    public void testUpdateEngine_NotFound() {
        Engine updatedData = new Engine();
        updatedData.setName("Test");
        updatedData.setDesigner("Test");
        updatedData.setOrigin("USA");
        updatedData.setThrustN(100000L);
        updatedData.setIsp_s(200.0);
        updatedData.setPropellant("LOX/H2");
        updatedData.setMassKg(100.0);

        assertThrows(IllegalArgumentException.class, () ->
            engineService.updateEngine(99999L, updatedData)
        );
    }

    @Test
    public void testUpdateEngine_PartialUpdate() {
        Engine updatedData = new Engine();
        updatedData.setName("Merlin 1D - Modified");
        updatedData.setDesigner("SpaceX");
        updatedData.setOrigin("USA");
        updatedData.setThrustN(934000L);
        updatedData.setIsp_s(282.0);
        updatedData.setPropellant("RP-1/LOX");
        updatedData.setMassKg(470.0);
        updatedData.setDescription("Modified description");

        Engine updated = engineService.updateEngine(testEngine.getId(), updatedData);

        assertEquals("Merlin 1D - Modified", updated.getName());
        assertEquals("Modified description", updated.getDescription());
        assertEquals(934000L, updated.getThrustN()); // Unchanged
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

    // ==================== Filter by Designer ====================
    @Test
    public void testGetEnginesByDesigner_Success() {
        List<Engine> spacexEngines = engineService.getEnginesByDesigner("SpaceX");

        assertEquals(2, spacexEngines.size());
        assertTrue(spacexEngines.stream().allMatch(e -> "SpaceX".equals(e.getDesigner())));
    }

    @Test
    public void testGetEnginesByDesigner_SingleResult() {
        List<Engine> blueOriginEngines = engineService.getEnginesByDesigner("Blue Origin");

        assertEquals(1, blueOriginEngines.size());
        assertEquals("BE-4", blueOriginEngines.get(0).getName());
    }

    @Test
    public void testGetEnginesByDesigner_NoResults() {
        List<Engine> engines = engineService.getEnginesByDesigner("NonExistent");

        assertEquals(0, engines.size());
    }

    @Test
    public void testGetEnginesByDesigner_CaseSensitive() {
        List<Engine> engines = engineService.getEnginesByDesigner("spacex");

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
        List<Engine> highThrustEngines = engineService.getEnginesByMinThrust(2000000L);

        assertEquals(2, highThrustEngines.size());
        assertTrue(highThrustEngines.stream().allMatch(e -> e.getThrustN() >= 2000000));
    }

    @Test
    public void testGetEnginesByMinThrust_AllEngines() {
        List<Engine> allEngines = engineService.getEnginesByMinThrust(0L);

        assertEquals(3, allEngines.size());
    }

    @Test
    public void testGetEnginesByMinThrust_NoResults() {
        List<Engine> engines = engineService.getEnginesByMinThrust(5000000L);

        assertEquals(0, engines.size());
    }

    @Test
    public void testGetEnginesByMinThrust_BoundaryCondition() {
        // Note: findByThrustNGreaterThan uses strict > not >=
        // So thrust > 934000 returns only Raptor 2 and BE-4
        List<Engine> engines = engineService.getEnginesByMinThrust(934000L);

        assertEquals(2, engines.size());
        assertTrue(engines.stream().allMatch(e -> e.getThrustN() > 934000));
    }

    // ==================== Filter by Minimum ISP ====================
    @Test
    public void testGetEnginesByMinIsp_Success() {
        List<Engine> efficientEngines = engineService.getEnginesByMinIsp(350.0);

        assertEquals(2, efficientEngines.size());
        assertTrue(efficientEngines.stream().allMatch(e -> e.getIsp_s() >= 350.0));
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
        // Note: findByIsp_sGreaterThan uses strict > not >=
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
        List<Engine> spacexEngines = engineService.getEnginesByDesigner("SpaceX");

        // Filter by thrust
        List<Engine> spacexHighThrust = spacexEngines.stream()
            .filter(e -> e.getThrustN() >= 2000000)
            .toList();

        assertEquals(1, spacexHighThrust.size());
        assertEquals("Raptor 2", spacexHighThrust.get(0).getName());
    }

    @Test
    public void testDatabaseConsistency() {
        // Add an engine
        Engine newEngine = new Engine();
        newEngine.setName("Test Engine");
        newEngine.setDesigner("Test Co");
        newEngine.setOrigin("USA");
        newEngine.setThrustN(100000L);
        newEngine.setIsp_s(200.0);
        newEngine.setPropellant("LOX/RP-1");
        newEngine.setMassKg(50.0);

        engineService.saveEngine(newEngine);

        // Retrieve via different methods
        List<Engine> allEngines = engineService.getAllEngines();
        Optional<Engine> byDesigner = engineService.getEnginesByDesigner("Test Co")
            .stream().findFirst();

        assertTrue(byDesigner.isPresent());
        assertEquals(allEngines.size(), engineRepository.count());
    }
}
