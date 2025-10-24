package com.mig.sales.api.service;

import com.mig.sales.api.model.dto.LeadDTO;
import com.mig.sales.api.model.dto.CreateLeadRequest;
import com.mig.sales.api.model.dto.UpdateLeadRequest;
import com.mig.sales.api.model.dto.AssignLeadRequest;
import com.mig.sales.api.model.entity.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Lead management operations.
 */
public interface LeadService {

    /**
     * Create a new lead.
     */
    LeadDTO createLead(CreateLeadRequest request);

    /**
     * Get lead by ID.
     */
    Optional<LeadDTO> getLeadById(Long id);

    /**
     * Get all leads with pagination.
     */
    Page<LeadDTO> getAllLeads(Pageable pageable);

    /**
     * Get leads by status with pagination.
     */
    Page<LeadDTO> getLeadsByStatus(LeadStatus status, Pageable pageable);

    /**
     * Get leads by assigned user with pagination.
     */
    Page<LeadDTO> getLeadsByAssignedUser(Long userId, Pageable pageable);

    /**
     * Get leads by status and assigned user with pagination.
     */
    Page<LeadDTO> getLeadsByStatusAndAssignedUser(LeadStatus status, Long userId, Pageable pageable);

    /**
     * Get leads by potential value range with pagination.
     */
    Page<LeadDTO> getLeadsByPotentialValueRange(BigDecimal minValue, BigDecimal maxValue, Pageable pageable);

    /**
     * Get leads created between dates with pagination.
     */
    Page<LeadDTO> getLeadsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Get leads by status and date range with pagination.
     */
    Page<LeadDTO> getLeadsByStatusAndDateRange(LeadStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Search leads by term with pagination.
     */
    Page<LeadDTO> searchLeads(String searchTerm, Pageable pageable);

    /**
     * Search leads by term and status with pagination.
     */
    Page<LeadDTO> searchLeadsByStatus(String searchTerm, LeadStatus status, Pageable pageable);

    /**
     * Get high-value leads with pagination.
     */
    Page<LeadDTO> getHighValueLeads(BigDecimal threshold, Pageable pageable);

    /**
     * Get unassigned leads with pagination.
     */
    Page<LeadDTO> getUnassignedLeads(Pageable pageable);

    /**
     * Get unassigned leads by status with pagination.
     */
    Page<LeadDTO> getUnassignedLeadsByStatus(LeadStatus status, Pageable pageable);

    /**
     * Update lead.
     */
    LeadDTO updateLead(Long id, UpdateLeadRequest request);

    /**
     * Assign lead to user.
     */
    LeadDTO assignLead(Long id, AssignLeadRequest request);

    /**
     * Escalate lead to manager.
     */
    LeadDTO escalateLead(Long id);

    /**
     * Convert lead.
     */
    LeadDTO convertLead(Long id);

    /**
     * Reject lead.
     */
    LeadDTO rejectLead(Long id);

    /**
     * Delete lead (soft delete).
     */
    void deleteLead(Long id);

    /**
     * Get lead count by status.
     */
    long getLeadCountByStatus(LeadStatus status);

    /**
     * Get lead count by assigned user.
     */
    long getLeadCountByAssignedUser(Long userId);

    /**
     * Get lead count by status and assigned user.
     */
    long getLeadCountByStatusAndAssignedUser(LeadStatus status, Long userId);

    /**
     * Get lead count by potential value range.
     */
    long getLeadCountByPotentialValueRange(BigDecimal minValue, BigDecimal maxValue);

    /**
     * Get lead count by date range.
     */
    long getLeadCountByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get lead count by status and date range.
     */
    long getLeadCountByStatusAndDateRange(LeadStatus status, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get total lead count.
     */
    long getTotalLeadCount();

    /**
     * Get leads needing Pega sync.
     */
    List<LeadDTO> getLeadsNeedingPegaSync(LocalDateTime syncThreshold);

    /**
     * Update lead Pega status.
     */
    LeadDTO updateLeadPegaStatus(Long id, String pegaStatus, String pegaCaseId);

    /**
     * Get lead by Pega workflow ID.
     */
    Optional<LeadDTO> getLeadByPegaWorkflowId(String pegaWorkflowId);

    /**
     * Get lead by Pega case ID.
     */
    Optional<LeadDTO> getLeadByPegaCaseId(String pegaCaseId);
}
