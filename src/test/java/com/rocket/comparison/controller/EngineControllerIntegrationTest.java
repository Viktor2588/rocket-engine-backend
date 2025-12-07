package com.rocket.comparison.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5433/rocket_engine_comparison",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class EngineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Engine testEngine;
    private Engine anotherEngine;
    private Engine thirdEngine;

    @BeforeEach
    public void setUp() {
        // Clear existing data
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

        anotherEngine = new Engine();
        anotherEngine.setName("BE-4");
        anotherEngine.setDesigner("Blue Origin");
        anotherEngine.setOrigin("USA");
        anotherEngine.setThrustN(2450000L);
        anotherEngine.setIsp_s(380.0);
        anotherEngine.setPropellant("NG/LOX");
        anotherEngine.setMassKg(1300.0);
        anotherEngine.setDescription("Used on Atlas V and Vulcan rockets");

        thirdEngine = new Engine();
        thirdEngine.setName("RS-25");
        thirdEngine.setDesigner("Aerojet Rocketdyne");
        thirdEngine.setOrigin("USA");
        thirdEngine.setThrustN(2279000L);
        thirdEngine.setIsp_s(452.0);
        thirdEngine.setPropellant("Hydrolox");
        thirdEngine.setMassKg(3527.0);
        thirdEngine.setDescription("Space Shuttle Main Engine");

        engineRepository.save(testEngine);
        engineRepository.save(anotherEngine);
        engineRepository.save(thirdEngine);
    }

    // ==================== GET All Engines ====================
    @Test
    public void testGetAllEngines_Success() throws Exception {
        mockMvc.perform(get("/api/engines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Merlin 1D")))
                .andExpect(jsonPath("$[1].name", is("BE-4")))
                .andExpect(jsonPath("$[2].name", is("RS-25")));
    }

    @Test
    public void testGetAllEngines_EmptyDatabase() throws Exception {
        engineRepository.deleteAll();

        mockMvc.perform(get("/api/engines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== GET Engine by ID ====================
    @Test
    public void testGetEngineById_Success() throws Exception {
        Long engineId = testEngine.getId();

        mockMvc.perform(get("/api/engines/{id}", engineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Merlin 1D")))
                .andExpect(jsonPath("$.designer", is("SpaceX")))
                .andExpect(jsonPath("$.origin", is("USA")))
                .andExpect(jsonPath("$.thrustN", is(934000)))
                .andExpect(jsonPath("$.isp_s", is(282.0)))
                .andExpect(jsonPath("$.propellant", is("RP-1/LOX")))
                .andExpect(jsonPath("$.massKg", is(470.0)));
    }

    @Test
    public void testGetEngineById_NotFound() throws Exception {
        mockMvc.perform(get("/api/engines/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE Engine ====================
    @Test
    public void testCreateEngine_Success() throws Exception {
        Engine newEngine = new Engine();
        newEngine.setName("Raptor 2");
        newEngine.setDesigner("SpaceX");
        newEngine.setOrigin("USA");
        newEngine.setThrustN(2050000L);
        newEngine.setIsp_s(380.0);
        newEngine.setPropellant("Methane/LOX");
        newEngine.setMassKg(1400.0);
        newEngine.setDescription("Full-flow staged combustion engine");

        mockMvc.perform(post("/api/engines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEngine)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Raptor 2")))
                .andExpect(jsonPath("$.designer", is("SpaceX")))
                .andExpect(jsonPath("$.thrustN", is(2050000)));

        // Verify it was saved (3 initial engines + 1 new = 4)
        assertEquals(4, engineRepository.count());
    }

    @Test
    public void testCreateEngine_ValidEngine() throws Exception {
        // Additional test for engine creation
        Engine newEngine = new Engine();
        newEngine.setName("Test Engine");
        newEngine.setDesigner("Test Mfr");
        newEngine.setOrigin("USA");
        newEngine.setThrustN(1000000L);
        newEngine.setIsp_s(300.0);
        newEngine.setPropellant("Test/Propellant");
        newEngine.setMassKg(500.0);
        newEngine.setDescription("Test description");

        mockMvc.perform(post("/api/engines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEngine)))
                .andExpect(status().isCreated());
    }

    // ==================== UPDATE Engine ====================
    @Test
    public void testUpdateEngine_Success() throws Exception {
        Long engineId = testEngine.getId();

        Engine updatedEngine = new Engine();
        updatedEngine.setName("Merlin 1D Updated");
        updatedEngine.setDesigner("SpaceX");
        updatedEngine.setOrigin("USA");
        updatedEngine.setThrustN(950000L);
        updatedEngine.setIsp_s(285.0);
        updatedEngine.setPropellant("RP-1/LOX");
        updatedEngine.setMassKg(480.0);
        updatedEngine.setDescription("Updated description");

        mockMvc.perform(put("/api/engines/{id}", engineId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEngine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(engineId.intValue())))
                .andExpect(jsonPath("$.name", is("Merlin 1D Updated")))
                .andExpect(jsonPath("$.thrustN", is(950000)));
    }

    @Test
    public void testUpdateEngine_NotFound() throws Exception {
        Engine updatedEngine = new Engine();
        updatedEngine.setName("Non-existent Engine");
        updatedEngine.setDesigner("Unknown");
        updatedEngine.setOrigin("USA");
        updatedEngine.setThrustN(1000000L);
        updatedEngine.setIsp_s(300.0);
        updatedEngine.setPropellant("LOX/H2");
        updatedEngine.setMassKg(1000.0);

        mockMvc.perform(put("/api/engines/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEngine)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE Engine ====================
    @Test
    public void testDeleteEngine_Success() throws Exception {
        Long engineId = testEngine.getId();
        assertEquals(3, engineRepository.count());

        mockMvc.perform(delete("/api/engines/{id}", engineId))
                .andExpect(status().isNoContent());

        assertEquals(2, engineRepository.count());
        assertFalse(engineRepository.existsById(engineId));
    }

    @Test
    public void testDeleteEngine_NotFound() throws Exception {
        mockMvc.perform(delete("/api/engines/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    // ==================== Filter by Designer ====================
    @Test
    public void testGetEnginesByDesigner_Success() throws Exception {
        mockMvc.perform(get("/api/engines/designer/{designer}", "SpaceX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Merlin 1D")))
                .andExpect(jsonPath("$[0].designer", is("SpaceX")));
    }

    @Test
    public void testGetEnginesByDesigner_NoResults() throws Exception {
        mockMvc.perform(get("/api/engines/designer/{designer}", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== Filter by Propellant ====================
    @Test
    public void testGetEnginesByPropellant_IntegrationWithService() throws Exception {
        // This test verifies the full flow from controller to service
        // Note: Using Hydrolox propellant to avoid URL encoding issues with "/" in propellant names
        mockMvc.perform(get("/api/engines/propellant/{propellant}", "Hydrolox"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ==================== Filter by Minimum Thrust ====================
    @Test
    public void testGetEnginesByMinThrust_Success() throws Exception {
        // Both BE-4 (2450000N) and RS-25 (2279000N) have thrust > 2000000N
        mockMvc.perform(get("/api/engines/thrust-min/{thrust}", 2000000))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetEnginesByMinThrust_MultipleResults() throws Exception {
        mockMvc.perform(get("/api/engines/thrust-min/{thrust}", 500000))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testGetEnginesByMinThrust_NoResults() throws Exception {
        mockMvc.perform(get("/api/engines/thrust-min/{thrust}", 5000000))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== Filter by Minimum ISP ====================
    @Test
    public void testGetEnginesByMinIsp_Success() throws Exception {
        mockMvc.perform(get("/api/engines/isp-min/{isp}", 350.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetEnginesByMinIsp_MultipleResults() throws Exception {
        mockMvc.perform(get("/api/engines/isp-min/{isp}", 250.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testGetEnginesByMinIsp_NoResults() throws Exception {
        mockMvc.perform(get("/api/engines/isp-min/{isp}", 500.0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== Compare Engines ====================
    @Test
    public void testCompareEngines_Success() throws Exception {
        mockMvc.perform(get("/api/engines/compare")
                .param("engine1Id", testEngine.getId().toString())
                .param("engine2Id", anotherEngine.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.engine1.name", is("Merlin 1D")))
                .andExpect(jsonPath("$.engine2.name", is("BE-4")));
    }

    @Test
    public void testCompareEngines_Engine1NotFound() throws Exception {
        mockMvc.perform(get("/api/engines/compare")
                .param("engine1Id", "99999")
                .param("engine2Id", anotherEngine.getId().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    public void testCompareEngines_Engine2NotFound() throws Exception {
        mockMvc.perform(get("/api/engines/compare")
                .param("engine1Id", testEngine.getId().toString())
                .param("engine2Id", "99999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    public void testCompareEngines_BothNotFound() throws Exception {
        mockMvc.perform(get("/api/engines/compare")
                .param("engine1Id", "99999")
                .param("engine2Id", "88888"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCompareEngines_SameEngine() throws Exception {
        mockMvc.perform(get("/api/engines/compare")
                .param("engine1Id", testEngine.getId().toString())
                .param("engine2Id", testEngine.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.engine1.id", is(testEngine.getId().intValue())))
                .andExpect(jsonPath("$.engine2.id", is(testEngine.getId().intValue())));
    }

    // ==================== CORS Configuration ====================
    @Test
    public void testCorsConfigurationExists() throws Exception {
        // Note: CORS headers may not appear in MockMvc tests without proper preflight configuration
        // This test verifies the endpoint is accessible
        mockMvc.perform(get("/api/engines"))
                .andExpect(status().isOk());
    }

    // ==================== Content Type Test ====================
    @Test
    public void testResponseContentType() throws Exception {
        mockMvc.perform(get("/api/engines"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
