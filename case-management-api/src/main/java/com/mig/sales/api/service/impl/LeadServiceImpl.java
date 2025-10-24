package com.mig.sales.api.service.impl;

import com.mig.sales.api.model.dto.*;
import com.mig.sales.api.model.entity.Lead;
import com.mig.sales.api.model.entity.LeadStatus;
import com.mig.sales.api.model.entity.User;
import com.mig.sales.api.model.mapper.LeadMapper;
import com.mig.sales.api.repository.LeadRepository;
import com.mig.sales.api.repository.UserRepository;
import com.mig.sales.api.service.LeadService;
import com.mig.sales.api.service.LeadScoringService;
import com.mig.sales.api.service.LeadDistributionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of LeadService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final LeadMapper leadMapper;
    private final LeadScoringService leadScoringService;
    private final LeadDistributionService leadDistributionService;

    @Override
    @Transactional
    public LeadDTO createLead(CreateLeadRequest request) {
        log.info("Creating new lead: {}", request.getLeadName());
        
        // Calculate lead score
        Integer leadScore = leadScoringService.calculateScore(request);
        
        Lead lead = leadMapper.toEntity(request);
        lead.setLeadScore(leadScore);
        lead.setStatus(LeadStatus.NEW);
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead created successfully with ID: {}", savedLead.getId());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> getLeadById(Long id) {
        log.debug("Getting lead by ID: {}", id);
        return leadRepository.findById(id)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getAllLeads(Pageable pageable) {
        log.debug("Getting all leads with pagination");
        return leadRepository.findAll(pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getLeadsByStatus(LeadStatus status, Pageable pageable) {
        log.debug("Getting leads by status: {} with pagination", status);
        return leadRepository.findByStatus(status, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getLeadsByAssignedUser(Long userId, Pageable pageable) {
        log.debug("Getting leads by assigned user: {} with pagination", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return leadRepository.findByAssignedTo(user, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getLeadsByStatusAndAssignedUser(LeadStatus status, Long userId, Pageable pageable) {
        log.debug("Getting leads by status: {} and assigned user: {} with pagination", status, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return leadRepository.findByStatusAndAssignedTo(status, user, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getLeadsByPotentialValueRange(BigDecimal minValue, BigDecimal maxValue, Pageable pageable) {
        log.debug("Getting leads by potential value range: {} - {} with pagination", minValue, maxValue);
        return leadRepository.findByPotentialValueBetween(minValue, maxValue, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getLeadsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Getting leads by date range: {} - {} with pagination", startDate, endDate);
        return leadRepository.findByCreatedDateBetween(startDate, endDate, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getLeadsByStatusAndDateRange(LeadStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Getting leads by status: {} and date range: {} - {} with pagination", status, startDate, endDate);
        return leadRepository.findByStatusAndCreatedDateBetween(status, startDate, endDate, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> searchLeads(String searchTerm, Pageable pageable) {
        log.debug("Searching leads with term: {} with pagination", searchTerm);
        return leadRepository.searchLeads(searchTerm, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> searchLeadsByStatus(String searchTerm, LeadStatus status, Pageable pageable) {
        log.debug("Searching leads with term: {} and status: {} with pagination", searchTerm, status);
        return leadRepository.searchLeadsByStatus(searchTerm, status, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getHighValueLeads(BigDecimal threshold, Pageable pageable) {
        log.debug("Getting high-value leads with threshold: {} with pagination", threshold);
        return leadRepository.findHighValueLeads(threshold, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getUnassignedLeads(Pageable pageable) {
        log.debug("Getting unassigned leads with pagination");
        return leadRepository.findByAssignedToIsNull(pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> getUnassignedLeadsByStatus(LeadStatus status, Pageable pageable) {
        log.debug("Getting unassigned leads by status: {} with pagination", status);
        return leadRepository.findByStatusAndAssignedToIsNull(status, pageable)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional
    public LeadDTO updateLead(Long id, UpdateLeadRequest request) {
        log.info("Updating lead: {}", id);
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        // Update fields
        leadMapper.updateEntity(request, lead);
        
        // Recalculate lead score if potential value or lead source changed
        if (request.getPotentialValue() != null || request.getLeadSource() != null) {
            Integer newScore = leadScoringService.calculateScore(lead);
            lead.setLeadScore(newScore);
        }
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead updated successfully: {}", savedLead.getId());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional
    public LeadDTO assignLead(Long id, AssignLeadRequest request) {
        log.info("Assigning lead: {} to user: {}", id, request.getAssignedTo());
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        User assignedUser = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getAssignedTo()));
        
        lead.setAssignedTo(assignedUser);
        lead.setStatus(LeadStatus.ASSIGNED);
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead assigned successfully: {} to user: {}", savedLead.getId(), assignedUser.getUsername());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional
    public LeadDTO escalateLead(Long id) {
        log.info("Escalating lead: {}", id);
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        // Check if lead is high value (requires manager approval)
        if (!lead.isHighValue()) {
            throw new RuntimeException("Lead does not meet escalation criteria (potential value must be > $1M)");
        }
        
        // Find a sales manager
        User manager = userRepository.findByRoleAndIsActiveTrue(com.mig.sales.api.model.entity.UserRole.SALES_MANAGER)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No sales manager found"));
        
        lead.setAssignedTo(manager);
        lead.setStatus(LeadStatus.PRE_CONVERSION);
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead escalated successfully: {} to manager: {}", savedLead.getId(), manager.getUsername());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional
    public LeadDTO convertLead(Long id) {
        log.info("Converting lead: {}", id);
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        lead.setStatus(LeadStatus.CONVERTED);
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead converted successfully: {}", savedLead.getId());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional
    public LeadDTO rejectLead(Long id) {
        log.info("Rejecting lead: {}", id);
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        lead.setStatus(LeadStatus.REJECTED);
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead rejected successfully: {}", savedLead.getId());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional
    public void deleteLead(Long id) {
        log.info("Deleting lead: {}", id);
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        leadRepository.delete(lead);
        log.info("Lead deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByStatus(LeadStatus status) {
        return leadRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByAssignedUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return leadRepository.countByAssignedTo(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByStatusAndAssignedUser(LeadStatus status, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return leadRepository.countByStatusAndAssignedTo(status, user);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByPotentialValueRange(BigDecimal minValue, BigDecimal maxValue) {
        return leadRepository.countByPotentialValueBetween(minValue, maxValue);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return leadRepository.countByCreatedDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLeadCountByStatusAndDateRange(LeadStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return leadRepository.countByStatusAndCreatedDateBetween(status, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalLeadCount() {
        return leadRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsNeedingPegaSync(LocalDateTime syncThreshold) {
        log.debug("Getting leads needing Pega sync with threshold: {}", syncThreshold);
        return leadRepository.findLeadsNeedingPegaSync(syncThreshold)
                .stream()
                .map(leadMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public LeadDTO updateLeadPegaStatus(Long id, String pegaStatus, String pegaCaseId) {
        log.info("Updating lead Pega status: {} for lead: {}", pegaStatus, id);
        
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
        
        lead.setPegaStatus(pegaStatus);
        if (pegaCaseId != null) {
            lead.setPegaCaseId(pegaCaseId);
        }
        lead.setPegaLastSyncDate(LocalDateTime.now());
        
        Lead savedLead = leadRepository.save(lead);
        log.info("Lead Pega status updated successfully: {}", savedLead.getId());
        
        return leadMapper.toDTO(savedLead);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> getLeadByPegaWorkflowId(String pegaWorkflowId) {
        log.debug("Getting lead by Pega workflow ID: {}", pegaWorkflowId);
        return leadRepository.findByPegaWorkflowId(pegaWorkflowId)
                .map(leadMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> getLeadByPegaCaseId(String pegaCaseId) {
        log.debug("Getting lead by Pega case ID: {}", pegaCaseId);
        return leadRepository.findByPegaCaseId(pegaCaseId)
                .map(leadMapper::toDTO);
    }
}
