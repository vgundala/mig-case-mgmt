package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LeadScoringService
 */
@ExtendWith(MockitoExtension.class)
class LeadScoringServiceTest {

    @InjectMocks
    private LeadScoringService leadScoringService;

    private Lead lead;

    @BeforeEach
    void setUp() {
        lead = new Lead();
    }

    @Test
    void testCalculateScore_HighValue_PartnerReferral_CompleteData() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("600000"));
        lead.setLeadSource("Partner Referral");
        lead.setEmail("test@example.com");
        lead.setPhone("123-456-7890");

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(95, score); // 50 (high value) + 30 (partner referral) + 15 (complete data)
    }

    @Test
    void testCalculateScore_MediumValue_Webinar_PartialData() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("200000"));
        lead.setLeadSource("Webinar");
        lead.setEmail("test@example.com");
        lead.setPhone(null);

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(40, score); // 20 (medium value) + 15 (webinar) + 5 (partial data)
    }

    @Test
    void testCalculateScore_LowValue_ColdCall_NoData() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("50000"));
        lead.setLeadSource("Cold Call");
        lead.setEmail(null);
        lead.setPhone(null);

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(10, score); // 5 (low value) + 5 (cold call) + 0 (no data)
    }

    @Test
    void testCalculateScore_WebsiteSignup_CompleteData() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("100000"));
        lead.setLeadSource("Website Signup");
        lead.setEmail("test@example.com");
        lead.setPhone("123-456-7890");

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(45, score); // 20 (medium value) + 10 (website signup) + 15 (complete data)
    }

    @Test
    void testCalculateScore_NullValues() {
        // Arrange
        lead.setPotentialValue(null);
        lead.setLeadSource(null);
        lead.setEmail(null);
        lead.setPhone(null);

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(0, score);
    }

    @Test
    void testCalculateScore_EmptyStrings() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("100000"));
        lead.setLeadSource("Cold Call");
        lead.setEmail("");
        lead.setPhone("");

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(25, score); // 20 (medium value) + 5 (cold call) + 0 (empty data)
    }

    @Test
    void testCalculateScore_ScoreCappedAt100() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("1000000"));
        lead.setLeadSource("Partner Referral");
        lead.setEmail("test@example.com");
        lead.setPhone("123-456-7890");

        // Act
        int score = leadScoringService.calculateScore(lead);

        // Assert
        assertEquals(100, score); // Should be capped at 100
    }

    @Test
    void testIsHighValueLead_True() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("1500000"));

        // Act
        boolean isHighValue = leadScoringService.isHighValueLead(lead);

        // Assert
        assertTrue(isHighValue);
    }

    @Test
    void testIsHighValueLead_False() {
        // Arrange
        lead.setPotentialValue(new BigDecimal("500000"));

        // Act
        boolean isHighValue = leadScoringService.isHighValueLead(lead);

        // Assert
        assertFalse(isHighValue);
    }

    @Test
    void testIsHighValueLead_NullValue() {
        // Arrange
        lead.setPotentialValue(null);

        // Act
        boolean isHighValue = leadScoringService.isHighValueLead(lead);

        // Assert
        assertFalse(isHighValue);
    }

    @Test
    void testGetLeadPriority_High() {
        // Act & Assert
        assertEquals("HIGH", leadScoringService.getLeadPriority(85));
        assertEquals("HIGH", leadScoringService.getLeadPriority(100));
    }

    @Test
    void testGetLeadPriority_Medium() {
        // Act & Assert
        assertEquals("MEDIUM", leadScoringService.getLeadPriority(50));
        assertEquals("MEDIUM", leadScoringService.getLeadPriority(79));
    }

    @Test
    void testGetLeadPriority_Low() {
        // Act & Assert
        assertEquals("LOW", leadScoringService.getLeadPriority(49));
        assertEquals("LOW", leadScoringService.getLeadPriority(0));
    }
}

