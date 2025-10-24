package com.mig.sales.api.service;

import com.mig.sales.api.model.entity.Lead;
import com.mig.sales.api.model.entity.LeadStatus;
import com.mig.sales.api.model.entity.User;
import com.mig.sales.api.model.entity.UserRole;
import com.mig.sales.api.repository.LeadRepository;
import com.mig.sales.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service for distributing leads among sales people using round-robin algorithm.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeadDistributionService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final LeadScoringService leadScoringService;
    
    // Thread-safe counter for round-robin distribution
    private final AtomicInteger distributionCounter = new AtomicInteger(0);

    /**
     * Distribute unassigned leads among available sales people using round-robin.
     */
    @Transactional
    public int distributeLeads() {
        log.info("Starting lead distribution process");
        
        // Get all unassigned leads
        List<Lead> unassignedLeads = leadRepository.findByStatusAndAssignedToIsNull(LeadStatus.NEW);
        
        if (unassignedLeads.isEmpty()) {
            log.info("No unassigned leads found for distribution");
            return 0;
        }
        
        // Get all active sales people
        List<User> salesPeople = userRepository.findByRoleAndIsActiveTrue(UserRole.SALES_PERSON);
        
        if (salesPeople.isEmpty()) {
            log.warn("No active sales people found for lead distribution");
            return 0;
        }
        
        log.info("Found {} unassigned leads and {} sales people", unassignedLeads.size(), salesPeople.size());
        
        int distributedCount = 0;
        
        // Sort leads by score (highest first) for better distribution
        unassignedLeads.sort((l1, l2) -> {
            Integer score1 = l1.getLeadScore() != null ? l1.getLeadScore() : 0;
            Integer score2 = l2.getLeadScore() != null ? l2.getLeadScore() : 0;
            return score2.compareTo(score1);
        });
        
        // Distribute leads using round-robin
        for (Lead lead : unassignedLeads) {
            User assignedUser = getNextSalesPerson(salesPeople);
            
            lead.setAssignedTo(assignedUser);
            lead.setStatus(LeadStatus.ASSIGNED);
            
            leadRepository.save(lead);
            distributedCount++;
            
            log.info("Assigned lead {} to sales person {}", lead.getLeadName(), assignedUser.getUsername());
        }
        
        log.info("Lead distribution completed. {} leads distributed", distributedCount);
        return distributedCount;
    }

    /**
     * Distribute a specific lead to the next available sales person.
     */
    @Transactional
    public Lead distributeLead(Lead lead) {
        log.info("Distributing specific lead: {}", lead.getLeadName());
        
        // Get all active sales people
        List<User> salesPeople = userRepository.findByRoleAndIsActiveTrue(UserRole.SALES_PERSON);
        
        if (salesPeople.isEmpty()) {
            log.warn("No active sales people found for lead distribution");
            throw new RuntimeException("No active sales people available");
        }
        
        User assignedUser = getNextSalesPerson(salesPeople);
        
        lead.setAssignedTo(assignedUser);
        lead.setStatus(LeadStatus.ASSIGNED);
        
        Lead savedLead = leadRepository.save(lead);
        
        log.info("Lead {} assigned to sales person {}", lead.getLeadName(), assignedUser.getUsername());
        return savedLead;
    }

    /**
     * Get the next sales person using round-robin algorithm.
     */
    private User getNextSalesPerson(List<User> salesPeople) {
        int index = distributionCounter.getAndIncrement() % salesPeople.size();
        return salesPeople.get(index);
    }

    /**
     * Get distribution statistics.
     */
    public DistributionStats getDistributionStats() {
        List<User> salesPeople = userRepository.findByRoleAndIsActiveTrue(UserRole.SALES_PERSON);
        
        DistributionStats stats = new DistributionStats();
        stats.setTotalSalesPeople(salesPeople.size());
        stats.setUnassignedLeads(leadRepository.findByStatusAndAssignedToIsNull(LeadStatus.NEW).size());
        
        for (User salesPerson : salesPeople) {
            long assignedCount = leadRepository.countByAssignedTo(salesPerson);
            stats.addSalesPersonStats(salesPerson.getUsername(), assignedCount);
        }
        
        return stats;
    }

    /**
     * Reset distribution counter (useful for testing).
     */
    public void resetDistributionCounter() {
        distributionCounter.set(0);
        log.info("Distribution counter reset");
    }

    /**
     * Get current distribution counter value.
     */
    public int getDistributionCounter() {
        return distributionCounter.get();
    }

    /**
     * Distribution statistics class.
     */
    public static class DistributionStats {
        private int totalSalesPeople;
        private int unassignedLeads;
        private java.util.Map<String, Long> salesPersonStats = new java.util.HashMap<>();

        public int getTotalSalesPeople() {
            return totalSalesPeople;
        }

        public void setTotalSalesPeople(int totalSalesPeople) {
            this.totalSalesPeople = totalSalesPeople;
        }

        public int getUnassignedLeads() {
            return unassignedLeads;
        }

        public void setUnassignedLeads(int unassignedLeads) {
            this.unassignedLeads = unassignedLeads;
        }

        public java.util.Map<String, Long> getSalesPersonStats() {
            return salesPersonStats;
        }

        public void addSalesPersonStats(String username, long assignedCount) {
            this.salesPersonStats.put(username, assignedCount);
        }
    }
}
