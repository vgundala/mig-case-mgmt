package com.mig.sales.leadmanagement.repository;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.LeadHistory;
import com.mig.sales.leadmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for LeadHistory entity
 * Provides data access methods for lead history operations
 */
@Repository
public interface LeadHistoryRepository extends JpaRepository<LeadHistory, Long> {

    /**
     * Find lead history by lead
     * @param lead the lead to get history for
     * @return List of lead history records ordered by timestamp descending
     */
    List<LeadHistory> findByLeadOrderByTimestampDesc(Lead lead);

    /**
     * Find lead history by lead with pagination
     * @param lead the lead to get history for
     * @param pageable pagination information
     * @return Page of lead history records ordered by timestamp descending
     */
    Page<LeadHistory> findByLeadOrderByTimestampDesc(Lead lead, Pageable pageable);

    /**
     * Find lead history by user
     * @param user the user to get history for
     * @return List of lead history records ordered by timestamp descending
     */
    List<LeadHistory> findByUserOrderByTimestampDesc(User user);

    /**
     * Find lead history by lead and user
     * @param lead the lead to get history for
     * @param user the user to get history for
     * @return List of lead history records ordered by timestamp descending
     */
    List<LeadHistory> findByLeadAndUserOrderByTimestampDesc(Lead lead, User user);

    /**
     * Find lead history by action
     * @param action the action to search for
     * @return List of lead history records with the specified action
     */
    List<LeadHistory> findByAction(String action);

    /**
     * Find lead history by action type
     * @param actionType the action type to search for
     * @return List of lead history records with the specified action type
     */
    List<LeadHistory> findByActionType(String actionType);

    /**
     * Find lead history by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return List of lead history records within the date range
     */
    List<LeadHistory> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find lead history by lead and date range
     * @param lead the lead to get history for
     * @param startDate the start date
     * @param endDate the end date
     * @return List of lead history records for the lead within the date range
     */
    List<LeadHistory> findByLeadAndTimestampBetween(Lead lead, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find recent lead history for a lead (last N records)
     * @param lead the lead to get history for
     * @param limit the maximum number of records to return
     * @return List of recent lead history records
     */
    @Query("SELECT h FROM LeadHistory h WHERE h.lead = :lead ORDER BY h.timestamp DESC")
    List<LeadHistory> findRecentLeadHistory(@Param("lead") Lead lead, Pageable pageable);

    /**
     * Count lead history records by lead
     * @param lead the lead to count history for
     * @return number of history records for the lead
     */
    long countByLead(Lead lead);

    /**
     * Find lead history by lead and action
     * @param lead the lead to get history for
     * @param action the action to search for
     * @return List of lead history records with the specified action
     */
    List<LeadHistory> findByLeadAndAction(Lead lead, String action);

    /**
     * Find lead history by lead and action type
     * @param lead the lead to get history for
     * @param actionType the action type to search for
     * @return List of lead history records with the specified action type
     */
    List<LeadHistory> findByLeadAndActionType(Lead lead, String actionType);

    /**
     * Find lead history with comments only
     * @param lead the lead to get history for
     * @return List of lead history records that have comments
     */
    @Query("SELECT h FROM LeadHistory h WHERE h.lead = :lead AND h.commentText IS NOT NULL AND h.commentText != '' ORDER BY h.timestamp DESC")
    List<LeadHistory> findLeadHistoryWithComments(@Param("lead") Lead lead);
}

