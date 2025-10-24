package com.mig.sales.api.model.entity;

/**
 * Enumeration for lead sources.
 */
public enum LeadSource {
    PARTNER_REFERRAL("Partner Referral", "Partner Referral"),
    WEBINAR("Webinar", "Webinar"),
    WEBSITE_SIGNUP("Website Signup", "Website Signup"),
    COLD_CALL("Cold Call", "Cold Call");

    private final String code;
    private final String description;

    LeadSource(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static LeadSource fromCode(String code) {
        for (LeadSource source : values()) {
            if (source.code.equals(code)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown lead source code: " + code);
    }
}
