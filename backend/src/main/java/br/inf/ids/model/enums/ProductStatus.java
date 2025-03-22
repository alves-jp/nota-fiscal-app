package br.inf.ids.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductStatus {
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static ProductStatus fromString(String status) {
        for (ProductStatus ps : ProductStatus.values()) {
            if (ps.name().equalsIgnoreCase(status)) {
                return ps;

            }
        }
        throw new IllegalArgumentException("Status inválido: " + status);
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
