package com.mig.sales.leadmanagement.controller;

import com.mig.sales.leadmanagement.dto.CommentRequest;
import com.mig.sales.leadmanagement.dto.LeadHistoryResponse;
import com.mig.sales.leadmanagement.dto.ApiResponse;
import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.LeadHistory;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.service.LeadService;
import com.mig.sales.leadmanagement.service.LeadHistoryService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lead history controller for managing lead activities and comments
 */
@RestController
@RequestMapping("/leads/{leadId}/history")
@Tag(name = "Lead History", description = "Lead history and activity management APIs")
public class LeadHistoryController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private LeadHistoryService leadHistoryService;

    @Autowired
    private UserService userService;

    /**
     * Get lead history
     */
    @GetMapping
    @Operation(summary = "Get lead history", description = "Retrieve lead history with pagination")
    public ResponseEntity<ApiResponse<Page<LeadHistoryResponse>>> getLeadHistory(
            @PathVariable Long leadId, Pageable pageable) {
        
        Lead lead = leadService.findById(leadId);
        Page<LeadHistory> history = leadHistoryService.findByLead(lead, pageable);
        Page<LeadHistoryResponse> responses = history.map(this::convertToResponse);
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get all lead history (without pagination)
     */
    @GetMapping("/all")
    @Operation(summary = "Get all lead history", description = "Retrieve all lead history records")
    public ResponseEntity<ApiResponse<List<LeadHistoryResponse>>> getAllLeadHistory(@PathVariable Long leadId) {
        Lead lead = leadService.findById(leadId);
        List<LeadHistory> history = leadHistoryService.findByLead(lead);
        List<LeadHistoryResponse> responses = history.stream().map(this::convertToResponse).collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Add comment to lead
     */
    @PostMapping("/comments")
    @Operation(summary = "Add comment", description = "Add a comment to the lead")
    public ResponseEntity<ApiResponse<LeadHistoryResponse>> addComment(
            @PathVariable Long leadId, 
            @Valid @RequestBody CommentRequest commentRequest,
            Authentication authentication) {
        
        Lead lead = leadService.findById(leadId);
        User currentUser = getCurrentUser(authentication);
        
        LeadHistory history = leadHistoryService.addComment(lead, currentUser, 
                commentRequest.getCommentText(), commentRequest.getAction());
        
        LeadHistoryResponse response = convertToResponse(history);
        return ResponseEntity.ok(ApiResponse.success("Comment added successfully", response));
    }

    /**
     * Get lead history by action
     */
    @GetMapping("/action/{action}")
    @Operation(summary = "Get history by action", description = "Get lead history filtered by action")
    public ResponseEntity<ApiResponse<List<LeadHistoryResponse>>> getHistoryByAction(
            @PathVariable Long leadId, @PathVariable String action) {
        
        Lead lead = leadService.findById(leadId);
        List<LeadHistory> history = leadHistoryService.findByLeadAndAction(lead, action);
        List<LeadHistoryResponse> responses = history.stream().map(this::convertToResponse).collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get lead history by action type
     */
    @GetMapping("/action-type/{actionType}")
    @Operation(summary = "Get history by action type", description = "Get lead history filtered by action type")
    public ResponseEntity<ApiResponse<List<LeadHistoryResponse>>> getHistoryByActionType(
            @PathVariable Long leadId, @PathVariable String actionType) {
        
        Lead lead = leadService.findById(leadId);
        List<LeadHistory> history = leadHistoryService.findByLeadAndActionType(lead, actionType);
        List<LeadHistoryResponse> responses = history.stream().map(this::convertToResponse).collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get lead history by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get history by date range", description = "Get lead history within a date range")
    public ResponseEntity<ApiResponse<List<LeadHistoryResponse>>> getHistoryByDateRange(
            @PathVariable Long leadId,
            @Parameter(description = "Start date (ISO format)") @RequestParam String startDate,
            @Parameter(description = "End date (ISO format)") @RequestParam String endDate) {
        
        Lead lead = leadService.findById(leadId);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        
        List<LeadHistory> history = leadHistoryService.findByLeadAndDateRange(lead, start, end);
        List<LeadHistoryResponse> responses = history.stream().map(this::convertToResponse).collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get recent lead history
     */
    @GetMapping("/recent")
    @Operation(summary = "Get recent history", description = "Get recent lead history (last N records)")
    public ResponseEntity<ApiResponse<List<LeadHistoryResponse>>> getRecentHistory(
            @PathVariable Long leadId,
            @Parameter(description = "Number of recent records") @RequestParam(defaultValue = "10") int limit) {
        
        Lead lead = leadService.findById(leadId);
        List<LeadHistory> history = leadHistoryService.findRecentLeadHistory(lead, limit);
        List<LeadHistoryResponse> responses = history.stream().map(this::convertToResponse).collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get lead history with comments only
     */
    @GetMapping("/comments")
    @Operation(summary = "Get comments only", description = "Get lead history records that have comments")
    public ResponseEntity<ApiResponse<List<LeadHistoryResponse>>> getCommentsOnly(@PathVariable Long leadId) {
        Lead lead = leadService.findById(leadId);
        List<LeadHistory> history = leadHistoryService.findLeadHistoryWithComments(lead);
        List<LeadHistoryResponse> responses = history.stream().map(this::convertToResponse).collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Get lead history count
     */
    @GetMapping("/count")
    @Operation(summary = "Get history count", description = "Get total number of history records for the lead")
    public ResponseEntity<ApiResponse<Long>> getHistoryCount(@PathVariable Long leadId) {
        Lead lead = leadService.findById(leadId);
        long count = leadHistoryService.countByLead(lead);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    // Helper methods

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    private LeadHistoryResponse convertToResponse(LeadHistory history) {
        LeadHistoryResponse response = new LeadHistoryResponse();
        response.setId(history.getId());
        response.setLeadId(history.getLead().getId());
        response.setUserId(history.getUser() != null ? history.getUser().getId() : null);
        response.setUsername(history.getUser() != null ? history.getUser().getUsername() : null);
        response.setUserFirstName(history.getUser() != null ? history.getUser().getFirstName() : null);
        response.setUserLastName(history.getUser() != null ? history.getUser().getLastName() : null);
        response.setCommentText(history.getCommentText());
        response.setAction(history.getAction());
        response.setTimestamp(history.getTimestamp());
        response.setActionType(history.getActionType());
        response.setOldStatus(history.getOldStatus());
        response.setNewStatus(history.getNewStatus());
        return response;
    }
}

