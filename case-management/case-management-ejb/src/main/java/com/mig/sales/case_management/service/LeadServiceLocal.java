package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.LeadHistory;
import com.mig.sales.case_management.model.User;
import javax.ejb.Local;
import java.util.List;

@Local
public interface LeadServiceLocal {
    void createLead(Lead lead);
    Lead getLeadById(Long id);
    List<Lead> getAllLeads();
    void distributeLeads();
    void updateLead(Lead lead);
    void addComment(Lead lead, User user, String commentText);
    List<LeadHistory> getLeadHistory(Lead lead);
}