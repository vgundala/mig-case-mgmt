package com.mig.sales.api.controller;

import com.mig.sales.api.model.dto.*;
import com.mig.sales.api.model.entity.LeadStatus;
import com.mig.sales.api.service.LeadService;
import com.mig.sales.api.service.LeadDistributionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Lead management operations.
 */
@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Leads", description = "Lead management APIs")
public class LeadController {

    private final LeadService leadService;
    private final LeadDistributionService leadDistributionService;

    @GetMapping
    @Operation(summary = "Get all leads", description = "Retrieve all leads with pagination and filtering")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getAllLeads(
            @Parameter(description = "Lead status filter") @RequestParam(required = false) LeadStatus status,
            @Parameter(description = "Assigned user ID filter") @RequestParam(required = false) Long assignedTo,
            @Parameter(description = "Search term") @RequestParam(required = false) String search,
            @Parameter(description = "Minimum potential value") @RequestParam(required = false) BigDecimal minValue,
            @Parameter(description = "Maximum potential value") @RequestParam(required = false) BigDecimal maxValue,
            @Parameter(description = "Start date (ISO format)") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)") @RequestParam(required = false) LocalDateTime endDate,
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting leads with filters - status: {}, assignedTo: {}, search: {}", status, assignedTo, search);
        
        Page<LeadDTO> leads;
        
