package com.mig.sales.leadmanagement.repository;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Lead entity
 * Provides data access methods for lead operations
 */
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    /**
     * Find leads by status
     * @param status the status to search for
     * @return List of leads with the specified status
     */
    List<Lead> findByStatus(String status);

    /**
     * Find leads by assigned user
     * @param assignedTo the user the lead is assigned to
     * @return List of leads assigned to the user
     */
    List<Lead> findByAssignedTo(User assignedTo);

    /**
     * Find leads by assigned user and status
     * @param assignedTo the user the lead is assigned to
     * @param status the status to search for
     * @return List of leads assigned to the user with the specified status
     */
    List<Lead> findByAssignedToAndStatus(User assignedTo, String status);

    /**
     * Find leads by status with pagination
     * @param status the status to search for
     * @param pageable pagination information
     * @return Page of leads with the specified status
     */
    Page<Lead> findByStatus(String status, Pageable pageable);

    /**
     * Find leads by assigned user with pagination
     * @param assignedTo the user the lead is assigned to
     * @param pageable pagination information
     * @return Page of leads assigned to the user
     */
    Page<Lead> findByAssignedTo(User assignedTo, Pageable pageable);

    /**
     * Find leads by potential value greater than or equal to threshold
     * @param threshold the minimum potential value
     * @return List of leads with potential value >= threshold
     */
    List<Lead> findByPotentialValueGreaterThanEqual(BigDecimal threshold);

    /**
     * Find leads by lead source
     * @param leadSource the lead source to search for
     * @return List of leads with the specified source
     */
    List<Lead> findByLeadSource(String leadSource);

    /**
     * Find leads ordered by lead score descending
     * @return List of leads ordered by score (highest first)
     */
    @Query("SELECT l FROM Lead l ORDER BY l.leadScore DESC, l.createdDate DESC")
    List<Lead> findAllOrderByLeadScoreDesc();

    /**
     * Find new leads for distribution
     * @return List of leads with status 'NEW'
     */
    @Query("SELECT l FROM Lead l WHERE l.status = 'NEW' ORDER BY l.leadScore DESC, l.createdDate ASC")
    List<Lead> findNewLeadsForDistribution();

    /**
     * Find leads assigned to user with specific statuses
     * @param assignedTo the user the lead is assigned to
     * @param statuses list of statuses to search for
     * @return List of leads assigned to the user with any of the specified statuses
     */
    @Query("SELECT l FROM Lead l WHERE l.assignedTo = :assignedTo AND l.status IN :statuses ORDER BY l.leadScore DESC, l.createdDate DESC")
    List<Lead> findByAssignedToAndStatusIn(@Param("assignedTo") User assignedTo, @Param("statuses") List<String> statuses);

    /**
     * Find high-value leads (potential value >= $1M)
     * @param threshold the minimum potential value (1000000)
     * @return List of high-value leads
     */
    @Query("SELECT l FROM Lead l WHERE l.potentialValue >= :threshold ORDER BY l.potentialValue DESC, l.leadScore DESC")
    List<Lead> findHighValueLeads(@Param("threshold") BigDecimal threshold);

    /**
     * Count leads by status
     * @param status the status to count
     * @return number of leads with the specified status
     */
    long countByStatus(String status);

    /**
     * Count leads by assigned user
     * @param assignedTo the user the lead is assigned to
     * @return number of leads assigned to the user
     */
    long countByAssignedTo(User assignedTo);

    /**
     * Find leads with pagination and filtering
     * @param status optional status filter
     * @param assignedTo optional assigned user filter
     * @param leadSource optional lead source filter
     * @param pageable pagination information
     * @return Page of leads matching the criteria
     */
    @Query("SELECT l FROM Lead l WHERE " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:assignedTo IS NULL OR l.assignedTo = :assignedTo) AND " +
           "(:leadSource IS NULL OR l.leadSource = :leadSource) " +
           "ORDER BY l.leadScore DESC, l.createdDate DESC")
    Page<Lead> findLeadsWithFilters(@Param("status") String status,
                                   @Param("assignedTo") User assignedTo,
                                   @Param("leadSource") String leadSource,
                                   Pageable pageable);
}

