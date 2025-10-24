package com.mig.sales.api.controller;

import com.mig.sales.api.model.dto.LeadDTO;
import com.mig.sales.api.model.dto.UserDTO;
import com.mig.sales.api.model.entity.LeadStatus;
import com.mig.sales.api.model.entity.UserRole;
import com.mig.sales.api.service.LeadService;
import com.mig.sales.api.service.UserService;
import com.mig.sales.api.service.LeadDistributionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Dashboard operations.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "Dashboard and analytics APIs")
public class DashboardController {

    private final LeadService leadService;
    private final UserService userService;
    private final LeadDistributionService leadDistributionService;

    @GetMapping("/overview")
    @Operation(summary = "Get dashboard overview", description = "Get comprehensive dashboard statistics and data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<DashboardOverviewResponse> getDashboardOverview() {
        log.debug("Getting dashboard overview");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        DashboardOverviewResponse overview = DashboardOverviewResponse.builder()
                .leadStats(LeadStats.builder()
                        .totalLeads(leadService.getTotalLeadCount())
                        .newLeads(leadService.getLeadCountByStatus(LeadStatus.NEW))
                        .assignedLeads(leadService.getLeadCountByStatus(LeadStatus.ASSIGNED))
                        .inProgressLeads(leadService.getLeadCountByStatus(LeadStatus.IN_PROGRESS))
                        .convertedLeads(leadService.getLeadCountByStatus(LeadStatus.CONVERTED))
                        .rejectedLeads(leadService.getLeadCountByStatus(LeadStatus.REJECTED))
                        .build())
                .userStats(UserStats.builder()
                        .totalUsers(userService.getTotalActiveUserCount())
                        .salesPeople(userService.getActiveUserCountByRole(UserRole.SALES_PERSON))
                        .salesManagers(userService.getActiveUserCountByRole(UserRole.SALES_MANAGER))
                        .build())
                .distributionStats(leadDistributionService.getDistributionStats())
                .build();
        
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/my-leads")
    @Operation(summary = "Get my leads", description = "Get leads assigned to the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getMyLeads(
            @Parameter(description = "Lead status filter") @RequestParam(required = false) LeadStatus status,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting my leads with status filter: {}", status);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Get current user ID (this would need to be implemented based on your user lookup)
        // For now, we'll use a placeholder - in real implementation, you'd get the user ID from the authenticated user
        Long userId = getCurrentUserId(username);
        
        Page<LeadDTO> leads;
        if (status != null) {
            leads = leadService.getLeadsByStatusAndAssignedUser(status, userId, pageable);
        } else {
            leads = leadService.getLeadsByAssignedUser(userId, pageable);
        }
        
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/recent-leads")
    @Operation(summary = "Get recent leads", description = "Get recently created leads")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recent leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getRecentLeads(
            @Parameter(description = "Number of days to look back") @RequestParam(defaultValue = "7") int days,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting recent leads for last {} days", days);
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        LocalDateTime endDate = LocalDateTime.now();
        
        Page<LeadDTO> leads = leadService.getLeadsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/high-value-leads")
    @Operation(summary = "Get high-value leads", description = "Get leads with high potential value")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "High-value leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getHighValueLeads(
            @Parameter(description = "Value threshold") @RequestParam(defaultValue = "1000000") BigDecimal threshold,
            @PageableDefault(size = 10, sort = "potentialValue", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting high-value leads with threshold: {}", threshold);
        
        Page<LeadDTO> leads = leadService.getHighValueLeads(threshold, pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/unassigned-leads")
    @Operation(summary = "Get unassigned leads", description = "Get leads that are not yet assigned to anyone")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unassigned leads retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<LeadDTO>> getUnassignedLeads(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        
        log.debug("Getting unassigned leads");
        
        Page<LeadDTO> leads = leadService.getUnassignedLeads(pageable);
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/sales-people")
    @Operation(summary = "Get sales people", description = "Get all active sales people for assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sales people retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<UserDTO>> getSalesPeople() {
        log.debug("Getting all sales people");
        
        List<UserDTO> salesPeople = userService.getSalesPeople();
        return ResponseEntity.ok(salesPeople);
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get analytics data", description = "Get detailed analytics and metrics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analytics data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @Parameter(description = "Start date (ISO format)") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)") @RequestParam(required = false) LocalDateTime endDate) {
        
        log.debug("Getting analytics data for date range: {} - {}", startDate, endDate);
        
        // Default to last 30 days if no date range provided
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        AnalyticsResponse analytics = AnalyticsResponse.builder()
                .period(Period.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build())
                .leadMetrics(LeadMetrics.builder()
                        .totalCreated(leadService.getLeadCountByDateRange(startDate, endDate))
                        .converted(leadService.getLeadCountByStatusAndDateRange(LeadStatus.CONVERTED, startDate, endDate))
                        .rejected(leadService.getLeadCountByStatusAndDateRange(LeadStatus.REJECTED, startDate, endDate))
                        .build())
                .build();
        
        return ResponseEntity.ok(analytics);
    }

    /**
     * Get current user ID from username (placeholder implementation).
     * In a real implementation, you would inject a service to get the user ID.
     */
    private Long getCurrentUserId(String username) {
        // This is a placeholder - in real implementation, you'd have a service to get user ID by username
        return 1L; // Placeholder
    }

    /**
     * Response DTO for dashboard overview.
     */
    public static class DashboardOverviewResponse {
        private LeadStats leadStats;
        private UserStats userStats;
        private LeadDistributionService.DistributionStats distributionStats;

        public static DashboardOverviewResponseBuilder builder() {
            return new DashboardOverviewResponseBuilder();
        }

        public static class DashboardOverviewResponseBuilder {
            private LeadStats leadStats;
            private UserStats userStats;
            private LeadDistributionService.DistributionStats distributionStats;

            public DashboardOverviewResponseBuilder leadStats(LeadStats leadStats) {
                this.leadStats = leadStats;
                return this;
            }

            public DashboardOverviewResponseBuilder userStats(UserStats userStats) {
                this.userStats = userStats;
                return this;
            }

            public DashboardOverviewResponseBuilder distributionStats(LeadDistributionService.DistributionStats distributionStats) {
                this.distributionStats = distributionStats;
                return this;
            }

            public DashboardOverviewResponse build() {
                DashboardOverviewResponse response = new DashboardOverviewResponse();
                response.leadStats = this.leadStats;
                response.userStats = this.userStats;
                response.distributionStats = this.distributionStats;
                return response;
            }
        }

        // Getters and setters
        public LeadStats getLeadStats() { return leadStats; }
        public void setLeadStats(LeadStats leadStats) { this.leadStats = leadStats; }
        public UserStats getUserStats() { return userStats; }
        public void setUserStats(UserStats userStats) { this.userStats = userStats; }
        public LeadDistributionService.DistributionStats getDistributionStats() { return distributionStats; }
        public void setDistributionStats(LeadDistributionService.DistributionStats distributionStats) { this.distributionStats = distributionStats; }
    }

    /**
     * Lead statistics DTO.
     */
    public static class LeadStats {
        private long totalLeads;
        private long newLeads;
        private long assignedLeads;
        private long inProgressLeads;
        private long convertedLeads;
        private long rejectedLeads;

        public static LeadStatsBuilder builder() {
            return new LeadStatsBuilder();
        }

        public static class LeadStatsBuilder {
            private long totalLeads;
            private long newLeads;
            private long assignedLeads;
            private long inProgressLeads;
            private long convertedLeads;
            private long rejectedLeads;

            public LeadStatsBuilder totalLeads(long totalLeads) {
                this.totalLeads = totalLeads;
                return this;
            }

            public LeadStatsBuilder newLeads(long newLeads) {
                this.newLeads = newLeads;
                return this;
            }

            public LeadStatsBuilder assignedLeads(long assignedLeads) {
                this.assignedLeads = assignedLeads;
                return this;
            }

            public LeadStatsBuilder inProgressLeads(long inProgressLeads) {
                this.inProgressLeads = inProgressLeads;
                return this;
            }

            public LeadStatsBuilder convertedLeads(long convertedLeads) {
                this.convertedLeads = convertedLeads;
                return this;
            }

            public LeadStatsBuilder rejectedLeads(long rejectedLeads) {
                this.rejectedLeads = rejectedLeads;
                return this;
            }

            public LeadStats build() {
                LeadStats stats = new LeadStats();
                stats.totalLeads = this.totalLeads;
                stats.newLeads = this.newLeads;
                stats.assignedLeads = this.assignedLeads;
                stats.inProgressLeads = this.inProgressLeads;
                stats.convertedLeads = this.convertedLeads;
                stats.rejectedLeads = this.rejectedLeads;
                return stats;
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

    /**
     * User statistics DTO.
     */
    public static class UserStats {
        private long totalUsers;
        private long salesPeople;
        private long salesManagers;

        public static UserStatsBuilder builder() {
            return new UserStatsBuilder();
        }

        public static class UserStatsBuilder {
            private long totalUsers;
            private long salesPeople;
            private long salesManagers;

            public UserStatsBuilder totalUsers(long totalUsers) {
                this.totalUsers = totalUsers;
                return this;
            }

            public UserStatsBuilder salesPeople(long salesPeople) {
                this.salesPeople = salesPeople;
                return this;
            }

            public UserStatsBuilder salesManagers(long salesManagers) {
                this.salesManagers = salesManagers;
                return this;
            }

            public UserStats build() {
                UserStats stats = new UserStats();
                stats.totalUsers = this.totalUsers;
                stats.salesPeople = this.salesPeople;
                stats.salesManagers = this.salesManagers;
                return stats;
            }
        }

        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getSalesPeople() { return salesPeople; }
        public void setSalesPeople(long salesPeople) { this.salesPeople = salesPeople; }
        public long getSalesManagers() { return salesManagers; }
        public void setSalesManagers(long salesManagers) { this.salesManagers = salesManagers; }
    }

    /**
     * Analytics response DTO.
     */
    public static class AnalyticsResponse {
        private Period period;
        private LeadMetrics leadMetrics;

        public static AnalyticsResponseBuilder builder() {
            return new AnalyticsResponseBuilder();
        }

        public static class AnalyticsResponseBuilder {
            private Period period;
            private LeadMetrics leadMetrics;

            public AnalyticsResponseBuilder period(Period period) {
                this.period = period;
                return this;
            }

            public AnalyticsResponseBuilder leadMetrics(LeadMetrics leadMetrics) {
                this.leadMetrics = leadMetrics;
                return this;
            }

            public AnalyticsResponse build() {
                AnalyticsResponse response = new AnalyticsResponse();
                response.period = this.period;
                response.leadMetrics = this.leadMetrics;
                return response;
            }
        }

        // Getters and setters
        public Period getPeriod() { return period; }
        public void setPeriod(Period period) { this.period = period; }
        public LeadMetrics getLeadMetrics() { return leadMetrics; }
        public void setLeadMetrics(LeadMetrics leadMetrics) { this.leadMetrics = leadMetrics; }
    }

    /**
     * Period DTO.
     */
    public static class Period {
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        public static PeriodBuilder builder() {
            return new PeriodBuilder();
        }

        public static class PeriodBuilder {
            private LocalDateTime startDate;
            private LocalDateTime endDate;

            public PeriodBuilder startDate(LocalDateTime startDate) {
                this.startDate = startDate;
                return this;
            }

            public PeriodBuilder endDate(LocalDateTime endDate) {
                this.endDate = endDate;
                return this;
            }

            public Period build() {
                Period period = new Period();
                period.startDate = this.startDate;
                period.endDate = this.endDate;
                return period;
            }
        }

        // Getters and setters
        public LocalDateTime getStartDate() { return startDate; }
        public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
        public LocalDateTime getEndDate() { return endDate; }
        public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    }

    /**
     * Lead metrics DTO.
     */
    public static class LeadMetrics {
        private long totalCreated;
        private long converted;
        private long rejected;

        public static LeadMetricsBuilder builder() {
            return new LeadMetricsBuilder();
        }

        public static class LeadMetricsBuilder {
            private long totalCreated;
            private long converted;
            private long rejected;

            public LeadMetricsBuilder totalCreated(long totalCreated) {
                this.totalCreated = totalCreated;
                return this;
            }

            public LeadMetricsBuilder converted(long converted) {
                this.converted = converted;
                return this;
            }

            public LeadMetricsBuilder rejected(long rejected) {
                this.rejected = rejected;
                return this;
            }

            public LeadMetrics build() {
                LeadMetrics metrics = new LeadMetrics();
                metrics.totalCreated = this.totalCreated;
                metrics.converted = this.converted;
                metrics.rejected = this.rejected;
                return metrics;
            }
        }

        // Getters and setters
        public long getTotalCreated() { return totalCreated; }
        public void setTotalCreated(long totalCreated) { this.totalCreated = totalCreated; }
        public long getConverted() { return converted; }
        public void setConverted(long converted) { this.converted = converted; }
        public long getRejected() { return rejected; }
        public void setRejected(long rejected) { this.rejected = rejected; }
    }
}
