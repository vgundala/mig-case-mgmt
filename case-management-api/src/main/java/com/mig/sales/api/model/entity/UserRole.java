package com.mig.sales.api.model.entity;

/**
 * Enumeration for user roles in the system.
 */
public enum UserRole {
    SALES_PERSON("SALES_PERSON", "Sales Person"),
    SALES_MANAGER("SALES_MANAGER", "Sales Manager");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown user role code: " + code);
    }
}
