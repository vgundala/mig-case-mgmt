package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for lead distribution operations
 */
@Service
@Transactional
public class LeadDistributionService {

    @Autowired
    private LeadService leadService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeadHistoryService leadHistoryService;

    /**
     * Distribute new leads to sales team using round-robin algorithm
     * @return number of leads distributed
     */
    @CacheEvict(value = "leads", allEntries = true)
    public int distributeLeads() {
        // Get all new leads
        List<Lead> newLeads = leadService.findNewLeadsForDistribution();
        
        if (newLeads.isEmpty()) {
            return 0;
        }

        // Get all active sales persons
        List<User> salesPersons = userService.findActiveSalesPersons();
        
        if (salesPersons.isEmpty()) {
            throw new BusinessException("No active sales persons found for lead distribution");
        }

        // Distribute leads using round-robin algorithm
        int salesPersonIndex = 0;
        int distributedCount = 0;
        
        for (Lead lead : newLeads) {
            User assignedUser = salesPersons.get(salesPersonIndex % salesPersons.size());
            
            // Assign lead to user
            lead.setAssignedTo(assignedUser);
            lead.setStatus("ASSIGNED");
            leadService.updateLead(lead);
            
            // Log distribution activity
            leadHistoryService.logActivity(lead, assignedUser, 
                    "Lead distributed to " + assignedUser.getUsername(), 
                    "Distributed", "SYSTEM", "NEW", "ASSIGNED");
            
            salesPersonIndex++;
            distributedCount++;
        }

        return distributedCount;
    }

    /**
     * Distribute specific leads to specific users
     * @param leadIds list of lead IDs to distribute
     * @param userIds list of user IDs to distribute to
     * @return number of leads distributed
     */
    @CacheEvict(value = "leads", allEntries = true)
    public int distributeSpecificLeads(List<Long> leadIds, List<Long> userIds) {
        if (leadIds.isEmpty() || userIds.isEmpty()) {
            return 0;
        }

        // Get users
        List<User> users = userIds.stream()
                .map(userService::findById)
                .toList();

        int distributedCount = 0;
        int userIndex = 0;

        for (Long leadId : leadIds) {
            try {
                Lead lead = leadService.findById(leadId);
                
                if (!"NEW".equals(lead.getStatus())) {
                    continue; // Skip leads that are not new
                }

                User assignedUser = users.get(userIndex % users.size());
                
                // Assign lead to user
                lead.setAssignedTo(assignedUser);
                lead.setStatus("ASSIGNED");
                leadService.updateLead(lead);
                
                // Log distribution activity
                leadHistoryService.logActivity(lead, assignedUser, 
                        "Lead manually distributed to " + assignedUser.getUsername(), 
                        "Manually Distributed", "USER_ACTION", "NEW", "ASSIGNED");
                
                userIndex++;
                distributedCount++;
                
            } catch (Exception e) {
                // Log error but continue with other leads
                System.err.println("Error distributing lead " + leadId + ": " + e.getMessage());
            }
        }

        return distributedCount;
    }

    /**
     * Reassign lead to different user
     * @param leadId lead ID
     * @param newUserId new user ID
     * @param currentUser current user making the reassignment
     * @return updated lead
     */
    @CacheEvict(value = "leads", allEntries = true)
    public Lead reassignLead(Long leadId, Long newUserId, User currentUser) {
        Lead lead = leadService.findById(leadId);
        User newUser = userService.findById(newUserId);
        
        User oldUser = lead.getAssignedTo();
        lead.setAssignedTo(newUser);
        lead.setUpdatedDate(java.time.LocalDateTime.now());
        
        Lead updatedLead = leadService.updateLead(lead);
        
        // Log reassignment
        leadHistoryService.logActivity(updatedLead, currentUser, 
                "Lead reassigned from " + (oldUser != null ? oldUser.getUsername() : "unassigned") + 
                " to " + newUser.getUsername(), 
                "Reassigned", "USER_ACTION", null, null);
        
        return updatedLead;
    }

    /**
     * Get distribution statistics
     * @return distribution statistics
     */
    public DistributionStats getDistributionStats() {
        List<User> salesPersons = userService.findActiveSalesPersons();
        long newLeadsCount = leadService.countByStatus("NEW");
        long assignedLeadsCount = leadService.countByStatus("ASSIGNED");
        
        return new DistributionStats(
                salesPersons.size(),
                newLeadsCount,
                assignedLeadsCount,
                newLeadsCount + assignedLeadsCount
        );
    }

    /**
     * Distribution statistics data class
     */
    public static class DistributionStats {
        private final int activeSalesPersons;
        private final long newLeadsCount;
        private final long assignedLeadsCount;
        private final long totalLeadsCount;

        public DistributionStats(int activeSalesPersons, long newLeadsCount, long assignedLeadsCount, long totalLeadsCount) {
            this.activeSalesPersons = activeSalesPersons;
            this.newLeadsCount = newLeadsCount;
            this.assignedLeadsCount = assignedLeadsCount;
            this.totalLeadsCount = totalLeadsCount;
        }

        public int getActiveSalesPersons() { return activeSalesPersons; }
        public long getNewLeadsCount() { return newLeadsCount; }
        public long getAssignedLeadsCount() { return assignedLeadsCount; }
        public long getTotalLeadsCount() { return totalLeadsCount; }
    }
}

