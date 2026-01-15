package com.rocket.comparison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper for a field value with optional truth verification score
 * @param <T> The type of the field value
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedField<T> {

    private T value;
    private Double truthScore;  // 0.0-1.0, null if unverified
    private Boolean verified;

    public static <T> VerifiedField<T> of(T value, Double truthScore) {
        VerifiedField<T> field = new VerifiedField<>();
        field.setValue(value);
        field.setTruthScore(truthScore);
        field.setVerified(truthScore != null && truthScore > 0.5);
        return field;
    }

    public static <T> VerifiedField<T> unverified(T value) {
        VerifiedField<T> field = new VerifiedField<>();
        field.setValue(value);
        field.setTruthScore(null);
        field.setVerified(false);
        return field;
    }
}
