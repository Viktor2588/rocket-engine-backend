package com.rocket.comparison.controller;

import com.rocket.comparison.dto.ExportDto;
import com.rocket.comparison.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for data export functionality (BE-004).
 * Provides endpoints to download all data from the system.
 *
 * Note: In production, this endpoint should be secured with admin-only access.
 */
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Export", description = "Data export endpoints for downloading all system data (admin-only)")
public class ExportController {

    private final ExportService exportService;

    @Operation(
        summary = "Export all data",
        description = "Downloads all data from the system as JSON. This is an admin-only endpoint for data backup purposes."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully exported all data"),
        @ApiResponse(responseCode = "500", description = "Internal server error during export")
    })
    @GetMapping
    public ResponseEntity<ExportDto> exportAllData() {
        log.info("Export request received: all data");
        ExportDto exportData = exportService.exportAllData();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"rocket-data-export-" + getCurrentTimestamp() + ".json\"")
                .body(exportData);
    }

    @Operation(
        summary = "Export selected data types",
        description = "Downloads selected entity types from the system as JSON. Use query parameters to include specific data types."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully exported selected data"),
        @ApiResponse(responseCode = "500", description = "Internal server error during export")
    })
    @GetMapping("/selective")
    public ResponseEntity<ExportDto> exportSelectiveData(
            @Parameter(description = "Include countries") @RequestParam(defaultValue = "true") boolean countries,
            @Parameter(description = "Include engines") @RequestParam(defaultValue = "true") boolean engines,
            @Parameter(description = "Include launch vehicles") @RequestParam(defaultValue = "true") boolean launchVehicles,
            @Parameter(description = "Include space missions") @RequestParam(defaultValue = "true") boolean spaceMissions,
            @Parameter(description = "Include space milestones") @RequestParam(defaultValue = "true") boolean spaceMilestones,
            @Parameter(description = "Include launch sites") @RequestParam(defaultValue = "true") boolean launchSites,
            @Parameter(description = "Include satellites") @RequestParam(defaultValue = "true") boolean satellites,
            @Parameter(description = "Include capability scores") @RequestParam(defaultValue = "true") boolean capabilityScores) {

        log.info("Export request received: selective data");
        ExportDto exportData = exportService.exportSelectedData(
                countries, engines, launchVehicles, spaceMissions,
                spaceMilestones, launchSites, satellites, capabilityScores);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"rocket-data-export-selective-" + getCurrentTimestamp() + ".json\"")
                .body(exportData);
    }

    @Operation(
        summary = "Get export metadata only",
        description = "Returns only the counts and metadata without the actual data. Useful for checking data size before downloading."
    )
    @GetMapping("/metadata")
    public ResponseEntity<ExportDto.ExportMetadata> getExportMetadata() {
        log.info("Export metadata request received");
        ExportDto exportData = exportService.exportAllData();
        return ResponseEntity.ok(exportData.getMetadata());
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"));
    }
}
