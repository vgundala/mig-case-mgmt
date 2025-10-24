package com.mig.sales.api.repository;

import com.mig.sales.api.model.entity.Lead;
import com.mig.sales.api.model.entity.LeadHistory;
import com.mig.sales.api.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for LeadHistory entity operations.
 */
@Repository
public interface LeadHistoryRepository extends JpaRepository<LeadHistory, Long> {

    /**
     * Find all history records for a lead.
     */
    List<LeadHistory> findByLead(Lead lead);

    /**
     * Find all history records for a lead with pagination.
     */
    Page<LeadHistory> findByLead(Lead lead, Pageable pageable);

    /**
     * Find all history records for a lead ordered by timestamp descending.
     */
    List<LeadHistory> findByLeadOrderByTimestampDesc(Lead lead);

    /**
     * Find all history records for a lead ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByLeadOrderByTimestampDesc(Lead lead, Pageable pageable);

    /**
     * Find all history records by user.
     */
    List<LeadHistory> findByUser(User user);

    /**
     * Find all history records by user with pagination.
     */
    Page<LeadHistory> findByUser(User user, Pageable pageable);

    /**
     * Find all history records by user ordered by timestamp descending.
     */
    List<LeadHistory> findByUserOrderByTimestampDesc(User user);

    /**
     * Find all history records by user ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByUserOrderByTimestampDesc(User user, Pageable pageable);

    /**
     * Find all history records by action.
     */
    List<LeadHistory> findByAction(String action);

    /**
     * Find all history records by action with pagination.
     */
    Page<LeadHistory> findByAction(String action, Pageable pageable);

    /**
     * Find all history records by action ordered by timestamp descending.
     */
    List<LeadHistory> findByActionOrderByTimestampDesc(String action);

    /**
     * Find all history records by action ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByActionOrderByTimestampDesc(String action, Pageable pageable);

    /**
     * Find all history records for a lead by action.
     */
    List<LeadHistory> findByLeadAndAction(Lead lead, String action);

    /**
     * Find all history records for a lead by action with pagination.
     */
    Page<LeadHistory> findByLeadAndAction(Lead lead, String action, Pageable pageable);

    /**
     * Find all history records for a lead by action ordered by timestamp descending.
     */
    List<LeadHistory> findByLeadAndActionOrderByTimestampDesc(Lead lead, String action);

    /**
     * Find all history records for a lead by action ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByLeadAndActionOrderByTimestampDesc(Lead lead, String action, Pageable pageable);

    /**
     * Find all history records by user and action.
     */
    List<LeadHistory> findByUserAndAction(User user, String action);

    /**
     * Find all history records by user and action with pagination.
     */
    Page<LeadHistory> findByUserAndAction(User user, String action, Pageable pageable);

    /**
     * Find all history records by user and action ordered by timestamp descending.
     */
    List<LeadHistory> findByUserAndActionOrderByTimestampDesc(User user, String action);

    /**
     * Find all history records by user and action ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByUserAndActionOrderByTimestampDesc(User user, String action, Pageable pageable);

    /**
     * Find all history records created between dates.
     */
    List<LeadHistory> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all history records created between dates with pagination.
     */
    Page<LeadHistory> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all history records created between dates ordered by timestamp descending.
     */
    List<LeadHistory> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all history records created between dates ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all history records for a lead created between dates.
     */
    List<LeadHistory> findByLeadAndTimestampBetween(Lead lead, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all history records for a lead created between dates with pagination.
     */
    Page<LeadHistory> findByLeadAndTimestampBetween(Lead lead, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all history records for a lead created between dates ordered by timestamp descending.
     */
    List<LeadHistory> findByLeadAndTimestampBetweenOrderByTimestampDesc(Lead lead, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all history records for a lead created between dates ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByLeadAndTimestampBetweenOrderByTimestampDesc(Lead lead, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all history records by user created between dates.
     */
    List<LeadHistory> findByUserAndTimestampBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all history records by user created between dates with pagination.
     */
    Page<LeadHistory> findByUserAndTimestampBetween(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all history records by user created between dates ordered by timestamp descending.
     */
    List<LeadHistory> findByUserAndTimestampBetweenOrderByTimestampDesc(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all history records by user created between dates ordered by timestamp descending with pagination.
     */
    Page<LeadHistory> findByUserAndTimestampBetweenOrderByTimestampDesc(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Search history records by comment text.
     */
    @Query("SELECT h FROM LeadHistory h WHERE LOWER(h.commentText) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<LeadHistory> searchByCommentText(@Param("searchTerm") String searchTerm);

    /**
     * Search history records by comment text with pagination.
     */
    @Query("SELECT h FROM LeadHistory h WHERE LOWER(h.commentText) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<LeadHistory> searchByCommentText(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Search history records by comment text ordered by timestamp descending.
     */
    @Query("SELECT h FROM LeadHistory h WHERE LOWER(h.commentText) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY h.timestamp DESC")
    List<LeadHistory> searchByCommentTextOrderByTimestampDesc(@Param("searchTerm") String searchTerm);

    /**
     * Search history records by comment text ordered by timestamp descending with pagination.
     */
    @Query("SELECT h FROM LeadHistory h WHERE LOWER(h.commentText) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY h.timestamp DESC")
    Page<LeadHistory> searchByCommentTextOrderByTimestampDesc(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Count history records for a lead.
     */
    long countByLead(Lead lead);

    /**
     * Count history records by user.
     */
    long countByUser(User user);

    /**
     * Count history records by action.
     */
    long countByAction(String action);

    /**
     * Count history records for a lead by action.
     */
    long countByLeadAndAction(Lead lead, String action);

    /**
     * Count history records by user and action.
     */
    long countByUserAndAction(User user, String action);

    /**
     * Count history records created between dates.
     */
    long countByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count history records for a lead created between dates.
     */
    long countByLeadAndTimestampBetween(Lead lead, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count history records by user created between dates.
     */
    long countByUserAndTimestampBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find the latest history record for a lead.
     */
    @Query("SELECT h FROM LeadHistory h WHERE h.lead = :lead ORDER BY h.timestamp DESC")
    List<LeadHistory> findLatestByLead(@Param("lead") Lead lead, Pageable pageable);

    /**
     * Find the latest history record for a lead by action.
     */
    @Query("SELECT h FROM LeadHistory h WHERE h.lead = :lead AND h.action = :action ORDER BY h.timestamp DESC")
    List<LeadHistory> findLatestByLeadAndAction(@Param("lead") Lead lead, @Param("action") String action, Pageable pageable);
}
