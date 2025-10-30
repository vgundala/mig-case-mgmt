package com.mig.sales.leadmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mig.sales.leadmanagement.dto.LoginRequest;
import com.mig.sales.leadmanagement.dto.LeadRequest;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Lead Management Service
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class LeadManagementServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole("SALES_PERSON");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("555-0123");
        testUser.setIsActive(true);
        userRepository.save(testUser);

        // Login to get auth token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        authToken = objectMapper.readTree(response)
                .get("data")
                .get("token")
                .asText();
    }

    @Test
    void testCompleteLeadWorkflow() throws Exception {
        // 1. Create a new lead
        LeadRequest leadRequest = new LeadRequest();
        leadRequest.setLeadName("Integration Test Lead");
        leadRequest.setCompany("Test Company");
        leadRequest.setEmail("test@testcompany.com");
        leadRequest.setPhone("555-9999");
        leadRequest.setPotentialValue(new BigDecimal("100000"));
        leadRequest.setLeadSource("Website Signup");

        MvcResult createResult = mockMvc.perform(post("/leads")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.leadName").value("Integration Test Lead"))
                .andReturn();

        // Extract lead ID from response
        String createResponse = createResult.getResponse().getContentAsString();
        Long leadId = objectMapper.readTree(createResponse)
                .get("data")
                .get("id")
                .asLong();

        // 2. Get the created lead
        mockMvc.perform(get("/leads/" + leadId)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.leadName").value("Integration Test Lead"));

        // 3. Update lead status
        mockMvc.perform(put("/leads/" + leadId + "/status")
                .header("Authorization", "Bearer " + authToken)
                .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 4. Add a comment
        mockMvc.perform(post("/leads/" + leadId + "/comments")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"commentText\": \"Integration test comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 5. Get lead history
        mockMvc.perform(get("/leads/" + leadId + "/history")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        // 6. Recalculate lead score
        mockMvc.perform(post("/leads/" + leadId + "/recalculate-score")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 7. Get my leads
        mockMvc.perform(get("/leads/my-leads")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testAuthenticationFlow() throws Exception {
        // Test login with valid credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.type").value("Bearer"))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        // Test login with invalid credentials
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // Test protected endpoint without token
        mockMvc.perform(get("/leads"))
                .andExpect(status().isUnauthorized());

        // Test protected endpoint with invalid token
        mockMvc.perform(get("/leads")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLeadCRUDOperations() throws Exception {
        // Create lead
        LeadRequest leadRequest = new LeadRequest();
        leadRequest.setLeadName("CRUD Test Lead");
        leadRequest.setCompany("CRUD Company");
        leadRequest.setEmail("crud@testcompany.com");
        leadRequest.setPhone("555-1111");
        leadRequest.setPotentialValue(new BigDecimal("75000"));
        leadRequest.setLeadSource("Cold Call");

        MvcResult createResult = mockMvc.perform(post("/leads")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Long leadId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("data")
                .get("id")
                .asLong();

        // Read lead
        mockMvc.perform(get("/leads/" + leadId)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.leadName").value("CRUD Test Lead"));

        // Update lead
        leadRequest.setLeadName("Updated CRUD Lead");
        leadRequest.setCompany("Updated CRUD Company");

        mockMvc.perform(put("/leads/" + leadId)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.leadName").value("Updated CRUD Lead"));

        // Verify update
        mockMvc.perform(get("/leads/" + leadId)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.leadName").value("Updated CRUD Lead"));
    }

    @Test
    void testErrorHandling() throws Exception {
        // Test 404 for non-existent lead
        mockMvc.perform(get("/leads/99999")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        // Test validation error for invalid lead data
        LeadRequest invalidRequest = new LeadRequest();
        // Missing required fields

        mockMvc.perform(post("/leads")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Test unauthorized access
        mockMvc.perform(get("/leads"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPaginationAndFiltering() throws Exception {
        // Create multiple leads for testing
        for (int i = 1; i <= 5; i++) {
            LeadRequest leadRequest = new LeadRequest();
            leadRequest.setLeadName("Test Lead " + i);
            leadRequest.setCompany("Test Company " + i);
            leadRequest.setEmail("test" + i + "@testcompany.com");
            leadRequest.setPhone("555-000" + i);
            leadRequest.setPotentialValue(new BigDecimal(50000 + i * 10000));
            leadRequest.setLeadSource("Website Signup");

            mockMvc.perform(post("/leads")
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(leadRequest)))
                    .andExpect(status().isOk());
        }

        // Test pagination
        mockMvc.perform(get("/leads")
                .header("Authorization", "Bearer " + authToken)
                .param("page", "0")
                .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.size").value(3));

        // Test filtering by status
        mockMvc.perform(get("/leads")
                .header("Authorization", "Bearer " + authToken)
                .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }
}

