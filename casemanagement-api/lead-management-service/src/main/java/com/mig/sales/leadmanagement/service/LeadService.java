package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.repository.LeadRepository;
import com.mig.sales.leadmanagement.exception.ResourceNotFoundException;
import com.mig.sales.leadmanagement.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for lead management operations
 */
@Service
@Transactional
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private LeadScoringService leadScoringService;

    @Autowired
    private LeadHistoryService leadHistoryService;

    /**
     * Create a new lead
     * @param lead the lead to create
     * @return created lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead createLead(Lead lead) {
        // Set initial status and creation date
        lead.setStatus("NEW");
        lead.setCreatedDate(LocalDateTime.now());
        lead.setUpdatedDate(LocalDateTime.now());

        // Calculate and set the lead score
        int score = leadScoringService.calculateScore(lead);
        lead.setLeadScore(score);

        Lead savedLead = leadRepository.save(lead);

        // Log lead creation
        leadHistoryService.logActivity(savedLead, null, "Lead created", "Created", "SYSTEM", null, "NEW");

        return savedLead;
    }

    /**
     * Find lead by ID
     * @param id lead ID
     * @return lead if found
     * @throws ResourceNotFoundException if lead not found
     */
    @Cacheable(value = "leads", key = "#id")
    public Lead findById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
    }

    /**
     * Find all leads
     * @return list of all leads ordered by score
     */
    @Cacheable(value = "leads", key = "'all'")
    public List<Lead> findAllLeads() {
        return leadRepository.findAllOrderByLeadScoreDesc();
    }

    /**
     * Find leads with pagination and filtering
     * @param status optional status filter
     * @param assignedTo optional assigned user filter
     * @param leadSource optional lead source filter
     * @param pageable pagination information
     * @return page of leads
     */
    public Page<Lead> findLeadsWithFilters(String status, User assignedTo, String leadSource, Pageable pageable) {
        return leadRepository.findLeadsWithFilters(status, assignedTo, leadSource, pageable);
    }

    /**
     * Find leads by status
     * @param status lead status
     * @return list of leads with the specified status
     */
    @Cacheable(value = "leads", key = "#status")
    public List<Lead> findByStatus(String status) {
        return leadRepository.findByStatus(status);
    }

    /**
     * Find leads assigned to user
     * @param user assigned user
     * @return list of leads assigned to the user
     */
    @Cacheable(value = "leads", key = "#user.id")
    public List<Lead> findByAssignedTo(User user) {
        return leadRepository.findByAssignedTo(user);
    }

    /**
     * Find leads assigned to user with specific statuses
     * @param user assigned user
     * @param statuses list of statuses
     * @return list of leads assigned to the user with any of the specified statuses
     */
    @Cacheable(value = "leads", key = "#user.id + '_' + #statuses")
    public List<Lead> findByAssignedToAndStatusIn(User user, List<String> statuses) {
        return leadRepository.findByAssignedToAndStatusIn(user, statuses);
    }

    /**
     * Find new leads for distribution
     * @return list of new leads
     */
    @Cacheable(value = "leads", key = "'new'")
    public List<Lead> findNewLeadsForDistribution() {
        return leadRepository.findNewLeadsForDistribution();
    }

    /**
     * Find high-value leads
     * @return list of high-value leads
     */
    @Cacheable(value = "leads", key = "'high_value'")
    public List<Lead> findHighValueLeads() {
        return leadRepository.findHighValueLeads(new BigDecimal("1000000"));
    }

    /**
     * Update lead
     * @param lead lead to update
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead updateLead(Lead lead) {
        Lead existingLead = findById(lead.getId());
        
        // Track status change
        String oldStatus = existingLead.getStatus();
        String newStatus = lead.getStatus();
        
        // Update fields
        existingLead.setLeadName(lead.getLeadName());
        existingLead.setCompany(lead.getCompany());
        existingLead.setEmail(lead.getEmail());
        existingLead.setPhone(lead.getPhone());
        existingLead.setStatus(lead.getStatus());
        existingLead.setAssignedTo(lead.getAssignedTo());
        existingLead.setPotentialValue(lead.getPotentialValue());
        existingLead.setLeadSource(lead.getLeadSource());
        existingLead.setDescription(lead.getDescription());
        existingLead.setIndustry(lead.getIndustry());
        existingLead.setCompanySize(lead.getCompanySize());
        existingLead.setLocation(lead.getLocation());
        existingLead.setUpdatedDate(LocalDateTime.now());

        Lead savedLead = leadRepository.save(existingLead);

        // Log status change if different
        if (!oldStatus.equals(newStatus)) {
            leadHistoryService.logActivity(savedLead, null, "Status changed from " + oldStatus + " to " + newStatus, 
                    "Status Changed", "USER_ACTION", oldStatus, newStatus);
        }

        return savedLead;
    }

    /**
     * Update lead status
     * @param leadId lead ID
     * @param newStatus new status
     * @param user user making the change
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead updateLeadStatus(Long leadId, String newStatus, User user) {
        Lead lead = findById(leadId);
        String oldStatus = lead.getStatus();
        
        lead.setStatus(newStatus);
        lead.setUpdatedDate(LocalDateTime.now());
        
        Lead savedLead = leadRepository.save(lead);

        // Log status change
        leadHistoryService.logActivity(savedLead, user, "Status changed from " + oldStatus + " to " + newStatus, 
                "Status Changed", "USER_ACTION", oldStatus, newStatus);

        return savedLead;
    }

    /**
     * Assign lead to user
     * @param leadId lead ID
     * @param user user to assign to
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead assignLeadToUser(Long leadId, User user) {
        Lead lead = findById(leadId);
        String oldStatus = lead.getStatus();
        
        lead.setAssignedTo(user);
        lead.setStatus("ASSIGNED");
        lead.setUpdatedDate(LocalDateTime.now());
        
        Lead savedLead = leadRepository.save(lead);

        // Log assignment
        leadHistoryService.logActivity(savedLead, user, "Lead assigned to " + user.getUsername(), 
                "Assigned", "SYSTEM", oldStatus, "ASSIGNED");

        return savedLead;
    }

    /**
     * Recalculate lead score
     * @param leadId lead ID
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead recalculateLeadScore(Long leadId) {
        Lead lead = findById(leadId);
        int newScore = leadScoringService.calculateScore(lead);
        int oldScore = lead.getLeadScore();
        
        lead.setLeadScore(newScore);
        lead.setUpdatedDate(LocalDateTime.now());
        
        Lead savedLead = leadRepository.save(lead);

        // Log score recalculation
        leadHistoryService.logActivity(savedLead, null, "Lead score recalculated from " + oldScore + " to " + newScore, 
                "Score Recalculated", "SYSTEM", null, null);

        return savedLead;
    }

    /**
     * Delete lead
     * @param id lead ID
     */
    @CacheEvict(value = "leads", allEntries = true)
    public void deleteLead(Long id) {
        Lead lead = findById(id);
        leadRepository.delete(lead);
    }

    /**
     * Count leads by status
     * @param status lead status
     * @return count of leads with the specified status
     */
    public long countByStatus(String status) {
        return leadRepository.countByStatus(status);
    }

    /**
     * Count leads by assigned user
     * @param user assigned user
     * @return count of leads assigned to the user
     */
    public long countByAssignedTo(User user) {
        return leadRepository.countByAssignedTo(user);
    }
}

