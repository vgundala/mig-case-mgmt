package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.User;
import javax.ejb.Local;

@Local
public interface WorkflowServiceLocal {
    void escalateLead(Lead lead, User manager);
    void approveLead(Lead lead);
}