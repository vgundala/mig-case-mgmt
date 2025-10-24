package com.mig.sales.api.model.entity;

/**
 * Enumeration for lead status in the sales pipeline.
 */
public enum LeadStatus {
    NEW("NEW", "New Lead"),
    ASSIGNED("ASSIGNED", "Assigned to Sales Person"),
    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    PRE_CONVERSION("PRE_CONVERSION", "Pre-Conversion"),
    CONVERTED("CONVERTED", "Converted"),
    REJECTED("REJECTED", "Rejected");

    private final String code;
    private final String description;

    LeadStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static LeadStatus fromCode(String code) {
        for (LeadStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown lead status code: " + code);
    }
}
