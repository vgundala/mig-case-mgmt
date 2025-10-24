package com.mig.sales.api.repository;

import com.mig.sales.api.model.entity.Lead;
import com.mig.sales.api.model.entity.LeadStatus;
import com.mig.sales.api.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Lead entity operations.
 */
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    /**
     * Find leads by status.
     */
    List<Lead> findByStatus(LeadStatus status);

    /**
     * Find leads by status with pagination.
     */
    Page<Lead> findByStatus(LeadStatus status, Pageable pageable);

    /**
     * Find leads by assigned user.
     */
    List<Lead> findByAssignedTo(User assignedTo);

    /**
     * Find leads by assigned user with pagination.
     */
    Page<Lead> findByAssignedTo(User assignedTo, Pageable pageable);

    /**
     * Find leads by status and assigned user.
     */
    List<Lead> findByStatusAndAssignedTo(LeadStatus status, User assignedTo);

    /**
     * Find leads by status and assigned user with pagination.
     */
    Page<Lead> findByStatusAndAssignedTo(LeadStatus status, User assignedTo, Pageable pageable);

    /**
     * Find leads by potential value range.
     */
    @Query("SELECT l FROM Lead l WHERE l.potentialValue BETWEEN :minValue AND :maxValue")
    List<Lead> findByPotentialValueBetween(@Param("minValue") BigDecimal minValue, 
                                          @Param("maxValue") BigDecimal maxValue);

    /**
     * Find leads by potential value range with pagination.
     */
    @Query("SELECT l FROM Lead l WHERE l.potentialValue BETWEEN :minValue AND :maxValue")
    Page<Lead> findByPotentialValueBetween(@Param("minValue") BigDecimal minValue, 
                                         @Param("maxValue") BigDecimal maxValue, 
                                         Pageable pageable);

    /**
     * Find leads created between dates.
     */
    List<Lead> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find leads created between dates with pagination.
     */
    Page<Lead> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find leads by status and created date range.
     */
    @Query("SELECT l FROM Lead l WHERE l.status = :status AND l.createdDate BETWEEN :startDate AND :endDate")
    List<Lead> findByStatusAndCreatedDateBetween(@Param("status") LeadStatus status,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * Find leads by status and created date range with pagination.
     */
    @Query("SELECT l FROM Lead l WHERE l.status = :status AND l.createdDate BETWEEN :startDate AND :endDate")
    Page<Lead> findByStatusAndCreatedDateBetween(@Param("status") LeadStatus status,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                Pageable pageable);

    /**
     * Search leads by name, company, email, or phone.
     */
    @Query("SELECT l FROM Lead l WHERE " +
           "LOWER(l.leadName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.company) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Lead> searchLeads(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Search leads by name, company, email, or phone with status filter.
     */
    @Query("SELECT l FROM Lead l WHERE l.status = :status AND (" +
           "LOWER(l.leadName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.company) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Lead> searchLeadsByStatus(@Param("searchTerm") String searchTerm, 
                                  @Param("status") LeadStatus status, 
                                  Pageable pageable);

    /**
     * Find leads by Pega workflow ID.
     */
    Optional<Lead> findByPegaWorkflowId(String pegaWorkflowId);

    /**
     * Find leads by Pega case ID.
     */
    Optional<Lead> findByPegaCaseId(String pegaCaseId);

    /**
     * Find leads by Pega status.
     */
    List<Lead> findByPegaStatus(String pegaStatus);

    /**
     * Find leads by Pega status with pagination.
     */
    Page<Lead> findByPegaStatus(String pegaStatus, Pageable pageable);

    /**
     * Find leads that need Pega sync (have Pega workflow but not synced recently).
     */
    @Query("SELECT l FROM Lead l WHERE l.pegaWorkflowId IS NOT NULL AND " +
           "(l.pegaLastSyncDate IS NULL OR l.pegaLastSyncDate < :syncThreshold)")
    List<Lead> findLeadsNeedingPegaSync(@Param("syncThreshold") LocalDateTime syncThreshold);

    /**
     * Count leads by status.
     */
    long countByStatus(LeadStatus status);

    /**
     * Count leads by assigned user.
     */
    long countByAssignedTo(User assignedTo);

    /**
     * Count leads by status and assigned user.
     */
    long countByStatusAndAssignedTo(LeadStatus status, User assignedTo);

    /**
     * Count leads by potential value range.
     */
    @Query("SELECT COUNT(l) FROM Lead l WHERE l.potentialValue BETWEEN :minValue AND :maxValue")
    long countByPotentialValueBetween(@Param("minValue") BigDecimal minValue, 
                                     @Param("maxValue") BigDecimal maxValue);

    /**
     * Count leads created between dates.
     */
    long countByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count leads by status and created date range.
     */
    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = :status AND l.createdDate BETWEEN :startDate AND :endDate")
    long countByStatusAndCreatedDateBetween(@Param("status") LeadStatus status,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Find high-value leads (potential value > 1M).
     */
    @Query("SELECT l FROM Lead l WHERE l.potentialValue > :threshold")
    List<Lead> findHighValueLeads(@Param("threshold") BigDecimal threshold);

    /**
     * Find high-value leads with pagination.
     */
    @Query("SELECT l FROM Lead l WHERE l.potentialValue > :threshold")
    Page<Lead> findHighValueLeads(@Param("threshold") BigDecimal threshold, Pageable pageable);

    /**
     * Find unassigned leads.
     */
    List<Lead> findByAssignedToIsNull();

    /**
     * Find unassigned leads with pagination.
     */
    Page<Lead> findByAssignedToIsNull(Pageable pageable);

    /**
     * Find unassigned leads by status.
     */
    List<Lead> findByStatusAndAssignedToIsNull(LeadStatus status);

    /**
     * Find unassigned leads by status with pagination.
     */
    Page<Lead> findByStatusAndAssignedToIsNull(LeadStatus status, Pageable pageable);
}
