package com.mig.sales.leadmanagement.repository;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository test for Lead entity with filtering
 */
@DataJpaTest
@ActiveProfiles("test")
class LeadRepositoryTest {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Lead lead1;
    private Lead lead2;
    private Lead lead3;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("$2a$10$hashed");
        testUser.setRole("SALES_PERSON");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setIsActive(true);
        testUser.setCreatedDate(LocalDateTime.now());
        testUser = userRepository.save(testUser);

        // Create test leads
        lead1 = new Lead();
        lead1.setLeadName("Test Lead 1");
        lead1.setCompany("Company 1");
        lead1.setEmail("lead1@test.com");
        lead1.setPhone("555-0001");
        lead1.setStatus("NEW");
        lead1.setLeadSource("Website");
        lead1.setPotentialValue(new BigDecimal("50000"));
        lead1.setLeadScore(75);
        lead1.setCreatedDate(LocalDateTime.now());
        lead1.setUpdatedDate(LocalDateTime.now());
        lead1 = leadRepository.save(lead1);

        lead2 = new Lead();
        lead2.setLeadName("Test Lead 2");
        lead2.setCompany("Company 2");
        lead2.setEmail("lead2@test.com");
        lead2.setPhone("555-0002");
        lead2.setStatus("ASSIGNED");
        lead2.setAssignedTo(testUser);
        lead2.setLeadSource("Referral");
        lead2.setPotentialValue(new BigDecimal("100000"));
        lead2.setLeadScore(85);
        lead2.setCreatedDate(LocalDateTime.now());
        lead2.setUpdatedDate(LocalDateTime.now());
        lead2 = leadRepository.save(lead2);

        lead3 = new Lead();
        lead3.setLeadName("Test Lead 3");
        lead3.setCompany("Company 3");
        lead3.setEmail("lead3@test.com");
        lead3.setPhone("555-0003");
        lead3.setStatus("NEW");
        lead3.setLeadSource("Website");
        lead3.setPotentialValue(new BigDecimal("75000"));
        lead3.setLeadScore(80);
        lead3.setCreatedDate(LocalDateTime.now());
        lead3.setUpdatedDate(LocalDateTime.now());
        lead3 = leadRepository.save(lead3);
    }

    @Test
    void testFindLeadsWithFilters_AllNull() {
        // Test with all null filters - should return all leads
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters(null, null, null, pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 3);
    }

    @Test
    void testFindLeadsWithFilters_StatusFilter() {
        // Test with status filter
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters("NEW", null, null, pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
        result.getContent().forEach(lead -> assertEquals("NEW", lead.getStatus()));
    }

    @Test
    void testFindLeadsWithFilters_AssignedToFilter() {
        // Test with assignedTo filter
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters(null, testUser, null, pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 1);
        result.getContent().forEach(lead -> assertEquals(testUser.getId(), lead.getAssignedTo().getId()));
    }

    @Test
    void testFindLeadsWithFilters_LeadSourceFilter() {
        // Test with leadSource filter
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters(null, null, "Website", pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
        result.getContent().forEach(lead -> assertEquals("Website", lead.getLeadSource()));
    }

    @Test
    void testFindLeadsWithFilters_CombinedFilters() {
        // Test with multiple filters
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters("NEW", null, "Website", pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
        result.getContent().forEach(lead -> {
            assertEquals("NEW", lead.getStatus());
            assertEquals("Website", lead.getLeadSource());
        });
    }

    @Test
    void testFindLeadsWithFilters_EmptyStringStatus() {
        // Test with empty string for status (should be treated as no filter)
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters("", null, null, pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 3); // Should return all
    }

    @Test
    void testFindLeadsWithFilters_EmptyStringLeadSource() {
        // Test with empty string for leadSource (should be treated as no filter)
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters(null, null, "", pageable);

        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 3); // Should return all
    }

    @Test
    void testFindLeadsWithFilters_Pagination() {
        // Test pagination
        Pageable pageable = PageRequest.of(0, 2);
        Page<Lead> result = leadRepository.findLeadsWithFilters(null, null, null, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getTotalElements() >= 3);
        assertTrue(result.getTotalPages() >= 2);
    }

    @Test
    void testFindLeadsWithFilters_NoMatches() {
        // Test with filter that matches nothing
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> result = leadRepository.findLeadsWithFilters("NONEXISTENT", null, null, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}

