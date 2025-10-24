package com.mig.sales.api.service;

import com.mig.sales.api.model.dto.CreateLeadRequest;
import com.mig.sales.api.model.entity.Lead;
import com.mig.sales.api.model.entity.LeadSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for calculating lead scores based on various criteria.
 */
@Service
@Slf4j
public class LeadScoringService {

    private static final int BASE_SCORE = 0;
    private static final int MAX_SCORE = 100;
    
    // Scoring weights
    private static final int POTENTIAL_VALUE_WEIGHT = 40;
    private static final int LEAD_SOURCE_WEIGHT = 30;
    private static final int DATA_COMPLETENESS_WEIGHT = 30;

    /**
     * Calculate lead score based on potential value, lead source, and data completeness.
     */
    public Integer calculateScore(CreateLeadRequest request) {
        log.debug("Calculating score for lead: {}", request.getLeadName());
        
        int score = BASE_SCORE;
        
        // Score based on potential value (0-40 points)
        score += calculatePotentialValueScore(request.getPotentialValue());
        
        // Score based on lead source (0-30 points)
        score += calculateLeadSourceScore(request.getLeadSource());
        
        // Score based on data completeness (0-30 points)
        score += calculateDataCompletenessScore(request);
        
        // Ensure score is within bounds
        score = Math.max(BASE_SCORE, Math.min(MAX_SCORE, score));
        
        log.debug("Calculated score for lead {}: {}", request.getLeadName(), score);
        return score;
    }

    /**
     * Calculate lead score for existing lead.
     */
    public Integer calculateScore(Lead lead) {
        log.debug("Calculating score for existing lead: {}", lead.getLeadName());
        
        int score = BASE_SCORE;
        
        // Score based on potential value (0-40 points)
        score += calculatePotentialValueScore(lead.getPotentialValue());
        
        // Score based on lead source (0-30 points)
        score += calculateLeadSourceScore(lead.getLeadSource());
        
        // Score based on data completeness (0-30 points)
        score += calculateDataCompletenessScore(lead);
        
        // Ensure score is within bounds
        score = Math.max(BASE_SCORE, Math.min(MAX_SCORE, score));
        
        log.debug("Calculated score for lead {}: {}", lead.getLeadName(), score);
        return score;
    }

    /**
     * Calculate potential value score (0-40 points).
     */
    private int calculatePotentialValueScore(BigDecimal potentialValue) {
        if (potentialValue == null) {
            return 0;
        }
        
        // Score based on value ranges
        if (potentialValue.compareTo(new BigDecimal("1000000")) >= 0) {
            return POTENTIAL_VALUE_WEIGHT; // 40 points for 1M+
        } else if (potentialValue.compareTo(new BigDecimal("500000")) >= 0) {
            return 30; // 30 points for 500K-1M
        } else if (potentialValue.compareTo(new BigDecimal("100000")) >= 0) {
            return 20; // 20 points for 100K-500K
        } else if (potentialValue.compareTo(new BigDecimal("50000")) >= 0) {
            return 10; // 10 points for 50K-100K
        } else {
            return 5; // 5 points for <50K
        }
    }

    /**
     * Calculate lead source score (0-30 points).
     */
    private int calculateLeadSourceScore(LeadSource leadSource) {
        if (leadSource == null) {
            return 0;
        }
        
        return switch (leadSource) {
            case PARTNER_REFERRAL -> LEAD_SOURCE_WEIGHT; // 30 points
            case WEBINAR -> 25; // 25 points
            case WEBSITE_SIGNUP -> 20; // 20 points
            case COLD_CALL -> 10; // 10 points
        };
    }

    /**
     * Calculate data completeness score (0-30 points).
     */
    private int calculateDataCompletenessScore(CreateLeadRequest request) {
        int score = 0;
        
        // Email and phone (20 points)
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            score += 10;
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            score += 10;
        }
        
        // Company name (10 points)
        if (request.getCompany() != null && !request.getCompany().trim().isEmpty()) {
            score += 10;
        }
        
        return score;
    }

    /**
     * Calculate data completeness score for existing lead (0-30 points).
     */
    private int calculateDataCompletenessScore(Lead lead) {
        int score = 0;
        
        // Email and phone (20 points)
        if (lead.getEmail() != null && !lead.getEmail().trim().isEmpty()) {
            score += 10;
        }
        if (lead.getPhone() != null && !lead.getPhone().trim().isEmpty()) {
            score += 10;
        }
        
        // Company name (10 points)
        if (lead.getCompany() != null && !lead.getCompany().trim().isEmpty()) {
            score += 10;
        }
        
        return score;
    }

    /**
     * Get score category based on score value.
     */
    public String getScoreCategory(Integer score) {
        if (score == null) {
            return "UNKNOWN";
        }
        
        if (score >= 80) {
            return "HIGH";
        } else if (score >= 60) {
            return "MEDIUM";
        } else if (score >= 40) {
            return "LOW";
        } else {
            return "VERY_LOW";
        }
    }

    /**
     * Check if lead is high priority based on score.
     */
    public boolean isHighPriority(Integer score) {
        return score != null && score >= 70;
    }
}