        if (search != null && !search.trim().isEmpty()) {
            if (status != null) {
                leads = leadService.searchLeadsByStatus(search, status, pageable);
            } else {
                leads = leadService.searchLeads(search, pageable);
            }
        } else if (status != null && assignedTo != null) {
            leads = leadService.getLeadsByStatusAndAssignedUser(status, assignedTo, pageable);
        } else if (status != null) {
            leads = leadService.getLeadsByStatus(status, pageable);
        } else if (assignedTo != null) {
            leads = leadService.getLeadsByAssignedUser(assignedTo, pageable);
        } else if (minValue != null && maxValue != null) {
            leads = leadService.getLeadsByPotentialValueRange(minValue, maxValue, pageable);
        } else if (startDate != null && endDate != null) {
            leads = leadService.getLeadsByDateRange(startDate, endDate, pageable);
        } else {
            leads = leadService.getAllLeads(pageable);
        }
        
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lead by ID", description = "Retrieve a specific lead by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lead retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Lead not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> getLeadById(@PathVariable Long id) {
        log.debug("Getting lead by ID: {}", id);
        
        return leadService.getLeadById(id)
                .map(lead -> ResponseEntity.ok(lead))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new lead", description = "Create a new lead in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lead created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> createLead(@Valid @RequestBody CreateLeadRequest request) {
        log.info("Creating new lead: {}", request.getLeadName());
        
        LeadDTO lead = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(lead);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update lead", description = "Update an existing lead")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lead updated successfully"),
        @ApiResponse(responseCode = "404", description = "Lead not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> updateLead(@PathVariable Long id, @Valid @RequestBody UpdateLeadRequest request) {
        log.info("Updating lead: {}", id);
        
        try {
            LeadDTO lead = leadService.updateLead(id, request);
            return ResponseEntity.ok(lead);
        } catch (RuntimeException e) {
            log.error("Error updating lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign lead", description = "Assign a lead to a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lead assigned successfully"),
        @ApiResponse(responseCode = "404", description = "Lead or user not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> assignLead(@PathVariable Long id, @Valid @RequestBody AssignLeadRequest request) {
        log.info("Assigning lead: {} to user: {}", id, request.getAssignedTo());
        
        try {
            LeadDTO lead = leadService.assignLead(id, request);
            return ResponseEntity.ok(lead);
        } catch (RuntimeException e) {
            log.error("Error assigning lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/escalate")
    @Operation(summary = "Escalate lead", description = "Escalate a high-value lead to manager")
    @PreAuthorize("hasRole('SALES_PERSON') or hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lead escalated successfully"),
        @ApiResponse(responseCode = "404", description = "Lead not found"),
        @ApiResponse(responseCode = "400", description = "Lead does not meet escalation criteria"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> escalateLead(@PathVariable Long id) {
        log.info("Escalating lead: {}", id);
        
        try {
            LeadDTO lead = leadService.escalateLead(id);
            return ResponseEntity.ok(lead);
        } catch (RuntimeException e) {
            log.error("Error escalating lead: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/convert")
    @Operation(summary = "Convert lead", description = "Convert a lead to a client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lead converted successfully"),
        @ApiResponse(responseCode = "404", description = "Lead not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> convertLead(@PathVariable Long id) {
        log.info("Converting lead: {}", id);
        
        try {
            LeadDTO lead = leadService.convertLead(id);
            return ResponseEntity.ok(lead);
        } catch (RuntimeException e) {
            log.error("Error converting lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject lead", description = "Reject a lead")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lead rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Lead not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadDTO> rejectLead(@PathVariable Long id) {
        log.info("Rejecting lead: {}", id);
        
        try {
            LeadDTO lead = leadService.rejectLead(id);
            return ResponseEntity.ok(lead);
        } catch (RuntimeException e) {
            log.error("Error rejecting lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete lead", description = "Delete a lead from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Lead deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Lead not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        log.info("Deleting lead: {}", id);
        
        try {
            leadService.deleteLead(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting lead: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unassigned")
    @Operation(summary = "Get unassigned leads", description = "Retrieve all unassigned leads")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unassigned leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getUnassignedLeads(
            @Parameter(description = "Lead status filter") @RequestParam(required = false) LeadStatus status,
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting unassigned leads with status filter: {}", status);
        
        Page<LeadDTO> leads;
        if (status != null) {
            leads = leadService.getUnassignedLeadsByStatus(status, pageable);
        } else {
            leads = leadService.getUnassignedLeads(pageable);
        }
        
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/high-value")
    @Operation(summary = "Get high-value leads", description = "Retrieve leads with potential value above threshold")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "High-value leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getHighValueLeads(
            @Parameter(description = "Value threshold") @RequestParam(defaultValue = "1000000") BigDecimal threshold,
            @PageableDefault(size = 20, sort = "potentialValue", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting high-value leads with threshold: {}", threshold);
        
        Page<LeadDTO> leads = leadService.getHighValueLeads(threshold, pageable);
        return ResponseEntity.ok(leads);
    }

    @PostMapping("/distribute")
    @Operation(summary = "Distribute leads", description = "Distribute unassigned leads among sales people")
    @PreAuthorize("hasRole('SALES_MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leads distributed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Sales Manager role required")
    })
    public ResponseEntity<DistributionResponse> distributeLeads() {
        log.info("Starting lead distribution process");
        
        int distributedCount = leadDistributionService.distributeLeads();
        
        DistributionResponse response = DistributionResponse.builder()
                .distributedCount(distributedCount)
                .message("Successfully distributed " + distributedCount + " leads")
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get lead statistics", description = "Get distribution statistics for leads")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LeadStatsResponse> getLeadStats() {
        log.debug("Getting lead statistics");
        
        LeadStatsResponse stats = LeadStatsResponse.builder()
                .totalLeads(leadService.getTotalLeadCount())
                .newLeads(leadService.getLeadCountByStatus(LeadStatus.NEW))
                .assignedLeads(leadService.getLeadCountByStatus(LeadStatus.ASSIGNED))
                .inProgressLeads(leadService.getLeadCountByStatus(LeadStatus.IN_PROGRESS))
                .convertedLeads(leadService.getLeadCountByStatus(LeadStatus.CONVERTED))
                .rejectedLeads(leadService.getLeadCountByStatus(LeadStatus.REJECTED))
                .build();
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Response DTO for lead distribution.
     */
    public static class DistributionResponse {
        private int distributedCount;
        private String message;

        public static DistributionResponseBuilder builder() {
            return new DistributionResponseBuilder();
        }

        public static class DistributionResponseBuilder {
            private int distributedCount;
            private String message;

            public DistributionResponseBuilder distributedCount(int distributedCount) {
                this.distributedCount = distributedCount;
                return this;
            }

            public DistributionResponseBuilder message(String message) {
                this.message = message;
                return this;
            }

            public DistributionResponse build() {
                DistributionResponse response = new DistributionResponse();
                response.distributedCount = this.distributedCount;
                response.message = this.message;
                return response;
            }
        }

        // Getters and setters
        public int getDistributedCount() { return distributedCount; }
        public void setDistributedCount(int distributedCount) { this.distributedCount = distributedCount; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * Response DTO for lead statistics.
     */
    public static class LeadStatsResponse {
        private long totalLeads;
        private long newLeads;
        private long assignedLeads;
        private long inProgressLeads;
        private long convertedLeads;
        private long rejectedLeads;

        public static LeadStatsResponseBuilder builder() {
            return new LeadStatsResponseBuilder();
        }

        public static class LeadStatsResponseBuilder {
            private long totalLeads;
            private long newLeads;
            private long assignedLeads;
            private long inProgressLeads;
            private long convertedLeads;
            private long rejectedLeads;

            public LeadStatsResponseBuilder totalLeads(long totalLeads) {
                this.totalLeads = totalLeads;
                return this;
            }

            public LeadStatsResponseBuilder newLeads(long newLeads) {
                this.newLeads = newLeads;
                return this;
            }

            public LeadStatsResponseBuilder assignedLeads(long assignedLeads) {
                this.assignedLeads = assignedLeads;
                return this;
            }

            public LeadStatsResponseBuilder inProgressLeads(long inProgressLeads) {
                this.inProgressLeads = inProgressLeads;
                return this;
            }

            public LeadStatsResponseBuilder convertedLeads(long convertedLeads) {
                this.convertedLeads = convertedLeads;
                return this;
            }

            public LeadStatsResponseBuilder rejectedLeads(long rejectedLeads) {
                this.rejectedLeads = rejectedLeads;
                return this;
            }

            public LeadStatsResponse build() {
                LeadStatsResponse response = new LeadStatsResponse();
                response.totalLeads = this.totalLeads;
                response.newLeads = this.newLeads;
                response.assignedLeads = this.assignedLeads;
                response.inProgressLeads = this.inProgressLeads;
                response.convertedLeads = this.convertedLeads;
                response.rejectedLeads = this.rejectedLeads;
                return response;
            }
        }

        // Getters and setters
        public long getTotalLeads() { return totalLeads; }
        public void setTotalLeads(long totalLeads) { this.totalLeads = totalLeads; }
        public long getNewLeads() { return newLeads; }
        public void setNewLeads(long newLeads) { this.newLeads = newLeads; }
        public long getAssignedLeads() { return assignedLeads; }
        public void setAssignedLeads(long assignedLeads) { this.assignedLeads = assignedLeads; }
        public long getInProgressLeads() { return inProgressLeads; }
        public void setInProgressLeads(long inProgressLeads) { this.inProgressLeads = inProgressLeads; }
        public long getConvertedLeads() { return convertedLeads; }
        public void setConvertedLeads(long convertedLeads) { this.convertedLeads = convertedLeads; }
        public long getRejectedLeads() { return rejectedLeads; }
        public void setRejectedLeads(long rejectedLeads) { this.rejectedLeads = rejectedLeads; }
    }
}
