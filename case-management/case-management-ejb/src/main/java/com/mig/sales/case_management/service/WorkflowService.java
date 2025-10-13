package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class WorkflowService implements WorkflowServiceLocal {

    @PersistenceContext(unitName = "case-management-pu")
    private EntityManager em;

    public void escalateLead(Lead lead, User manager) {
        if (lead.getPotentialValue().doubleValue() > 1000000) {
            lead.setStatus("PRE_CONVERSION");
            lead.setAssignedTo(manager);
            em.merge(lead);
        }
    }

    public void approveLead(Lead lead) {
        lead.setStatus("CONVERTED");
        em.merge(lead);
    }
}