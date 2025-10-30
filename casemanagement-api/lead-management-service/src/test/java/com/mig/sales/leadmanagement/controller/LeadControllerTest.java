package com.mig.sales.leadmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mig.sales.leadmanagement.dto.LeadRequest;
import com.mig.sales.leadmanagement.dto.LeadResponse;
import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.service.LeadService;
import com.mig.sales.leadmanagement.service.LeadDistributionService;
import com.mig.sales.leadmanagement.service.WorkflowService;
import com.mig.sales.leadmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for LeadController
 */
@WebMvcTest(LeadController.class)
class LeadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeadService leadService;

    @MockBean
    private LeadDistributionService leadDistributionService;

    @MockBean
    private WorkflowService workflowService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Lead testLead;
    private User testUser;
    private LeadRequest leadRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole("SALES_PERSON");
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        testLead = new Lead();
        testLead.setId(1L);
        testLead.setLeadName("Test Lead");
        testLead.setCompany("Test Company");
        testLead.setEmail("test@example.com");
        testLead.setPhone("555-0123");
        testLead.setStatus("NEW");
        testLead.setAssignedTo(testUser);
        testLead.setPotentialValue(new BigDecimal("100000"));
        testLead.setLeadSource("Website Signup");
        testLead.setLeadScore(50);
        testLead.setCreatedDate(LocalDateTime.now());
        testLead.setUpdatedDate(LocalDateTime.now());

        leadRequest = new LeadRequest();
        leadRequest.setLeadName("New Lead");
        leadRequest.setCompany("New Company");
        leadRequest.setEmail("new@example.com");
        leadRequest.setPhone("555-9999");
        leadRequest.setPotentialValue(new BigDecimal("50000"));
        leadRequest.setLeadSource("Cold Call");
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllLeads_Success() throws Exception {
        // Arrange
        Page<Lead> leadPage = new PageImpl<>(Arrays.asList(testLead));
        when(leadService.findLeadsWithFilters(anyString(), any(User.class), anyString(), any(Pageable.class)))
                .thenReturn(leadPage);
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(get("/leads")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].leadName").value("Test Lead"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetLeadById_Success() throws Exception {
        // Arrange
        when(leadService.findById(1L)).thenReturn(testLead);

        // Act & Assert
        mockMvc.perform(get("/leads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.leadName").value("Test Lead"))
                .andExpect(jsonPath("$.data.company").value("Test Company"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateLead_Success() throws Exception {
        // Arrange
        when(leadService.createLead(any(Lead.class))).thenReturn(testLead);

        // Act & Assert
        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead created successfully"))
                .andExpect(jsonPath("$.data.leadName").value("Test Lead"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateLead_ValidationError() throws Exception {
        // Arrange
        LeadRequest invalidRequest = new LeadRequest();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateLead_Success() throws Exception {
        // Arrange
        when(leadService.updateLead(any(Lead.class))).thenReturn(testLead);

        // Act & Assert
        mockMvc.perform(put("/leads/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead updated successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetMyLeads_Success() throws Exception {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadService.findByAssignedTo(any(User.class))).thenReturn(leads);
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(get("/leads/my-leads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].leadName").value("Test Lead"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetNewLeads_Success() throws Exception {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadService.findNewLeadsForDistribution()).thenReturn(leads);

        // Act & Assert
        mockMvc.perform(get("/leads/new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetHighValueLeads_Success() throws Exception {
        // Arrange
        List<Lead> leads = Arrays.asList(testLead);
        when(leadService.findHighValueLeads()).thenReturn(leads);

        // Act & Assert
        mockMvc.perform(get("/leads/high-value"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "testmanager", roles = "SALES_MANAGER")
    void testDistributeLeads_Success() throws Exception {
        // Arrange
        when(leadDistributionService.distributeLeads()).thenReturn(5);
        when(userService.findByUsername("testmanager")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/leads/distribute")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Distributed 5 leads"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEscalateLead_Success() throws Exception {
        // Arrange
        when(workflowService.escalateLead(1L, testUser)).thenReturn(testLead);
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/leads/1/escalate")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead escalated successfully"));
    }

    @Test
    @WithMockUser(username = "testmanager", roles = "SALES_MANAGER")
    void testApproveLead_Success() throws Exception {
        // Arrange
        when(workflowService.approveLead(1L, testUser)).thenReturn(testLead);
        when(userService.findByUsername("testmanager")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/leads/1/approve")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead approved successfully"));
    }

    @Test
    @WithMockUser(username = "testmanager", roles = "SALES_MANAGER")
    void testRejectLead_Success() throws Exception {
        // Arrange
        when(workflowService.rejectLead(1L, testUser, "Not qualified")).thenReturn(testLead);
        when(userService.findByUsername("testmanager")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/leads/1/reject")
                .param("reason", "Not qualified")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead rejected successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testRequestApproval_Success() throws Exception {
        // Arrange
        when(workflowService.requestApproval(1L, testUser)).thenReturn(testLead);
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/leads/1/request-approval")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Approval requested successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateLeadStatus_Success() throws Exception {
        // Arrange
        when(leadService.updateLeadStatus(1L, "IN_PROGRESS", testUser)).thenReturn(testLead);
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(put("/leads/1/status")
                .param("status", "IN_PROGRESS")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead status updated successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testRecalculateLeadScore_Success() throws Exception {
        // Arrange
        when(leadService.recalculateLeadScore(1L)).thenReturn(testLead);

        // Act & Assert
        mockMvc.perform(post("/leads/1/recalculate-score")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lead score recalculated successfully"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetDistributionStats_Success() throws Exception {
        // Arrange
        LeadDistributionService.DistributionStats stats = 
                new LeadDistributionService.DistributionStats(3, 5, 12, 17);
        when(leadDistributionService.getDistributionStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/leads/distribution-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.activeSalesPersons").value(3))
                .andExpect(jsonPath("$.data.newLeadsCount").value(5))
                .andExpect(jsonPath("$.data.assignedLeadsCount").value(12))
                .andExpect(jsonPath("$.data.totalLeadsCount").value(17));
    }
}

