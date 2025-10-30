package com.mig.sales.case_management.action.form;

import org.apache.struts.action.ActionForm;
import java.math.BigDecimal;

public class LeadForm extends ActionForm {

    private String leadName;
    private String company;
    private String email;
    private String phone;
    private BigDecimal potentialValue;
    private String leadSource;

    // Getters and Setters

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getPotentialValue() {
        return potentialValue;
    }

    public void setPotentialValue(BigDecimal potentialValue) {
        this.potentialValue = potentialValue;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }
}