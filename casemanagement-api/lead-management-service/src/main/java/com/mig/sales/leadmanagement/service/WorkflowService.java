package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.exception.BusinessException;
import com.mig.sales.leadmanagement.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service for workflow operations (escalation, approval, etc.)
 */
@Service
@Transactional
public class WorkflowService {

    @Autowired
    private LeadService leadService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeadScoringService leadScoringService;

    @Autowired
    private LeadHistoryService leadHistoryService;

    /**
     * Escalate high-value lead to manager
     * @param leadId lead ID
     * @param currentUser current user escalating the lead
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead escalateLead(Long leadId, User currentUser) {
        Lead lead = leadService.findById(leadId);
        
        // Check if lead is high-value
        if (!leadScoringService.isHighValueLead(lead)) {
            throw new BusinessException("Lead does not meet high-value criteria for escalation");
        }
        
        // Check if user is authorized to escalate (must be assigned to the lead)
        if (lead.getAssignedTo() == null || !lead.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("User is not authorized to escalate this lead");
        }
        
        // Find a manager to assign to
        User manager = findManager();
        if (manager == null) {
            throw new BusinessException("No active manager found for lead escalation");
        }
        
        String oldStatus = lead.getStatus();
        lead.setStatus("PRE_CONVERSION");
        lead.setAssignedTo(manager);
        lead.setUpdatedDate(java.time.LocalDateTime.now());
        
        Lead updatedLead = leadService.updateLead(lead);
        
        // Log escalation
        leadHistoryService.logActivity(updatedLead, currentUser, 
                "Lead escalated to manager " + manager.getUsername() + " due to high value", 
                "Escalated", "WORKFLOW", oldStatus, "PRE_CONVERSION");
        
        return updatedLead;
    }

    /**
     * Approve lead conversion
     * @param leadId lead ID
     * @param currentUser current user approving the lead
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead approveLead(Long leadId, User currentUser) {
        Lead lead = leadService.findById(leadId);
        
        // Check if user is authorized to approve (must be manager and assigned to the lead)
        if (!"SALES_MANAGER".equals(currentUser.getRole())) {
            throw new UnauthorizedException("Only managers can approve lead conversions");
        }
        
        if (lead.getAssignedTo() == null || !lead.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("User is not authorized to approve this lead");
        }
        
        String oldStatus = lead.getStatus();
        lead.setStatus("CONVERTED");
        lead.setUpdatedDate(java.time.LocalDateTime.now());
        
        Lead updatedLead = leadService.updateLead(lead);
        
        // Log approval
        leadHistoryService.logActivity(updatedLead, currentUser, 
                "Lead conversion approved by manager", 
                "Approved", "WORKFLOW", oldStatus, "CONVERTED");
        
        return updatedLead;
    }

    /**
     * Reject lead conversion
     * @param leadId lead ID
     * @param currentUser current user rejecting the lead
     * @param reason rejection reason
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead rejectLead(Long leadId, User currentUser, String reason) {
        Lead lead = leadService.findById(leadId);
        
        // Check if user is authorized to reject (must be manager and assigned to the lead)
        if (!"SALES_MANAGER".equals(currentUser.getRole())) {
            throw new UnauthorizedException("Only managers can reject lead conversions");
        }
        
        if (lead.getAssignedTo() == null || !lead.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("User is not authorized to reject this lead");
        }
        
        String oldStatus = lead.getStatus();
        lead.setStatus("REJECTED");
        lead.setUpdatedDate(java.time.LocalDateTime.now());
        
        Lead updatedLead = leadService.updateLead(lead);
        
        // Log rejection
        leadHistoryService.logActivity(updatedLead, currentUser, 
                "Lead conversion rejected by manager. Reason: " + (reason != null ? reason : "No reason provided"), 
                "Rejected", "WORKFLOW", oldStatus, "REJECTED");
        
        return updatedLead;
    }

    /**
     * Request approval for standard lead
     * @param leadId lead ID
     * @param currentUser current user requesting approval
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead requestApproval(Long leadId, User currentUser) {
        Lead lead = leadService.findById(leadId);
        
        // Check if user is authorized to request approval (must be assigned to the lead)
        if (lead.getAssignedTo() == null || !lead.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("User is not authorized to request approval for this lead");
        }
        
        // Check if lead is not high-value (standard approval process)
        if (leadScoringService.isHighValueLead(lead)) {
            throw new BusinessException("High-value leads must be escalated, not approved through standard process");
        }
        
        String oldStatus = lead.getStatus();
        lead.setStatus("IN_PROGRESS"); // Keep in progress until manager approves
        lead.setUpdatedDate(java.time.LocalDateTime.now());
        
        Lead updatedLead = leadService.updateLead(lead);
        
        // Log approval request
        leadHistoryService.logActivity(updatedLead, currentUser, 
                "Approval requested for lead conversion", 
                "Approval Requested", "WORKFLOW", oldStatus, "IN_PROGRESS");
        
        return updatedLead;
    }

    /**
     * Find an active manager
     * @return active manager or null if none found
     */
    private User findManager() {
        try {
            return userService.findActiveUsersByRole("SALES_MANAGER").stream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if lead can be escalated
     * @param leadId lead ID
     * @param currentUser current user
     * @return true if lead can be escalated
     */
    public boolean canEscalateLead(Long leadId, User currentUser) {
        try {
            Lead lead = leadService.findById(leadId);
            
            // Check if lead is high-value
            if (!leadScoringService.isHighValueLead(lead)) {
                return false;
            }
            
            // Check if user is assigned to the lead
            if (lead.getAssignedTo() == null || !lead.getAssignedTo().getId().equals(currentUser.getId())) {
                return false;
            }
            
            // Check if lead is in a state that can be escalated
            return "IN_PROGRESS".equals(lead.getStatus()) || "ASSIGNED".equals(lead.getStatus());
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if lead can be approved
     * @param leadId lead ID
     * @param currentUser current user
     * @return true if lead can be approved
     */
    public boolean canApproveLead(Long leadId, User currentUser) {
        try {
            Lead lead = leadService.findById(leadId);
            
            // Check if user is a manager
            if (!"SALES_MANAGER".equals(currentUser.getRole())) {
                return false;
            }
            
            // Check if user is assigned to the lead
            if (lead.getAssignedTo() == null || !lead.getAssignedTo().getId().equals(currentUser.getId())) {
                return false;
            }
            
            // Check if lead is in a state that can be approved
            return "PRE_CONVERSION".equals(lead.getStatus()) || "IN_PROGRESS".equals(lead.getStatus());
            
        } catch (Exception e) {
            return false;
        }
    }
}

