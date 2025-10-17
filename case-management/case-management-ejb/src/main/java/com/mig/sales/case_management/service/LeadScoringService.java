package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.Lead;
import javax.ejb.Stateless;
import java.math.BigDecimal;

@Stateless
public class LeadScoringService {

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
                case "Website Signup":
                    score += 10;
                    break;
                case "Cold Call":
                    score += 5;
                    break;
            }
        }

        // Rule 3: Data Completeness
        boolean hasEmail = lead.getEmail() != null && !lead.getEmail().trim().isEmpty();
        boolean hasPhone = lead.getPhone() != null && !lead.getPhone().trim().isEmpty();
        if (hasEmail && hasPhone) {
            score += 15;
        }

        return score;
    }
}