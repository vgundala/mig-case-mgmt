package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.exception.ResourceNotFoundException;
import com.mig.sales.leadmanagement.repository.LeadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LeadService
 */
@ExtendWith(MockitoExtension.class)
class LeadServiceTest {

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private LeadScoringService leadScoringService;

    @Mock
    private LeadHistoryService leadHistoryService;

    @InjectMocks
    private LeadService leadService;

    private Lead testLead;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole("SALES_PERSON");

        testLead = new Lead();
        testLead.setId(1L);
        testLead.setLeadName("Test Lead");
        testLead.setCompany("Test Company");
        testLead.setEmail("test@example.com");
        testLead.setPhone("555-0123");
        testLead.setPotentialValue(new BigDecimal("100000"));
        testLead.setLeadSource("Website Signup");
        testLead.setAssignedTo(testUser);
        testLead.setStatus("NEW");
        testLead.setCreatedDate(LocalDateTime.now());
        testLead.setUpdatedDate(LocalDateTime.now());
    }

    @Test
    void testCreateLead_Success() {
        // Arrange
        Lead newLead = new Lead();
        newLead.setLeadName("New Lead");
        newLead.setCompany("New Company");
        newLead.setEmail("new@example.com");
        newLead.setPhone("555-9999");
        newLead.setPotentialValue(new BigDecimal("50000"));
        newLead.setLeadSource("Cold Call");

        when(leadScoringService.calculateScore(any(Lead.class))).thenReturn(25);
        when(leadRepository.save(any(Lead.class))).thenReturn(newLead);

        // Act
        Lead result = leadService.createLead(newLead);

        // Assert
        assertNotNull(result);
        assertEquals("NEW", result.getStatus());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdatedDate());
        assertEquals(25, result.getLeadScore());
        verify(leadScoringService).calculateScore(newLead);
        verify(leadRepository).save(newLead);
        verify(leadHistoryService).logActivity(eq(newLead), isNull(), eq("Lead created"), eq("Created"), eq("SYSTEM"), isNull(), eq("NEW"));
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));

        // Act
        Lead result = leadService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testLead.getId(), result.getId());
        assertEquals(testLead.getLeadName(), result.getLeadName());
        verify(leadRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(leadRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            leadService.findById(999L);
        });

        assertEquals("Lead not found with id: 999", exception.getMessage());
        verify(leadRepository).findById(999L);
    }

    @Test
    void testFindAllLeads_Success() {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadRepository.findAllOrderByLeadScoreDesc()).thenReturn(leads);

        // Act
        List<Lead> result = leadService.findAllLeads();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testLead.getLeadName(), result.get(0).getLeadName());
        verify(leadRepository).findAllOrderByLeadScoreDesc();
    }

    @Test
    void testFindLeadsWithFilters_Success() {
        // Arrange
        Page<Lead> leadPage = new PageImpl<>(Arrays.asList(testLead));
        when(leadRepository.findLeadsWithFilters(anyString(), any(User.class), anyString(), any(Pageable.class)))
                .thenReturn(leadPage);

        // Act
        Page<Lead> result = leadService.findLeadsWithFilters("NEW", testUser, "Website Signup", Pageable.unpaged());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(leadRepository).findLeadsWithFilters("NEW", testUser, "Website Signup", Pageable.unpaged());
    }

    @Test
    void testFindByStatus_Success() {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadRepository.findByStatus("NEW")).thenReturn(leads);

        // Act
        List<Lead> result = leadService.findByStatus("NEW");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("NEW", result.get(0).getStatus());
        verify(leadRepository).findByStatus("NEW");
    }

    @Test
    void testFindByAssignedTo_Success() {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadRepository.findByAssignedTo(testUser)).thenReturn(leads);

        // Act
        List<Lead> result = leadService.findByAssignedTo(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getAssignedTo().getId());
        verify(leadRepository).findByAssignedTo(testUser);
    }

    @Test
    void testFindByAssignedToAndStatusIn_Success() {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        List<String> statuses = Arrays.asList("ASSIGNED", "IN_PROGRESS");
        when(leadRepository.findByAssignedToAndStatusIn(testUser, statuses)).thenReturn(leads);

        // Act
        List<Lead> result = leadService.findByAssignedToAndStatusIn(testUser, statuses);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leadRepository).findByAssignedToAndStatusIn(testUser, statuses);
    }

    @Test
    void testFindNewLeadsForDistribution_Success() {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadRepository.findNewLeadsForDistribution()).thenReturn(leads);

        // Act
        List<Lead> result = leadService.findNewLeadsForDistribution();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leadRepository).findNewLeadsForDistribution();
    }

    @Test
    void testFindHighValueLeads_Success() {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadRepository.findHighValueLeads(new BigDecimal("1000000"))).thenReturn(leads);

        // Act
        List<Lead> result = leadService.findHighValueLeads();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leadRepository).findHighValueLeads(new BigDecimal("1000000"));
    }

    @Test
    void testUpdateLead_Success() {
        // Arrange
        Lead updatedLead = new Lead();
        updatedLead.setId(1L);
        updatedLead.setLeadName("Updated Lead");
        updatedLead.setCompany("Updated Company");
        updatedLead.setStatus("IN_PROGRESS");

        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));
        when(leadRepository.save(any(Lead.class))).thenReturn(updatedLead);

        // Act
        Lead result = leadService.updateLead(updatedLead);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Lead", result.getLeadName());
        assertEquals("Updated Company", result.getCompany());
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(leadRepository).findById(1L);
        verify(leadRepository).save(any(Lead.class));
    }

    @Test
    void testUpdateLeadStatus_Success() {
        // Arrange
        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));
        when(leadRepository.save(any(Lead.class))).thenReturn(testLead);

        // Act
        Lead result = leadService.updateLeadStatus(1L, "IN_PROGRESS", testUser);

        // Assert
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
        verify(leadRepository).findById(1L);
        verify(leadRepository).save(any(Lead.class));
        verify(leadHistoryService).logActivity(eq(testLead), eq(testUser), contains("Status changed"), eq("Status Changed"), eq("USER_ACTION"), anyString(), anyString());
    }

    @Test
    void testAssignLeadToUser_Success() {
        // Arrange
        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));
        when(leadRepository.save(any(Lead.class))).thenReturn(testLead);

        // Act
        Lead result = leadService.assignLeadToUser(1L, testUser);

        // Assert
        assertNotNull(result);
        assertEquals("ASSIGNED", result.getStatus());
        assertEquals(testUser.getId(), result.getAssignedTo().getId());
        verify(leadRepository).findById(1L);
        verify(leadRepository).save(any(Lead.class));
        verify(leadHistoryService).logActivity(eq(testLead), eq(testUser), contains("Lead assigned"), eq("Assigned"), eq("SYSTEM"), anyString(), eq("ASSIGNED"));
    }

    @Test
    void testRecalculateLeadScore_Success() {
        // Arrange
        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));
        when(leadScoringService.calculateScore(testLead)).thenReturn(30);
        when(leadRepository.save(any(Lead.class))).thenReturn(testLead);

        // Act
        Lead result = leadService.recalculateLeadScore(1L);

        // Assert
        assertNotNull(result);
        assertEquals(30, result.getLeadScore());
        verify(leadRepository).findById(1L);
        verify(leadScoringService).calculateScore(testLead);
        verify(leadRepository).save(any(Lead.class));
        verify(leadHistoryService).logActivity(eq(testLead), isNull(), contains("Lead score recalculated"), eq("Score Recalculated"), eq("SYSTEM"), isNull(), isNull());
    }

    @Test
    void testDeleteLead_Success() {
        // Arrange
        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));

        // Act
        leadService.deleteLead(1L);

        // Assert
        verify(leadRepository).findById(1L);
        verify(leadRepository).delete(testLead);
    }

    @Test
    void testCountByStatus_Success() {
        // Arrange
        when(leadRepository.countByStatus("NEW")).thenReturn(5L);

        // Act
        long count = leadService.countByStatus("NEW");

        // Assert
        assertEquals(5L, count);
        verify(leadRepository).countByStatus("NEW");
    }

    @Test
    void testCountByAssignedTo_Success() {
        // Arrange
        when(leadRepository.countByAssignedTo(testUser)).thenReturn(3L);

        // Act
        long count = leadService.countByAssignedTo(testUser);

        // Assert
        assertEquals(3L, count);
        verify(leadRepository).countByAssignedTo(testUser);
    }
}

