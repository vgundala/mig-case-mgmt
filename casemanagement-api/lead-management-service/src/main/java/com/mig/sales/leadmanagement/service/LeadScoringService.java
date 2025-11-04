package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for calculating lead scores based on business rules
 */
@Service
public class LeadScoringService {

    /**
     * Calculate lead score based on multiple criteria
     * @param lead the lead to score
     * @return calculated score (0-100)
     */
    public int calculateScore(Lead lead) {
        int score = 0;

        // Rule 1: Potential Value
        if (lead.getPotentialValue() != null) {
            if (lead.getPotentialValue().compareTo(new BigDecimal("500000")) > 0) {
                score += 50;
            } else if (lead.getPotentialValue().compareTo(new BigDecimal("100000")) >= 0) {
                score += 20;
            } else {
                score += 5;
            }
        }

        // Rule 2: Lead Source
        if (lead.getLeadSource() != null) {
            switch (lead.getLeadSource()) {
                case "Partner Referral":
                    score += 30;
                    break;
                case "Webinar":
                    score += 15;
                    break;
                case "LinkedIn Campaign":
                    score += 12;
                    break;
                case "Website Signup":
                    score += 10;
                    break;
                case "Email Campaign":
                    score += 8;
                    break;
                case "Social Media":
                    score += 8;
                    break;
                case "Trade Show":
                    score += 12;
                    break;
                case "Referral":
                    score += 20;
                    break;
                case "Cold Call":
                    score += 5;
                    break;
                case "Other":
                    score += 3;
                    break;
                default:
                    score += 0;
                    break;
            }
        }

        // Rule 3: Data Completeness
        boolean hasEmail = lead.getEmail() != null && !lead.getEmail().trim().isEmpty();
        boolean hasPhone = lead.getPhone() != null && !lead.getPhone().trim().isEmpty();
        if (hasEmail && hasPhone) {
            score += 15;
        } else if (hasEmail || hasPhone) {
            score += 5;
        }

        // Ensure score is within 0-100 range
        return Math.min(Math.max(score, 0), 100);
    }

    /**
     * Check if lead is high-value (>= $1M)
     * @param lead the lead to check
     * @return true if high-value, false otherwise
     */
    public boolean isHighValueLead(Lead lead) {
        return lead.getPotentialValue() != null && 
               lead.getPotentialValue().compareTo(new BigDecimal("1000000")) >= 0;
    }

    /**
     * Get lead priority based on score
     * @param score the lead score
     * @return priority level
     */
    public String getLeadPriority(int score) {
        if (score >= 80) {
            return "HIGH";
        } else if (score >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}

