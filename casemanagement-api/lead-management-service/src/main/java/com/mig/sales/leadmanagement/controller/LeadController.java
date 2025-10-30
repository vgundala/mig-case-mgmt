package com.mig.sales.leadmanagement.controller;

import com.mig.sales.leadmanagement.dto.LeadRequest;
import com.mig.sales.leadmanagement.dto.LeadResponse;
import com.mig.sales.leadmanagement.dto.ApiResponse;
import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.service.LeadService;
import com.mig.sales.leadmanagement.service.LeadDistributionService;
import com.mig.sales.leadmanagement.service.WorkflowService;
import com.mig.sales.leadmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lead management controller
 */
@RestController
@RequestMapping("/leads")
@Tag(name = "Lead Management", description = "Lead management APIs")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private LeadDistributionService leadDistributionService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UserService userService;

    /**
     * Get all leads with pagination and filtering
     */
    @GetMapping
    @Operation(summary = "Get all leads", description = "Retrieve all leads with pagination and filtering")
    public ResponseEntity<ApiResponse<Page<LeadResponse>>> getAllLeads(
            @Parameter(description = "Lead status filter") @RequestParam(required = false) String status,
            @Parameter(description = "Assigned user ID filter") @RequestParam(required = false) Long assignedTo,
            @Parameter(description = "Lead source filter") @RequestParam(required = false) String leadSource,
            Pageable pageable) {
        
        User assignedUser = assignedTo != null ? userService.findById(assignedTo) : null;
        Page<Lead> leads = leadService.findLeadsWithFilters(status, assignedUser, leadSource, pageable);
        Page<LeadResponse> leadResponses = leads.map(this::convertToResponse);
        
        return ResponseEntity.ok(ApiResponse.success(leadResponses));
    }

    /**
     * Get lead by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get lead by ID", description = "Retrieve a specific lead by its ID")
    public ResponseEntity<ApiResponse<LeadResponse>> getLeadById(@PathVariable Long id) {
        Lead lead = leadService.findById(id);
        LeadResponse response = convertToResponse(lead);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Create new lead
     */
    @PostMapping
    @Operation(summary = "Create new lead", description = "Create a new lead")
    public ResponseEntity<ApiResponse<LeadResponse>> createLead(@Valid @RequestBody LeadRequest leadRequest) {
        Lead lead = convertToEntity(leadRequest);
        Lead createdLead = leadService.createLead(lead);
        LeadResponse response = convertToResponse(createdLead);
        return ResponseEntity.ok(ApiResponse.success("Lead created successfully", response));
    }

    /**
     * Update lead
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update lead", description = "Update an existing lead")
    public ResponseEntity<ApiResponse<LeadResponse>> updateLead(@PathVariable Long id, @Valid @RequestBody LeadRequest leadRequest) {
        Lead lead = convertToEntity(leadRequest);
        lead.setId(id);
        Lead updatedLead = leadService.updateLead(lead);
        LeadResponse response = convertToResponse(updatedLead);
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully", response));
    }

    /**
     * Get current user's assigned leads
     */
    @GetMapping("/my-leads")
    @Operation(summary = "Get my leads", description = "Get leads assigned to the current user")
    public ResponseEntity<ApiResponse<List<LeadResponse>>> getMyLeads(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        List<Lead> leads = leadService.findByAssignedTo(currentUser);
        List<LeadResponse> responses = leads.stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get new leads (for managers)
     */
    @GetMapping("/new")
    @Operation(summary = "Get new leads", description = "Get all new unassigned leads")
    public ResponseEntity<ApiResponse<List<LeadResponse>>> getNewLeads() {
        List<Lead> leads = leadService.findNewLeadsForDistribution();
        List<LeadResponse> responses = leads.stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get high-value leads
     */
    @GetMapping("/high-value")
    @Operation(summary = "Get high-value leads", description = "Get all high-value leads (>= $1M)")
    public ResponseEntity<ApiResponse<List<LeadResponse>>> getHighValueLeads() {
        List<Lead> leads = leadService.findHighValueLeads();
        List<LeadResponse> responses = leads.stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Distribute leads (Manager only)
     */
    @PostMapping("/distribute")
    @Operation(summary = "Distribute leads", description = "Distribute new leads to sales team (Manager only)")
    public ResponseEntity<ApiResponse<String>> distributeLeads() {
        int distributedCount = leadDistributionService.distributeLeads();
        return ResponseEntity.ok(ApiResponse.success("Distributed " + distributedCount + " leads"));
    }

    /**
     * Escalate lead (Sales Person)
     */
    @PostMapping("/{id}/escalate")
    @Operation(summary = "Escalate lead", description = "Escalate high-value lead to manager")
    public ResponseEntity<ApiResponse<LeadResponse>> escalateLead(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Lead escalatedLead = workflowService.escalateLead(id, currentUser);
        LeadResponse response = convertToResponse(escalatedLead);
        return ResponseEntity.ok(ApiResponse.success("Lead escalated successfully", response));
    }

    /**
     * Approve lead (Manager only)
     */
    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve lead", description = "Approve lead conversion (Manager only)")
    public ResponseEntity<ApiResponse<LeadResponse>> approveLead(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Lead approvedLead = workflowService.approveLead(id, currentUser);
        LeadResponse response = convertToResponse(approvedLead);
        return ResponseEntity.ok(ApiResponse.success("Lead approved successfully", response));
    }

    /**
     * Reject lead (Manager only)
     */
    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject lead", description = "Reject lead conversion (Manager only)")
    public ResponseEntity<ApiResponse<LeadResponse>> rejectLead(@PathVariable Long id, 
            @RequestParam(required = false) String reason, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Lead rejectedLead = workflowService.rejectLead(id, currentUser, reason);
        LeadResponse response = convertToResponse(rejectedLead);
        return ResponseEntity.ok(ApiResponse.success("Lead rejected successfully", response));
    }

    /**
     * Request approval for standard lead
     */
    @PostMapping("/{id}/request-approval")
    @Operation(summary = "Request approval", description = "Request approval for standard lead conversion")
    public ResponseEntity<ApiResponse<LeadResponse>> requestApproval(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Lead updatedLead = workflowService.requestApproval(id, currentUser);
        LeadResponse response = convertToResponse(updatedLead);
        return ResponseEntity.ok(ApiResponse.success("Approval requested successfully", response));
    }

    /**
     * Update lead status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update lead status", description = "Update lead status")
    public ResponseEntity<ApiResponse<LeadResponse>> updateLeadStatus(@PathVariable Long id, 
            @RequestParam String status, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Lead updatedLead = leadService.updateLeadStatus(id, status, currentUser);
        LeadResponse response = convertToResponse(updatedLead);
        return ResponseEntity.ok(ApiResponse.success("Lead status updated successfully", response));
    }

    /**
     * Recalculate lead score
     */
    @PostMapping("/{id}/recalculate-score")
    @Operation(summary = "Recalculate lead score", description = "Recalculate lead score based on current data")
    public ResponseEntity<ApiResponse<LeadResponse>> recalculateLeadScore(@PathVariable Long id) {
        Lead updatedLead = leadService.recalculateLeadScore(id);
        LeadResponse response = convertToResponse(updatedLead);
        return ResponseEntity.ok(ApiResponse.success("Lead score recalculated successfully", response));
    }

    /**
     * Get distribution statistics
     */
    @GetMapping("/distribution-stats")
    @Operation(summary = "Get distribution statistics", description = "Get lead distribution statistics")
    public ResponseEntity<ApiResponse<LeadDistributionService.DistributionStats>> getDistributionStats() {
        LeadDistributionService.DistributionStats stats = leadDistributionService.getDistributionStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    // Helper methods

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    private Lead convertToEntity(LeadRequest request) {
        Lead lead = new Lead();
        lead.setLeadName(request.getLeadName());
        lead.setCompany(request.getCompany());
        lead.setEmail(request.getEmail());
        lead.setPhone(request.getPhone());
        lead.setPotentialValue(request.getPotentialValue());
        lead.setLeadSource(request.getLeadSource());
        lead.setDescription(request.getDescription());
        lead.setIndustry(request.getIndustry());
        lead.setCompanySize(request.getCompanySize());
        lead.setLocation(request.getLocation());
        return lead;
    }

    private LeadResponse convertToResponse(Lead lead) {
        LeadResponse response = new LeadResponse();
        response.setId(lead.getId());
        response.setLeadName(lead.getLeadName());
        response.setCompany(lead.getCompany());
        response.setEmail(lead.getEmail());
        response.setPhone(lead.getPhone());
        response.setStatus(lead.getStatus());
        response.setAssignedTo(lead.getAssignedTo() != null ? lead.getAssignedTo().getId() : null);
        response.setAssignedToUsername(lead.getAssignedTo() != null ? lead.getAssignedTo().getUsername() : null);
        response.setAssignedToFirstName(lead.getAssignedTo() != null ? lead.getAssignedTo().getFirstName() : null);
        response.setAssignedToLastName(lead.getAssignedTo() != null ? lead.getAssignedTo().getLastName() : null);
        response.setPotentialValue(lead.getPotentialValue());
        response.setLeadSource(lead.getLeadSource());
        response.setLeadScore(lead.getLeadScore());
        response.setCreatedDate(lead.getCreatedDate());
        response.setUpdatedDate(lead.getUpdatedDate());
        response.setDescription(lead.getDescription());
        response.setIndustry(lead.getIndustry());
        response.setCompanySize(lead.getCompanySize());
        response.setLocation(lead.getLocation());
        return response;
    }
}

