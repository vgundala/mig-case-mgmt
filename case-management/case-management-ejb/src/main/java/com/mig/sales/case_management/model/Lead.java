package com.mig.sales.case_management.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LEADS")
public class Lead implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leads_seq")
    @SequenceGenerator(name = "leads_seq", sequenceName = "LEADS_SEQ", allocationSize = 1)
    @Column(name = "LEAD_ID")
    private Long id;

    @Column(name = "LEAD_NAME", nullable = false)
    private String leadName;

    @Column(name = "COMPANY")
    private String company;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "ASSIGNED_TO")
    private User assignedTo;

    @Column(name = "POTENTIAL_VALUE")
    private BigDecimal potentialValue;

    @Column(name = "LEAD_SOURCE")
    private String leadSource;

    @Column(name = "LEAD_SCORE")
    private Integer leadScore;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
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

    public Integer getLeadScore() {
        return leadScore;
    }

    public void setLeadScore(Integer leadScore) {
        this.leadScore = leadScore;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}