package com.mig.sales.leadmanagement.service;

import com.mig.sales.leadmanagement.entity.Lead;
import com.mig.sales.leadmanagement.entity.LeadHistory;
import com.mig.sales.leadmanagement.entity.User;
import com.mig.sales.leadmanagement.repository.LeadHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for lead history operations
 */
@Service
@Transactional
public class LeadHistoryService {

    @Autowired
    private LeadHistoryRepository leadHistoryRepository;

    /**
     * Log activity for a lead
     * @param lead the lead
     * @param user the user performing the action
     * @param commentText comment text
     * @param action action description
     * @param actionType type of action (SYSTEM, USER_ACTION, WORKFLOW)
     * @param oldStatus previous status
     * @param newStatus new status
     * @return logged activity
     */
    public LeadHistory logActivity(Lead lead, User user, String commentText, String action, 
                                 String actionType, String oldStatus, String newStatus) {
        LeadHistory history = new LeadHistory();
        history.setLead(lead);
        history.setUser(user);
        history.setCommentText(commentText);
        history.setAction(action);
        history.setActionType(actionType);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setTimestamp(LocalDateTime.now());
        
        return leadHistoryRepository.save(history);
    }

    /**
     * Add comment to lead
     * @param lead the lead
     * @param user the user adding the comment
     * @param commentText comment text
     * @param action action description
     * @return logged comment
     */
    public LeadHistory addComment(Lead lead, User user, String commentText, String action) {
        return logActivity(lead, user, commentText, action, "USER_ACTION", null, null);
    }

    /**
     * Find lead history by lead
     * @param lead the lead
     * @return list of lead history records
     */
    public List<LeadHistory> findByLead(Lead lead) {
        return leadHistoryRepository.findByLeadOrderByTimestampDesc(lead);
    }

    /**
     * Find lead history by lead with pagination
     * @param lead the lead
     * @param pageable pagination information
     * @return page of lead history records
     */
    public Page<LeadHistory> findByLead(Lead lead, Pageable pageable) {
        return leadHistoryRepository.findByLeadOrderByTimestampDesc(lead, pageable);
    }

    /**
     * Find lead history by user
     * @param user the user
     * @return list of lead history records
     */
    public List<LeadHistory> findByUser(User user) {
        return leadHistoryRepository.findByUserOrderByTimestampDesc(user);
    }

    /**
     * Find lead history by lead and user
     * @param lead the lead
     * @param user the user
     * @return list of lead history records
     */
    public List<LeadHistory> findByLeadAndUser(Lead lead, User user) {
        return leadHistoryRepository.findByLeadAndUserOrderByTimestampDesc(lead, user);
    }

    /**
     * Find lead history by action
     * @param action the action
     * @return list of lead history records
     */
    public List<LeadHistory> findByAction(String action) {
        return leadHistoryRepository.findByAction(action);
    }

    /**
     * Find lead history by action type
     * @param actionType the action type
     * @return list of lead history records
     */
    public List<LeadHistory> findByActionType(String actionType) {
        return leadHistoryRepository.findByActionType(actionType);
    }

    /**
     * Find lead history by date range
     * @param startDate start date
     * @param endDate end date
     * @return list of lead history records within the date range
     */
    public List<LeadHistory> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return leadHistoryRepository.findByTimestampBetween(startDate, endDate);
    }

    /**
     * Find lead history by lead and date range
     * @param lead the lead
     * @param startDate start date
     * @param endDate end date
     * @return list of lead history records for the lead within the date range
     */
    public List<LeadHistory> findByLeadAndDateRange(Lead lead, LocalDateTime startDate, LocalDateTime endDate) {
        return leadHistoryRepository.findByLeadAndTimestampBetween(lead, startDate, endDate);
    }

    /**
     * Find recent lead history for a lead
     * @param lead the lead
     * @param limit maximum number of records
     * @return list of recent lead history records
     */
    public List<LeadHistory> findRecentLeadHistory(Lead lead, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return leadHistoryRepository.findRecentLeadHistory(lead, pageable);
    }

    /**
     * Find lead history with comments only
     * @param lead the lead
     * @return list of lead history records with comments
     */
    public List<LeadHistory> findLeadHistoryWithComments(Lead lead) {
        return leadHistoryRepository.findLeadHistoryWithComments(lead);
    }

    /**
     * Count lead history records by lead
     * @param lead the lead
     * @return number of history records for the lead
     */
    public long countByLead(Lead lead) {
        return leadHistoryRepository.countByLead(lead);
    }

    /**
     * Find lead history by lead and action
     * @param lead the lead
     * @param action the action
     * @return list of lead history records with the specified action
     */
    public List<LeadHistory> findByLeadAndAction(Lead lead, String action) {
        return leadHistoryRepository.findByLeadAndAction(lead, action);
    }

    /**
     * Find lead history by lead and action type
     * @param lead the lead
     * @param actionType the action type
     * @return list of lead history records with the specified action type
     */
    public List<LeadHistory> findByLeadAndActionType(Lead lead, String actionType) {
        return leadHistoryRepository.findByLeadAndActionType(lead, actionType);
    }
}

