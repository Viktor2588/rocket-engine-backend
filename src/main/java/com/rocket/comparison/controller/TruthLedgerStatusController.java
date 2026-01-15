package com.rocket.comparison.controller;

import com.rocket.comparison.integration.truthledger.TruthLedgerClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for Truth Ledger integration status
 */
@RestController
@RequestMapping("/api/truth-ledger")
@RequiredArgsConstructor
@Tag(name = "Truth Ledger", description = "Truth Ledger integration status and management")
public class TruthLedgerStatusController {

    private final TruthLedgerClient truthLedgerClient;

    @Value("${truthledger.enabled:true}")
    private boolean enabled;

    @Value("${truthledger.base-url:http://localhost:3000/api/v1}")
    private String baseUrl;

    @Operation(summary = "Get Truth Ledger integration status")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", enabled);
        status.put("baseUrl", baseUrl);
        status.put("healthy", enabled && truthLedgerClient.isHealthy());

        return ResponseEntity.ok(status);
    }
}
