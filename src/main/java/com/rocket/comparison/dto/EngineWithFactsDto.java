package com.rocket.comparison.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for engine with verified facts merged
 * Includes truth scores for verified fields
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineWithFactsDto {

    private Long id;
    private String name;
    private String origin;
    private String designer;
    private String vehicle;
    private String status;
    private String use;
    private String propellant;
    private String powerCycle;
    private String description;

    // Verified fields with truth scores
    private VerifiedField<Long> thrustN;
    private VerifiedField<Double> isp_s;
    private VerifiedField<Double> massKg;
    private VerifiedField<Double> chamberPressureBar;
    private VerifiedField<Double> ofRatio;

    // Computed field (not verified)
    private Double thrustToWeightRatio;

    // Verification metadata
    private String verificationStatus;  // 'verified', 'disputed', 'insufficient', 'unverified'
    private Boolean conflictsPresent;
    private String truthLedgerEntityId;
}
